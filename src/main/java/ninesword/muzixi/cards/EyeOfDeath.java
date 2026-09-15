package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.actions.EnterTearPersonaAction;
import ninesword.muzixi.actions.EyeOfDeathAction;

public class EyeOfDeath extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:EyeOfDeath";
    private int loss = 2;

    public EyeOfDeath() {
        super(ID, "EyeOfDeath", 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        // EnterTearPersonaAction performs the persona transition before any
        // following card effects.  In particular, Eye of Death must not make
        // its HP loss happen before the Tear persona's entry conversion.
        addToBot(new EnterTearPersonaAction(player, true));
        addToBot(new EyeOfDeathAction(player, loss));
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
        return new EyeOfDeath();
    }
}
