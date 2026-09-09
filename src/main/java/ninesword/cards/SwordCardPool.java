package ninesword.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import java.util.ArrayList;

public final class SwordCardPool {
    private SwordCardPool() {
    }

    public static ArrayList<AbstractCard> create() {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        cards.add(new HiddenSword());
        cards.add(new IllusorySword());
        cards.add(new NonSword());
        cards.add(new MyriadSword());
        cards.add(new EmotionalSword());
        cards.add(new TrueSword());
        cards.add(new GhostlySword());
        cards.add(new MindSword());
        cards.add(new NinefoldSword());
        return cards;
    }
}
