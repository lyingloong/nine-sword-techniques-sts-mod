package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.muzixi.actions.VitalityRiotAction;

/** Converts all available Vitality into one modified hit against every enemy. */
public class VitalityRiot extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:VitalityRiot";

    public VitalityRiot() {
        super(ID, "VitalityRiot", 0, CardType.ATTACK, CardRarity.UNCOMMON,
                CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = 2;
        baseDamage = 0;
        isMultiDamage = true;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new VitalityRiotAction(player, this));
    }

    /** Applies normal attack modifiers once to the combined Vitality damage. */
    public int[] calculateDamageForVitality(int spent) {
        int originalBaseDamage = baseDamage;
        int originalDamage = damage;
        boolean originalDamageModified = isDamageModified;
        int[] originalMultiDamage = multiDamage;

        long scaled = (long) Math.max(0, spent) * magicNumber;
        baseDamage = scaled >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) scaled;
        super.calculateCardDamage(null);
        int[] calculated = multiDamage == null ? new int[0] : multiDamage.clone();

        baseDamage = originalBaseDamage;
        damage = originalDamage;
        isDamageModified = originalDamageModified;
        multiDamage = originalMultiDamage;
        return calculated;
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
        return new VitalityRiot();
    }
}
