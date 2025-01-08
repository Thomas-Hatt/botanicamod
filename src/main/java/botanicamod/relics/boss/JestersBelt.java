package botanicamod.relics.boss;

import botanicamod.Botanica;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import static botanicamod.Botanica.makeID;

public class JestersBelt extends BaseRelic {
    // Jester's Belt - Gain 1 Energy at the start of each turn. Gain 1 Dexterity for every 3 cards played in a turn, but lose 2 Dexterity if you play a Power card. Dexterity is now capped at 8.

    private static final String NAME = "Jesters_Belt"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.BOSS; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.FLAT; // The sound played when the relic is clicked.

    // Dexterity cap
    private final int dexterityCap = 8;

    // Cards played requirement
    private final int cardRequirement = 3;

    // How much Dexterity is reduced if the player uses a power card
    private final int dexterityReductionAmount = -2;

    // How much Dexterity the player gets for every X cards played
    private final int dexterityGainAmount = 1;

    public JestersBelt() {
        super(ID, NAME, RARITY, SOUND);
    }

    // Give the player +1 energy
    @Override
    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster++;
    }

    // Remove the energy gained from the relic
    @Override
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster--;
    }

    // Reset the counter
    @Override
    public void atTurnStart() {
        this.counter = 0;
    }

    public void reduceDexterity() {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractPower dexterityPower = AbstractDungeon.player.getPower(DexterityPower.POWER_ID);
        if (dexterityPower != null) {
            int amountToReduce = dexterityPower.amount - dexterityCap;
            if (amountToReduce > 0) {
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, amountToReduce), amountToReduce));
            }
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        ++this.counter;
        AbstractPower dexterityPower = AbstractDungeon.player.getPower(DexterityPower.POWER_ID);
        // Check if the number of cards played meets the requirement and if the dexterity is below the cap
        if (this.counter % cardRequirement == 0 && (dexterityPower == null || dexterityPower.amount < dexterityCap) && card.type != AbstractCard.CardType.POWER) {
            this.counter = 0;
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, dexterityGainAmount), dexterityGainAmount));
        }

        // Check if a power card was used
        if (card.type == AbstractCard.CardType.POWER) {
            this.counter = 0;
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, dexterityReductionAmount), dexterityReductionAmount));
        }
    }

    @Override
    public boolean canSpawn() {
        if (Botanica.isRelicEnabled("JestersBelt")) {
            return (Settings.isEndless || AbstractDungeon.floorNum <= 48) && !(AbstractDungeon.getCurrRoom() instanceof ShopRoom);
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + dexterityGainAmount + DESCRIPTIONS[1] + cardRequirement + DESCRIPTIONS[2] + -dexterityReductionAmount + DESCRIPTIONS[3] + dexterityCap;
    }
}