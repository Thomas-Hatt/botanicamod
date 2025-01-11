package botanicamod.cards;

import botanicamod.util.CardStats;
import com.megacrit.cardcrawl.actions.unique.LimitBreakAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.PurgeField;

public class PurgeLimitBreak extends BaseCard {
    public static final String ID = makeID(PurgeLimitBreak.class.getSimpleName());

    private static final CardStats info = new CardStats(
            CardColor.RED,
            CardType.SKILL,
            CardRarity.SPECIAL,
            CardTarget.SELF,
            0
    );

    public PurgeLimitBreak() {
        super(ID, info);
        PurgeField.purge.set(this, true);

        // Make the card look like a rare card, even though it's a special card (cannot be seen in the card pool)
        setDisplayRarity(CardRarity.RARE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new LimitBreakAction());
    }

    @Override
    public AbstractCard makeCopy() { //Optional
        return new PurgeLimitBreak();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0); // Reduce cost to 0
            this.selfRetain = true; // Make the card retain
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}