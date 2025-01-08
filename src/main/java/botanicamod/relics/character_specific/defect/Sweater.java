package botanicamod.relics.character_specific.defect;

import botanicamod.Botanica;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import static botanicamod.Botanica.makeID;

public class Sweater extends BaseRelic {
    // At the start of each combat, Channel 2 Frost. Gain 1 Focus every 3 turns.

    private static final String NAME = "Sweater"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.RARE; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.FLAT; // The sound played when the relic is clicked.

    public Sweater() {
        super(ID, NAME, AbstractCard.CardColor.BLUE, RARITY, SOUND);
    }

    public void onEquip() {
        this.counter = 0;
    }

    public void atPreBattle() {
        AbstractDungeon.player.channelOrb(new Frost());
        AbstractDungeon.player.channelOrb(new Frost());
    }

    public void atTurnStart() {
        if (this.counter == -1) {
            this.counter += 2;
        } else {
            ++this.counter;
        }

        if (this.counter == 3) {
            this.counter = 0;
            this.flash();

            // Give the player 1 Focus
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, 1), 1));
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    @Override
    public boolean canSpawn() {
        if (Botanica.isRelicEnabled("Sweater")) {
            return (Settings.isEndless || AbstractDungeon.floorNum <= 48) && !(AbstractDungeon.getCurrRoom() instanceof ShopRoom);
        }
        return false;
    }


    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}