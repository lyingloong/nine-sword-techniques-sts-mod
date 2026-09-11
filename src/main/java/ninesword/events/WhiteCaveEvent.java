package ninesword.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import ninesword.enlightenment.EnlightenmentManager;

public class WhiteCaveEvent extends AbstractImageEvent {
    public static final String ID = "NineSwordTechniques:WhiteCave";
    private static final EventStrings EVENT_STRINGS = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = EVENT_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = EVENT_STRINGS.DESCRIPTIONS;
    public static final String[] OPTIONS = EVENT_STRINGS.OPTIONS;
    private static final String IMG_PATH = "NineSwordResources/img/events/WhiteCave.png";

    private Screen screen = Screen.INITIAL;

    public WhiteCaveEvent() {
        super(NAME, DESCRIPTIONS[0], IMG_PATH);
        int slaughterDamage = SwordEventUtils.percentMaxHealth(25);
        int swordDamage = SwordEventUtils.percentMaxHealth(10);
        imageEventText.setDialogOption(OPTIONS[0] + slaughterDamage + OPTIONS[1]);
        imageEventText.setDialogOption(OPTIONS[2] + swordDamage + OPTIONS[3]);
        imageEventText.setDialogOption(OPTIONS[4]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        if (screen == Screen.COMPLETE) {
            openMap();
            return;
        }

        switch (buttonPressed) {
            case 0:
                int enlightenmentDamage = SwordEventUtils.losePercentMaxHealth(25);
                if (AbstractDungeon.player.currentHealth > 0) {
                    EnlightenmentManager.request();
                }
                logMetric(ID, "Joined the Slaughter", null, null, null, null, null, null,
                        null, enlightenmentDamage, 0, 0, 0, 0, 0);
                complete(DESCRIPTIONS[1] + enlightenmentDamage + DESCRIPTIONS[2]);
                break;
            case 1:
                int swordDamage = SwordEventUtils.losePercentMaxHealth(10);
                if (AbstractDungeon.player.currentHealth > 0) {
                    AbstractCard sword = SwordEventUtils.randomBaseSword();
                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(
                            sword, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                    logMetricObtainCardAndDamage(ID, "Grabbed a Sword", sword, swordDamage);
                } else {
                    logMetric(ID, "Grabbed a Sword", null, null, null, null, null, null,
                            null, swordDamage, 0, 0, 0, 0, 0);
                }
                complete(DESCRIPTIONS[3] + swordDamage + DESCRIPTIONS[4]);
                break;
            default:
                logMetric(ID, "Left");
                complete(DESCRIPTIONS[5]);
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
