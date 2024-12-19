package botanicamod.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static botanicamod.BasicMod.makeID;

public class CrystallizePower extends BasePower {

//    private static final PowerStrings powerStrings;
//    public static final String NAME;
//    public static final String[] DESCRIPTIONS;

    public static final String POWER_ID = makeID("Crystallize");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.

    public CrystallizePower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

//    public CrystallizePower(AbstractCreature owner, int amount) {
//        this.ID = POWER_ID;
//        this.owner = owner;
//        this.amount = amount;
//        updateDescription();
//        this.loadRegion("strength");
//        this.canGoNegative = false;
//        this.type = PowerType.BUFF;
//    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        // Give the player an orb slot
        this.addToBot(new IncreaseMaxOrbAction(1));

        // Generate and channel a random orb
        AbstractOrb orb = AbstractOrb.getRandomOrb(true);
        this.addToBot(new ChannelAction(orb));

        this.addToBot(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, CrystallizePower.POWER_ID, 1));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

//    static {
//        powerStrings = CardCrawlGame.languagePack.getPowerStrings("Crystallize");
//        NAME = powerStrings.NAME;
//        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
//    }
}