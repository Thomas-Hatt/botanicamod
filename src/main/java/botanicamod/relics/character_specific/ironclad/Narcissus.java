package botanicamod.relics.character_specific.ironclad;

import botanicamod.cards.PurgeLimitBreak;
import botanicamod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static botanicamod.BasicMod.makeID;

public class Narcissus extends BaseRelic {
    // Narcissus (Ironclad Specific) - Start each combat with Limit Break. It has Purge and costs 0.
    private static final String NAME = "Narcissus";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.RARE;
    private static final LandingSound SOUND = LandingSound.MAGICAL;
    private static final int AMOUNT = 1;

    public Narcissus() {
        super(ID, NAME, AbstractCard.CardColor.RED, RARITY, SOUND);
    }

    @Override
    public void atBattleStartPreDraw() {
        this.flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

        // Add the card to the player's hand
        addToBot(new MakeTempCardInHandAction(new PurgeLimitBreak(), AMOUNT, false));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}