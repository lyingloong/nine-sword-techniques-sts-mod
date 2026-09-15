package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.ParalysisPower;

public class ParalyzingBloom extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:ParalyzingBloom";

    public ParalyzingBloom() {
        super(ID, "ParalyzingBloom", 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        magicNumber = baseMagicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        if (AbstractDungeon.getMonsters() == null) {
            return;
        }
        for (AbstractMonster target : AbstractDungeon.getMonsters().monsters) {
            if (target != null && !target.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(target, player,
                        new ParalysisPower(target, magicNumber), magicNumber));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ParalyzingBloom();
    }
}
