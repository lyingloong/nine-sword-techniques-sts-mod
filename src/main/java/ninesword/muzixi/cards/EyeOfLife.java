package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.actions.EnterMuzixiPersonaAction;

public class EyeOfLife extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:EyeOfLife";

    public EyeOfLife() {
        super(ID, "EyeOfLife", 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = 2;
        baseMagicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new EnterMuzixiPersonaAction(player));
        gainVitality(player, magicNumber);
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
        return new EyeOfLife();
    }
}
