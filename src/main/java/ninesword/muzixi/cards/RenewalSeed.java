package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RenewalSeed extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:RenewalSeed";
    private int energyAmount = 1;

    public RenewalSeed() {
        super(ID, "RenewalSeed", 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new HealAction(player, player, magicNumber));
        addToBot(new GainEnergyAction(energyAmount));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            energyAmount = 2;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new RenewalSeed();
    }
}
