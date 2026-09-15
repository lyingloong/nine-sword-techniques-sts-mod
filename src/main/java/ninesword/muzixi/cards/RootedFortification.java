package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.actions.VitalitySpendAction;

public class RootedFortification extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:RootedFortification";
    private int maximumSpend = 6;
    private int perVitality = 4;

    public RootedFortification() {
        super(ID, "RootedFortification", 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 10;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new GainBlockAction(player, player, block));
        addToBot(new VitalitySpendAction(player, maximumSpend, perVitality,
                VitalitySpendAction.Effect.BLOCK));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            selfRetain = true;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new RootedFortification();
    }
}
