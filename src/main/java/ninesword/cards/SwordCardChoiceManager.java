package ninesword.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public final class SwordCardChoiceManager {
    private static final String UI_ID = "NineSwordTechniques:SwordAncestorsLegacy";
    private static int pendingCount;
    private static boolean choosingCard;
    private static AbstractDungeon.CurrentScreen mapReturnScreen;

    private SwordCardChoiceManager() {
    }

    public static void request() {
        pendingCount++;
    }

    public static int getPendingCountForSave() {
        return pendingCount;
    }

    public static void restorePendingCount(int count) {
        pendingCount = Math.max(0, count);
        choosingCard = false;
        mapReturnScreen = null;
    }

    public static void clearPendingCount() {
        pendingCount = 0;
        choosingCard = false;
        mapReturnScreen = null;
    }

    public static void update() {
        if (AbstractDungeon.player == null) {
            pendingCount = 0;
            choosingCard = false;
            mapReturnScreen = null;
            return;
        }
        restoreMapReturnScreen();
        if (choosingCard) {
            handleSelection();
            return;
        }
        if (pendingCount <= 0
                || AbstractDungeon.gridSelectScreen == null
                || !canOpenSelectionScreen()) {
            return;
        }

        CardGroup cards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : SwordCardPool.create()) {
            cards.addToBottom(card);
        }

        preserveCurrentScreen();
        choosingCard = true;
        String prompt = CardCrawlGame.languagePack.getUIString(UI_ID).TEXT[0];
        AbstractDungeon.gridSelectScreen.open(
                cards, 1, prompt, false, false, false, false);
    }

    public static boolean canOpenSelectionScreen() {
        if (!AbstractDungeon.isScreenUp) {
            return true;
        }
        // The map can be opened from another screen and retain its previous screen.
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP) {
            return true;
        }
        if (AbstractDungeon.previousScreen != null) {
            return false;
        }
        return AbstractDungeon.screen == AbstractDungeon.CurrentScreen.SHOP
                || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD
                || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.BOSS_REWARD
                || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MASTER_DECK_VIEW;
    }

    public static void preserveCurrentScreen() {
        if (AbstractDungeon.isScreenUp) {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP
                    && AbstractDungeon.previousScreen != null) {
                mapReturnScreen = AbstractDungeon.previousScreen;
            }
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
    }

    private static void restoreMapReturnScreen() {
        if (mapReturnScreen == null || AbstractDungeon.screen != AbstractDungeon.CurrentScreen.MAP) {
            return;
        }
        if (AbstractDungeon.previousScreen == null) {
            AbstractDungeon.previousScreen = mapReturnScreen;
        }
        mapReturnScreen = null;
    }

    private static void handleSelection() {
        if (AbstractDungeon.gridSelectScreen == null
                || AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            return;
        }

        AbstractCard selected = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
        AbstractDungeon.gridSelectScreen.selectedCards.clear();
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(
                selected, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        choosingCard = false;
        pendingCount--;
    }
}
