package botanicamod.relics.common;

import botanicamod.Botanica;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import static botanicamod.Botanica.makeID;

public class ShortCircuit extends BaseRelic {
    // If you do not play any Skills during your turn, gain an extra Energy next turn.

    private static final String NAME = "Short_Circuit"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.COMMON; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.FLAT; // The sound played when the relic is clicked.

    public ShortCircuit() {
        super(ID, NAME, RARITY, SOUND);
    }

    private boolean gainEnergyNext = false;
    private boolean firstTurn = false;

    public void atPreBattle() {
        this.flash();
        this.firstTurn = true;
        this.gainEnergyNext = true;
        if (!this.pulse) {
            this.beginPulse();
            this.pulse = true;
        }
    }

    public void atTurnStart() {
        this.beginPulse();
        this.pulse = true;
        if (this.gainEnergyNext && !this.firstTurn) {
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new GainEnergyAction(1));
        }

        this.firstTurn = false;
        this.gainEnergyNext = true;
    }

    @Override
    public void onVictory() {
        this.pulse = false;
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.SKILL) {
            this.gainEnergyNext = false;
            this.pulse = false;
        }
    }

    @Override
    public boolean canSpawn() {
        if (Botanica.isRelicEnabled("ShortCircuit")) {
            return (Settings.isEndless || AbstractDungeon.floorNum <= 48);
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
