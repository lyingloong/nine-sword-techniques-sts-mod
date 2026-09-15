package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.actions.GainVitalityAction;

/** Marker power for the persona-switch trigger; SwitchPersonaAction calls trigger(). */
public class DualityConvergencePower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:DualityConvergence";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public DualityConvergencePower(AbstractCreature owner, int amount) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = Math.max(0, amount);
        type = PowerType.BUFF;
        loadIcons("DualityConvergence");
        updateDescription();
    }

    public static void trigger(AbstractCreature owner) {
        if (owner == null || !owner.hasPower(POWER_ID)) {
            return;
        }
        DualityConvergencePower power = (DualityConvergencePower) owner.getPower(POWER_ID);
        if (AbstractDungeon.getMonsters() != null && power.amount > 0) {
            for (int i = AbstractDungeon.getMonsters().monsters.size() - 1; i >= 0; i--) {
                AbstractMonster monster = AbstractDungeon.getMonsters().monsters.get(i);
                if (monster != null && !monster.isDeadOrEscaped()) {
                    power.addToTop(new ApplyPowerAction(monster, owner,
                            new ParalysisPower(monster, power.amount), power.amount));
                }
            }
        }
        if (power.amount > 0) {
            power.addToTop(new GainVitalityAction(owner, power.amount));
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], amount, amount);
    }
}
