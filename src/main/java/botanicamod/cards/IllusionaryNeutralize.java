package botanicamod.cards;

import botanicamod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.PurgeField;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class IllusionaryNeutralize extends BaseCard {
    // Deal 1 damage. Apply 1 Weak. Apply 1 Vulnerable. Gain 2 Block. Purge.
    // Upgrade: Deal 3 damage. Apply 2 Weak. Apply 1 Vulnerable. Gain 4 Block. Purge.

    // Get the stats of the card
    private static final int DAMAGE = 1;
    private static final int UPG_DAMAGE = 3;
    private static final int WEAK = 1;
    private static final int UPG_WEAK = 2;
    private static final int VULNERABLE = 1;
    private static final int BLOCK = 2;
    private static final int UPG_BLOCK = 4;

    public static final String ID = makeID(IllusionaryNeutralize.class.getSimpleName());

    private static final CardStats info = new CardStats(
            CardColor.GREEN,
            CardType.ATTACK,
            CardRarity.SPECIAL,
            CardTarget.ENEMY,
            0
    );

    public IllusionaryNeutralize() {
        super(ID, info);
        setDamage(DAMAGE);
        setBlock(BLOCK);
        this.magicNumber = this.baseMagicNumber = WEAK;
        PurgeField.purge.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Deal damage
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

        // Apply weak
        addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));

        // Apply vulnerable
        addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, VULNERABLE, false), VULNERABLE));

        // Gain block
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE - DAMAGE);
            upgradeBlock(UPG_BLOCK - BLOCK);
            upgradeMagicNumber(UPG_WEAK - WEAK);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
