package botanicamod.relics.shop;

import botanicamod.Botanica;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.Random;

import static botanicamod.Botanica.makeID;

public class GamblersDebt extends BaseRelic {
    // Card rewards have 2 extra cards, but 1 random card is disguised as a curse.
    private static final String NAME = "Gamblers_Debt";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.SHOP;
    private static final LandingSound SOUND = LandingSound.HEAVY;

    public GamblersDebt() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public int changeNumberOfCardsInReward(int numberOfCards) {
        return numberOfCards + 2;
    }

    @Override
    public void onObtainCard(AbstractCard card) {

        int rewardGroupSize = AbstractDungeon.cardRewardScreen.rewardGroup.size();
        if (rewardGroupSize == 0) {
            return; // No cards in reward group, so we can't replace anything
        }

        Random random = new Random();
        if (random.nextInt(rewardGroupSize) == 0) {
            AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(
                    CardLibrary.getCurse().makeCopy(),
                    Settings.WIDTH / 2.0F,
                    Settings.HEIGHT / 2.0F
            ));
            AbstractDungeon.player.masterDeck.removeCard(card);
        }
    }

    @Override
    public boolean canSpawn() {
        if (Botanica.isRelicEnabled("GamblersDebt")) {
            return (Settings.isEndless || AbstractDungeon.floorNum <= 48) && !(AbstractDungeon.getCurrRoom() instanceof ShopRoom);
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
