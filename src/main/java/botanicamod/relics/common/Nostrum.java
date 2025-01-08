package botanicamod.relics.common;

import botanicamod.Botanica;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import static botanicamod.Botanica.makeID;

public class Nostrum extends BaseRelic {
    // At the end of your first turn, Exhaust any number of cards in your hand

    private static final String NAME = "Nostrum";
    public static final String ID = makeID(NAME);
    private static final AbstractRelic.RelicTier RARITY = AbstractRelic.RelicTier.COMMON;
    private static final AbstractRelic.LandingSound SOUND = AbstractRelic.LandingSound.FLAT;

    private boolean firstTurn = true;

    public Nostrum() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void onPlayerEndTurn() {
        if (firstTurn) {
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new ExhaustAction(99, false, true, true));
            firstTurn = false;
        }
    }

    @Override
    public void atPreBattle() {
        firstTurn = true;
    }

    @Override
    public boolean canSpawn() {
        if (Botanica.isRelicEnabled("Nostrum")) {
            return (Settings.isEndless || AbstractDungeon.floorNum <= 48) && !(AbstractDungeon.getCurrRoom() instanceof ShopRoom);
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
