package ninesword.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import ninesword.enlightenment.SwordEvolution;
import ninesword.relics.TheCanonOfSwordObservation;

public class BazunanEvent extends AbstractImageEvent {
    public static final String ID = "NineSwordTechniques:Bazunan";
    private static final EventStrings EVENT_STRINGS = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = EVENT_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = EVENT_STRINGS.DESCRIPTIONS;
    public static final String[] OPTIONS = EVENT_STRINGS.OPTIONS;
    private static final String IMG_PATH = "NineSwordResources/img/events/Bazunan.png";
    private static final int GOLD_REWARD = 50;

    private Screen screen = Screen.INITIAL;

    public BazunanEvent() {
        super(NAME, DESCRIPTIONS[0], IMG_PATH);

        boolean hasCanon = AbstractDungeon.player.hasRelic(TheCanonOfSwordObservation.ID);
        imageEventText.setDialogOption(
                hasCanon ? OPTIONS[1] : OPTIONS[0], hasCanon, new TheCanonOfSwordObservation());

        boolean canSalute = hasEvolvedSword() && !purgeableCards().isEmpty();
        imageEventText.setDialogOption(canSalute ? OPTIONS[2] : OPTIONS[3], !canSalute);
        imageEventText.setDialogOption(OPTIONS[4]);
    }

    @Override
    public void update() {
        super.update();
        if (screen != Screen.REMOVE || AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            return;
        }

        AbstractCard selected = AbstractDungeon.gridSelectScreen.selectedCards.remove(0);
        AbstractDungeon.player.masterDeck.removeCard(selected);
        AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(
                selected, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        logMetricCardRemoval(ID, "Saluted", selected);
        complete(DESCRIPTIONS[2]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        if (screen == Screen.COMPLETE) {
            openMap();
            return;
        }
        if (screen == Screen.REMOVE) {
            return;
        }

        switch (buttonPressed) {
            case 0:
                TheCanonOfSwordObservation relic = new TheCanonOfSwordObservation();
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(
                        Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, relic);
                logMetricObtainRelic(ID, "Became an Apprentice", relic);
                complete(DESCRIPTIONS[1]);
                break;
            case 1:
                beginCardRemoval();
                break;
            default:
                AbstractDungeon.player.gainGold(GOLD_REWARD);
                AbstractDungeon.effectList.add(new RainingGoldEffect(GOLD_REWARD));
                logMetricGainGold(ID, "Beat Bazun'an", GOLD_REWARD);
                complete(DESCRIPTIONS[3]);
                break;
        }
    }

    private void beginCardRemoval() {
        screen = Screen.REMOVE;
        imageEventText.updateBodyText(DESCRIPTIONS[4]);
        imageEventText.updateDialogOption(0, OPTIONS[6]);
        imageEventText.clearRemainingOptions();
        AbstractDungeon.gridSelectScreen.open(
                purgeableCards(), 1, OPTIONS[5], false, false, false, true);
    }

    private boolean hasEvolvedSword() {
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (SwordEvolution.isEvolvedSwordTechnique(card)) {
                return true;
            }
        }
        return false;
    }

    private CardGroup purgeableCards() {
        return CardGroup.getGroupWithoutBottledCards(
                AbstractDungeon.player.masterDeck.getPurgeableCards());
    }

    private void complete(String text) {
        screen = Screen.COMPLETE;
        showProceedScreen(text);
    }

    private enum Screen {
        INITIAL,
        REMOVE,
        COMPLETE
    }
}
