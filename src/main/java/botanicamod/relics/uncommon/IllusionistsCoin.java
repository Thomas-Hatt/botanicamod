package botanicamod.relics.uncommon;

import botanicamod.Botanica;
import botanicamod.cards.IllusionaryNeutralize;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import static botanicamod.Botanica.makeID;

public class IllusionistsCoin extends BaseRelic {
    // Illusionist's Coin - At the start of turn 3, add a copy of Illusion Neutralize to your hand.
    // Side Quest - Every 3rd time you play Illusion Neutralize, Draw 3 cards.

    private static final String NAME = "Illusionists_Coin";  // Name of the relic
    public static final String ID = makeID(NAME);  // Unique ID for the relic
    private static final RelicTier RARITY = RelicTier.UNCOMMON;  // Rarity of the relic
    private static final LandingSound SOUND = LandingSound.MAGICAL;  // Sound effect for the relic

    private static final int TURN_REQUIREMENT = 3;  // Turn requirement to add Purge Neutralize
    private static final int AMOUNT = 1;  // Amount of Purge Neutralize to add
    private static final int DRAW_AMOUNT = 3;  // Number of cards to draw for the side quest
    private static final int REQUIRED_PURGES = 3;  // Number of Purge Neutralizes to play for side quest

    // Store the amount of Purge Neutralizes played
    private int purgeNeutralizePlayed = 0;

    public IllusionistsCoin() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void onEquip() {
        purgeNeutralizePlayed = 0;  // Initialize purgeNeutralizePlayed to 0 when the relic is equipped
        this.counter = 0;
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;  // Initialize counter to 0 at the start of a battle
        this.grayscale = false;  // Ensure grayscale is false at the start of a battle
    }

    @Override
    public void atTurnStart() {
        if (!this.grayscale) {
            this.counter++;  // Increment counter each turn if the relic is not in grayscale mode
        }

        if (this.counter == TURN_REQUIREMENT) {
            this.flash();  // Flash the relic to indicate activation
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));  // Show relic effect above player
            this.addToBot(new MakeTempCardInHandAction(new IllusionaryNeutralize(), AMOUNT, false));  // Add a Purge Neutralize to the hand
            this.counter = -1;  // Reset counter after using
            this.grayscale = true;
        }
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        // Check if the played card is Illusionary Neutralize
        if (card.cardID.equals(IllusionaryNeutralize.ID)) {

            // Increment the counter for Purge Neutralizes played
            purgeNeutralizePlayed++;
            getUpdatedDescription();

            // Check if the required number of Purge Neutralizes have been played
            if (purgeNeutralizePlayed == REQUIRED_PURGES) {
                this.flash();  // Flash the relic to indicate activation
                this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));  // Show relic effect above player
                this.addToBot(new DrawCardAction(AbstractDungeon.player, DRAW_AMOUNT));  // Draw the specified number of cards
                purgeNeutralizePlayed = 0;  // Reset the counter after drawing cards
            }
        }
    }

    @Override
    public boolean canSpawn() {
        if (Botanica.isRelicEnabled("IllusionistsCoin")) {
            return (Settings.isEndless || AbstractDungeon.floorNum <= 48);
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + TURN_REQUIREMENT + DESCRIPTIONS[1];  // Return the updated description for the relic
    }
}
