package botanicamod.relics.boss;

import botanicamod.Botanica;
import botanicamod.misc_classes.ActTracker;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import static botanicamod.Botanica.makeID;

// Thorned Crown - Start each combat with Thorns equal to 4 times the current act number

public class ThornedCrown extends BaseRelic
{
    private static final String NAME = "Thorned_Crown"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.BOSS; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.FLAT; // The sound played when the relic is clicked.

    public ThornedCrown() {
        super(ID, NAME, RARITY, SOUND);
    }

    int currentAct = ActTracker.getCurrentAct();

    public void atBattleStart() {
        this.flash();

        // Multiply the current act by 4 to get the thorns amount
        int thornsAmount = currentAct * 4;

        // Apply thorns
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ThornsPower(AbstractDungeon.player, thornsAmount), thornsAmount));
    }

    @Override
    public boolean canSpawn() {
        if (Botanica.isRelicEnabled("ThornedCrown")) {
            return (Settings.isEndless || AbstractDungeon.floorNum <= 48) && !(AbstractDungeon.getCurrRoom() instanceof ShopRoom);
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}