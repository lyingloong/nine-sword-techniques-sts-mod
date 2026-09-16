package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.actions.VitalitySpendAction;
import ninesword.muzixi.powers.VitalityPower;

public class VitalCycle extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:VitalCycle";
    public VitalCycle() {
        super(ID, "VitalCycle", 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new VitalitySpendAction(player, 1, 1, VitalitySpendAction.Effect.DRAW));
    }

    @Override
    public boolean canUse(AbstractPlayer player, AbstractMonster monster) {
        if (!super.canUse(player, monster)) {
            return false;
        }
        if (VitalityPower.getAmount(player) < 1) {
            cantUseMessage = strings(ID).EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            returnToHand = true;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new VitalCycle();
    }
}
