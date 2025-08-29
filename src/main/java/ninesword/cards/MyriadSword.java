package ninesword.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

public class MyriadSword extends SwordTechniqueCard {
    public static final String ID = "NineSwordTechniques:MyriadSword";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final String IMG_PATH = "NineSwordResources/img/cards/MyriadSword.png";
    private static final int COST = 1;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private int attackTimes;

    public MyriadSword() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = 1;
        this.attackTimes = 5;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.attackTimes = 6;
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void repeatEffect(AbstractPlayer p, AbstractMonster m) {
//        System.out.println("=== repeatEffect 方法触发 ===");
//        System.out.println("当前玩家：" + p.name + "（生命值：" + p.currentHealth + "）");
//        System.out.println("预期攻击次数 attackTimes：" + attackTimes);
//        System.out.println("单次伤害值 damage：" + damage);
//        System.out.println("伤害类型：" + damageTypeForTurn);
        ArrayList<AbstractMonster> monsterList = AbstractDungeon.getMonsters().monsters;
//        System.out.println("当前战场怪物总数：" + monsterList.size());

        for (int idx = 0; idx < monsterList.size(); idx++) {
            AbstractMonster monster = monsterList.get(idx);
//            System.out.println("  怪物" + idx + "：" + monster.name +
//                    "（存活：" + !monster.isDeadOrEscaped() + "）");
        }

        if (monsterList.isEmpty()) {
            System.out.println("⚠️  警告：战场无怪物，无法执行攻击！");
            return;
        }

        Random random = new Random();
//        System.out.println("=== 开始执行攻击循环 ===");
        for (int i = 0; i < this.attackTimes; i++) {
//            System.out.println("  正在执行第 " + (i + 1) + "/" + attackTimes + " 次攻击");
            // 创建有效怪物列表
            ArrayList<AbstractMonster> validMonsters = new ArrayList<AbstractMonster>();
            for (AbstractMonster monster : monsterList) {
                if (!monster.isDeadOrEscaped()) {
                    validMonsters.add(monster);
                }
            }

            // 检查有效怪物数量
            if (validMonsters.isEmpty()) {
                System.out.println("  ⚠️  第 " + (i + 1) + " 次攻击：无有效怪物，跳过此次攻击");
                continue;
            }

            AbstractMonster targetMonster = validMonsters.get(random.nextInt(validMonsters.size()));
//            System.out.println("  第 " + (i + 1) + " 次攻击目标：" + targetMonster.name);
//            System.out.println("  即将添加伤害动作：目标=" + targetMonster.name +
//                    "，伤害值=" + damage + "，攻击特效=SLASH_HORIZONTAL");

            this.addToBot(new com.megacrit.cardcrawl.actions.common.DamageAction(
                    targetMonster,
                    new com.megacrit.cardcrawl.cards.DamageInfo(p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
            ));
        }

//        System.out.println("=== 攻击循环结束 ===");
//        System.out.println("预期攻击次数：" + attackTimes);
//        System.out.println("实际执行的攻击动作数：" + (attackTimes -
//                (monsterList.isEmpty() ? attackTimes : 0) - // 无怪物时跳过的次数
//                (int) IntStream.range(0, attackTimes)
//                        .filter(i -> {
//                            // 计算有效怪物为空时跳过的次数（与循环内逻辑一致）
//                            ArrayList<AbstractMonster> valid = new ArrayList<AbstractMonster>();
//                            for (AbstractMonster monster : monsterList) {
//                                if (!monster.isDeadOrEscaped()) {
//                                    valid.add(monster);
//                                }
//                            }
//                            return valid.isEmpty();
//                        })
//                        .count()));
    }

    @Override
    public AbstractCard makeCopy() {
        return new MyriadSword();
    }
}