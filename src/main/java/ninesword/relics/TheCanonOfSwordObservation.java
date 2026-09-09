package ninesword.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import ninesword.enlightenment.EnlightenmentManager;

public class TheCanonOfSwordObservation extends CustomRelic {
    public static final String ID = "NineSwordTechniques:TheCanonOfSwordObservation";
    private static final String IMG_PATH =
            "NineSwordResources/img/relics/TheCanonOfSwordObservation_128.png";
    private static final String OUTLINE_PATH =
            "NineSwordResources/img/relics/MyRelic_Outline.png";

    public TheCanonOfSwordObservation() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH),
                RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void onEquip() {
        flash();
        EnlightenmentManager.request();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new TheCanonOfSwordObservation();
    }
}
