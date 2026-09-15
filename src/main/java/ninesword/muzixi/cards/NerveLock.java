package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.NerveLockPower;

public class NerveLock extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:NerveLock";
    public NerveLock() {
        super(ID, "NerveLock", 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
        magicNumber = baseMagicNumber = 2;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new ApplyPowerAction(monster, player,
                new NerveLockPower(monster, player, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            exhaust = false;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new NerveLock();
    }
}
