package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

/**
 * Lifecycle helper for the two persona marker powers.
 *
 * <p>Personas are combat-local state.  The base game invokes power
 * {@code onVictory()} callbacks while iterating the owner's power list, so
 * removing a persona from that callback would mutate the list and can cause a
 * concurrent-modification failure. This helper is called from BaseMod's
 * post-battle hook after that iteration has completed.</p>
 */
public final class PersonaManager {
    private PersonaManager() {
    }

    /**
     * Silently reset both persona markers at the combat boundary.
     *
     * <p>This mirrors the base game's stance reset: leaving Calm or Wrath at
     * the room boundary is not itself treated as a stance change. Likewise,
     * clearing a persona here must not become a gameplay transition if a
     * future persona power gains an {@code onRemove()} effect.</p>
     */
    public static void clear(AbstractCreature creature) {
        if (creature == null || creature.powers == null || creature.powers.isEmpty()) {
            return;
        }

        // Work on a snapshot so the loop remains safe if another combat-end
        // subscriber has already changed the live power list.
        boolean changed = false;
        for (AbstractPower power : new ArrayList<>(creature.powers)) {
            if (power == null || !isPersona(power.ID) || !creature.powers.contains(power)) {
                continue;
            }
            creature.powers.remove(power);
            changed = true;
        }
        // PostBattle runs after the action queue has been cleared, so refresh
        // the UI directly once instead of enqueueing removal actions.
        if (changed && AbstractDungeon.player != null
                && AbstractDungeon.getCurrMapNode() != null) {
            AbstractDungeon.onModifyPower();
        }
    }

    private static boolean isPersona(String powerId) {
        return MuzixiPersonaPower.POWER_ID.equals(powerId)
                || TearPersonaPower.POWER_ID.equals(powerId);
    }
}
