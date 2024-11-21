package botanicamod.relics.shop;

import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import java.util.Random;

import static botanicamod.BasicMod.makeID;

// Gambler's debt - Card rewards have 2 extra cards, but 1 random card is disguised as a curse.

public class GamblersDebt extends BaseRelic {
    private static final String NAME = "Gamblers_Debt"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.SHOP; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.HEAVY; // The sound played when the relic is clicked.

    public GamblersDebt() {
        super(ID, NAME, RARITY, SOUND);
    }

    // Add 2 extra card rewards to the card reward screen
    public int changeNumberOfCardsInReward(int numberOfCards) {
        return numberOfCards + 2;
    }

    // Method to get the number of cards in the card reward screen
    private int getRewardGroupSize()
    {
        if (AbstractDungeon.cardRewardScreen != null && AbstractDungeon.cardRewardScreen.rewardGroup != null)
        {
            return AbstractDungeon.cardRewardScreen.rewardGroup.size();
        }
        return 0;
    }

    // Generate a random number based on the amount of cards in the card reward screen
    public int getRandomRewardIndex()
    {
        int rewardGroupSize = getRewardGroupSize();
        Random random = new Random();
        return random.nextInt(rewardGroupSize);
    }

    public void onMasterDeckChange() {
        AbstractCard lastAddedCard = AbstractDungeon.player.masterDeck.group.get(AbstractDungeon.player.masterDeck.group.size() - 1);

        int randomIndex = getRandomRewardIndex();
        System.out.println("Random index: " + randomIndex);

        // Check to see if the random number is equal to the minimum number
        if (randomIndex == 0) {
            // Remove the card from the player's deck
            AbstractDungeon.player.masterDeck.removeCard(lastAddedCard);

            // Add the random curse to the player's deck
            AbstractDungeon.player.masterDeck.addToTop(CardLibrary.getCurse().makeCopy());
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
