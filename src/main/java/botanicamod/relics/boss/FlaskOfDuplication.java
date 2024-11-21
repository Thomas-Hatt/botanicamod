package botanicamod.relics.boss;

import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;
import java.util.Iterator;

import static botanicamod.BasicMod.makeID;

public class FlaskOfDuplication extends BaseRelic {
    // Flask of Duplication - Upon pickup, gain 2 copies of Normality. Duplicate every card in your deck and gain 10 gold for each card added.

    private static final String NAME = "Flask_Of_Duplication"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.BOSS; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.CLINK; // The sound played when the relic is clicked.

    public FlaskOfDuplication() {
        super(ID, NAME, RARITY, SOUND);
    }

    public void onEquip() {

        // Create an iterator to look through the player's master deck
        Iterator<AbstractCard> i = AbstractDungeon.player.masterDeck.group.iterator();
        ArrayList<AbstractCard> duplicates = new ArrayList<>();

        while (i.hasNext()) {
            AbstractCard card = i.next();

            // Make a copy of the card
            AbstractCard duplicateCard = card.makeCopy();

            // Optional: Mark the duplicated card as seen
            UnlockTracker.markCardAsSeen(duplicateCard.cardID);
            duplicateCard.isSeen = true;

            // Add the duplicated card to the list
            duplicates.add(duplicateCard);
        }

        // Add all duplicated cards to the master deck and give the player 10 gold for each card added
        for (AbstractCard card : duplicates) {
            AbstractDungeon.player.masterDeck.addToTop(card);
            AbstractDungeon.player.gainGold(10);
        }

        // Add 2 copies of Normality to the player's master deck
        for (int e = 0; e < 2; e++) {
            Normality card = new Normality();
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
