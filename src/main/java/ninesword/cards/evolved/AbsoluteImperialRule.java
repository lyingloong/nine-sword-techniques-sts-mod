package ninesword.cards.evolved;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.actions.AbsoluteImperialRuleAction;
import ninesword.cards.SwordTechniqueCard;

public class AbsoluteImperialRule extends SwordTechniqueCard {
    public static final String ID = "NineSwordTechniques:AbsoluteImperialRule";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "NineSwordResources/img/cards/MyriadSword.png";

    public AbsoluteImperialRule() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, 1, CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ALL_ENEMY);
        baseDamage = damage = 1;
        baseMagicNumber = magicNumber = 5;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    protected void repeatEffect(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AbsoluteImperialRuleAction(this, p, magicNumber));
    }

    public int calculateHitDamage(AbstractMonster monster, int bonusDamage) {
        int originalBaseDamage = baseDamage;
        int originalDamage = damage;
        boolean originalModified = isDamageModified;
        baseDamage += bonusDamage;
        super.calculateCardDamage(monster);
        int result = damage;
        baseDamage = originalBaseDamage;
        damage = originalDamage;
        isDamageModified = originalModified;
        return result;
    }

    @Override
    public AbstractCard makeCopy() {
        return new AbsoluteImperialRule();
    }
}
