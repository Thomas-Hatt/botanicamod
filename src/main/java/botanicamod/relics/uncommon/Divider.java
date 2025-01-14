package botanicamod.relics.uncommon;

import botanicamod.Botanica;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static botanicamod.Botanica.makeID;

public class Divider extends BaseRelic {
    // Every 4 turns, add a random skill into your hand.

    private static final String NAME = "Divider"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.UNCOMMON; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.CLINK; // The sound played when the relic is clicked.

    // Create a variable to be used for the turn counter
    private final int turnCounter = 4;

    public Divider() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    // Set the relic to 0 when picked up
    public void onEquip() {
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        if (this.counter == -1) {
            this.counter += 2;
        } else {
            ++this.counter;
        }

        if (this.counter == turnCounter) {
            this.counter = 0;
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

            AbstractCard c = AbstractDungeon.returnTrulyRandomCardInCombat(AbstractCard.CardType.SKILL).makeCopy();
            this.addToBot(new MakeTempCardInHandAction(c, true));
        }
    }

    @Override
    public boolean canSpawn() {
        if (Botanica.isRelicEnabled("Divider")) {
            return (AbstractDungeon.floorNum <= 48);
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + turnCounter + DESCRIPTIONS[1];
    }
}