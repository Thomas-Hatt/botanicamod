package botanicamod.relics.common;

import botanicamod.Botanica;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static botanicamod.Botanica.makeID;

public class Blossom extends BaseRelic {
    // The first time you lose HP each combat, add a Miracle+ to your hand.

    // Initialize a variable that tracks if the effect has been used
    private boolean usedThisCombat = false;

    private static final String NAME = "Blossom"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.COMMON; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.FLAT; // The sound played when the relic is clicked.

    public Blossom() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void atBattleStart() {
        usedThisCombat = false;
    }

    public void onLoseHp(int HP) {
        if (!usedThisCombat && HP > 0) {
            if (AbstractDungeon.getCurrRoom() == null || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
                return;
            }
            AbstractPlayer p = AbstractDungeon.player;
            addToBot(new RelicAboveCreatureAction(p, this));
            // Create a new Miracle card and upgrade it
            Miracle upgradedMiracle = new Miracle();
            upgradedMiracle.upgrade();

            this.addToBot(new MakeTempCardInHandAction(upgradedMiracle));
            usedThisCombat = true;
            grayscale = true;
        }
    }

    @Override
    public boolean canSpawn() {
        if (Botanica.isRelicEnabled("Blossom")) {
            return (AbstractDungeon.floorNum <= 48);
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
