package botanicamod.relics.rare;


// Gambler's debt - Card rewards have 2 extra cards, but 1 random card is disguised as a curse.


import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.cards.curses.Clumsy;
import com.megacrit.cardcrawl.cards.curses.Decay;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.cards.curses.Injury;
import com.megacrit.cardcrawl.cards.curses.Pain;
import com.megacrit.cardcrawl.cards.curses.Parasite;
import com.megacrit.cardcrawl.cards.curses.Regret;
import com.megacrit.cardcrawl.cards.curses.Shame;
import com.megacrit.cardcrawl.cards.curses.Writhe;

import java.util.Random;

import static botanicamod.BasicMod.makeID;

public class GamblersDebt extends BaseRelic {
    private static final String NAME = "Gamblers_Debt"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.RARE; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.FLAT; // The sound played when the relic is clicked.

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

    // When the user click a card, there is a random chance for the card to turn into a curse
    @Override
    public void onObtainCard(AbstractCard abstractCard) {
        System.out.println("onObtainCard called with card: " + abstractCard.name);

        // Assign a variable equal to the random number
        int randomIndex = getRandomRewardIndex();
        System.out.println("Random index: " + randomIndex);

        // Check to see if the random number is equal to the minimum number
        if (randomIndex == 0) {
            // Remove the card from the player's deck
            AbstractDungeon.player.masterDeck.removeCard(abstractCard);
            System.out.println("Card removed: " + abstractCard.name);

            // Create a new random curse card
            AbstractCard curseCard = getRandomCurseCard();

            // Add the random curse to the player's deck
            AbstractDungeon.player.masterDeck.addToTop(curseCard);
            System.out.println("Added curse card: " + curseCard.name);
        }
    }

    // Helper method to get a random curse card
    private AbstractCard getRandomCurseCard()
    {
        AbstractCard[] curses = { new Normality(), new Clumsy(), new Decay(), new Doubt(), new Injury(),
                new Pain(), new Parasite(), new Regret(), new Shame(), new Writhe() };
        Random random = new Random();
        return curses[random.nextInt(curses.length)];
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
