package botanicamod.relics.boss;

import botanicamod.Botanica;
import botanicamod.relics.BaseRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import static botanicamod.Botanica.makeID;

public class JestersBelt extends BaseRelic {
    private static final String NAME = "Jesters_Belt";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.BOSS;
    private static final LandingSound SOUND = LandingSound.FLAT;

    // Dexterity cap
    public final int dexterityCap = 8;

    // Cards played requirement
    private final int cardRequirement = 3;

    // How much Dexterity is reduced if the player uses a power card
    private final int dexterityReductionAmount = -2;

    // How much Dexterity the player gets for every X cards played
    private final int dexterityGainAmount = 1;

    public JestersBelt() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster++;
    }

    @Override
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster--;
    }

    @Override
    public void atTurnStart() {
        this.counter = 0;
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