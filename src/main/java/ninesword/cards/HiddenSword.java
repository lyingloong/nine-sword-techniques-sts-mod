package ninesword.cards;

import com.megacrit.cardcrawl.actions.common.EndTurnAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class HiddenSword extends SwordTechniqueCard {
    public static final String ID = "NineSwordTechniques:HiddenSword";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "NineSwordResources/img/cards/HiddenSword.png";

    public HiddenSword() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, 3, CARD_STRINGS.DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS, CardRarity.RARE, CardTarget.NONE);
        exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    protected void repeatEffect(AbstractPlayer p, AbstractMonster m) {
        addToBot(new MakeTempCardInDrawPileAction(
                new UnsheathedBlade(), 1, false, true, true));
        addToBot(new EndTurnAction());
    }

    @Override
    public AbstractCard makeCopy() {
        return new HiddenSword();
    }
}
