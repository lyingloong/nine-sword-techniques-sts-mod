package ninesword.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import ninesword.powers.SwordIntent;

public class TheCanonOfSwordObservation extends CustomRelic {
    public static final String ID = "NineSwordTechniques:TheCanonOfSwordObservation";
    // 图片路径（大小128x128）
    private static final String IMG_PATH = "NineSwordResources/img/relics/TheCanonOfSwordObservation_128.png";
    // 遗物未解锁时的轮廓
    private static final String OUTLINE_PATH = "NineSwordResources/img/relics/MyRelic_Outline.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public TheCanonOfSwordObservation() {
//        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        // 如果你需要轮廓图，取消注释下面一行并注释上面一行，不需要就删除
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.addToBot(new ApplyPowerAction(
                AbstractDungeon.player,
                AbstractDungeon.player,
                new SwordIntent(AbstractDungeon.player),
                1
        ));
    }

    public AbstractRelic makeCopy() {
        return new TheCanonOfSwordObservation();
    }
}
