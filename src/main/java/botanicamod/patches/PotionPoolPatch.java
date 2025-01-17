package botanicamod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import java.util.ArrayList;

import static botanicamod.Botanica.editPotionPool;

@SpirePatch2(clz = PotionHelper.class, method = "getPotions")
public class PotionPoolPatch {

    @SpirePostfixPatch
    public static void addPotions(ArrayList<String> __result, AbstractPlayer.PlayerClass c, boolean getAll) {
        if (!getAll) {
            editPotionPool(__result);
        }

        System.out.println("Potion Pool before modification for class " + c + ": " + __result);

        // Add potions as expected
        editPotionPool(__result);

        // Remove potions with invalid IDs or placeholders
        __result.removeIf((id) -> {
            AbstractPotion p = PotionHelper.getPotion(id);
            return (p == null || p.rarity == AbstractPotion.PotionRarity.PLACEHOLDER);
        });

        System.out.println("Potion Pool after modification: " + __result);
    }
}