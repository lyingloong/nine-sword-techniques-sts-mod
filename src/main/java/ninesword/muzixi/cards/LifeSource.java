package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * Restores a fraction of the player's missing health.  The divisor is kept
 * in magicNumber so the upgraded value is reflected by the card's !M!
 * dynamic variable as well as by the effect itself.
 */
public class LifeSource extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:LifeSource";

    public LifeSource() {
        super(ID, "LifeSource", 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 10;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        int missingHealth = Math.max(0, player.maxHealth - player.currentHealth);
        int healAmount = missingHealth / Math.max(1, magicNumber);
        if (healAmount > 0) {
            addToBot(new HealAction(player, player, healAmount));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(-2);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LifeSource();
    }
}
