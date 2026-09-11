package ninesword.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

/** A charged event relic that converts enemy kills into Intangible. */
public class VoidCrystal extends CustomRelic {
    public static final String ID = "NineSwordTechniques:VoidCrystal";
    private static final String IMG_PATH =
            "NineSwordResources/img/relics/VoidCrystal_128.png";
    private static final String OUTLINE_PATH =
            "NineSwordResources/img/relics/MyRelic_Outline.png";
    private static final int CHARGE_COST = 2;

    public VoidCrystal() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH),
                RelicTier.SPECIAL, LandingSound.MAGICAL);
        counter = 0;
    }

    @Override
    public void onMonsterDeath(AbstractMonster monster) {
        if (monster == null) {
            return;
        }
        flash();
        setCounter(counter + 1);
    }

    @Override
    public void atTurnStart() {
        if (counter < CHARGE_COST || AbstractDungeon.player == null) {
            return;
        }
        flash();
        setCounter(counter - CHARGE_COST);
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new IntangiblePlayerPower(AbstractDungeon.player, 1), 1));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new VoidCrystal();
    }
}
