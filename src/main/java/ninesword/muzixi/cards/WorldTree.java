package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import ninesword.muzixi.powers.WorldTreePower;

public class WorldTree extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:WorldTree";

    public WorldTree() {
        super(ID, "WorldTree", 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new ApplyPowerAction(player, player, new WorldTreePower(player), 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            isInnate = true;
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WorldTree();
    }
}
