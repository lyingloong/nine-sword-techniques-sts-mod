package ninesword.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class EmotionalSword extends SwordTechniqueCard {
    public static final String ID = "NineSwordTechniques:EmotionalSword";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "NineSwordResources/img/cards/EmotionalSword.png";

    public EmotionalSword() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, 1, CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK, CardColor.COLORLESS, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = damage = 2;
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0);
            rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    protected void repeatEffect(AbstractPlayer p, AbstractMonster m) {
        if (m == null || m.isDeadOrEscaped()) {
            return;
        }
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        if (m.intent == AbstractMonster.Intent.ATTACK
                || m.intent == AbstractMonster.Intent.ATTACK_BUFF
                || m.intent == AbstractMonster.Intent.ATTACK_DEBUFF
                || m.intent == AbstractMonster.Intent.ATTACK_DEFEND) {
            addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));
        } else {
            addToBot(new DamageAction(m, new DamageInfo(p, 10, DamageInfo.DamageType.HP_LOSS),
                    AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new EmotionalSword();
    }
}
