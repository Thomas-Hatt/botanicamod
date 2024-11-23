package botanicamod.patches;

import botanicamod.relics.boss.HandOfMidas;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.colorless.HandOfGreed;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class HandOfMidas_Patched {
    @SpirePatch(clz = HandOfGreed.class, method = SpirePatch.CONSTRUCTOR)
    public static class DoubleInitialValue {
        @SpirePostfixPatch
        public static void Postfix(HandOfGreed __instance) {
            if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(HandOfMidas.ID)) {
                __instance.baseMagicNumber *= 2;
                __instance.magicNumber = __instance.baseMagicNumber;
            }
        }
    }

    @SpirePatch(clz = HandOfGreed.class, method = "upgrade")
    public static class DoubleUpgrade {
        @SpirePrefixPatch
        public static void Prefix(HandOfGreed __instance) {
            if (!__instance.upgraded && AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(HandOfMidas.ID)) {
                __instance.baseMagicNumber += 5;
            }
        }
    }
}
