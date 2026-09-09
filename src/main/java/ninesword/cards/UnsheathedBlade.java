package ninesword.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.powers.UnsheathedBladePower;

/** Special card created by Hidden Sword and the evolved form of Hidden Sword. */
public class UnsheathedBlade extends SwordTechniqueCard {
    public static final String ID = "NineSwordTechniques:UnsheathedBlade";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "NineSwordResources/img/cards/UnsheathedBlade.png";

    public UnsheathedBlade() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, 1, CARD_STRINGS.DESCRIPTION,
                CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0);
            rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    protected void repeatEffect(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new UnsheathedBladePower(p), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new UnsheathedBlade();
    }
}
