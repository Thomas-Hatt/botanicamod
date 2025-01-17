package botanicamod.relics.boss;

import botanicamod.Botanica;
import botanicamod.misc_classes.ActTracker;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import static botanicamod.Botanica.makeID;

// Thorned Crown - Start each combat with Thorns equal to 4 times the current act number. Lose 20% of your Max HP at the beginning of Act 3.

public class ThornedCrown extends BaseRelic
{
    private static final String NAME = "Thorned_Crown"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.BOSS; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.FLAT; // The sound played when the relic is clicked.

    public ThornedCrown() {
        super(ID, NAME, RARITY, SOUND);
    }

    private boolean hasLostHPInAct3 = false;


    public void atBattleStart() {
        this.flash();
        // Get the act
        int currentAct = ActTracker.getCurrentAct();

        // Multiply the current act by 4 to get the thorns amount
        int thornsAmount = currentAct * 4;

        // Apply thorns
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ThornsPower(AbstractDungeon.player, thornsAmount), thornsAmount));
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        if (AbstractDungeon.floorNum == 35 && !hasLostHPInAct3) {
            loseMaxHPInAct3();
        }
    }

    private void loseMaxHPInAct3() {
        int hpLoss = (int)(AbstractDungeon.player.maxHealth * 0.2f);
        AbstractDungeon.player.decreaseMaxHealth(hpLoss);
        this.flash();
        hasLostHPInAct3 = true;
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