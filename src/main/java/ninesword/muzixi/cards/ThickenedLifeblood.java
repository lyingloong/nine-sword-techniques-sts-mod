package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.ThickenedLifebloodPower;

public class ThickenedLifeblood extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:ThickenedLifeblood";
    private int blockAmount = 4;

    public ThickenedLifeblood() {
        super(ID, "ThickenedLifeblood", 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new ApplyPowerAction(player, player,
                new ThickenedLifebloodPower(player, blockAmount), blockAmount));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            blockAmount = 6;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ThickenedLifeblood();
    }
}
