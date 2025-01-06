package botanicamod.relics.boss;

import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static botanicamod.BasicMod.makeID;

// Thorned Crown - Start each combat with 4 Thorns for each boss relic you have

public class ThornedCrown extends BaseRelic
{
    private static final String NAME = "Thorned_Crown"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.BOSS; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.FLAT; // The sound played when the relic is clicked.

    public ThornedCrown() {
        super(ID, NAME, RARITY, SOUND);
    }

    public void atBattleStart() {
        this.flash();

        // Check to see how many boss relics the player has
        long bossRelicCount = AbstractDungeon.player.relics.stream()
                .filter(r -> r.tier == AbstractRelic.RelicTier.BOSS)
                .count();

        // Multiply the count by 4
        int thornsAmount = (int) bossRelicCount * 4;

        // Apply thorns equal to twice the amount of boss relics that the player has
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ThornsPower(AbstractDungeon.player, thornsAmount), thornsAmount));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}