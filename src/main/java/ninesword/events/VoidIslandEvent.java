package ninesword.events;

import com.megacrit.cardcrawl.cards.curses.Pain;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import ninesword.enlightenment.EnlightenmentManager;
import ninesword.relics.VoidCrystal;

public class VoidIslandEvent extends AbstractImageEvent {
    public static final String ID = "NineSwordTechniques:VoidIsland";
    private static final EventStrings EVENT_STRINGS = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = EVENT_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = EVENT_STRINGS.DESCRIPTIONS;
    public static final String[] OPTIONS = EVENT_STRINGS.OPTIONS;
    private static final String IMG_PATH = "NineSwordResources/img/events/VoidIsland.png";
    private static final int GOLD_REWARD = 50;

    private Screen screen = Screen.INITIAL;

    public VoidIslandEvent() {
        super(NAME, DESCRIPTIONS[0], IMG_PATH);
        imageEventText.setDialogOption(OPTIONS[0], new VoidCrystal());
        imageEventText.setDialogOption(OPTIONS[1], new Pain());
        imageEventText.setDialogOption(OPTIONS[2]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        if (screen == Screen.COMPLETE) {
            openMap();
            return;
        }

        switch (buttonPressed) {
            case 0:
                int damage = SwordEventUtils.losePercentMaxHealth(25);
                if (AbstractDungeon.player.currentHealth > 0) {
                    VoidCrystal relic = new VoidCrystal();
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain(
                            Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, relic);
                    logMetricObtainRelicAndDamage(ID, "Hunted Ghost Beasts", relic, damage);
                } else {
                    logMetric(ID, "Hunted Ghost Beasts", null, null, null, null, null, null,
                            null, damage, 0, 0, 0, 0, 0);
                }
                complete(DESCRIPTIONS[1]);
                break;
            case 1:
                Pain pain = new Pain();
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(
                        pain, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                EnlightenmentManager.request();
                logMetricObtainCard(ID, "Rebelled", pain);
                complete(DESCRIPTIONS[2]);
                break;
            default:
                AbstractDungeon.player.gainGold(GOLD_REWARD);
                AbstractDungeon.effectList.add(new RainingGoldEffect(GOLD_REWARD));
                logMetricGainGold(ID, "Fled", GOLD_REWARD);
                complete(DESCRIPTIONS[3]);
                break;
        }
    }

    private void complete(String text) {
        screen = Screen.COMPLETE;
        showProceedScreen(text);
    }

    private enum Screen {
        INITIAL,
        COMPLETE
    }
}
