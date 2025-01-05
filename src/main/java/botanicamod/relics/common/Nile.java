package botanicamod.relics.common;

import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static botanicamod.BasicMod.makeID;

public class Nile extends BaseRelic {
    // Whenever you enter a ? room, all enemies in the next combat lose 1 Strength.

    private static final String NAME = "Nile";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.COMMON;
    private static final LandingSound SOUND = LandingSound.FLAT;

    private static final int STRENGTH_LOSS = 1;
    private boolean firstTurn = true;

    public Nile() {
        super(ID, NAME, RARITY, SOUND);
    }

    public void atTurnStart() {
        if (this.firstTurn) {
            if (this.counter == -2) {
                this.pulse = false;
                this.counter = -1;
                this.flash();
                this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));

                // Apply negative strength to all alive monsters
                for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                    if (!monster.isDeadOrEscaped()) {
                        this.addToTop(new ApplyPowerAction(monster, AbstractDungeon.player,
                                new StrengthPower(monster, -STRENGTH_LOSS), -STRENGTH_LOSS));
                    }
                }
            }
            this.firstTurn = false; // Only allow once per battle
        }
    }

    public void atPreBattle() {
        this.firstTurn = true; // Reset for next battle
    }

    // New method to handle entering question mark rooms
    public void onEnterRoom(AbstractRoom room) {
        if (room instanceof com.megacrit.cardcrawl.rooms.EventRoom) { // Check if it's an Event Room (question mark)
            this.flash();
            this.counter = -2; // Set counter for next turn
            this.pulse = true; // Start pulsing
        }
    }

    public void setCounter(int counter) {
        super.setCounter(counter);
        if (counter == -2) {
            this.pulse = true; // Start pulsing when counter is -2
        }
    }

    public boolean canSpawn() {
        return Settings.isEndless || AbstractDungeon.floorNum <= 40; // Control spawn conditions
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0]; // Ensure descriptions are properly set up
    }
}
