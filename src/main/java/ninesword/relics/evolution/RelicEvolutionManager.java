package ninesword.relics.evolution;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import ninesword.relics.EmberSeed;
import ninesword.relics.EmberWhiteFlame;

public final class RelicEvolutionManager {
    private RelicEvolutionManager() {
    }

    public static void update() {
        if (AbstractDungeon.player == null) {
            return;
        }
        int relicIndex = findRelicIndex();
        if (relicIndex < 0) {
            return;
        }
        if (AbstractDungeon.player.relics.get(relicIndex).counter
                < EmberSeed.VICTORIES_TO_EVOLVE) {
            return;
        }

        AbstractDungeon.player.relics.get(relicIndex).onUnequip();
        new EmberWhiteFlame().instantObtain(AbstractDungeon.player, relicIndex, true);
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
