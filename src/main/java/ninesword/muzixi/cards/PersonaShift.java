package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PersonaShift extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:PersonaShift";

    public PersonaShift() {
        super(ID, "PersonaShift", 1, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        exhaust = false;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        switchPersona(player);
        addToBot(new DrawCardAction(1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PersonaShift();
    }
}
