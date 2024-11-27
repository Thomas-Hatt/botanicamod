package botanicamod.relics.uncommon;

import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.Iterator;

import static botanicamod.BasicMod.makeID;

public class TerrifyingTrinket extends BaseRelic {
    // Terrifying Trinket - At the start of turn 3, all Minions run away.

    // Gremlin Leader

    private static final int TURN_ACTIVATION = 3;

    private static final String NAME = "Terrifying_Trinket"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final AbstractRelic.RelicTier RARITY = AbstractRelic.RelicTier.UNCOMMON; // The relic's rarity.
    private static final AbstractRelic.LandingSound SOUND = LandingSound.MAGICAL; // The sound played when the relic is clicked.

    public TerrifyingTrinket() {
        super(ID, NAME, RARITY, SOUND);
    }

    public void atTurnStart() {
        if (!this.grayscale)
            this.counter++;

        if (this.counter == 2) {
            this.beginLongPulse();
        }
        if (this.counter == 3) {
            this.stopPulse();
            this.flash();
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!m.isDying && m.hasPower("Minion")) {
                    AbstractDungeon.actionManager.addToBottom(new EscapeAction(m));
                }
            }
            this.counter = -1;
            this.grayscale = true;
        }
    }

    public void onVictory() {
        this.counter = -1;
        this.grayscale = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
