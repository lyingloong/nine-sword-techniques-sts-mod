package ninesword.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class EmberWhiteFlame extends CustomRelic {
    public static final String ID = "NineSwordTechniques:EmberWhiteFlame";
    private static final String IMG_PATH =
            "NineSwordResources/img/relics/EmberWhiteFlame_128.png";
    private static final String OUTLINE_PATH =
            "NineSwordResources/img/relics/MyRelic_Outline.png";
    private static final int NORMAL_HEALTH_DIVISOR = 10;
    private static final int BOSS_HEALTH_DIVISOR = 20;

    public EmberWhiteFlame() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH),
                RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.increaseMaxHp(30, true);
    }

    @Override
    public void atTurnStart() {
        flash();
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                int divisor = monster.type == AbstractMonster.EnemyType.BOSS
                        ? BOSS_HEALTH_DIVISOR : NORMAL_HEALTH_DIVISOR;
                int hpLoss = Math.max(1, monster.maxHealth / divisor);
                addToBot(new LoseHPAction(monster, AbstractDungeon.player, hpLoss,
                        AbstractGameAction.AttackEffect.FIRE));
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new EmberWhiteFlame();
    }
}
