package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.HashSet;
import java.util.Set;

public class VineStrike extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:VineStrike";

    public VineStrike() {
        super(ID, "VineStrike", 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 3;
        damageTypeForTurn = DamageInfo.DamageType.NORMAL;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        if (monster == null || monster.powers == null) {
            return;
        }

        Set<String> debuffIds = new HashSet<>();
        for (AbstractPower power : monster.powers) {
            if (power != null && power.type == AbstractPower.PowerType.DEBUFF) {
                debuffIds.add(power.ID);
            }
        }

        int debuffCount = debuffIds.size();
        if (debuffCount > 0) {
            player.useFastAttackAnimation();
        }
        for (int i = 0; i < debuffCount; i++) {
            addToBot(new DamageAction(monster,
                    new DamageInfo(player, damage, damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new VineStrike();
    }
}
