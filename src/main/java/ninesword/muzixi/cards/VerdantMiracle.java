package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class VerdantMiracle extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:VerdantMiracle";
    private int vitalityAmount = 8;

    public VerdantMiracle() {
        super(ID, "VerdantMiracle", 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = 4;
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
            upgradeMagicNumber(2);
            vitalityAmount = 12;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new VerdantMiracle();
    }
}
