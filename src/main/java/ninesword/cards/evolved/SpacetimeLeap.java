package ninesword.cards.evolved;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ConservePower;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import ninesword.cards.SwordTechniqueCard;

import java.util.ArrayList;

public class SpacetimeLeap extends SwordTechniqueCard {
    public static final String ID = "NineSwordTechniques:SpacetimeLeap";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String IMG_PATH = "NineSwordResources/img/cards/IllusorySword.png";

    public SpacetimeLeap() {
        super(ID, CARD_STRINGS.NAME, IMG_PATH, 1, CARD_STRINGS.DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    protected void repeatEffect(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster.isDeadOrEscaped()) {
                continue;
            }
            addToBot(new RemoveAllBlockAction(monster, p));
            for (AbstractPower power : new ArrayList<AbstractPower>(monster.powers)) {
                if (power.type == AbstractPower.PowerType.BUFF) {
                    addToBot(new RemoveSpecificPowerAction(monster, p, power.ID));
                }
            }
            addToBot(new ApplyPowerAction(monster, p,
                    new WeakPower(monster, magicNumber, false), magicNumber));
            addToBot(new ApplyPowerAction(monster, p,
                    new VulnerablePower(monster, magicNumber, false), magicNumber));
        }
        addToBot(new ApplyPowerAction(p, p, new EquilibriumPower(p, 1), 1));
        addToBot(new ApplyPowerAction(p, p, new ConservePower(p, 1), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new SpacetimeLeap();
    }
}
