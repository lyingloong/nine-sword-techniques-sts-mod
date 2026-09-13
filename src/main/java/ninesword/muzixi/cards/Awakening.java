package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Awakening extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:Awakening";

    public Awakening() {
        super(ID, "Awakening", 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        switchPersona(player);
        gainVitality(player, upgraded ? 3 : 2);
        addToBot(new DrawCardAction(upgraded ? 2 : 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Awakening();
    }
}
