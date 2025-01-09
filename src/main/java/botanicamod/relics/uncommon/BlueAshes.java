package botanicamod.relics.uncommon;

import botanicamod.Botanica;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FireBreathingPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import static botanicamod.Botanica.makeID;

public class BlueAshes extends BaseRelic {
    // Blue Ashes (as suggested by blueash7077!) - At the start of each combat, gain the Fire Breathing buff.
    // Blue Ashes Side Quest - Upon drawing 20 Status Cards, gain the Fire Breathing+ buff instead.

    private static final String NAME = "Blue_Ashes"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final AbstractRelic.RelicTier RARITY = AbstractRelic.RelicTier.UNCOMMON; // The relic's rarity.
    private static final AbstractRelic.LandingSound SOUND = LandingSound.MAGICAL; // The sound played when the relic is clicked.

    // Store the amount of statuses drawn
    private int STATUSES_DRAWN = 0;

    public BlueAshes() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void onEquip() {
        STATUSES_DRAWN = 0;
    }

    @Override
    public void atBattleStart() {
        this.flash();
        if (STATUSES_DRAWN < 20) {
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FireBreathingPower(AbstractDungeon.player, 6), 6));
        }
        else {
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FireBreathingPower(AbstractDungeon.player, 10), 10));
        }
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (card.type == AbstractCard.CardType.STATUS) {
            STATUSES_DRAWN += 1;
        }
    }

    @Override
    public boolean canSpawn() {
        if (Botanica.isRelicEnabled("BlueAshes")) {
            return (AbstractDungeon.floorNum <= 48);
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}