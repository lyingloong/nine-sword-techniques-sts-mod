package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.VitalityReservoirPower;

public class VitalityReservoir extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:VitalityReservoir";
    private int threshold = 4;
    private int gain = 2;

    public VitalityReservoir() {
        super(ID, "VitalityReservoir", 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new ApplyPowerAction(player, player,
                new VitalityReservoirPower(player, gain, threshold), gain));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            threshold = 5;
            gain = 3;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new VitalityReservoir();
    }
}
