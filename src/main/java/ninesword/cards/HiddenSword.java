package ninesword.cards;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

class HiddenSwordForbidPower extends AbstractPower {
    public static final String POWER_ID = "NineSwordTechniques:HiddenSwordForbidPower";
    private static final String POWER_NAME = "藏剑封印";
    private static final String POWER_DESCRIPTION = "本回合无法打出攻击牌和技能牌。";

    public HiddenSwordForbidPower(AbstractCreature owner) {
        this.name = POWER_NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.DEBUFF; // 负面减益
        this.isTurnBased = true; // 回合结束自动消失
        this.amount = -1; // 不能叠加

        // 能力图
        String path128 = "NineSwordResources/img/powers/HiddenSwordForbid128.png";
        String path48 = "NineSwordResources/img/powers/HiddenSwordForbid48.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = POWER_DESCRIPTION;
    }

    // 禁止攻击牌使用的核心逻辑
    @Override
    public boolean canPlayCard(AbstractCard card) {
        return !(card.type == AbstractCard.CardType.ATTACK || card.type == AbstractCard.CardType.SKILL);
    }

    // 回合结束强制移除
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.onRemove();
    }
}

class HiddenSwordDelayPower extends AbstractPower {
    public static final String POWER_ID = "NineSwordTechniques:HiddenSwordDelayPower";
    private static final String POWER_NAME = "藏剑蓄势";
    private static final String POWER_DESCRIPTION = "下回合开始时，对随机敌人造成!D!点伤害。";

    private final int delayDamage;

    public HiddenSwordDelayPower(AbstractCreature owner, int delayDamage) {
        this.name = POWER_NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        this.delayDamage = delayDamage;

        String path128 = "NineSwordResources/img/powers/HiddenSwordDelay128.png";
        String path48 = "NineSwordResources/img/powers/HiddenSwordDelay48.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = POWER_DESCRIPTION.replace("!D!", String.valueOf(this.delayDamage));
    }

    @Override
    public void atStartOfTurn() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            // 随机选择1个存活敌人
            AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(
                    null, true, AbstractDungeon.cardRandomRng
            );
            if (target != null) {
                // 执行伤害动作
                AbstractDungeon.actionManager.addToTop(
                        new DamageAction(
                                target,
                                new DamageInfo(this.owner, this.delayDamage, DamageInfo.DamageType.NORMAL),
                                AbstractGameAction.AttackEffect.SLASH_HEAVY
                        )
                );
            }
        }
        // 触发后立即移除标记
        this.onRemove();
    }
}

public class HiddenSword extends CustomCard {
    public static final String ID = "NineSwordTechniques:HiddenSword";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final String IMG_PATH = "NineSwordResources/img/cards/HiddenSword.png";
    private static final int COST = 2;
    private static final int BASE_DAMAGE = 36;
    private static final int UPGRADE_DAMAGE = 45;

    public HiddenSword() {
        super(
                ID,
                NAME,
                IMG_PATH,
                COST,
                DESCRIPTION,
                CardType.SKILL,
                CardColor.COLORLESS,
                CardRarity.UNCOMMON,
                CardTarget.NONE
        );

        this.baseDamage = BASE_DAMAGE;
        this.damage = this.baseDamage;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE - BASE_DAMAGE); // 36→45
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(
                        p,
                        p,
                        new HiddenSwordForbidPower(p),
                        1
                )
        );

        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(
                        p,
                        p,
                        new HiddenSwordDelayPower(p, this.damage),
                        1
                )
        );
    }

    @Override
    public AbstractCard makeCopy() {
        return new HiddenSword();
    }
}