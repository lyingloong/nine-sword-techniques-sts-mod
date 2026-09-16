package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.actions.LifeDevouringWoodBodyAction;

public class LifeDevouringWoodBody extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:LifeDevouringWoodBody";

    public LifeDevouringWoodBody() {
        super(ID, "LifeDevouringWoodBody", 3, CardType.SKILL, CardRarity.RARE,
                CardTarget.SELF);
        isEthereal = true;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new LifeDevouringWoodBodyAction(player));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            isEthereal = false;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LifeDevouringWoodBody();
    }
}
