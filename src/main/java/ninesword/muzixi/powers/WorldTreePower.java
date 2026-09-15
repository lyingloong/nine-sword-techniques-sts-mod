package ninesword.muzixi.powers;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.localization.PowerStrings;
import ninesword.muzixi.actions.GainVitalityAction;

/**
 * Watches the player's actual current energy value.  It intentionally does
 * not infer costs from cards, so temporary discounts and non-card energy
 * changes behave exactly like ordinary energy changes.
 */
public class WorldTreePower extends MuzixiPower {
    public static final String POWER_ID = "NineSwordTechniques:WorldTree";
    private static final PowerStrings STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private int lastEnergy;

    public WorldTreePower(AbstractCreature owner) {
        name = STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        amount = 1;
        type = PowerType.BUFF;
        loadIcons("WorldTree");
        lastEnergy = readEnergy(owner);
        updateDescription();
    }

    private static int readEnergy(AbstractCreature creature) {
        return creature instanceof AbstractPlayer
                ? EnergyPanel.getCurrentEnergy() : 0;
    }

    /** Called once per frame by the mod subscriber. */
    public static void poll(AbstractPlayer player) {
        if (player == null) {
            return;
        }
        com.megacrit.cardcrawl.powers.AbstractPower raw = player.getPower(POWER_ID);
        if (!(raw instanceof WorldTreePower)) {
            return;
        }
        WorldTreePower power = (WorldTreePower) raw;
        int current = readEnergy(player);
        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if (room == null || room.phase != AbstractRoom.RoomPhase.COMBAT
                || AbstractDungeon.actionManager == null) {
            power.lastEnergy = current;
            return;
        }
        if (current < power.lastEnergy) {
            int lost = power.lastEnergy - current;
            power.flash();
            // Advance the baseline before queuing effects; gaining Vitality
            // can cause another post-update pass in the same frame.
            power.lastEnergy = current;
            power.addVitalityOnePointAtATime(lost);
        } else {
            // Energy gains and cap changes establish a new baseline without
            // triggering the effect.
            power.lastEnergy = current;
        }
    }

    private void addVitalityOnePointAtATime(int lostEnergy) {
        if (lostEnergy <= 0 || amount <= 0 || AbstractDungeon.actionManager == null) {
            return;
        }
        // A drop from E to E-k represents k separate one-point triggers.
        // Keep each copy's gain separate as well so "one gain" listeners see
        // the same events they would if the energy changed point by point.
        // These go to the top because the energy was already spent: World
        // Tree must resolve before effects queued by the card that spent it.
        for (int i = 0; i < lostEnergy; i++) {
            for (int copy = 0; copy < amount; copy++) {
                addToTop(new GainVitalityAction(owner, 1));
            }
        }
    }

    @Override
    public void onInitialApplication() {
        lastEnergy = readEnergy(owner);
    }

    @Override
    public void stackPower(int stackAmount) {
        int current = readEnergy(owner);
        if (current < lastEnergy) {
            int lost = lastEnergy - current;
            flash();
            lastEnergy = current;
            // Only copies already in play observe the cost of the new copy.
            addVitalityOnePointAtATime(lost);
        }
        super.stackPower(stackAmount);
        lastEnergy = current;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(STRINGS.DESCRIPTIONS[0], amount);
    }
}
