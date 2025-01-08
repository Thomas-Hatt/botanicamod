package botanicamod.relics.rare;

import botanicamod.Botanica;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import static botanicamod.Botanica.makeID;

public class Silene extends BaseRelic {
    // Silene - Heal 10% of damage taken at the end of combat.

    private static final String NAME = "Silene"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.RARE; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.MAGICAL; // The sound played when the relic is clicked.

    // Store the amount of damage the player has taken into a relic
    private int damageTaken = 0;

    public Silene() {
        super(ID, NAME, RARITY, SOUND);
    }

    // Reset damageTaken at the start of each combat
    @Override
    public void atBattleStart() {
        damageTaken = 0;
    }

    @Override
    public void onLoseHp(int damageAmount) {
        damageTaken += damageAmount;
    }

    @Override
    public void onVictory() {
        int healAmount = (int) Math.ceil(damageTaken * 0.1);
        if (healAmount > 0) {
            AbstractDungeon.player.heal(healAmount);
            flash();
        }
    }

    @Override
    public boolean canSpawn() {
        if (Botanica.isRelicEnabled("Silene")) {
            return (AbstractDungeon.floorNum <= 48) && !(AbstractDungeon.getCurrRoom() instanceof ShopRoom);
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
