package botanicamod.relics.common;

import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import static botanicamod.BasicMod.makeID;

public class MerchantsRobes extends BaseRelic {
    // Upon entering a shop, obtain a Potion and a Card reward.

    private static final String NAME = "Merchants_Robes"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.COMMON; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.FLAT; // The sound played when the relic is clicked.

    public MerchantsRobes() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (room instanceof ShopRoom) {
            this.flash();

            // Add potions to rewards
            for (int i = 0; i < 1; i++) {
                AbstractDungeon.getCurrRoom().addPotionToRewards(PotionHelper.getRandomPotion());
            }

            // Find and remove card reward
            AbstractDungeon.getCurrRoom().rewards.removeIf(reward -> reward.type == RewardItem.RewardType.CARD);

            // Open reward screen
            if (this.DESCRIPTIONS.length > 1) {
                AbstractDungeon.combatRewardScreen.open(this.DESCRIPTIONS[1]);
            } else {
                AbstractDungeon.combatRewardScreen.open();
            }
            AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.0F;
        }
    }



    @Override
    public boolean canSpawn() {
        return (Settings.isEndless || AbstractDungeon.floorNum <= 48) && !(AbstractDungeon.getCurrRoom() instanceof ShopRoom);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
