package botanicamod.relics.uncommon;

import botanicamod.Botanica;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.unique.ExpertiseAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static botanicamod.Botanica.makeID;

public class Manna extends BaseRelic {
    // At the start of each Elite combat, draw until your hand is full.

    private static final String NAME = "Manna"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.UNCOMMON; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.CLINK; // The sound played when the relic is clicked.

    public Manna() {
        super(ID, NAME, RARITY, SOUND);
    }

    public void atBattleStart() {
        if (AbstractDungeon.getCurrRoom().eliteTrigger) {
            this.flash();
            this.addToBot(new ExpertiseAction(AbstractDungeon.player, 10));
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    @Override
    public boolean canSpawn() {
        if (Botanica.isRelicEnabled("Manna")) {
            return (Settings.isEndless || AbstractDungeon.floorNum <= 48);
        }
        return false;
    }

    @Override
    public String getUpdatedDescription()
    {
        return this.DESCRIPTIONS[0];
    }
}
