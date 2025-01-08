package botanicamod.relics.shop;

import botanicamod.Botanica;
import botanicamod.relics.BaseRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static botanicamod.Botanica.makeID;

public class AlchemistsMask extends BaseRelic implements ClickableRelic {
    // Right click to activate. Lose all gold. At the end of your turn, if you have 0 gold, obtain a random potion and add a random curse to your deck and draw pile.

    private static final String NAME = "Alchemists_Mask"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final AbstractRelic.RelicTier RARITY = AbstractRelic.RelicTier.SHOP; // The relic's rarity.
    private static final AbstractRelic.LandingSound SOUND = AbstractRelic.LandingSound.HEAVY; // The sound played when the relic is clicked.

    public AlchemistsMask() {
        super(ID, NAME, RARITY, SOUND);
    }

    // Create a boolean for the right click logic
    boolean toggle;

    @Override
    public void onRightClick() {
        // Set the player's gold to 0
        AbstractDungeon.player.gold = 0;
    }

    public void onPlayerEndTurn() {
        // Check to see if the player's gold is 0
        if (AbstractDungeon.player.gold == 0) {
            this.flash();

            // Add a random curse to the player's deck and draw pile
            AbstractCard curse = CardLibrary.getCurse().makeCopy();
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(curse, (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            AbstractDungeon.player.drawPile.addToTop(CardLibrary.getCurse());

            // Give the player a random potion
            AbstractPotion randomPotion = PotionHelper.getRandomPotion();
            AbstractDungeon.player.obtainPotion(randomPotion);
        }
    }

    @Override
    public boolean canSpawn() {
        if (Botanica.isRelicEnabled("AlchemistsMask")) {
            return (Settings.isEndless || AbstractDungeon.floorNum <= 48) && !(AbstractDungeon.getCurrRoom() instanceof ShopRoom);
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}