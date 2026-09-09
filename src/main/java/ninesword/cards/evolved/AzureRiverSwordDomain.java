package ninesword.cards.evolved;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.cards.SwordTechniqueCard;

public class AzureRiverSwordDomain extends SwordTechniqueCard {
    public static final String ID = "NineSwordTechniques:AzureRiverSwordDomain";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "NineSwordResources/img/cards/TrueSword.png";

    public AzureRiverSwordDomain() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, 3, CARD_STRINGS.DESCRIPTION,
                CardType.ATTACK, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ALL_ENEMY);
        baseDamage = damage = 36;
        damageType = DamageInfo.DamageType.HP_LOSS;
        damageTypeForTurn = DamageInfo.DamageType.HP_LOSS;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(2);
            rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    protected void repeatEffect(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToBot(new DamageAction(monster, new DamageInfo(p, damage, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SLASH_HEAVY));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new AzureRiverSwordDomain();
    }
}
