package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TenThousandFlowers extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:TenThousandFlowers";

    public TenThousandFlowers() {
        super(ID, "TenThousandFlowers", 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        magicNumber = 5;
        baseMagicNumber = 5;
        baseDamage = 8;
        damageTypeForTurn = DamageInfo.DamageType.NORMAL;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        player.useFastAttackAnimation();
        int bonus = Math.min(magicNumber, ninesword.muzixi.powers.VitalityPower.getAmount(player));
        if (bonus > 0) {
            addToBot(new ReducePowerAction(player, player, ninesword.muzixi.powers.VitalityPower.POWER_ID, bonus));
        }
        addToBot(new DamageAllEnemiesAction(AbstractDungeon.player,
                DamageInfo.createDamageMatrix(damage + bonus * 2, false),
                DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.POISON));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(4);
            upgradeMagicNumber(2);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new TenThousandFlowers();
    }
}
