package botanicamod;

import basemod.*;
import basemod.interfaces.*;
import botanicamod.cards.BaseCard;
import botanicamod.potions.BasePotion;
import botanicamod.relics.BaseRelic;
import botanicamod.relics.boss.JestersBelt;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;

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
        RelicGetSubscriber {
    public static ModInfo info;
    public static String modID; //Edit your pom.xml to change this

    static {
        loadModInfo();
    }

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
                    if (isPotionEnabled(potion.ID)) {
                        BaseMod.addPotion(potion.getClass(), null, null, null, potion.ID, potion.playerClass);
                        System.out.println("Added potion: " + potion.ID);
                    }
                    //playerClass will make a potion character-specific. By default, it's null and will do nothing.
                });
    }

    public static void editPotionPool(ArrayList<String> potionList) {
        System.out.println("Potion Pool before modification: " + potionList); // Log initial state

        Set<String> potionsToRetain = new HashSet<>();

        for (String potionID : potionList) {
            if (isPotionEnabled(potionID)) {
                potionsToRetain.add(potionID);
            }
        }

        if (potionsToRetain.isEmpty()) {
            System.out.println("No potions enabled, the pool will be empty!");
            return;
        }

        potionList.clear();
        potionList.addAll(potionsToRetain);

        System.out.println("Potion Pool after modification: " + potionList); // Log final state
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
    public static float SPACING_Y = 25f;
    public static final float FULL_PAGE_Y = (SPACING_Y * 1.5f);
    public static float deltaY = 0;
    public static int currentPage = 0;

    public static SpireConfig modConfig = null;

    private static void setupSettingsPanel() {
        float screenWidthRatio = (float) Settings.WIDTH / 1920f;
        float screenHeightRatio = (float) Settings.HEIGHT / 1080f;
        LAYOUT_X *= screenWidthRatio;
        LAYOUT_Y *= screenHeightRatio;
        SPACING_Y *= screenHeightRatio;

        logger.info("Loading badge image and mod options");
        settingsPanel = new ModPanel();

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

    // Relic Descriptions

    private static final Map<String, String> RELIC_DESCRIPTIONS = new HashMap<>();

    static {
        Properties properties = new Properties();
        try (InputStream input = Botanica.class.getClassLoader().getResourceAsStream("relic_descriptions.properties")) {
            if (input == null) {
                System.out.println("Unable to find relic_descriptions.properties");
            }
            properties.load(input);
            for (String key : properties.stringPropertyNames()) {
                RELIC_DESCRIPTIONS.put(key, properties.getProperty(key));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getRelicDescription(String relicName) {
        return RELIC_DESCRIPTIONS.getOrDefault(relicName, "Description not available.");
    }

    // Potion Descriptions

    private static final Map<String, String> POTION_DESCRIPTIONS = new HashMap<>();

    static {
        Properties properties = new Properties();
        try (InputStream input = Botanica.class.getClassLoader().getResourceAsStream("potion_descriptions.properties")) {
            if (input == null) {
                System.out.println("Unable to find potion_descriptions.properties");
            }
            properties.load(input);
            for (String key : properties.stringPropertyNames()) {
                POTION_DESCRIPTIONS.put(key, properties.getProperty(key));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getPotionDescription(String potionName) {
        return POTION_DESCRIPTIONS.getOrDefault(potionName, "Description not available.");
    }

    String ENABLE_DISABLE = "Enable/Disable ";

    private static final List<String> RELIC_NAMES_LIST = new ArrayList<>();
    private static final String[] RELIC_NAMES = RELIC_NAMES_LIST.toArray(new String[0]);


    private void initializeConfig() {
        UIStrings configStrings = CardCrawlGame.languagePack.getUIString(makeID("ConfigMenuText"));

        settingsPanel = new ModPanel();

        // Use these variables when creating ModLabeledToggleButton and ModLabel

        // Organize relics by type
        Map<String, List<String>> relicsByType = new LinkedHashMap<>();

        relicsByType.put(ENABLE_DISABLE + "Common", Arrays.asList("Blossom", "BurningStone", "MerchantsRobes", "Nile", "Nostrum", "Quill", "ShortCircuit", "Trifocal"));
        relicsByType.put(ENABLE_DISABLE + "Uncommon", Arrays.asList("BlueAshes", "Cardoon", "Divider", "Equinox", "IllusionistsCoin", "Manna", "MirrorShard", "TerrifyingTrinket"));
        relicsByType.put(ENABLE_DISABLE + "Rare", Arrays.asList("Crystal", "Hemlock", "Marigold", "Nebula", "Silene", "Sweater", "Narcissus"));
        relicsByType.put(ENABLE_DISABLE + "Boss", Arrays.asList("DragonHeart", "FlaskOfDuplication", "GlimmeringOrb", "HandOfMidas", "JestersBelt", "PrismaticBox", "ThornedCrown"));
        relicsByType.put(ENABLE_DISABLE + "Shop", Arrays.asList("AlchemistsMask", "GamblersDebt", "Tapinella"));

        for (List<String> relics : relicsByType.values()) {
            RELIC_NAMES_LIST.addAll(relics);
        }

        // Add other types and relics as needed

        // Organize potions by type

        Map<String, List<String>> potionsByType = new LinkedHashMap<>();

        potionsByType.put(ENABLE_DISABLE + "Common", Arrays.asList(
                "PotionOfVigor"
        ));
        potionsByType.put(ENABLE_DISABLE + "Uncommon", Arrays.asList(
                "PotionOfAThousandCuts",
                "RetainPotion",
                "EssenceOfFrost",
                "EssenceOfLightning"
        ));
        potionsByType.put(ENABLE_DISABLE + "Rare", Arrays.asList(
                "PotionOfSlowness"
        ));

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
                    yPos -= SPACING_Y * 2.5f; // Move the header down
                }

                // Add up to 4 relics per page
                List<String> currentRelics = relics.subList(0, Math.min(4 - relicCount, relics.size()));
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
                            (label) -> {
                            },
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
                    yPos -= SPACING_Y * 2;

                    // Add relic description
                    String description = "-" + getRelicDescription(relicName);
                    float descriptionXOffset = 10f; // Adjust this value to move description
                    float wrapWidth = 800f; // Dynamic wrap width based on screen width
                    float fontScale = 0.85f; // Adjusted font scale for screen scaling

                    FontHelper.cardDescFont_L.getData().setScale(fontScale);
                    // Set a maximum number of words per line
                    int maxWordsPerLine = 20; // Adjust this value as needed
                    ArrayList<String> words = new ArrayList<>(Arrays.asList(description.split(" ")));
                    StringBuilder wrappedDescription = new StringBuilder();
                    int wordCount = 0;

                    for (String word : words) {
                        if (wordCount >= maxWordsPerLine) {
                            wrappedDescription.append("\n");
                            wordCount = 0; // Reset word count for the new line
                        }
                        wrappedDescription.append(word).append(" ");
                        wordCount++;
                    }



                    // Create the description label
                    ModLabel descLabel = new ModLabel(
                            wrappedDescription.toString(),
                            LAYOUT_X + descriptionXOffset,
                            yPos,
                            Settings.CREAM_COLOR,
                            FontHelper.cardDescFont_L,
                            settingsPanel,
                            (l) -> {
                            }
                    );
                    registerUIElement(descLabel, pageIndex);


                    // Move the yPos further down after placing the description
                    yPos -= (float) (SPACING_Y * 3.0); // Adjust this value for spacing between relics
                    relicCount++;
                }


                // Remove added relics from the list
                relics = relics.subList(currentRelics.size(), relics.size());

                // Check if we need to move to a new page
                if (relicCount >= 4 && !relics.isEmpty()) {
                    relicCount = 0; // Reset count for the next page
                    yPos = LAYOUT_Y; // Reset Y position
                    pageIndex++; // Move to the next page
                }
            }

            // Reset layout for the next category
            LAYOUT_X = 400f;
            pageIndex++;

        }

        // Add Potions by Type
        for (Map.Entry<String, List<String>> entry : potionsByType.entrySet()) {
            String potionType = entry.getKey();
            List<String> potions = entry.getValue();

            float yPos = LAYOUT_Y;
            int potionCount = 0; // Count potions per page

            // Ensure the header is on each page
            while (!potions.isEmpty()) {
                // Add potion type header
                if (potionCount == 0) {
                    ModLabel typeLabel = new ModLabel(potionType + " Potions", LAYOUT_X - 40f, yPos, Settings.CREAM_COLOR, FontHelper.charDescFont, settingsPanel, (l) -> {
                    });
                    registerUIElement(typeLabel, pageIndex);
                    yPos -= SPACING_Y * 2.5f; // Move the header down
                }

                // Add up to 4 potions per page
                List<String> currentPotions = new ArrayList<>(potions.subList(0, Math.min(4 - potionCount, potions.size())));

                // Add potions and descriptions
                for (String potionName : currentPotions) {
                    boolean enabled = modConfig.getBool("Botanica" + potionName + "PotionEnabled");

                    // Add potion name toggle button
                    ModLabeledToggleButton potionToggle = new ModLabeledToggleButton(
                            potionName, LAYOUT_X - 40f, yPos, Settings.CREAM_COLOR, FontHelper.charDescFont,
                            enabled, settingsPanel,
                            (label) -> {},
                            (button) -> {
                                modConfig.setBool("Botanica" + potionName + "PotionEnabled", button.enabled);
                                try {
                                    modConfig.save();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                // Call the function to update the potion pool
                                editPotionPool(new ArrayList<>(potionsByType.get(potionType)));
                                registerPotions();
                            }
                    );
                    registerUIElement(potionToggle, pageIndex);

                    // Move the yPos down for description
                    yPos -= SPACING_Y * 2;

                    // Add potion description
                    String description = "-" + getPotionDescription(potionName);

                    float descriptionXOffset = 10f;
                    float wrapWidth = 800f;
                    float fontScale = 0.85f;

                    FontHelper.cardDescFont_L.getData().setScale(fontScale);
                    ArrayList<String> words = new ArrayList<>(Arrays.asList(description.split(" ")));
                    StringBuilder wrappedDescription = new StringBuilder();
                    int maxWordsPerLine = 20;

                    int wordCount = 0;
                    for (String word : words) {
                        if (wordCount >= maxWordsPerLine) {
                            wrappedDescription.append("\n");
                            wordCount = 0;
                        }
                        wrappedDescription.append(word).append(" ");
                        wordCount++;
                    }

                    // Create the description label
                    ModLabel descLabel = new ModLabel(
                            wrappedDescription.toString(),
                            LAYOUT_X + descriptionXOffset,
                            yPos,
                            Settings.CREAM_COLOR,
                            FontHelper.cardDescFont_L,
                            settingsPanel,
                            (l) -> {
                            }
                    );
                    registerUIElement(descLabel, pageIndex);

                    // Move the yPos further down after placing the description
                    yPos -= (float) (SPACING_Y * 3.0);
                    potionCount++;
                }

                // Remove added potions from the list
                potions = potions.subList(currentPotions.size(), potions.size());

                // Check if we need to move to a new page
                if (potionCount >= 4 && !potions.isEmpty()) {
                    potionCount = 0; // Reset count for the next page
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

        BiggerModButton leftButton = new BiggerModButton(Settings.WIDTH / 2F / Settings.xScale - 100f - ImageMaster.CF_LEFT_ARROW.getWidth() / 2F, LAYOUT_Y + 45f, -5f, ImageMaster.CF_LEFT_ARROW, settingsPanel, b -> {
            if (currentPage > 0) previousPage();
            else for (int i = 0; i < totalPages - 1; i++) nextPage();
        });

        BiggerModButton rightButton = new BiggerModButton(
                Settings.WIDTH / 2F / Settings.xScale + 100f - ImageMaster.CF_LEFT_ARROW.getWidth() / 2F, LAYOUT_Y + 45f, -5f, ImageMaster.CF_RIGHT_ARROW, settingsPanel, b -> {
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
        elem.setX(elem.getX() + (pageIndex * Settings.WIDTH) / Settings.scale);
    }


    private static void registerUIElement(IUIElement elem) {
        settingsPanel.addUIElement(elem);
        if (pages.isEmpty()) {
            pages.put(0, new ArrayList<>());
        }
        int page = pages.size() - 1;
        pages.get(page).add(elem);
        elem.setY(elem.getY() - deltaY);
        elem.setX(elem.getX() + (page * Settings.WIDTH) / Settings.scale);
        deltaY += SPACING_Y;
        if (deltaY > FULL_PAGE_Y) {
            deltaY = 0;
            pages.put(page + 1, new ArrayList<>());
        }
    }

    private static void nextPage() {
        for (ArrayList<IUIElement> elems : pages.values()) {
            for (IUIElement elem : elems) {
                elem.setX(elem.getX() - Settings.WIDTH / Settings.scale);
            }
        }
        currentPage++;
    }

    private static void previousPage() {
        for (ArrayList<IUIElement> elems : pages.values()) {
            for (IUIElement elem : elems) {
                elem.setX(elem.getX() + Settings.WIDTH / Settings.scale);
            }
        }
        currentPage--;
    }

    public static boolean isRelicEnabled(String relicID) {
        return modConfig.getBool("Botanica" + relicID + "RelicEnabled");
    }

    public static boolean isPotionEnabled(String potionID) {
        return modConfig.getBool("Botanica" + potionID + "PotionEnabled");
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
    private static String getLangString() {
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
            } catch (GdxRuntimeException e) {
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
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal(localizationPath(defaultLanguage, "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        KeywordInfo[] keywords = gson.fromJson(json, KeywordInfo[].class);
        for (KeywordInfo keyword : keywords) {
            keyword.prep();
            registerKeyword(keyword);
        }

        if (!defaultLanguage.equals(getLangString())) {
            try {
                json = Gdx.files.internal(localizationPath(getLangString(), "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
                keywords = gson.fromJson(json, KeywordInfo[].class);
                for (KeywordInfo keyword : keywords) {
                    keyword.prep();
                    registerKeyword(keyword);
                }
            } catch (Exception e) {
                logger.warn(modID + " does not support " + getLangString() + " keywords.");
            }
        }
    }

    private void registerKeyword(KeywordInfo info) {
        BaseMod.addKeyword(modID.toLowerCase(), info.PROPER_NAME, info.NAMES, info.DESCRIPTION);
        if (!info.ID.isEmpty()) {
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
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo) -> {
            AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null)
                return false;
            Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
            return initializers.contains(Botanica.class.getName());
        }).findFirst();
        if (infos.isPresent()) {
            info = infos.get();
            modID = info.ID;
        } else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower power, com.megacrit.cardcrawl.core.AbstractCreature target, com.megacrit.cardcrawl.core.AbstractCreature source) {
        if (target == AbstractDungeon.player)
            if (power.ID.equals(DexterityPower.POWER_ID)) {
                // Jester's Belt
                JestersBelt jestersBelt = (JestersBelt) AbstractDungeon.player.getRelic(JestersBelt.ID);
                if (jestersBelt != null) {
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