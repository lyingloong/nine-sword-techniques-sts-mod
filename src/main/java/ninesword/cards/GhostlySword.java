package ninesword.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.powers.GhostlySwordPower;

public class GhostlySword extends SwordTechniqueCard {
    public static final String ID = "NineSwordTechniques:GhostlySword";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "NineSwordResources/img/cards/GhostlySword.png";

    public GhostlySword() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, 1, CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK, CardColor.COLORLESS, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = damage = 8;
        returnToHand = true;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(2);
            rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    protected void repeatEffect(AbstractPlayer p, AbstractMonster m) {
        if (m == null || m.isDeadOrEscaped()) {
            return;
        }
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot(new ApplyPowerAction(p, p, new GhostlySwordPower(p, 2), 2));
    }

    @Override
    public void applyPowers() {
        int originalBaseDamage = baseDamage;
        baseDamage += combatDamageBonus();
        super.applyPowers();
        baseDamage = originalBaseDamage;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster monster) {
        int originalBaseDamage = baseDamage;
        baseDamage += combatDamageBonus();
        super.calculateCardDamage(monster);
        baseDamage = originalBaseDamage;
        isDamageModified = damage != baseDamage;
    }

    private int combatDamageBonus() {
        if (AbstractDungeon.player == null) {
            return 0;
        }
        GhostlySwordPower power = (GhostlySwordPower) AbstractDungeon.player
                .getPower(GhostlySwordPower.POWER_ID);
        return power == null ? 0 : power.amount;
    }

    @Override
    public AbstractCard makeCopy() {
        return new GhostlySword();
    }
}
