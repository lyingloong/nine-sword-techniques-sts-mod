package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import ninesword.muzixi.powers.HarmonicSoulPower;

public class DivineDemonicGaze extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:DivineDemonicGaze";

    public DivineDemonicGaze() {
        super(ID, "DivineDemonicGaze", 3, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new ApplyPowerAction(player, player, new HarmonicSoulPower(player, 9), 9));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(2);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DivineDemonicGaze();
    }
}
