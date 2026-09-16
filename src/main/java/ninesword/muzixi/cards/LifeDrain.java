package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import ninesword.muzixi.powers.VitalityPower;

public class LifeDrain extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:LifeDrain";

    public LifeDrain() {
        super(ID, "LifeDrain", 1, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);
        baseDamage = 0;
        damageTypeForTurn = DamageInfo.DamageType.NORMAL;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new LifeDrainAction(player, monster, this, upgraded ? 3 : 2));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LifeDrain();
    }
}

class LifeDrainAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final AbstractMonster monster;
    private final LifeDrain card;
    private final int amountPerVitality;

    LifeDrainAction(AbstractPlayer player, AbstractMonster monster, LifeDrain card,
                    int amountPerVitality) {
        this.player = player;
        this.monster = monster;
        this.card = card;
        this.amountPerVitality = amountPerVitality;
        actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        int spent = VitalityPower.spend(player, 3);
        int total = safeMultiply(spent, amountPerVitality);
        if (total > 0) {
            // Actions are a stack: queue healing first so the damage resolves
            // before the recovery, matching the card's visible order.
            addToTop(new HealAction(player, player, total));
            if (card != null && monster != null && !monster.isDeadOrEscaped()) {
                int modifiedDamage = card.calculateDamageForBase(monster, total);
                addToTop(new DamageAction(monster,
                        new DamageInfo(player, modifiedDamage, card.damageTypeForTurn),
                        AttackEffect.SLASH_HEAVY));
            }
        }
        isDone = true;
    }

    private static int safeMultiply(int left, int right) {
        long result = (long) left * right;
        return result >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result;
    }
}
