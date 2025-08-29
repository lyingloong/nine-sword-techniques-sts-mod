package ninesword.neow;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.InfiniteSpeechBubble;
import ninesword.cards.*;
import ninesword.modspire.ModEnums;
import ninesword.powers.SwordIntent;

import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowReward;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ninesword.relics.TheCanonOfSwordObservation;

/*
    Extend initial options
 */

@SpirePatch(clz = NeowEvent.class, method = "blessing")
class NeowBlessingPatch01 {
    @SpireInsertPatch(rloc = 464-452)
    private static void blessing(NeowEvent __instance,  ArrayList<NeowReward> ___rewards) {
        System.out.println("[NeowBlessingPatch01] 添加奖励前，___rewards长度：" + ___rewards.size());
        System.out.println("[NeowBlessingPatch01] 准备添加 category=4 的 NeowReward");
        NeowReward newReward = new NeowReward(4);
        ___rewards.add(newReward);
        System.out.println("[NeowBlessingPatch01] 添加奖励后，___rewards长度：" + ___rewards.size());
        System.out.println("[NeowBlessingPatch01] 新增奖励的optionLabel：" + newReward.optionLabel);
    }
}

@SpirePatch(clz = NeowEvent.class, method = "blessing")
class NeowBlessingPatch02 {
    @SpireInsertPatch(rloc = 470-452)
    private static void blessing(NeowEvent __instance,  ArrayList<NeowReward> ___rewards) {
        // 调试1：先检查___rewards是否有索引4的元素（避免越界）
        if (___rewards.size() <= 4) {
            System.out.println("[NeowBlessingPatch02] 警告：___rewards长度不足4，无法获取索引4的奖励！当前长度：" + ___rewards.size());
            return; // 避免空指针或越界
        }

        // 获取奖励标签并添加选项
        NeowReward targetReward = ___rewards.get(4);
        String optionLabel = targetReward.optionLabel;
        __instance.roomEventText.addDialogOption(optionLabel);

        // 调试2：打印添加的选项信息
        System.out.println("[NeowBlessingPatch02] 成功添加选项，标签：" + optionLabel);
//        System.out.println("[NeowBlessingPatch02] 当前界面选项总数：" + __instance.roomEventText.getOptionCount()); // 需反射获取选项数量
    }
}

@SpirePatch(clz = NeowEvent.class, method = "miniBlessing")
class NeowMiniBlessingPatch01 {
    @SpireInsertPatch(rloc = 444-437)
    private static void miniBlessing(NeowEvent __instance,  ArrayList<NeowReward> ___rewards) {
        // 调试1：打印rng初始化前的状态
        System.out.println("[NeowMiniBlessingPatch01] 初始化前，NeowEvent.rng是否为null：" + (NeowEvent.rng == null));

        // 初始化rng并添加奖励
        NeowEvent.rng = new Random(Settings.seed);
        NeowReward newReward = new NeowReward(4);
        ___rewards.add(newReward);

        // 调试2：打印初始化后的数据
        System.out.println("[NeowMiniBlessingPatch01] 初始化后，NeowEvent.rng.counter：" + NeowEvent.rng.counter);
        System.out.println("[NeowMiniBlessingPatch01] 添加奖励后，___rewards长度：" + ___rewards.size());
        System.out.println("[NeowMiniBlessingPatch01] " + ___rewards.get(0).optionLabel);
        System.out.println("[NeowMiniBlessingPatch01] " + ___rewards.get(1).optionLabel);
//        System.out.println("[NeowMiniBlessingPatch01] " + ___rewards.get(2).optionLabel);
        System.out.println("[NeowMiniBlessingPatch01] 新增奖励的type：" + newReward.type); // 确认奖励类型是否为自定义的ModEnums.TheCanonOfSwordObservation_BONUS
    }
}

@SpirePatch(clz = NeowEvent.class, method = "miniBlessing")
class NeowMiniBlessingPatch02 {
    @SpireInsertPatch(rloc = 448-437)
    private static void miniBlessing(NeowEvent __instance,  ArrayList<NeowReward> ___rewards) {
        // 调试1：检查___rewards是否有索引2的元素（原逻辑是2个奖励，新增后应为3个，索引2是新增的）
        if (___rewards.size() <= 2) {
            System.out.println("[NeowMiniBlessingPatch02] 警告：___rewards长度不足2，无法获取索引2的奖励！当前长度：" + ___rewards.size());
            return;
        }
        System.out.println("[NeowMiniBlessingPatch02] " + ___rewards.get(0).optionLabel);
        System.out.println("[NeowMiniBlessingPatch02] " + ___rewards.get(1).optionLabel);
        System.out.println("[NeowMiniBlessingPatch02] " + ___rewards.get(2).optionLabel);

        // 添加选项
        NeowReward targetReward = ___rewards.get(2);
        String optionLabel = targetReward.optionLabel;
        __instance.roomEventText.addDialogOption(optionLabel);

        // 调试2：打印选项信息
        System.out.println("[NeowMiniBlessingPatch02] 成功添加选项，标签：" + optionLabel);
        System.out.println("[NeowMiniBlessingPatch02] " + RoomEventDialog.optionList.get(0).msg);
//        System.out.println("[NeowMiniBlessingPatch02] 当前界面选项总数：" + getOptionCount(__instance.roomEventText));
    }
}

@SpirePatch(clz = NeowReward.class, method = "getRewardOptions")
class GetRewardPatch {
    public static final String ID = "NineSwordTechniques:NeowReward";
    private static final CharacterStrings CHARACTER_STRINGS = CardCrawlGame.languagePack.getCharacterString(ID);
    @SpirePostfixPatch
    private static ArrayList<NeowReward.NeowRewardDef> getRewardOptions(ArrayList<NeowReward.NeowRewardDef> __result, NeowReward __instance, int category) {
        // 调试1：打印当前category和原奖励列表长度
        System.out.println("[GetRewardPatch] 进入category：" + category + "，原奖励列表长度：" + __result.size());
        System.out.println("[GetRewardPatch] 自定义奖励类型ModEnums.TheCanonOfSwordObservation_BONUS是否为null：" + (ModEnums.TheCanonOfSwordObservation_BONUS == null));

        // category=4时添加自定义奖励
        if (category == 4) {
            NeowReward.NeowRewardDef customDef = new NeowReward.NeowRewardDef(ModEnums.TheCanonOfSwordObservation_BONUS, CHARACTER_STRINGS.TEXT[0]);
            __result.add(customDef);
            System.out.println("[GetRewardPatch] category=4，已添加自定义奖励，新列表长度：" + __result.size());
            System.out.println("[GetRewardPatch] 自定义奖励desc：" + customDef.desc + "，type：" + customDef.type);
        }

        return __result;
    }
}

@SpirePatch(clz = NeowReward.class, method = "activate")
class ActivatePatch {
    @SpireInsertPatch(rloc = 426-268)
    public static void activate(NeowReward __instance) {
        // 调试1：打印当前奖励类型，确认是否匹配自定义类型
        System.out.println("[ActivatePatch] 当前奖励type：" + __instance.type);
        System.out.println("[ActivatePatch] 自定义奖励type：" + ModEnums.TheCanonOfSwordObservation_BONUS);
        System.out.println("[ActivatePatch] 类型是否匹配：" + (__instance.type == ModEnums.TheCanonOfSwordObservation_BONUS));

        // 执行自定义奖励效果
        if (__instance.type == ModEnums.TheCanonOfSwordObservation_BONUS) {
            System.out.println("[ActivatePatch] 触发自定义奖励：获得观剑典");
            AbstractRelic targetRelic = new TheCanonOfSwordObservation();
            System.out.println("[ActivatePatch] 遗物ID：" + targetRelic.relicId);
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(
                    (float)(Settings.WIDTH / 2),
                    (float)(Settings.HEIGHT / 2),
                    targetRelic
            );
            System.out.println("[ActivatePatch] 遗物已生成并发放给玩家");

            ArrayList<AbstractCard> allSwords = new ArrayList<>();
            allSwords.add(new HiddenSword());
            allSwords.add(new IllusorySword());
            allSwords.add(new NonSword());
            allSwords.add(new MyriadSword());
            allSwords.add(new EmotionalSword());
            allSwords.add(new TrueSword());
            allSwords.add(new GhostlySword());
            allSwords.add(new MindSword());
            allSwords.add(new NinefoldSword());
            Collections.shuffle(allSwords);
            List<AbstractCard> selectedSwords = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                selectedSwords.add(allSwords.get(i));
            }

            allSwords.clear();
            allSwords.addAll(selectedSwords);
            AbstractDungeon.cardRewardScreen.open(allSwords, (RewardItem)null, NeowReward.TEXT[22]);
        }
    }
}

@SpirePatch(clz = NeowEvent.class, method = "buttonEffect")
class ButtonEffectPatch {
    private static final float DIALOG_X = 1100.0F * Settings.xScale, DIALOG_Y = AbstractDungeon.floorY + 60.0F * Settings.yScale;
    @SpireInsertPatch(rloc = 254-194)
    protected static void buttonEffect(NeowEvent __instance, int buttonPressed, ArrayList<NeowReward> ___rewards) {
        if (buttonPressed == 4) {
            ((NeowReward)___rewards.get(4)).activate();
            AbstractDungeon.effectList.add(new InfiniteSpeechBubble(DIALOG_X, DIALOG_Y, NeowEvent.TEXT[9]));
        }
    }
}
