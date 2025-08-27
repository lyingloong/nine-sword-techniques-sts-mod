package ninesword.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.powers.SwordIntent;

public class SwordTechniqueCard extends CustomCard {
    // 构造方法
    public SwordTechniqueCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    /**
     * 消耗所有剑念并返回消耗的层数
     * @param p 玩家对象
     * @return 消耗的剑念层数
     */
    public int consumeSwordIntent(AbstractPlayer p) {
        // 检查玩家是否有剑念效果
        if (p.hasPower(SwordIntent.POWER_ID)) {
            SwordIntent swordIntent = (SwordIntent) p.getPower(SwordIntent.POWER_ID);
            int stacks = swordIntent.amount; // 获取当前剑念层数

            // 消耗所有剑念
            if (stacks > 0) {
                // 减少所有层数
                swordIntent.reducePower(stacks);
                // 更新描述
                swordIntent.updateDescription();

                // 如果层数归0，移除这个power
                if (swordIntent.amount <= 0) {
                    p.powers.remove(swordIntent);
                }
            }

            return stacks;
        }
        return 0; // 没有剑念时返回0
    }

    /**
     * 添加剑念层数的方法
     * @param p 玩家对象
     * @param amount 要添加的层数
     */
    public void addSwordIntent(AbstractPlayer p, int amount) {
        if (p.hasPower(SwordIntent.POWER_ID)) {
            // 如果已有剑念，直接增加层数
            SwordIntent swordIntent = (SwordIntent) p.getPower(SwordIntent.POWER_ID);
            swordIntent.amount += amount;
            swordIntent.updateDescription();
        } else if (amount > 0) {
            // 如果没有剑念且要添加的层数为正，创建新的剑念
            p.addPower(new SwordIntent(p));
            // 调整初始层数（默认是1，所以需要减去1再加上要添加的层数）
            if (amount > 1) {
                SwordIntent swordIntent = (SwordIntent) p.getPower(SwordIntent.POWER_ID);
                swordIntent.amount += (amount - 1);
                swordIntent.updateDescription();
            }
        }
    }

    /**
     * 升级卡牌的方法
     */
    @Override
    public void upgrade() {}

    /**
     * 使用卡牌的方法
     */
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 基础释放一次
        this.repeatEffect(p, m);

        // 检查并消耗剑念，获取消耗的层数
        int consumedLayers = consumeSwordIntent(p);

        // 每消耗一层剑念，重复打出该卡牌一次
        for (int i = 0; i < consumedLayers; i++) {
            // 为了避免无限循环，这里调用一个单独的方法来执行效果
            // 而不是直接递归调用use()
            this.flash();
            this.repeatEffect(p, m);
        }
    }

    /**
     * 重复执行卡牌效果的方法
     * 用于避免在重复打出时再次触发剑念消耗逻辑
     */
    protected void repeatEffect(AbstractPlayer p, AbstractMonster m) {
        // 需要重复执行的卡牌效果
    }
}
