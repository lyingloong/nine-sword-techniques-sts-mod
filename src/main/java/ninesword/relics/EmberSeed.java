package ninesword.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import ninesword.relics.evolution.RelicEvolutionManager;

public class EmberSeed extends CustomRelic {
    public static final String ID = "NineSwordTechniques:EmberSeed";
    public static final int VICTORIES_TO_EVOLVE = 4;
    private static final String IMG_PATH =
            "NineSwordResources/img/relics/EmberSeed_128.png";
    private static final String OUTLINE_PATH =
            "NineSwordResources/img/relics/MyRelic_Outline.png";

    public EmberSeed() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH),
                RelicTier.COMMON, LandingSound.MAGICAL);
        counter = 0;
    }

    @Override
    public void onVictory() {
        flash();
        AbstractDungeon.player.decreaseMaxHealth(6);
        setCounter(counter + 1);
        if (counter >= VICTORIES_TO_EVOLVE) {
            RelicEvolutionManager.requestEmberWhiteFlameEvolution();
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new EmberSeed();
    }
}
