package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.actions.GainVitalityAction;
import ninesword.muzixi.powers.ParalysisPower;

public class ParalysisHarvest extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:ParalysisHarvest";
    private int threshold = 5;
    private int vitality = 2;

    public ParalysisHarvest() {
        super(ID, "ParalysisHarvest", 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 10;
        damageTypeForTurn = DamageInfo.DamageType.NORMAL;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        int paralysis = monster == null ? 0 : ParalysisPower.getAmount(monster);
        damage(player, monster, damage, com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect.SLASH_HEAVY);
        if (paralysis >= threshold) {
            addToBot(new GainVitalityAction(player, vitality));
            addToBot(new DrawCardAction(player, 1));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(3);
            threshold = 4;
            vitality = 3;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ParalysisHarvest();
    }
}
