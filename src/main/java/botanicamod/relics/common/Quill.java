package botanicamod.relics.common;

import botanicamod.Botanica;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static botanicamod.Botanica.makeID;

public class Quill extends BaseRelic {
    // At the start of each combat, add a random 0 cost card to your hand, discard pile, and draw pile.

    private static final String NAME = "Quill"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.COMMON; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.FLAT; // The sound played when the relic is clicked.

    public Quill() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void atBattleStart() {
        AbstractCard randomZeroCostCard = getRandomZeroCostCard();
        if (randomZeroCostCard != null) {
            addToBot(new MakeTempCardInHandAction(randomZeroCostCard.makeStatEquivalentCopy(), 1));
            addToBot(new MakeTempCardInDiscardAction(randomZeroCostCard.makeStatEquivalentCopy(), 1));
            addToBot(new MakeTempCardInDrawPileAction(randomZeroCostCard.makeStatEquivalentCopy(), 1, true, true));
        }
    }

    private AbstractCard getRandomZeroCostCard() {
        ArrayList<AbstractCard> zeroCostCards = CardLibrary.getAllCards().stream()
                .filter(c -> c.cost == 0)
                .collect(Collectors.toCollection(ArrayList::new));
        return zeroCostCards.isEmpty() ? null : zeroCostCards.get(AbstractDungeon.cardRandomRng.random(zeroCostCards.size() - 1));
    }

    @Override
    public boolean canSpawn() {
        if (Botanica.isRelicEnabled("Quill")) {
            return (Settings.isEndless || AbstractDungeon.floorNum <= 48) && !(AbstractDungeon.getCurrRoom() instanceof ShopRoom);
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
