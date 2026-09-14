package ninesword.muzixi.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import ninesword.muzixi.actions.EnterMuzixiPersonaAction;
import ninesword.muzixi.actions.GainVitalityAction;

public class DivineDemonicEyes extends CustomRelic {
    public static final String ID = "NineSwordTechniques:DivineDemonicEyes";
    private static final String IMAGE = "NineSwordResources/img/muzixi/relics/DivineDemonicEyes_128.png";
    private static final String OUTLINE = "NineSwordResources/img/muzixi/relics/DivineDemonicEyes_outline.png";

    public DivineDemonicEyes() {
        super(ID, ImageMaster.loadImage(IMAGE), ImageMaster.loadImage(OUTLINE),
                RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStartPreDraw() {
        flash();
        addToBot(new EnterMuzixiPersonaAction(AbstractDungeon.player));
        addToBot(new GainVitalityAction(AbstractDungeon.player, 2));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DivineDemonicEyes();
    }
}
