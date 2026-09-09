package ninesword.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/** Common card shell for the nine sword techniques. */
public abstract class SwordTechniqueCard extends CustomCard {
    protected SwordTechniqueCard(String id, String name, String img, int cost, String rawDescription,
                                  CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    @Override
    public final void use(AbstractPlayer p, AbstractMonster m) {
        repeatEffect(p, m);
    }

    protected abstract void repeatEffect(AbstractPlayer p, AbstractMonster m);

    @Override
    public abstract AbstractCard makeCopy();
}
