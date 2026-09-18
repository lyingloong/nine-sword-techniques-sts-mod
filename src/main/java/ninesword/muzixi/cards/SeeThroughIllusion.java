package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

/** Applies its effects only while the player is in Leixier's persona. */
public class SeeThroughIllusion extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:SeeThroughIllusion";

    public SeeThroughIllusion() {
        super(ID, "SeeThroughIllusion", 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        if (!isTear(player)) {
            return;
        }

        if (monster != null) {
            addToBot(new ApplyPowerAction(monster, player,
                    new VulnerablePower(monster, 1, false), 1));
        }
        applySelfPower(player, new StrengthPower(player, magicNumber), magicNumber);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SeeThroughIllusion();
    }
}
