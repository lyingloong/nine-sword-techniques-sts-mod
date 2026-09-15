package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.actions.VitalityPaymentAction;

public class MindGarden extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:MindGarden";

    public MindGarden() {
        super(ID, "MindGarden", 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = 2;
        baseMagicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new DrawCardAction(magicNumber));
        addToBot(new VitalityPaymentAction(player, 1, 0, 1, 0));
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
        return new MindGarden();
    }
}
