package ninesword.cards.options;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.relics.SwordAncestorsLegacyManager;

public class AbandonLegacyOption extends CustomCard {
    public static final String ID = "NineSwordTechniques:AbandonLegacyOption";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH =
            "NineSwordResources/img/cards/AbandonLegacyOption.png";

    public AbandonLegacyOption() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, -2, CARD_STRINGS.DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        onChoseThisOption();
    }

    @Override
    public void onChoseThisOption() {
        SwordAncestorsLegacyManager.chooseAbandon();
    }

    @Override
    public void upgrade() {
    }

    @Override
    public AbstractCard makeCopy() {
        return new AbandonLegacyOption();
    }
}
