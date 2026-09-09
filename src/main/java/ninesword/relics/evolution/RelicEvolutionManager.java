package ninesword.relics.evolution;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import ninesword.relics.EmberSeed;
import ninesword.relics.EmberWhiteFlame;

public final class RelicEvolutionManager {
    private static boolean emberWhiteFlamePending;

    private RelicEvolutionManager() {
    }

    public static void requestEmberWhiteFlameEvolution() {
        emberWhiteFlamePending = true;
    }

    public static void update() {
        if (AbstractDungeon.player == null) {
            emberWhiteFlamePending = false;
            return;
        }
        if (!emberWhiteFlamePending) {
            return;
        }

        int relicIndex = findRelicIndex();
        if (relicIndex >= 0) {
            AbstractDungeon.player.relics.get(relicIndex).onUnequip();
            new EmberWhiteFlame().instantObtain(AbstractDungeon.player, relicIndex, true);
        }
        emberWhiteFlamePending = false;
    }

    private static int findRelicIndex() {
        for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
            if (EmberSeed.ID.equals(AbstractDungeon.player.relics.get(i).relicId)) {
                return i;
            }
        }
        return -1;
    }
}
