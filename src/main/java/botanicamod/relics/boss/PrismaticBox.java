package botanicamod.relics.boss;

import botanicamod.Botanica;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PandorasBox;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen;

import java.util.ArrayList;
import java.util.Iterator;

import static botanicamod.Botanica.makeID;

public class PrismaticBox extends BaseRelic {
    // Transform all Strikes and Defends into Uncommon cards. These cards can be of any color.

    private static final String NAME = "Prismatic_Box"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.BOSS; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.FLAT; // The sound played when the relic is clicked.

    private boolean calledTransform = false;
    private int count = 0;

    public PrismaticBox() {
        super(ID, NAME, RARITY, SOUND);
        this.removeStrikeTip();
    }

    private void removeStrikeTip() {
        ArrayList<String> strikes = new ArrayList();
        String[] var2 = GameDictionary.STRIKE.NAMES;

        for (String s : var2) {
            strikes.add(s.toLowerCase());
        }

        Iterator<PowerTip> t = this.tips.iterator();

        while(t.hasNext()) {
            PowerTip deep = t.next();
            String s = deep.header.toLowerCase();
            if (strikes.contains(s)) {
                t.remove();
                break;
            }
        }
    }

    public void onEquip() {
        // Remove Pandora's Box from the relic pool
        AbstractDungeon.bossRelicPool.remove(PandorasBox.ID);

        // Create an iterator to look through the player's master deck
        this.calledTransform = false;
        Iterator<AbstractCard> i = AbstractDungeon.player.masterDeck.group.iterator();

        while(true) {
            AbstractCard e;
            do {
                if (!i.hasNext()) {
                    if (this.count > 0) {
                        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

                        for(int p = 0; p < this.count; ++p) {
                            AbstractCard card = getRandomCardOfAnyColor().makeCopy();
                            UnlockTracker.markCardAsSeen(card.cardID);
                            card.isSeen = true;

                            for (AbstractRelic r : AbstractDungeon.player.relics) {
                                r.onPreviewObtainCard(card);
                            }

                            group.addToBottom(card);
                        }

                        AbstractDungeon.gridSelectScreen.openConfirmationGrid(group, this.DESCRIPTIONS[0]);
                    }

                    return;
                }

                e = i.next();
            } while(!e.hasTag(AbstractCard.CardTags.STARTER_DEFEND) && !e.hasTag(AbstractCard.CardTags.STARTER_STRIKE));

            i.remove();
            ++this.count;
        }
    }

    @Override
    public void update() {
        super.update();
        if (!this.calledTransform && AbstractDungeon.screen != CurrentScreen.GRID) {
            this.calledTransform = true;
            AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.25F;
        }
    }

    private AbstractCard getRandomCardOfAnyColor() {
        return CardLibrary.getAnyColorCard(AbstractCard.CardRarity.UNCOMMON);
    }

    @Override
    public boolean canSpawn() {
        if (Botanica.isRelicEnabled("PrismaticBox")) {
            return (!(AbstractDungeon.player.hasRelic("PrismaticShard")) && !(AbstractDungeon.player.hasRelic("PandorasBox")));
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
