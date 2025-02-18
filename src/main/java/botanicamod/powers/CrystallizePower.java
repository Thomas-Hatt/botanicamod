package botanicamod.powers;
import botanicamod.Botanica;
import botanicamod.util.TextureLoader;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;

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

        // Images
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("botanicamod/images/powers/large/Crystallize.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("botanicamod/images/powers/Crystallize.png"), 0, 0, 34, 34);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        // Reduce Crystallize by 1
        this.addToBot(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, CrystallizePower.POWER_ID, 1));

        // Give the player an orb slot
        this.addToBot(new IncreaseMaxOrbAction(1));

        // Generate and channel a random orb
        AbstractOrb orb = AbstractOrb.getRandomOrb(true);
        this.addToBot(new ChannelAction(orb));

        // Give the player 1 Dexterity
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 1), 1));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}