package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ParalyticFieldPower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:ParalyticField";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public ParalyticFieldPower(AbstractCreature owner, int amount) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = Math.max(0, amount);
        type = PowerType.BUFF;
        loadIcons("ParalyticField");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (AbstractDungeon.getMonsters() == null) {
            return;
        }
        flash();
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster != null && !monster.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(monster, owner,
                        new ParalysisPower(monster, amount), amount));
            }
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], amount);
    }
}
