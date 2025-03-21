package botanicamod.patches;

import botanicamod.relics.boss.JestersBelt;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class JestersBelt_Patched {
    @SpirePatch(clz = DexterityPower.class, method = "stackPower")
    public static class StackPowerPatch {
        @SpirePostfixPatch
        public static void enforceCap(DexterityPower __instance, int amount) {
            AbstractPlayer player = AbstractDungeon.player;
            if (player != null && player.hasRelic(JestersBelt.ID)) {
                JestersBelt relic = (JestersBelt) player.getRelic(JestersBelt.ID);
                if (__instance.amount > relic.dexterityCap) {
                    __instance.amount = relic.dexterityCap; // Cap the Dexterity
                    __instance.updateDescription(); // Update the power description
                }
            }
        }
    }
}
