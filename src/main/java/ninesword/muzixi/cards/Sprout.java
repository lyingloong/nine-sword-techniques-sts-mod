package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;

/** A small, reliable Vitality generator. */
public class Sprout extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:Sprout";

    public Sprout() {
        super(ID, "Sprout", 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        gainVitality(player, magicNumber);
        addToBot(new DrawCardAction(player, upgraded ? 2 : 1));
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
        return new Sprout();
    }
}
