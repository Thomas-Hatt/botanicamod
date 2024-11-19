package botanicamod.relics.boss;

import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static botanicamod.BasicMod.makeID;

// Glimmering Orb - The first time you play 5 Power Cards each combat, Gain 1 Permanent Strength
public class GlimmeringOrb extends BaseRelic {

    private static final String NAME = "Glimmering_Orb"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final AbstractRelic.RelicTier RARITY = RelicTier.BOSS; // The relic's rarity.
    private static final AbstractRelic.LandingSound SOUND = AbstractRelic.LandingSound.CLINK; // The sound played when the relic is clicked.

    // How many power cards the player needs to play to gain 1 permanent strength
    private static final int Power_Threshold = 5;

    // Count how many power cards the player has played
    private int powerCardCount = 0;

    // Track how much strength the player has gained from this relic
    private int GlimmeringOrbStrength = 0;

    // Check if the strength has already been given this combat
    private boolean strengthAwarded = false;

    public GlimmeringOrb() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void atBattleStart() {
        this.flash();

        // Reset the power card count
        powerCardCount = 0;

        // Reset strength awarded to be false
        strengthAwarded = false;

        // Apply the amount of strength the player has gained from this relic
        if (GlimmeringOrbStrength > 0) {
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, GlimmeringOrbStrength)));
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction UseCardAction) {
        // Check to see if the player played a power card
        if (card.type == AbstractCard.CardType.POWER) {
            // Increase the power card counter
            powerCardCount++;

            // If the amount of powers played in the combat is over the power threshold
            // And the player hasn't already gained 1 permanent strength this combat
            // Give the player 1 permanent strength
            if (powerCardCount >= Power_Threshold && !strengthAwarded) {
                // Reset the power card count
                powerCardCount = 0;

                // Give the player 1 strength
                GlimmeringOrbStrength++;

                // Ensure the player can't get any more strength this combat
                strengthAwarded = true;

                // Give the 1 strength to the player *this* combat
                this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1)));
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + Power_Threshold + this.DESCRIPTIONS[1];
    }
}