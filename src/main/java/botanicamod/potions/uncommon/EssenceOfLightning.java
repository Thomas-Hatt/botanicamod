package botanicamod.potions.uncommon;

import botanicamod.actions.EssenceOfLightningAction;
import botanicamod.potions.BasePotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static botanicamod.Botanica.makeID;

public class EssenceOfLightning extends BasePotion {
    // Channel 1 (2) Lightning Orbs for each orb slot.

    public static final String ID = makeID(EssenceOfLightning.class.getSimpleName());

    private static final Color LIQUID_COLOR = CardHelper.getColor(223,239,33);
    private static final Color HYBRID_COLOR = CardHelper.getColor(156,164,76);

    public EssenceOfLightning() {
        super(ID, 1, PotionRarity.UNCOMMON, PotionSize.MOON, LIQUID_COLOR, HYBRID_COLOR, null);
        playerClass = AbstractPlayer.PlayerClass.DEFECT;
        isThrown = false;
    }

    @Override
    public void use(AbstractCreature target) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.addToBot(new EssenceOfLightningAction(this.potency));
        }
    }

    @Override
    public void addAdditionalTips() {
        this.tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.CHANNEL.NAMES[0]), GameDictionary.keywords.get(GameDictionary.CHANNEL.NAMES[0])));
        this.tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.LIGHTNING.NAMES[0]), GameDictionary.keywords.get(GameDictionary.LIGHTNING.NAMES[0])));
    }

    @Override
    public String getDescription() {
        // Check to see if the potency is 1, to make sure orbs is not plural.
        if (potency == 1) {
            return DESCRIPTIONS[2];
        }
        else {
            return DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];
        }
    }
}
