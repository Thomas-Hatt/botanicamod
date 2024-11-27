package botanicamod.relics.boss;

import botanicamod.actions.DragonHeartAction;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static botanicamod.BasicMod.makeID;

// Dragon Heart - Gain 2 Strength and 2 Dexterity at the start of each combat. Retain a random card at the end of your turn.

public class DragonHeart extends BaseRelic {
    private static final String NAME = "Dragon_Heart"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.BOSS; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.CLINK; // The sound played when the relic is clicked.

    // Strength variable
    private static final int STR = 2;

    // Dexterity variable
    private static final int DEX = 2;

    public DragonHeart() {
        super(ID, NAME, RARITY, SOUND);
    }

    public void onPlayerEndTurn() {
        // Retain a random card at the end of the player's turn
        AbstractDungeon.actionManager.addToBottom(new DragonHeartAction());
    }

    public void atBattleStart() {
        this.flash();

        // Add the Strength
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, STR), STR));

        // Add the Dexterity
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, DEX), DEX));

        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public String getUpdatedDescription()
    {
        return this.DESCRIPTIONS[0] + STR + this.DESCRIPTIONS[1] + DEX + this.DESCRIPTIONS[2];
    }
}
