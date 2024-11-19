package botanicamod.relics.event;

import botanicamod.relics.BaseRelic;

import static botanicamod.BasicMod.makeID;

public class CerberusMask extends BaseRelic {
    // Cerberus' Mask [] - Upon pickup, obtain all Face Relics. Lose 1 Energy at the start of your turn.

    private static final String NAME = "Cerberus_Mask"; // The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in modID:relic
    private static final RelicTier RARITY = RelicTier.SPECIAL; // The relic's rarity.
    private static final LandingSound SOUND = LandingSound.CLINK; // The sound played when the relic is clicked.

    public CerberusMask() {
        super(ID, NAME, RARITY, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
