package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class GreenPulse extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:GreenPulse";

    public GreenPulse() {
        super(ID, "GreenPulse", 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = 1;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        gainVitality(player, magicNumber);
        addToBot(new DrawCardAction(player, upgraded ? 3 : 2));
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
        return new GreenPulse();
    }
}
