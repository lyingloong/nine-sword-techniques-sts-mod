package ninesword.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import ninesword.powers.SwordIntent;

public class TheYousiSword extends CustomRelic {
    public static final String ID = "NineSwordTechniques:TheYousiSword";
    // 图片路径（大小128x128）
    private static final String IMG_PATH = "NineSwordResources/img/relics/TheYousiSword_128.png";
    // 遗物未解锁时的轮廓
    private static final String OUTLINE_PATH = "NineSwordResources/img/relics/MyRelic_Outline.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public TheYousiSword() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        // 如果你需要轮廓图，取消注释下面一行并注释上面一行，不需要就删除
//        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH), RELIC_TIER, LANDING_SOUND);
        this.counter = 0;
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            ++this.counter;
            if (this.counter == 4) {
                this.counter = 0;
                this.flash();
                this.pulse = false;
                this.addToBot(new ApplyPowerAction(
                        AbstractDungeon.player,
                        AbstractDungeon.player,
                        new SwordIntent(AbstractDungeon.player),
                        1
                ));
            } else if (this.counter == 3) {
                this.beginPulse();
                this.pulse = true;
            }
        }

    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        if (this.counter == 3) {
            this.beginPulse();
            this.pulse = true;
        }
    }

    public AbstractRelic makeCopy() {
        return new TheYousiSword();
    }
}
