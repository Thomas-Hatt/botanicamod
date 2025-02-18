package botanicamod.patches;

import botanicamod.relics.shop.Tapinella;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Tapinella_Patched {
    private static boolean isConverting = false;

    @SpirePatch(
            clz = ApplyPowerAction.class,
            method = "update"
    )
    public static class TapinellaPatch {
        public static void Prefix(ApplyPowerAction __instance, AbstractPower ___powerToApply, AbstractCreature ___target) {
            if (!isConverting && AbstractDungeon.player.hasRelic(Tapinella.ID) &&
                    ___powerToApply instanceof DexterityPower &&
                    ___target == AbstractDungeon.player) {
                isConverting = true;
                ___powerToApply = new StrengthPower(___target, ___powerToApply.amount);
                isConverting = false;
            }
        }
    }

}
