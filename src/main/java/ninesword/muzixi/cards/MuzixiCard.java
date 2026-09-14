package ninesword.muzixi.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import ninesword.modspire.ModEnums;
import ninesword.muzixi.actions.GainVitalityAction;
import ninesword.muzixi.actions.SwitchPersonaAction;
import ninesword.muzixi.powers.MuzixiPersonaPower;
import ninesword.muzixi.powers.TearPersonaPower;
import ninesword.muzixi.powers.VitalityPower;

public abstract class MuzixiCard extends CustomCard {
    protected static final String IMAGE_ROOT = "NineSwordResources/img/muzixi/cards/";

    protected MuzixiCard(String id, String imageName, int cost, CardType type,
                         CardRarity rarity, CardTarget target) {
        super(id, strings(id).NAME, IMAGE_ROOT + imageName + ".png", cost, strings(id).DESCRIPTION,
                type, ModEnums.MUZIXI_GREEN, rarity, target);
    }

    protected static CardStrings strings(String id) {
        return CardCrawlGame.languagePack.getCardStrings(id);
    }

    protected void gainVitality(AbstractPlayer player, int amount) {
        addToBot(new GainVitalityAction(player, amount));
    }

    protected void switchPersona(AbstractPlayer player) {
        addToBot(new SwitchPersonaAction(player));
    }

    protected boolean hasVitality(AbstractPlayer player, int amount) {
        return VitalityPower.getAmount(player) >= amount;
    }

    protected boolean isMuzixi(AbstractPlayer player) {
        return player.hasPower(MuzixiPersonaPower.POWER_ID);
    }

    protected boolean isTear(AbstractPlayer player) {
        return player.hasPower(TearPersonaPower.POWER_ID);
    }

    protected void damage(AbstractPlayer player, AbstractMonster monster, int amount) {
        player.useFastAttackAnimation();
        addToBot(new DamageAction(monster, new DamageInfo(player, amount, DamageInfo.DamageType.NORMAL)));
    }

    protected void damage(AbstractPlayer player, AbstractMonster monster, int amount,
                          com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect effect) {
        player.useFastAttackAnimation();
        addToBot(new DamageAction(monster, new DamageInfo(player, amount, DamageInfo.DamageType.NORMAL), effect));
    }

    protected void block(AbstractPlayer player, int amount) {
        addToBot(new GainBlockAction(player, player, amount));
    }

    protected void applySelfPower(AbstractPlayer player, com.megacrit.cardcrawl.powers.AbstractPower power,
                                  int amount) {
        addToBot(new ApplyPowerAction(player, player, power, amount));
    }

    @Override
    public abstract AbstractCard makeCopy();
}
