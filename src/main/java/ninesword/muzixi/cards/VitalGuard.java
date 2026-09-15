package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.actions.VitalitySpendAction;

public class VitalGuard extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:VitalGuard";
    private int maximumSpend = 3;

    public VitalGuard() {
        super(ID, "VitalGuard", 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = 5;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new VitalitySpendAction(player, maximumSpend, magicNumber,
                VitalitySpendAction.Effect.BLOCK));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            maximumSpend = 5;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new VitalGuard();
    }
}
