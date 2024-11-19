package botanicamod.relics.uncommon;

import botanicamod.relics.BaseRelic;

import static botanicamod.BasicMod.makeID;

// Tapinella - All Dexterity is converted to Strength

public class Tapinella extends BaseRelic {
    private static final String NAME = "Tapinella"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.UNCOMMON; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.CLINK; // The sound played when the relic is clicked.

    public Tapinella() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
