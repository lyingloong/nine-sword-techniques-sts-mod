package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.actions.PollenStormAction;

public class PollenStorm extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:PollenStorm";

    public PollenStorm() {
        super(ID, "PollenStorm", 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        magicNumber = baseMagicNumber = 9;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new PollenStormAction(player, magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(3);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PollenStorm();
    }
}
