package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class VerdantRebirth extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:VerdantRebirth";
    private int vitalityAmount = 3;

    public VerdantRebirth() {
        super(ID, "VerdantRebirth", 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = 6;
        baseMagicNumber = 6;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new HealAction(player, player, magicNumber));
        gainVitality(player, vitalityAmount);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(4);
            vitalityAmount = 4;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new VerdantRebirth();
    }
}
