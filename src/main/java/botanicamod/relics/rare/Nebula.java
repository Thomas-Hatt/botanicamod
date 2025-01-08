package botanicamod.relics.rare;

import botanicamod.Botanica;
import botanicamod.relics.BaseRelic;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.BurstPower;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import static botanicamod.Botanica.makeID;

// If you end your turn without Block, your next skill is played twice.
public class Nebula extends BaseRelic {
    private static final String NAME = "Nebula"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.RARE; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.FLAT; // The sound played when the relic is clicked.

    public boolean triggerNextSkill = false;

    public Nebula() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public int onPlayerGainedBlock(float blockAmount) {
        if (blockAmount > 0.0F) {
            this.stopPulse();
        }
        return MathUtils.floor(blockAmount);
    }

    @Override
    public void onVictory() {
        this.stopPulse();
    }

    @Override
    public void onPlayerEndTurn() {
        // Check to see if the player has 0 block or if triggerNextSkill is equal to true
        if (AbstractDungeon.player.currentBlock == 0) {
            this.flash();
            this.stopPulse();
            this.triggerNextSkill = true; // Set flag to trigger double skill play
        }
    }

    @Override
    public void atTurnStart() {
        if (this.triggerNextSkill) {
            this.triggerNextSkill = false;
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BurstPower(AbstractDungeon.player, 1), 1));
        }
    }

    @Override
    public BaseRelic makeCopy() {
        return new Nebula();
    }

    @Override
    public boolean canSpawn() {
        if (Botanica.isRelicEnabled("Nebula")) {
            return (AbstractDungeon.floorNum <= 48) && !(AbstractDungeon.getCurrRoom() instanceof ShopRoom);
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}