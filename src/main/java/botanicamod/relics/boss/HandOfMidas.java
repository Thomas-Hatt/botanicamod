package botanicamod.relics.boss;

import botanicamod.relics.BaseRelic;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.HandOfGreed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static botanicamod.BasicMod.makeID;

// Hand of Midas - Hand of Greed now grants double gold on kill. Upon pickup, upgrade all held Hand of Greed(s) and obtain 3 copies

public class HandOfMidas extends BaseRelic {
    private static final String NAME = "Hand_Of_Midas"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.BOSS; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.FLAT; // The sound played when the relic is clicked.

    public HandOfMidas() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void onEquip() {
        // Add 3 upgraded copies of Hand of Greed to the player's master deck
        for (int i = 0; i < 3; i++) {
            HandOfGreed card = new HandOfGreed();
            card.upgrade();
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
        }

        // Modify existing Hand of Greed cards in the player's master deck
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card.cardID.equals(HandOfGreed.ID)) {
                card.baseMagicNumber *= 2;
                card.magicNumber = card.baseMagicNumber;

                if (!card.upgraded) {
                    card.upgrade();
                }

                // Ensure the card's stats are updated
                card.applyPowers();
            }
        }

        CardCrawlGame.sound.play("NECRONOMICON");
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
