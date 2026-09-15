package ninesword.muzixi.dynamicvariables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import ninesword.muzixi.powers.VitalityPower;

/** Displays the player's current combat Vitality in card descriptions. */
public class CurrentVitalityVariable extends DynamicVariable {
    public static final String KEY = "NSTV";

    @Override
    public String key() {
        return KEY;
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return false;
    }

    @Override
    public int value(AbstractCard card) {
        return AbstractDungeon.player == null ? 0 : VitalityPower.getAmount(AbstractDungeon.player);
    }

    @Override
    public int baseValue(AbstractCard card) {
        return value(card);
    }

    @Override
    public int modifiedBaseValue(AbstractCard card) {
        return value(card);
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return false;
    }
}
