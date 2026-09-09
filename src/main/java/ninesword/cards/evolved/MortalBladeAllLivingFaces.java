package ninesword.cards.evolved;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import ninesword.cards.SwordTechniqueCard;

public class MortalBladeAllLivingFaces extends SwordTechniqueCard {
    public static final String ID = "NineSwordTechniques:MortalBladeAllLivingFaces";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "NineSwordResources/img/cards/EmotionalSword.png";

    public MortalBladeAllLivingFaces() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, 1, CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ENEMY);
        baseDamage = damage = 4;
        baseBlock = block = 6;
        baseMagicNumber = magicNumber = 3;
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
        if (isAttacking(m)) {
            addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));
            addToBot(new GainBlockAction(p, block));
        } else {
            addToBot(new DamageAction(m, new DamageInfo(p, 12, DamageInfo.DamageType.HP_LOSS),
                    AbstractGameAction.AttackEffect.SLASH_HEAVY));
            addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, 2, false), 2));
        }
    }

    private boolean isAttacking(AbstractMonster monster) {
        return monster.intent == AbstractMonster.Intent.ATTACK
                || monster.intent == AbstractMonster.Intent.ATTACK_BUFF
                || monster.intent == AbstractMonster.Intent.ATTACK_DEBUFF
                || monster.intent == AbstractMonster.Intent.ATTACK_DEFEND;
    }

    @Override
    public AbstractCard makeCopy() {
        return new MortalBladeAllLivingFaces();
    }
}
