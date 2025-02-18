package botanicamod.relics.shop;

import botanicamod.Botanica;
import botanicamod.relics.BaseRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static botanicamod.Botanica.makeID;

public class Tapinella extends BaseRelic {
    private static final String NAME = "Tapinella";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.SHOP;
    private static final LandingSound SOUND = LandingSound.HEAVY;

    public Tapinella() {
        super(ID, NAME, RARITY, SOUND);
    }

    private void convertDexterityToStrength() {
        if (AbstractDungeon.player.hasPower(DexterityPower.POWER_ID)) {
            int dexAmount = AbstractDungeon.player.getPower(DexterityPower.POWER_ID).amount;
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player, DexterityPower.POWER_ID));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player,
                    new StrengthPower(AbstractDungeon.player, dexAmount), dexAmount));
        }
    }

    @Override
        public void onPlayerEndTurn() {
        convertDexterityToStrength();
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