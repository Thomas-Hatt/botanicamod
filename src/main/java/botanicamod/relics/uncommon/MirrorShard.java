package botanicamod.relics.uncommon;

import botanicamod.Botanica;
import botanicamod.actions.MirrorShardAction;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import static botanicamod.Botanica.makeID;

// Mirror Shard - At the start of each combat, create a copy of a random card in your hand

public class MirrorShard extends BaseRelic {
    private static final String NAME = "Mirror_Shard"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.UNCOMMON; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.CLINK; // The sound played when the relic is clicked.

    public MirrorShard() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void atBattleStart() {
        Gdx.app.log("MirrorShard", "atBattleStart called");
        // Use a delayed action to wait until cards are drawn
        AbstractDungeon.actionManager.addToBottom(new MirrorShardAction());
    }

    @Override
    public boolean canSpawn() {
        if (Botanica.isRelicEnabled("MirrorShard")) {
            return (Settings.isEndless || AbstractDungeon.floorNum <= 48);
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}