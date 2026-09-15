package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.VitalityPower;

/** Pays Vitality and deals Rooted Blow's snapshotted single-hit damage. */
public class RootedBlowAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final AbstractMonster target;
    private final int[] damageBySpentVitality;

    public RootedBlowAction(AbstractPlayer player, AbstractMonster target,
                            int[] damageBySpentVitality) {
        this.player = player;
        this.target = target;
        this.damageBySpentVitality = damageBySpentVitality == null
                ? new int[0] : damageBySpentVitality.clone();
        actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        if (player != null && target != null && !target.isDeadOrEscaped()) {
            int maximum = Math.max(0, damageBySpentVitality.length - 1);
            int spent = VitalityPower.spend(player, maximum);
            int damage = spent < damageBySpentVitality.length
                    ? damageBySpentVitality[spent] : 0;
            addToTop(new DamageAction(target,
                    new DamageInfo(player, damage, DamageInfo.DamageType.NORMAL),
                    AttackEffect.SLASH_HEAVY));
        }
        isDone = true;
    }
}
