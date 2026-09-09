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

    private SwordCardChoiceManager() {
    }

    public static void request() {
        pendingCount++;
    }

    public static void update() {
        if (AbstractDungeon.player == null) {
            pendingCount = 0;
            choosingCard = false;
            return;
        }
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
        if (AbstractDungeon.previousScreen != null) {
            return false;
        }
        return AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP
                || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.SHOP
                || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD
                || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.BOSS_REWARD
                || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MASTER_DECK_VIEW;
    }

    public static void preserveCurrentScreen() {
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
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
