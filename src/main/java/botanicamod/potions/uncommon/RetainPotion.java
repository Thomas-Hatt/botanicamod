package botanicamod.potions.uncommon;

import botanicamod.potions.BasePotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static botanicamod.BasicMod.makeID;

public class RetainPotion extends BasePotion {
    // Retain your hand for X turns.

    public static final String ID = makeID(RetainPotion.class.getSimpleName());

    private static final Color LIQUID_COLOR = CardHelper.getColor(101,205,244);
    private static final Color HYBRID_COLOR = CardHelper.getColor(101,205,244);
    private static final Color SPOTS_COLOR = CardHelper.getColor(101,205,244);

    public RetainPotion() {
        super(ID, 1, AbstractPotion.PotionRarity.RARE, PotionSize.BOTTLE, LIQUID_COLOR, HYBRID_COLOR, SPOTS_COLOR);
        isThrown = false;
    }

    @Override
    public void use(AbstractCreature target) {
        if ((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EquilibriumPower(AbstractDungeon.player, this.potency), this.potency));
        }
    }

    @Override
    public String getDescription() {
        // Check to see if the potency is 1, to make sure turns is not plural.
        if (potency == 1) {
            return DESCRIPTIONS[2];
        }
        else {
            return DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];
        }
    }
}