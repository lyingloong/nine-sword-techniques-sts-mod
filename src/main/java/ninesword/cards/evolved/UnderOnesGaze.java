package ninesword.cards.evolved;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import ninesword.cards.SwordTechniqueCard;

public class UnderOnesGaze extends SwordTechniqueCard {
    public static final String ID = "NineSwordTechniques:UnderOnesGaze";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "NineSwordResources/img/cards/MindSword.png";

    public UnderOnesGaze() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, 1, CARD_STRINGS.DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        baseBlock = block = 8;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            upgradeBlock(2);
            rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    protected void repeatEffect(AbstractPlayer p, AbstractMonster m) {
        int debuffCount = 0;
        for (AbstractPower power : p.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF) {
                debuffCount++;
            }
        }
        addToBot(new RemoveDebuffsAction(p));
        addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p, magicNumber), magicNumber));
        addToBot(new GainBlockAction(p, block));
        if (debuffCount > 0) {
            addToBot(new DrawCardAction(p, Math.min(3, debuffCount)));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new UnderOnesGaze();
    }
}
