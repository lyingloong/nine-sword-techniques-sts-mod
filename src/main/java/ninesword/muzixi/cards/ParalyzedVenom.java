package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import ninesword.muzixi.powers.ParalysisPower;

public class ParalyzedVenom extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:ParalyzedVenom";
    public ParalyzedVenom() {
        super(ID, "ParalyzedVenom", 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 0;
        exhaust = true;
        damageTypeForTurn = DamageInfo.DamageType.NORMAL;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        int poisonAmount = monster == null ? 0 : ParalysisPower.getAmount(monster);
        if (monster != null && poisonAmount > 0) {
            addToBot(new ApplyPowerAction(monster, player,
                    new PoisonPower(monster, player, poisonAmount), poisonAmount));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            exhaust = false;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ParalyzedVenom();
    }
}
