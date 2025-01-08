package botanicamod.potions.rare;

import botanicamod.Botanica;
import botanicamod.potions.BasePotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.SlowPower;

import static botanicamod.Botanica.makeID;

public class PotionOfSlowness extends BasePotion {
        public static final String ID = makeID(PotionOfSlowness.class.getSimpleName());

        private static final Color LIQUID_COLOR = CardHelper.getColor(56,76,55);
        private static final Color HYBRID_COLOR = CardHelper.getColor(56,76,55);
        private static final Color SPOTS_COLOR = CardHelper.getColor(56,76,55);

    public PotionOfSlowness() {
        super(ID, 0, PotionRarity.RARE, PotionSize.GHOST, LIQUID_COLOR, HYBRID_COLOR, SPOTS_COLOR);
        isThrown = true;
        targetRequired = true;
    }

    @Override
    public void use(AbstractCreature target) {
        addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new SlowPower(target, this.potency), this.potency));
    }

    @Override
    public void addAdditionalTips() {
        //Adding a tooltip for Slow
        this.tips.add(new PowerTip(Botanica.keywords.get("slow").PROPER_NAME, Botanica.keywords.get("slow").DESCRIPTION));
    }

    @Override
    public String getDescription() {
        return DESCRIPTIONS[0];
    }
}