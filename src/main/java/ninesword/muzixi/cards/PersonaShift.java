package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.HarmonicSoulPower;

public class PersonaShift extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:PersonaShift";

    public PersonaShift() {
        super(ID, "PersonaShift", 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        exhaust = false;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new ApplyPowerAction(player, player, new HarmonicSoulPower(player, 1), 1));
        switchPersona(player);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PersonaShift();
    }
}
