package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class TearPersonaPower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:TearPersona";
    private static final int VITALITY_COST = 2;
    private static final int DAMAGE = 7;
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public TearPersonaPower(AbstractCreature owner) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        amount = 1;
        type = PowerType.BUFF;
        loadIcons("TearPersona");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (VitalityPower.getAmount(owner) < VITALITY_COST) {
            return;
        }
        flash();
        addToBot(new ReducePowerAction(owner, owner, VitalityPower.POWER_ID, VITALITY_COST));
        addToBot(new DamageAllEnemiesAction(owner, DamageInfo.createDamageMatrix(DAMAGE, false),
                DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], VITALITY_COST, DAMAGE);
    }
}
