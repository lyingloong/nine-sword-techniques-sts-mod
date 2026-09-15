package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.actions.NeuralCollapseAction;

public class NeuralCollapse extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:NeuralCollapse";
    private int interval = 4;

    public NeuralCollapse() {
        super(ID, "NeuralCollapse", 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new NeuralCollapseAction(player, monster, interval));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            interval = 3;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new NeuralCollapse();
    }
}
