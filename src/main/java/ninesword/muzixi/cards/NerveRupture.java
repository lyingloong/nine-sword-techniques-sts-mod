package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.actions.NerveRuptureAction;

public class NerveRupture extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:NerveRupture";
    private int damagePerStack = 2;

    public NerveRupture() {
        super(ID, "NerveRupture", 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 16;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        // The paralysis count is deliberately read by the action at resolution
        // time.  Other queued effects may add/remove paralysis after the card is
        // played, and the design calls for damage to match the layers actually
        // removed by this card.
        addToBot(new NerveRuptureAction(player, monster, this));
    }

    /**
     * Calculates the attack value for the number of paralysis stacks removed at
     * resolution.  Keeping this on the card preserves the normal card/relic/
     * power damage modifiers while allowing the action to use a late snapshot.
     */
    public int calculateDamageForParalysis(AbstractMonster monster, int paralysis) {
        long scaledBase = (long) baseDamage
                + (long) Math.max(0, paralysis) * damagePerStack;
        int dynamicBase = scaledBase >= Integer.MAX_VALUE
                ? Integer.MAX_VALUE : (int) scaledBase;
        return calculateDamageForBase(monster, dynamicBase);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(4);
            damagePerStack = 3;
            rawDescription = strings(ID).UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new NerveRupture();
    }
}
