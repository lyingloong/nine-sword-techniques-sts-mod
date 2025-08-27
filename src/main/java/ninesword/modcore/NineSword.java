package ninesword.modcore;

import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;

import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.PostDungeonInitializeSubscriber;
import ninesword.cards.*;
import java.util.ArrayList;
import java.util.Collections;
import com.badlogic.gdx.Gdx;
import ninesword.relics.TheCanonOfSwordObservation;

import static basemod.helpers.RelicType.SHARED;

@SpireInitializer
public class NineSword implements EditCardsSubscriber, EditStringsSubscriber, EditRelicsSubscriber, PostDungeonInitializeSubscriber {
    public NineSword() {
        BaseMod.subscribe(this);
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new HiddenSword());
        BaseMod.addCard(new IllusorySword());
        BaseMod.addCard(new NonSword());
        BaseMod.addCard(new MyriadSword());
        BaseMod.addCard(new EmotionalSword());
        BaseMod.addCard(new TrueSword());
        BaseMod.addCard(new GhostlySword());
        BaseMod.addCard(new MindSword());
        BaseMod.addCard(new NinefoldSword());

        BaseMod.logger.info("【九剑模组】卡牌注册完成");
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelic(new TheCanonOfSwordObservation(), SHARED);

        BaseMod.logger.info("【九剑模组】遗物注册完成");
    }

    @Override
    public void receiveEditStrings() {
        String lang;
        if (Settings.language == Settings.GameLanguage.ZHS) {
            lang = "ZHS"; // 如果语言设置为简体中文，则加载ZHS文件夹的资源
        } else {
            lang = "ENG"; // 如果没有相应语言的版本，默认加载英语
        }
        BaseMod.loadCustomStringsFile(CardStrings.class, "NineSwordResources/localization/" + lang + "/cards.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, "NineSwordResources/localization/" + lang + "/relics.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, "NineSwordResources/localization/" + lang + "/powers.json");
//        BaseMod.loadCustomStringsFile(CharacterStrings.class, "NineSwordResources/localization/" + lang + "/characters.json");
    }

    @Override
    public void receivePostDungeonInitialize() {
        BaseMod.logger.info("【九剑模组】进入PostDungeonInitialize阶段");

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                if (AbstractDungeon.actionManager == null ||
                        AbstractDungeon.getCurrRoom() == null ||
                        AbstractDungeon.player == null) {

                    BaseMod.logger.info("【九剑模组】等待组件就绪...");
                    Gdx.app.postRunnable(this);
                    return;
                }

                BaseMod.logger.info("【九剑模组】组件就绪，准备卡牌选择");
                if (AbstractDungeon.player.masterDeck != null) {
                    BaseMod.logger.info("【九剑模组】初始牌组大小：" + AbstractDungeon.player.masterDeck.size());
                } else {
                    BaseMod.logger.info("【九剑模组】masterDeck为null！");
                }

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
                CardGroup startingSwords = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for (int i = 0; i < 5; i++) {
                    startingSwords.addToTop(allSwords.get(i));
                }

                BaseMod.logger.info("【九剑模组】准备打开选择界面");
                AbstractDungeon.gridSelectScreen.open(
                        startingSwords,
                        1,
                        "选择初始剑术",
                        false, false, false, false
                );

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        // 循环等待选择完成
                        BaseMod.logger.info("【九剑模组】等待玩家选择卡牌...");
                        if (AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                            Gdx.app.postRunnable(this);
                            return;
                        }

                        // 执行选择结果处理
                        BaseMod.logger.info("【九剑模组】进入选择结果处理逻辑");
                        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() &&
                                AbstractDungeon.player != null &&
                                AbstractDungeon.player.masterDeck != null) {

                            AbstractCard selectedCard = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                            AbstractCard copy = selectedCard.makeCopy();

                            BaseMod.logger.info("【九剑模组】选中卡牌：" + copy.name + "（ID：" + copy.cardID + "）");

                            // 添加到主牌组
                            int beforeSize = AbstractDungeon.player.masterDeck.size();
                            AbstractDungeon.player.masterDeck.addToBottom(copy);
                            int afterSize = AbstractDungeon.player.masterDeck.size();

                            BaseMod.logger.info("【九剑模组】添加前牌组大小：" + beforeSize + "，添加后：" + afterSize);

                            if (AbstractDungeon.player.masterDeck.contains(copy)) {
                                BaseMod.logger.info("【九剑模组】卡牌成功加入主牌组");
                            } else {
                                BaseMod.logger.info("【九剑模组】卡牌未成功加入主牌组！");
                            }

                            // 清除选择
                            AbstractDungeon.gridSelectScreen.selectedCards.clear();
                        } else {
                            if (AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                                BaseMod.logger.info("【九剑模组】未选中任何卡牌");
                            } else if (AbstractDungeon.player == null) {
                                BaseMod.logger.info("【九剑模组】player为null");
                            } else if (AbstractDungeon.player.masterDeck == null) {
                                BaseMod.logger.info("【九剑模组】masterDeck为null");
                            }
                        }
                    }
                });
            }
        });
    }

    public static void initialize() {
        new NineSword();
    }
}
