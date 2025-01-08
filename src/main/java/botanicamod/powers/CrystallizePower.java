package botanicamod.powers;
//The only thing TURN_BASED controls is the color of the number on the power icon.
//Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
//For a power to actually decrease/go away on its own they do it themselves.
//Look at powers that do this like VulnerablePower and DoubleTapPower.
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static botanicamod.Botanica.makeID;

public class CrystallizePower extends BasePower {
    public static final String POWER_ID = makeID("Crystallize");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final boolean TURN_BASED = false;

    public CrystallizePower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
        updateDescription();
        loadRegion("crystallize"); // Ensure this region matches the graphical asset
    }

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
}