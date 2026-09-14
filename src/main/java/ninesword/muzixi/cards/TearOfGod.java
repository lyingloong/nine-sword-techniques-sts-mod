package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.actions.TearOfGodAction;

public class TearOfGod extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:TearOfGod";

    public TearOfGod() {
        super(ID, "TearOfGod", 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        damageTypeForTurn = com.megacrit.cardcrawl.cards.DamageInfo.DamageType.HP_LOSS;
        baseDamage = 2;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new TearOfGodAction(player, damage));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new TearOfGod();
    }
}
