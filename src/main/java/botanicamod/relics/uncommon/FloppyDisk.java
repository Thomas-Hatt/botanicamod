package botanicamod.relics.uncommon;

import botanicamod.Botanica;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.HeatsinkPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static botanicamod.Botanica.makeID;

public class FloppyDisk extends BaseRelic {
    // At the start of each combat, gain the Heat Sinks buff. This relic has a Flux Quest.

    private static final String NAME = "Floppy_Disk"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final AbstractRelic.RelicTier RARITY = AbstractRelic.RelicTier.UNCOMMON; // The relic's rarity.
    private static final AbstractRelic.LandingSound SOUND = LandingSound.MAGICAL; // The sound played when the relic is clicked.

    private int POWERS_PLAYED = 0;
    private int UPGRADE_REQUIREMENT = 10;

    public FloppyDisk() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void onEquip() {
        POWERS_PLAYED = 0;
        this.counter = 0;
    }

    @Override
    public void atBattleStart() {
        this.flash();
        if (POWERS_PLAYED < UPGRADE_REQUIREMENT) {
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new HeatsinkPower(AbstractDungeon.player, 1), 1));
        }
        else {
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new HeatsinkPower(AbstractDungeon.player, 2), 2));
        }
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if (targetCard.type == AbstractCard.CardType.POWER && POWERS_PLAYED < UPGRADE_REQUIREMENT) {
            POWERS_PLAYED += 1;
            this.counter = POWERS_PLAYED;
        }
    }

    @Override
    public boolean canSpawn() {
        if (Botanica.isRelicEnabled("FloppyDisk")) {
            return (AbstractDungeon.floorNum <= 48);
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
