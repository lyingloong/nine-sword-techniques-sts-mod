package ninesword.modcore;

import basemod.abstracts.CustomSavable;
import ninesword.cards.SwordCardChoiceManager;
import ninesword.enlightenment.EnlightenmentManager;

/** Persists requests that must survive closing the game before their selection screen opens. */
final class NineSwordRunState implements CustomSavable<int[]> {
    static final String SAVE_KEY = "NineSwordTechniques:RunState";

    @Override
    public int[] onSave() {
        return new int[]{
                EnlightenmentManager.getPendingCountForSave(),
                SwordCardChoiceManager.getPendingCountForSave()
        };
    }

    @Override
    public void onLoad(int[] values) {
        EnlightenmentManager.restorePendingCount(valueAt(values, 0));
        SwordCardChoiceManager.restorePendingCount(valueAt(values, 1));
    }

    void clear() {
        EnlightenmentManager.clearPendingCount();
        SwordCardChoiceManager.clearPendingCount();
    }

    private int valueAt(int[] values, int index) {
        return values != null && values.length > index ? values[index] : 0;
    }
}
