package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.VitalityTidePower;

public class VitalityTide extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:VitalityTide";

    public VitalityTide() {
        super(ID, "VitalityTide", 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = 6;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        gainVitality(player, magicNumber);
        addToBot(new ApplyPowerAction(player, player,
                new VitalityTidePower(player, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(2);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new VitalityTide();
    }
}
