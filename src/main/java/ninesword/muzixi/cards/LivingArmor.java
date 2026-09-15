package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.powers.LivingArmorPower;

public class LivingArmor extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:LivingArmor";
    private int blockAmount = 6;

    public LivingArmor() {
        super(ID, "LivingArmor", 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new ApplyPowerAction(player, player,
                new LivingArmorPower(player, blockAmount), blockAmount));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            blockAmount = 9;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LivingArmor();
    }
}
