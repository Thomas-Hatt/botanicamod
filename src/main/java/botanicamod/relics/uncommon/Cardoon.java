package botanicamod.relics.uncommon;

import botanicamod.Botanica;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.JackOfAllTrades;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import static botanicamod.Botanica.makeID;

public class Cardoon extends BaseRelic {
    // Cardoon - Every time you play 4 Attacks in a single turn, shuffle a Jack of All Trades into your draw pile.

    private static final String NAME = "Cardoon"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.UNCOMMON; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.CLINK; // The sound played when the relic is clicked.

    public Cardoon() {
        super(ID, NAME, RARITY, SOUND);
    }

    // Reset the counter
    @Override
    public void atTurnStart() {
        this.counter = 0;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            this.counter++;
            // Create a variable to be used for the attack requirement
            int attackRequirement = 4;
            if (this.counter % attackRequirement == 0) {
                this.counter = 0;
                flash();
                this.addToBot(new MakeTempCardInDrawPileAction(new JackOfAllTrades(), 1, true, true));
            }
        }
    }

    public void onVictory() {
        this.counter = -1;
    }

    @Override
    public boolean canSpawn() {
        if (Botanica.isRelicEnabled("Cardoon")) {
            return (Settings.isEndless || AbstractDungeon.floorNum <= 48) && !(AbstractDungeon.getCurrRoom() instanceof ShopRoom);
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
