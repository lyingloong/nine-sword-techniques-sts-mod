package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.actions.FlourishAction;

public class Flourish extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:Flourish";
    private int threshold = 6;
    private int fallbackVitality = 2;
    private int drawAmount = 2;

    public Flourish() {
        super(ID, "Flourish", 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = 6;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new FlourishAction(player, threshold, drawAmount, fallbackVitality));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            threshold = 5;
            drawAmount = 3;
            fallbackVitality = 3;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Flourish();
    }
}
