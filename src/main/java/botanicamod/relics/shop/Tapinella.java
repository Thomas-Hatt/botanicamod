package botanicamod.relics.shop;

import botanicamod.Botanica;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import static botanicamod.Botanica.makeID;

public class Tapinella extends BaseRelic {
    private static final String NAME = "Tapinella";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.SHOP;
    private static final LandingSound SOUND = LandingSound.HEAVY;

    private boolean isConverting = false;  // Flag to prevent infinite loop

    public Tapinella() {
        super(ID, NAME, RARITY, SOUND);
    }

    public boolean isConverting() {
        return isConverting;
    }

    public void setConverting(boolean converting) {
        isConverting = converting;
    }

    public void convertDexterityToStrength(AbstractCreature target, int dexterityAmount) {
        if (target == AbstractDungeon.player && dexterityAmount > 0 && !isConverting) {
            setConverting(true);  // Set flag to prevent loop

            // Remove the gained Dexterity
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(target, target, DexterityPower.POWER_ID));
            // Apply an equivalent amount of Strength
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, target, new StrengthPower(target, dexterityAmount), dexterityAmount));

            setConverting(false);  // Reset flag after conversion
        }
    }

    @Override
    public boolean canSpawn() {
        if (Botanica.isRelicEnabled("Tapinella")) {
            return (Settings.isEndless || AbstractDungeon.floorNum <= 48);
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}