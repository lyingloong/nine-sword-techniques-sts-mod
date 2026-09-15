package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.actions.VitalityPaymentAction;

public class VitalityDistillation extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:VitalityDistillation";
    private int threshold = 6;

    public VitalityDistillation() {
        super(ID, "VitalityDistillation", 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new VitalityPaymentAction(player, threshold, 0, 2, 2));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            threshold = 5;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new VitalityDistillation();
    }
}
