package ninesword.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import ninesword.cards.SwordCardPool;

import java.util.ArrayList;

final class SwordEventUtils {
    private SwordEventUtils() {
    }

    static int losePercentMaxHealth(int percent) {
        int damage = Math.max(1, AbstractDungeon.player.maxHealth * percent / 100);
        AbstractDungeon.player.damage(new DamageInfo(null, damage, DamageInfo.DamageType.HP_LOSS));
        return damage;
    }

    static AbstractCard randomBaseSword() {
        ArrayList<AbstractCard> cards = SwordCardPool.create();
        return cards.get(AbstractDungeon.miscRng.random(cards.size() - 1));
    }
}
