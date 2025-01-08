package botanicamod;

import basemod.*;
import basemod.interfaces.*;
import botanicamod.cards.BaseCard;
import botanicamod.potions.BasePotion;
import botanicamod.relics.BaseRelic;
import botanicamod.relics.boss.*;
import botanicamod.relics.shop.Tapinella;
import botanicamod.ui.BiggerModButton;
import botanicamod.ui.CenteredModLabel;
import botanicamod.util.GeneralUtils;
import botanicamod.util.KeywordInfo;
import botanicamod.util.TextureLoader;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFileHandle;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

@SpireInitializer
public class Botanica implements
        EditCardsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditRelicsSubscriber,
        PostInitializeSubscriber,
        OnPowersModifiedSubscriber,
        OnStartBattleSubscriber,
        PostPowerApplySubscriber,
        RelicGetSubscriber
{
    public static ModInfo info;
    public static String modID; //Edit your pom.xml to change this
    static { loadModInfo(); }
    private static final String resourcesFolder = checkResourcesPath();
    public static final Logger logger = LogManager.getLogger(modID); //Used to output to the console.

    @Override
    public void receiveEditCards() { //somewhere in the class
        new AutoAdd(modID) //Loads files from this mod
                .packageFilter(BaseCard.class) //In the same package as this class
                .setDefaultSeen(true) //And marks them as seen in the compendium
                .cards(); //Adds the cards
    }

    @Override
    public void receiveEditRelics() { //somewhere in the class
        new AutoAdd(modID) //Loads files from this mod
                .packageFilter(BaseRelic.class) //In the same package as this class
                .any(BaseRelic.class, (info, relic) -> { //Run this code for any classes that extend this class
                    if (relic.pool != null)
                        BaseMod.addRelicToCustomPool(relic, relic.pool); //Register a custom character specific relic
                    else
                        BaseMod.addRelic(relic, relic.relicType); //Register a shared or base game character specific relic

                    //If the class is annotated with @AutoAdd.Seen, it will be marked as seen, making it visible in the relic library.
                    //If you want all your relics to be visible by default, just remove this if statement.
                    if (info.seen)
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                });
    }

    public static void registerPotions() {
        new AutoAdd(modID) //Loads files from this mod
                .packageFilter(BasePotion.class) //In the same package as this class
                .any(BasePotion.class, (info, potion) -> { //Run this code for any classes that extend this class
                    //These three null parameters are colors.
                    //If they're not null, they'll overwrite whatever color is set in the potions themselves.
                    //This is an old feature added before having potions determine their own color was possible.
                    BaseMod.addPotion(potion.getClass(), null, null, null, potion.ID, potion.playerClass);
                    //playerClass will make a potion character-specific. By default, it's null and will do nothing.
                });
    }

    //This is used to prefix the IDs of various objects like cards and relics,
    //to avoid conflicts between different mods using the same name for things.
    public static String makeID(String id) {
        return modID + ":" + id;
    }

    //This will be called by ModTheSpire because of the @SpireInitializer annotation at the top of the class.
    public static void initialize() {
        new Botanica();

        try {
            Properties defaults = new Properties();

            // Set all relics to enabled by default
            for (String relicName : RELIC_NAMES) {
                defaults.put("Botanica" + relicName + "RelicEnabled", "TRUE");
            }

            modConfig = new SpireConfig(modID, "GeneralConfig", defaults);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Botanica() {
        BaseMod.subscribe(this); //This will make BaseMod trigger all the subscribers at their appropriate times.
        logger.info(modID + " subscribed to BaseMod.");
    }

    // Config stuff
    public static UIStrings uiStrings;
    public static UIStrings crossoverUIStrings;
    public static String[] TEXT;
    public static String[] EXTRA_TEXT;
    private static final String AUTHOR = "Thomas Hatt";

    public static ModPanel settingsPanel;
    public static HashMap<Integer, ArrayList<IUIElement>> pages = new HashMap<>();
    public static float LAYOUT_Y = 760f;
    public static float LAYOUT_X = 400f;
    public static final float SPACING_Y = 25f;
    public static final float FULL_PAGE_Y = (SPACING_Y * 1.5f);
    public static float deltaY = 0;
    public static int currentPage = 0;

    // Get the longest text so all sliders are centered
    private static float getSliderPosition(List<String> stringsToCompare) {
        float longest = 0;
        for (String s : stringsToCompare) {
            longest = Math.max(longest, FontHelper.getWidth(FontHelper.charDescFont, s, 1f /Settings.scale));
        }

        //Get the longest slider text for positioning
        ArrayList<String> labelStrings = new ArrayList<>(Arrays.asList(TEXT));
        float sliderOffset = getSliderPosition(labelStrings.subList(1,5));
        labelStrings.clear();

        return longest + 40f;
    }

    public static SpireConfig modConfig = null;

    private static void setupSettingsPanel() {
        logger.info("Loading badge image and mod options");
        settingsPanel = new ModPanel();
        float aspectRatio = (float)Settings.WIDTH/(float)Settings.HEIGHT;
        float sixteenByNine = 1920f/1080f;
        if (Settings.isFourByThree || (aspectRatio < 1.333F)) {
            LAYOUT_Y *= 1.2222f;
        } else if (Settings.isSixteenByTen) {
            LAYOUT_Y *= 1.08f;
        } else if (aspectRatio < sixteenByNine) {
            LAYOUT_Y *= 1.8888f - aspectRatio/2f;
        }


        // Grab the strings
        uiStrings = CardCrawlGame.languagePack.getUIString(makeID("ModConfigs"));
        crossoverUIStrings = CardCrawlGame.languagePack.getUIString(makeID("CrossoverConfig"));
        EXTRA_TEXT = uiStrings.EXTRA_TEXT;
        TEXT = uiStrings.TEXT;

        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(imagePath("badge.png"));
        BaseMod.registerModBadge(badgeTexture, EXTRA_TEXT[0], AUTHOR, EXTRA_TEXT[1], settingsPanel);

        // Get the longest slider text for positioning
        ArrayList<String> labelStrings = new ArrayList<>(Arrays.asList(TEXT));
        float sliderOffset = getSliderPosition(labelStrings.subList(1,5));
        labelStrings.clear();
        }

    @Override
    public void receiveRelicGet(AbstractRelic rel) {
        if (rel.relicId.startsWith("Botanica:")) {
            String relicName = rel.relicId.substring("Botanica:".length());
            if (!isRelicEnabled(relicName)) {
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F,
                        RelicLibrary.getRelic(Circlet.ID).makeCopy());
            }
        }
    }

    private static final String[] RELIC_NAMES = {
            "ShortCircuit", "MerchantsRobes", "BurningStone", "Quill", "Blossom", "Nile", "Nostrum", "Trifocal",
            "MirrorShard", "Divider", "Cardoon", "BlueAshes", "Equinox", "TerrifyingTrinket", "IllusionistsCoin", "Manna",
            "Nebula", "Narcissus", "Silene", "Marigold", "Hemlock", "Sweater", "Crystal", "Crystallize",
            "Tapinella", "AlchemistsMask", "GamblersDebt",
            "HandOfMidas", "ThornedCrown", "DragonHeart", "FlaskOfDuplication", "GlimmeringOrb", "JestersBelt", "PrismaticBox", "Narcissus", "Sweater"
    };

    private static final Map<String, String> RELIC_DESCRIPTIONS = new HashMap<>();

    static {
        RELIC_DESCRIPTIONS.put("ShortCircuit", "If you do not play any Skills during your turn, gain an extra Energy next turn.");
        RELIC_DESCRIPTIONS.put("MerchantsRobes", "Upon entering a shop, obtain 2 random Potions.");
        RELIC_DESCRIPTIONS.put("BurningStone", "At the start of each combat, add a Burn to your discard pile. Whenever you draw a Burn, gain 2 Strength.");
        RELIC_DESCRIPTIONS.put("Quill", "At the start of each combat, add a random 0 cost card to your hand, discard pile, and draw pile.");
        RELIC_DESCRIPTIONS.put("Blossom", "The first time you lose HP each combat, add a Miracle to your hand.");
        RELIC_DESCRIPTIONS.put("Nile", "Whenever you enter a ? room, all enemies in the next combat lose 1 Strength.");
        RELIC_DESCRIPTIONS.put("Nostrum", "At the end of your first turn, Exhaust any number of cards in your hand.");
        RELIC_DESCRIPTIONS.put("Trifocal", "At the end of your first turn, gain 5 gold for each card discarded, then Scry 3.");
        RELIC_DESCRIPTIONS.put("MirrorShard", "At the start of each combat, create a copy of a random card in your hand.");
        RELIC_DESCRIPTIONS.put("Divider", "Every 4 turns, add a random skill into your hand.");
        RELIC_DESCRIPTIONS.put("Cardoon", "Every time you play 4 Attacks in a single turn, shuffle a Jack of All Trades into your draw pile.");
        RELIC_DESCRIPTIONS.put("BlueAshes", "At the start of each combat, gain the Fire Breathing buff.");
        RELIC_DESCRIPTIONS.put("Equinox", "Every time you play 10 Attacks, play the top card of your draw pile.");
        RELIC_DESCRIPTIONS.put("TerrifyingTrinket", "At the start of turn 3, all minions run away.");
        RELIC_DESCRIPTIONS.put("IllusionistsCoin", "At the start of turn 3, add an Illusion Neutralize to your hand.");
        RELIC_DESCRIPTIONS.put("Manna", "At the start of each Elite combat, draw until your hand is full.");
        RELIC_DESCRIPTIONS.put("Nebula", "If you end your turn without Block, your next skill is played twice.");
        RELIC_DESCRIPTIONS.put("Narcissus", "Start each combat with Limit Break. It has Purge and costs 0.");
        RELIC_DESCRIPTIONS.put("Silene", "Heal 10% of damage taken at the end of combat.");
        RELIC_DESCRIPTIONS.put("Marigold", "Gain gold equal to 25% of the damage dealt in combat, but only for the first 100 damage dealt. For every 100 gold gained from this effect, draw one card at the start of combat.");
        RELIC_DESCRIPTIONS.put("Hemlock", "Upon entering a Rest Site, Scry 1 for every 6 cards in your deck at the start of your next combat.");
        RELIC_DESCRIPTIONS.put("Sweater", "At the start of each combat, Channel 2 Frost. Gain 1 Focus every 3 turns.");
        RELIC_DESCRIPTIONS.put("Crystal", "At the start of combat, gain 2 Crystallize. Crystallize: At the end of your turn, gain an Orb slot and Channel a random Orb.");
        RELIC_DESCRIPTIONS.put("Tapinella", "All Dexterity is converted to Strength.");
        RELIC_DESCRIPTIONS.put("AlchemistsMask", "Right click to activate. Lose all gold. At the end of your turn, if you have 0 gold, obtain a random potion and add a random curse to your deck and draw pile.");
        RELIC_DESCRIPTIONS.put("GamblersDebt", "Card rewards have 2 extra cards, but 1 random card is disguised as a curse.");
        RELIC_DESCRIPTIONS.put("HandOfMidas", "Hand of Greed now grants double gold on kill. Upon pickup, upgrade all held Hand of Greed(s) and obtain 3 copies.");
        RELIC_DESCRIPTIONS.put("ThornedCrown", "Start each combat with 4 Thorns for each boss relic you have.");
        RELIC_DESCRIPTIONS.put("DragonHeart", "Gain 2 Strength and 2 Dexterity at the start of each combat. Retain a random card at the end of your turn.");
        RELIC_DESCRIPTIONS.put("FlaskOfDuplication", "Upon pickup, gain 2 copies of Normality. Duplicate every card in your deck and gain 10 gold for each card added.");
        RELIC_DESCRIPTIONS.put("GlimmeringOrb", "The first time you play 5 Power Cards each combat, obtain a Vajra.");
        RELIC_DESCRIPTIONS.put("JestersBelt", "Gain 1 Energy at the start of each turn. Gain 1 Dexterity for every 3 cards played in a turn, but lose 2 Dexterity if you play a Power card. Dexterity is now capped at 8.");
        RELIC_DESCRIPTIONS.put("PrismaticBox", "Transform all Strikes and Defends into Uncommon cards. These cards can be of any color.");
    }


    String ENABLE_DISABLE = "Enable/Disable ";

    private void initializeConfig() {
        UIStrings configStrings = CardCrawlGame.languagePack.getUIString(makeID("ConfigMenuText"));

        settingsPanel = new ModPanel();

        // Organize relics by type
        Map<String, List<String>> relicsByType = new LinkedHashMap<>();
        relicsByType.put(ENABLE_DISABLE + "Common", Arrays.asList("Blossom", "BurningStone", "MerchantsRobes", "Nile", "Nostrum", "Quill", "ShortCircuit", "Trifocal"));
        relicsByType.put(ENABLE_DISABLE + "Uncommon", Arrays.asList("BlueAshes", "Cardoon", "Divider", "Equinox", "IllusionistsCoin", "Manna", "MirrorShard", "TerrifyingTrinket"));
        relicsByType.put(ENABLE_DISABLE + "Rare", Arrays.asList("Crystal", "Hemlock", "Marigold", "Nebula", "Silene"));
        relicsByType.put(ENABLE_DISABLE + "Boss", Arrays.asList("DragonHeart", "FlaskOfDuplication", "GlimmeringOrb", "HandOfMidas", "JestersBelt", "PrismaticBox", "ThornedCrown", "Sweater", "Narcissus"));
        relicsByType.put(ENABLE_DISABLE + "Shop", Arrays.asList("AlchemistsMask", "GamblersDebt", "Tapinella"));
        // Add other types and relics as needed

        int pageIndex = 0;

        for (Map.Entry<String, List<String>> entry : relicsByType.entrySet()) {
            String relicType = entry.getKey();
            List<String> relics = entry.getValue();

            float yPos = LAYOUT_Y;
            int relicCount = 0; // Count relics per page

            // Ensure the header is on each page
            while (!relics.isEmpty()) {
                // Add relic type header
                if (relicCount == 0) {
                    ModLabel typeLabel = new ModLabel(relicType + " Relics", LAYOUT_X - 40f, yPos, Settings.CREAM_COLOR, FontHelper.charDescFont, settingsPanel, (l) -> {
                    });
                    registerUIElement(typeLabel, pageIndex);
                    yPos -= SPACING_Y;
                }

                // Add up to 7 relics per page
                List<String> currentRelics = relics.subList(0, Math.min(7 - relicCount, relics.size()));
                // Add relic type header
                if (relicCount == 0) {
                    // Add extra space before relics appear
                    yPos -= 10f; // Push relics further down
                }

                // Add relics and descriptions
                for (String relicName : currentRelics) {
                    boolean enabled = modConfig.getBool("Botanica" + relicName + "RelicEnabled");

                    // Add relic name toggle button
                    ModLabeledToggleButton relicToggle = new ModLabeledToggleButton(
                            relicName, LAYOUT_X - 40f, yPos, Settings.CREAM_COLOR, FontHelper.charDescFont,
                            enabled, settingsPanel,
                            (label) -> {},
                            (button) -> {
                                modConfig.setBool("Botanica" + relicName + "RelicEnabled", button.enabled);
                                try {
                                    modConfig.save();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    );
                    registerUIElement(relicToggle, pageIndex);

                    // Move the yPos down for description
                    yPos -= SPACING_Y;

                    // Add relic description
                    String description = "-" + getRelicDescription(relicName);
                    float descriptionXOffset = 0f; // Adjust this value to move description
                    FontHelper.cardDescFont_L.getData().setScale(0.6f); // Adjust the scale of the font

                    ModLabel descLabel = new ModLabel(description, LAYOUT_X + descriptionXOffset, yPos, Settings.CREAM_COLOR, FontHelper.cardDescFont_L, settingsPanel, (l) -> {});
                    registerUIElement(descLabel, pageIndex);

                    // Move the yPos further down after placing the description
                    yPos -= SPACING_Y * 2.0; // Adjust this value for spacing between relics
                    relicCount++;
                }


                // Remove added relics from the list
                relics = relics.subList(currentRelics.size(), relics.size());

                // Check if we need to move to a new page
                if (relicCount >= 6 && !relics.isEmpty()) {
                    relicCount = 0; // Reset count for the next page
                    yPos = LAYOUT_Y; // Reset Y position
                    pageIndex++; // Move to the next page
                }
            }

            // Reset layout for the next category
            LAYOUT_X = 400f;
            pageIndex++;
        }

        // Add Page Navigation (Fixed Centering)
        int totalPages = pageIndex;

        CenteredModLabel pageLabel = new CenteredModLabel("", Settings.WIDTH / 2f, LAYOUT_Y + 70f, settingsPanel, l -> {
            l.text = "Page " + (currentPage + 1) + "/" + totalPages;
            l.setX((Settings.WIDTH - FontHelper.getWidth(FontHelper.charDescFont, l.text, 1.0f)) / 2.5f);
        });

        BiggerModButton leftButton = new BiggerModButton(Settings.WIDTH/2F/Settings.xScale - 100f - ImageMaster.CF_LEFT_ARROW.getWidth()/2F, LAYOUT_Y + 45f, -5f, ImageMaster.CF_LEFT_ARROW, settingsPanel, b -> {
            if (currentPage > 0) previousPage();
            else for (int i = 0; i < totalPages - 1; i++) nextPage();
        });

        BiggerModButton rightButton = new BiggerModButton(
                Settings.WIDTH/2F/Settings.xScale + 100f - ImageMaster.CF_LEFT_ARROW.getWidth()/2F, LAYOUT_Y + 45f, -5f, ImageMaster.CF_RIGHT_ARROW, settingsPanel, b -> {
            if (currentPage < totalPages - 1) nextPage();
            else for (int i = currentPage; i > 0; i--) previousPage();
        });

        // Add centered label and buttons
        settingsPanel.addUIElement(pageLabel);
        settingsPanel.addUIElement(leftButton);
        settingsPanel.addUIElement(rightButton);
    }


    private void registerUIElement(IUIElement elem, int pageIndex) {
        settingsPanel.addUIElement(elem);
        if (!pages.containsKey(pageIndex)) {
            pages.put(pageIndex, new ArrayList<>());
        }
        pages.get(pageIndex).add(elem);
        elem.setX(elem.getX() + (pageIndex * Settings.WIDTH)/Settings.scale);
    }

    private String getRelicDescription(String relicName) {
        return RELIC_DESCRIPTIONS.getOrDefault(relicName, "Description not available.");
    }


    private static void registerUIElement(IUIElement elem) {
        settingsPanel.addUIElement(elem);
        if (pages.isEmpty()) {
            pages.put(0, new ArrayList<>());
        }
        int page = pages.size()-1;
        pages.get(page).add(elem);
        elem.setY(elem.getY() - deltaY);
        elem.setX(elem.getX() + (page * Settings.WIDTH)/Settings.scale);
        deltaY += SPACING_Y;
        if (deltaY > FULL_PAGE_Y) {
            deltaY = 0;
            pages.put(page+1, new ArrayList<>());
        }
    }

    private static void nextPage() {
        for (ArrayList<IUIElement> elems : pages.values()) {
            for (IUIElement elem : elems) {
                elem.setX(elem.getX() - Settings.WIDTH/Settings.scale);
            }
        }
        currentPage++;
    }

    private static void previousPage() {
        for (ArrayList<IUIElement> elems : pages.values()) {
            for (IUIElement elem : elems) {
                elem.setX(elem.getX() + Settings.WIDTH/Settings.scale);
            }
        }
        currentPage--;
    }

    public static boolean isRelicEnabled(String relicID) {
        return modConfig.getBool("Botanica" + relicID + "RelicEnabled");
    }

    @Override
    public void receivePostInitialize() {
        registerPotions();
        Texture badgeTexture = TextureLoader.getTexture(imagePath("badge.png"));

        initializeConfig();

        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, settingsPanel);
    }

    /*----------Localization----------*/

    //This is used to load the appropriate localization files based on language.
    private static String getLangString()
    {
        return Settings.language.name().toLowerCase();
    }
    private static final String defaultLanguage = "eng";

    public static final Map<String, KeywordInfo> keywords = new HashMap<>();

    @Override
    public void receiveEditStrings() {
        /*
            First, load the default localization.
            Then, if the current language is different, attempt to load localization for that language.
            This results in the default localization being used for anything that might be missing.
            The same process is used to load keywords slightly below.
        */
        loadLocalization(defaultLanguage); //no exception catching for default localization; you better have at least one that works.
        if (!defaultLanguage.equals(getLangString())) {
            try {
                loadLocalization(getLangString());
            }
            catch (GdxRuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadLocalization(String lang) {
        //While this does load every type of localization, most of these files are just outlines so that you can see how they're formatted.
        //Feel free to comment out/delete any that you don't end up using.
        BaseMod.loadCustomStringsFile(CardStrings.class,
                localizationPath(lang, "CardStrings.json"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                localizationPath(lang, "CharacterStrings.json"));
        BaseMod.loadCustomStringsFile(EventStrings.class,
                localizationPath(lang, "EventStrings.json"));
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                localizationPath(lang, "OrbStrings.json"));
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                localizationPath(lang, "PotionStrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                localizationPath(lang, "PowerStrings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                localizationPath(lang, "RelicStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class,
                localizationPath(lang, "UIStrings.json"));
    }

    @Override
    public void receiveEditKeywords()
    {
        Gson gson = new Gson();
        String json = Gdx.files.internal(localizationPath(defaultLanguage, "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        KeywordInfo[] keywords = gson.fromJson(json, KeywordInfo[].class);
        for (KeywordInfo keyword : keywords) {
            keyword.prep();
            registerKeyword(keyword);
        }

        if (!defaultLanguage.equals(getLangString())) {
            try
            {
                json = Gdx.files.internal(localizationPath(getLangString(), "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
                keywords = gson.fromJson(json, KeywordInfo[].class);
                for (KeywordInfo keyword : keywords) {
                    keyword.prep();
                    registerKeyword(keyword);
                }
            }
            catch (Exception e)
            {
                logger.warn(modID + " does not support " + getLangString() + " keywords.");
            }
        }
    }

    private void registerKeyword(KeywordInfo info) {
        BaseMod.addKeyword(modID.toLowerCase(), info.PROPER_NAME, info.NAMES, info.DESCRIPTION);
        if (!info.ID.isEmpty())
        {
            keywords.put(info.ID, info);
        }
    }

    //These methods are used to generate the correct filepaths to various parts of the resources folder.
    public static String localizationPath(String lang, String file) {
        return resourcesFolder + "/localization/" + lang + "/" + file;
    }

    public static String imagePath(String file) {
        return resourcesFolder + "/images/" + file;
    }
    public static String characterPath(String file) {
        return resourcesFolder + "/images/character/" + file;
    }
    public static String powerPath(String file) {
        return resourcesFolder + "/images/powers/" + file;
    }
    public static String relicPath(String file) {
        return resourcesFolder + "/images/relics/" + file;
    }

    /**
     * Checks the expected resources path based on the package name.
     */
    private static String checkResourcesPath() {
        String name = Botanica.class.getName(); //getPackage can be iffy with patching, so class name is used instead.
        int separator = name.indexOf('.');
        if (separator > 0)
            name = name.substring(0, separator);

        FileHandle resources = new LwjglFileHandle(name, Files.FileType.Internal);

        if (!resources.exists()) {
            throw new RuntimeException("\n\tFailed to find resources folder; expected it to be named \"" + name + "\"." +
                    " Either make sure the folder under resources has the same name as your mod's package, or change the line\n" +
                    "\t\"private static final String resourcesFolder = checkResourcesPath();\"\n" +
                    "\tat the top of the " + Botanica.class.getSimpleName() + " java file.");
        }
        if (!resources.child("images").exists()) {
            throw new RuntimeException("\n\tFailed to find the 'images' folder in the mod's 'resources/" + name + "' folder; Make sure the " +
                    "images folder is in the correct location.");
        }
        if (!resources.child("localization").exists()) {
            throw new RuntimeException("\n\tFailed to find the 'localization' folder in the mod's 'resources/" + name + "' folder; Make sure the " +
                    "localization folder is in the correct location.");
        }

        return name;
    }

    /**
     * This determines the mod's ID based on information stored by ModTheSpire.
     */
    private static void loadModInfo() {
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo)->{
            AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null)
                return false;
            Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
            return initializers.contains(Botanica.class.getName());
        }).findFirst();
        if (infos.isPresent()) {
            info = infos.get();
            modID = info.ID;
        }
        else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower power, com.megacrit.cardcrawl.core.AbstractCreature target, com.megacrit.cardcrawl.core.AbstractCreature source) {
        if (target == AbstractDungeon.player)
            if (power.ID.equals(DexterityPower.POWER_ID)) {
                // Jester's Belt
                JestersBelt jestersBelt = (JestersBelt) AbstractDungeon.player.getRelic(JestersBelt.ID);
                if (jestersBelt != null)
                {
                    jestersBelt.reduceDexterity();
                }

                // Tapinella
                Tapinella tapinella = (Tapinella) AbstractDungeon.player.getRelic(Tapinella.ID);
                if (tapinella != null && !tapinella.isConverting()) {
                    tapinella.convertDexterityToStrength(AbstractDungeon.player, power.amount);
                }
        }
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        AbstractDungeon.player.powers.forEach(power -> {
            if (power.ID.equals(StrengthPower.POWER_ID)) {
                Tapinella tapinella = (Tapinella) AbstractDungeon.player.getRelic(Tapinella.ID);
                if (tapinella != null && !tapinella.isConverting()) {
                    tapinella.convertDexterityToStrength(AbstractDungeon.player, power.amount);
                }
            }
        });
    }

    @Override
    public void receivePowersModified() {

    }
}