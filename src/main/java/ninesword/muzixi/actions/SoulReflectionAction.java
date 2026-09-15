package ninesword.muzixi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

/** Sets current HP to max(1, current max HP - current HP). */
public class SoulReflectionAction extends AbstractGameAction {
    private final AbstractPlayer player;

    public SoulReflectionAction(AbstractPlayer player) {
        this.player = player;
        actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        int current = player.currentHealth;
        int desired = Math.max(1, player.maxHealth - current);
        if (desired < current) {
            addToTop(new LoseHPAction(player, player, current - desired));
        } else if (desired > current) {
            addToTop(new HealAction(player, player, desired - current));
        }
        isDone = true;
    }
}
