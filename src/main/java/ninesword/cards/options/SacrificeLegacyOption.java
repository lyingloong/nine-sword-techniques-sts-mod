package ninesword.cards.options;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.relics.SwordAncestorsLegacyManager;

public class SacrificeLegacyOption extends CustomCard {
    public static final String ID = "NineSwordTechniques:SacrificeLegacyOption";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH =
            "NineSwordResources/img/cards/SacrificeLegacyOption.png";

    public SacrificeLegacyOption() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, -2, CARD_STRINGS.DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        onChoseThisOption();
    }

    @Override
    public void onChoseThisOption() {
        SwordAncestorsLegacyManager.chooseSacrifice();
    }

    @Override
    public void upgrade() {
    }

    @Override
    public AbstractCard makeCopy() {
        return new SacrificeLegacyOption();
    }
}
