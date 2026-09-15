package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.DualityConvergencePower;

public class DualityConvergence extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:DualityConvergence";
    private int value = 2;

    public DualityConvergence() {
        super(ID, "DualityConvergence", 1, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new ApplyPowerAction(player, player,
                new DualityConvergencePower(player, value), value));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            value = 3;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DualityConvergence();
    }
}
