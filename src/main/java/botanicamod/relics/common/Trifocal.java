package botanicamod.relics.common;

import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static botanicamod.BasicMod.makeID;

public class Trifocal extends BaseRelic {
    // Trifocal - At the end of your first turn, gain 5 gold for each card discarded, then Scry 3.

    private static final String NAME = "Trifocal";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.COMMON;
    private static final LandingSound SOUND = LandingSound.CLINK;

    private boolean firstTurn = true;
    private final int scryAmount = 3;
    private final int goldPerDiscardedCard = 5;

    public Trifocal() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void onPlayerEndTurn() {
        if (firstTurn) {
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

            // Scry X
            addToBot(new ScryAction(scryAmount));

            int discardedCards = GameActionManager.totalDiscardedThisTurn;

            // Check if cards were discarded
            if (discardedCards > 0) {
                int goldGain = discardedCards * goldPerDiscardedCard;

                // Give the player gold
                AbstractDungeon.player.gainGold(goldGain);
            }
            firstTurn = false;
        }
    }

    @Override
    public void atPreBattle() {
        firstTurn = true;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + goldPerDiscardedCard + DESCRIPTIONS[1] + scryAmount + DESCRIPTIONS[2];
    }
}

