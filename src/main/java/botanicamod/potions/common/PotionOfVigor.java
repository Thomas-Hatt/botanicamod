package botanicamod.potions.common;

import botanicamod.potions.BasePotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static botanicamod.BasicMod.makeID;

public class PotionOfVigor extends BasePotion {
    // Gain 8 (16) Vigor.

    public static final String ID = makeID(PotionOfVigor.class.getSimpleName());

    private static final Color LIQUID_COLOR = CardHelper.getColor(251,192,85);
    private static final Color HYBRID_COLOR = CardHelper.getColor(252,140,52);

    public PotionOfVigor() {
        super(ID, 8, PotionRarity.COMMON, PotionSize.BOTTLE, LIQUID_COLOR, HYBRID_COLOR, null);
        playerClass = AbstractPlayer.PlayerClass.IRONCLAD;
    }

    @Override
    public void use(AbstractCreature target) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new VigorPower(AbstractDungeon.player, this.potency), this.potency));
        }
    }

    @Override
    public void addAdditionalTips() {
        this.tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.VIGOR.NAMES[0]), GameDictionary.keywords.get(GameDictionary.VIGOR.NAMES[0])));
    }

    @Override
    public String getDescription() {
        return DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];
    }
}
