// Crystal.java
package botanicamod.relics.rare;

import botanicamod.Botanica;
import botanicamod.powers.CrystallizePower;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import static botanicamod.Botanica.makeID;

public class Crystal extends BaseRelic {
    // Crystal - At the start of combat, gain 2 Crystallize.
    // Crystallize: At the end of your turn, gain an Orb slot and Channel a random Orb.

    private static final String NAME = "Crystal";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.RARE;
    private static final LandingSound SOUND = LandingSound.MAGICAL;

    // Amount of crystallize to apply
    private int crystallizeAmount = 2;

    AbstractPlayer p = AbstractDungeon.player;

    public Crystal() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public void atBattleStart() {

        this.flash();

        this.addToBot(new ApplyPowerAction(p, p, new CrystallizePower(p, crystallizeAmount)));
    }

    @Override
    public boolean canSpawn() {
        if (Botanica.isRelicEnabled("Crystal")) {
            return (Settings.isEndless || AbstractDungeon.floorNum <= 48);
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}