package ninesword.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import ninesword.cards.SwordCardChoiceManager;

public class SwordAncestorsLegacy extends CustomRelic {
    public static final String ID = "NineSwordTechniques:SwordAncestorsLegacy";
    private static final int USED_COUNTER = -2;
    private static final String IMG_PATH =
            "NineSwordResources/img/relics/SwordAncestorsLegacy_128.png";
    private static final String OUTLINE_PATH =
            "NineSwordResources/img/relics/MyRelic_Outline.png";

    public SwordAncestorsLegacy() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH),
                RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public void update() {
        super.update();
        restoreUsedUpAppearance();
        if (hb.hovered && InputHelper.justClickedRight && canActivate()) {
            SwordAncestorsLegacyManager.openChoice();
        }
    }

    boolean canActivate() {
        AbstractRoom room = AbstractDungeon.getCurrRoom();
        return counter != USED_COUNTER
                && AbstractDungeon.player != null
                && AbstractDungeon.cardRewardScreen != null
                && SwordCardChoiceManager.canOpenSelectionScreen()
                && room != null
                && room.phase != AbstractRoom.RoomPhase.COMBAT;
    }

    boolean consume() {
        if (counter == USED_COUNTER) {
            return false;
        }
        flash();
        setCounter(USED_COUNTER);
        usedUp();
        return true;
    }

    private void restoreUsedUpAppearance() {
        if (counter == USED_COUNTER && !usedUp) {
            usedUp();
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SwordAncestorsLegacy();
    }
}
