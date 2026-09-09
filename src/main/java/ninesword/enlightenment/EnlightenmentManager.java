package ninesword.enlightenment;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

public final class EnlightenmentManager {
    private static final String UI_ID = "NineSwordTechniques:Enlightenment";
    private static int pendingCount;
    private static boolean choosingCard;

    private EnlightenmentManager() {
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
        if (pendingCount <= 0 || AbstractDungeon.player.masterDeck == null
                || AbstractDungeon.gridSelectScreen == null
                || AbstractDungeon.isScreenUp) {
            return;
        }

        CardGroup candidates = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (SwordEvolution.canEvolve(card)) {
                candidates.addToBottom(card);
            }
        }

        if (candidates.isEmpty()) {
            applyFallback();
            pendingCount--;
            return;
        }

        choosingCard = true;
        String prompt = CardCrawlGame.languagePack.getUIString(UI_ID).TEXT[0];
        AbstractDungeon.gridSelectScreen.open(candidates, 1, prompt, false, false, false, false);
    }

    private static void handleSelection() {
        if (AbstractDungeon.gridSelectScreen == null
                || AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            return;
        }

        AbstractCard selected = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
        AbstractDungeon.gridSelectScreen.selectedCards.clear();
        int deckIndex = findDeckIndex(selected);
        if (deckIndex >= 0) {
            AbstractCard evolved = SwordEvolution.evolve(
                    AbstractDungeon.player.masterDeck.group.get(deckIndex));
            if (evolved != null) {
                AbstractDungeon.player.masterDeck.group.set(deckIndex, evolved);
                notifyMasterDeckChanged();
                AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(evolved));
                AbstractDungeon.topLevelEffects.add(
                        new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            }
        }
        choosingCard = false;
        pendingCount--;
    }

    private static int findDeckIndex(AbstractCard selected) {
        for (int i = 0; i < AbstractDungeon.player.masterDeck.group.size(); i++) {
            AbstractCard card = AbstractDungeon.player.masterDeck.group.get(i);
            if (card.uuid.equals(selected.uuid)) {
                return i;
            }
        }
        return -1;
    }

    private static void applyFallback() {
        CardGroup upgradableCards = AbstractDungeon.player.masterDeck.getUpgradableCards();
        if (!upgradableCards.isEmpty()) {
            AbstractCard card = upgradableCards.getRandomCard(AbstractDungeon.miscRng);
            card.upgrade();
            AbstractDungeon.player.bottledCardUpgradeCheck(card);
            notifyMasterDeckChanged();
            AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(card));
            AbstractDungeon.topLevelEffects.add(
                    new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        }
        AbstractDungeon.player.increaseMaxHp(5, true);
    }

    private static void notifyMasterDeckChanged() {
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            relic.onMasterDeckChange();
        }
    }
}
