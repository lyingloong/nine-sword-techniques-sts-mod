package ninesword.cards.evolved;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import ninesword.cards.SwordTechniqueCard;
import ninesword.powers.VoidBladeStylePower;

public class VoidBladeStyle extends SwordTechniqueCard {
    public static final String ID = "NineSwordTechniques:VoidBladeStyle";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "NineSwordResources/img/cards/NonSword.png";

    public VoidBladeStyle() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, 1, CARD_STRINGS.DESCRIPTION,
                CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    protected void repeatEffect(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(p, p, new FocusPower(p, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(p, p, new MetallicizePower(p, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(p, p, new VoidBladeStylePower(p, 1), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new VoidBladeStyle();
    }
}
