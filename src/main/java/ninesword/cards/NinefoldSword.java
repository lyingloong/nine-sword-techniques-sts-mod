package ninesword.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class NinefoldSword extends SwordTechniqueCard {
    public static final String ID = "NineSwordTechniques:NinefoldSword";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "NineSwordResources/img/cards/NinefoldSword.png";

    public NinefoldSword() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, 1, CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK, CardColor.COLORLESS, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = damage = 8;
        baseBlock = block = 8;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(2);
            upgradeBlock(2);
            rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    protected void repeatEffect(AbstractPlayer p, AbstractMonster m) {
        if (m == null) {
            applyPowers();
        } else {
            calculateCardDamage(m);
        }
        if (m != null && !m.isDeadOrEscaped()) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
        addToBot(new GainBlockAction(p, block));
    }

    @Override
    public void applyPowers() {
        int originalBaseDamage = baseDamage;
        int originalBaseBlock = baseBlock;
        int bonus = currentBonus();
        baseDamage += bonus;
        baseBlock += bonus;
        super.applyPowers();
        baseDamage = originalBaseDamage;
        baseBlock = originalBaseBlock;
        isDamageModified = damage != baseDamage;
        isBlockModified = block != baseBlock;
    }

    @Override
    public void calculateCardDamage(AbstractMonster monster) {
        int originalBaseDamage = baseDamage;
        int originalBaseBlock = baseBlock;
        int bonus = currentBonus();
        baseDamage += bonus;
        baseBlock += bonus;
        super.calculateCardDamage(monster);
        baseDamage = originalBaseDamage;
        baseBlock = originalBaseBlock;
        isDamageModified = damage != baseDamage;
        isBlockModified = block != baseBlock;
    }

    private int currentBonus() {
        if (AbstractDungeon.actionManager == null) {
            return 0;
        }
        return AbstractDungeon.actionManager.cardsPlayedThisTurn.size() * 2;
    }

    @Override
    public AbstractCard makeCopy() {
        return new NinefoldSword();
    }
}
