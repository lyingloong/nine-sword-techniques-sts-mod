package ninesword.muzixi.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MuzixiStrike extends MuzixiCard {
    public static final String ID = "NineSwordTechniques:MuzixiStrike";
    private static final int DAMAGE = 6;

    public MuzixiStrike() {
        super(ID, "MuzixiStrike", 1, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);
        baseDamage = DAMAGE;
        damageTypeForTurn = DamageInfo.DamageType.NORMAL;
        tags.add(CardTags.STRIKE);
        tags.add(CardTags.STARTER_STRIKE);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        damage(player, monster, damage, com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(3);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new MuzixiStrike();
    }
}
