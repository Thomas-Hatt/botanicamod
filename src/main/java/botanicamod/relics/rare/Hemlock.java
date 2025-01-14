package botanicamod.relics.rare;

import botanicamod.Botanica;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;

import static botanicamod.Botanica.makeID;

public class Hemlock extends BaseRelic {
    // Hemlock - Upon entering a Rest Site, Scry 1 for every 6 cards in your deck at the start of your next combat.

    private static final String NAME = "Hemlock"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.RARE; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.FLAT; // The sound played when the relic is clicked.

    // Store the first turn into a variable
    private boolean firstTurn = true;

    // Initialize the scryAmount variable
    private int scryAmount;

    public Hemlock() {
        super(ID, NAME, RARITY, SOUND);
    }

    // Eternal Feather Code
    public void onEnterRoom(AbstractRoom room) {
        if (room instanceof RestRoom) {
            this.flash();
            this.counter = -2;
            this.pulse = true;

            // Store the scry amount into a variable
            scryAmount = (int) Math.rint((double) AbstractDungeon.player.masterDeck.size() / 6);
        }
    }

    // Ancient tea set code
    public void atTurnStart() {
        if (this.firstTurn) {
            if (this.counter == -2) {
                this.pulse = false;
                this.counter = -1;
                this.flash();
                this.addToTop(new ScryAction(scryAmount));
                this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            }
            // Debug line
            System.out.println("Scry amount: " + scryAmount);
            this.firstTurn = false;
        }

    }

    public void atPreBattle() {
        this.firstTurn = true;
    }

    public void setCounter(int counter) {
        super.setCounter(counter);
        if (counter == -2) {
            this.pulse = true;
        }

    }

    @Override
    public boolean canSpawn() {
        if (Botanica.isRelicEnabled("Hemlock")) {
            return (Settings.isEndless || AbstractDungeon.floorNum <= 48);
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}