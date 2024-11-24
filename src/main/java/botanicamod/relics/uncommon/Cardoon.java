package botanicamod.relics.uncommon;

import botanicamod.relics.BaseRelic;

import static botanicamod.BasicMod.makeID;

public class Cardoon extends BaseRelic {
    // Cardoon - Every time you play 4 Attacks in a single turn, shuffle a Jack of All Trades into your draw pile.

    private static final String NAME = "Cardoon"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.UNCOMMON; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.CLINK; // The sound played when the relic is clicked.

    // Create a variable to be used for the turn counter
    private final int turnCounter = 4;

    public Cardoon() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
