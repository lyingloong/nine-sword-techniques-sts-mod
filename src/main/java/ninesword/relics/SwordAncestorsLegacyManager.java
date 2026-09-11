package ninesword.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import ninesword.cards.SwordCardChoiceManager;
import ninesword.cards.options.AbandonLegacyOption;
import ninesword.cards.options.SacrificeLegacyOption;

import java.util.ArrayList;

public final class SwordAncestorsLegacyManager {
    private SwordAncestorsLegacyManager() {
    }

    public static void openChoice() {
        SwordAncestorsLegacy relic = findRelic();
        if (relic == null || !relic.canActivate()) {
            return;
        }

        ArrayList<AbstractCard> choices = new ArrayList<>();
        choices.add(new SacrificeLegacyOption());
        choices.add(new AbandonLegacyOption());
        SwordCardChoiceManager.preserveCurrentScreen();
        AbstractDungeon.cardRewardScreen.chooseOneOpen(choices);
    }

    public static void chooseSacrifice() {
        SwordAncestorsLegacy relic = findRelic();
        if (relic == null || !relic.consume()) {
            return;
        }

        AbstractDungeon.player.damage(new DamageInfo(
                null, 18, DamageInfo.DamageType.HP_LOSS));
        if (AbstractDungeon.player.currentHealth > 0) {
            SwordCardChoiceManager.request();
        }
    }

    public static void chooseAbandon() {
        SwordAncestorsLegacy relic = findRelic();
        if (relic != null) {
            relic.consume();
        }
    }

    private static SwordAncestorsLegacy findRelic() {
        if (AbstractDungeon.player == null) {
            return null;
        }
        AbstractRelic relic = AbstractDungeon.player.getRelic(SwordAncestorsLegacy.ID);
        if (relic instanceof SwordAncestorsLegacy) {
            return (SwordAncestorsLegacy) relic;
        }
        return null;
    }
}
