package botanicamod.relics.rare;

import botanicamod.Botanica;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.SpotlightPlayerEffect;

import static botanicamod.Botanica.makeID;

public class Marigold extends BaseRelic {
    // Marigold - Gain gold equal to 25% of the damage dealt in combat, but only for the first 100 damage dealt. For every 100 Gold gained from this effect, draw one card at the start of combat.

    private static final String NAME = "Marigold"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.RARE; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.FLAT; // The sound played when the relic is clicked.

    // Store damage dealt in a variable
    private int damageDealt = 0;

    // Store the total amount of gold gained in a variable
    private int totalGoldGained = 0;

    public Marigold() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void onUnequip() {
        // Reset the variables to 0
        totalGoldGained = 0;
        damageDealt = 0;

        super.onUnequip();
    }

    @Override
    public void atBattleStart() {
        // Reset damageDealt at the start of each combat
        damageDealt = 0;

        // Check how many cards to draw based on total gold gained
        int cardsToDraw = (int) Math.floor((float) totalGoldGained / 100);  // Draw one card for every 100 gold gained
        if (cardsToDraw > 0) {
            for (int i = 0; i < cardsToDraw; i++) {
                AbstractDungeon.player.draw(); // Draw a card
            }
            this.flash();
        }
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageDealt < 100) {
            int allowedDamage = Math.min(damageAmount, 100 - damageDealt); // Calculate how much can be added without exceeding 100
            damageDealt += allowedDamage; // Track total damage dealt up to a cap of 100

            System.out.println("Damage Dealt: " + damageDealt); // Debug output
        }
    }

    @Override
    public void onVictory() {
        // Get 25% of the damage done in gold
        int goldGain = (int) Math.ceil(damageDealt * 0.25);
        if (goldGain >= 1) {
            AbstractDungeon.effectList.add(new RainingGoldEffect(goldGain, true));
            AbstractDungeon.effectsQueue.add(new SpotlightPlayerEffect());
            AbstractDungeon.player.gainGold(goldGain);
            totalGoldGained += goldGain;
            System.out.println("Gold Gained: " + goldGain + ", Total Gold: " + totalGoldGained); // Debug output
            this.flash();
        }
    }

    @Override
    public boolean canSpawn() {
        if (Botanica.isRelicEnabled("Marigold")) {
            return (AbstractDungeon.floorNum <= 48);
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
