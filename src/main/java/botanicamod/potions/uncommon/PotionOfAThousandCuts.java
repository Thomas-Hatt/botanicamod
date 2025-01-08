package botanicamod.potions.uncommon;

import botanicamod.potions.BasePotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.powers.ThousandCutsPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static botanicamod.Botanica.makeID;

public class PotionOfAThousandCuts extends BasePotion {
    // Whenever you play a card, deal 1 (2) damage to ALL enemies.

    public static final String ID = makeID(PotionOfAThousandCuts.class.getSimpleName());

    private static final Color LIQUID_COLOR = CardHelper.getColor(213,81,19);
    private static final Color HYBRID_COLOR = CardHelper.getColor(92,43,28);
    // private static final Color SPOTS_COLOR = CardHelper.getColor(92,43,28);

    public PotionOfAThousandCuts() {
        super(ID, 1, PotionRarity.RARE, PotionSize.EYE, LIQUID_COLOR, HYBRID_COLOR, null);
        isThrown = false;
    }

    @Override
    public void use(AbstractCreature target) {
        if ((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ThousandCutsPower(AbstractDungeon.player, this.potency), this.potency));
        }
    }

    @Override
    public String getDescription() {
        return DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];
    }
}