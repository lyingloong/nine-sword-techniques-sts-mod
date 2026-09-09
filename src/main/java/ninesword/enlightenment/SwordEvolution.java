package ninesword.enlightenment;

import com.megacrit.cardcrawl.cards.AbstractCard;
import ninesword.cards.EmotionalSword;
import ninesword.cards.GhostlySword;
import ninesword.cards.HiddenSword;
import ninesword.cards.IllusorySword;
import ninesword.cards.MindSword;
import ninesword.cards.MyriadSword;
import ninesword.cards.NinefoldSword;
import ninesword.cards.NonSword;
import ninesword.cards.TrueSword;
import ninesword.cards.UnsheathedBlade;
import ninesword.cards.evolved.AbsoluteImperialRule;
import ninesword.cards.evolved.AzureRiverSwordDomain;
import ninesword.cards.evolved.InfiniteNumeration;
import ninesword.cards.evolved.MortalBladeAllLivingFaces;
import ninesword.cards.evolved.SoulControlTrickery;
import ninesword.cards.evolved.SpacetimeLeap;
import ninesword.cards.evolved.UnderOnesGaze;
import ninesword.cards.evolved.VoidBladeStyle;

public final class SwordEvolution {
    private SwordEvolution() {
    }

    public static boolean canEvolve(AbstractCard card) {
        if (card == null) {
            return false;
        }
        String cardId = card.cardID;
        return HiddenSword.ID.equals(cardId)
                || IllusorySword.ID.equals(cardId)
                || NonSword.ID.equals(cardId)
                || MyriadSword.ID.equals(cardId)
                || EmotionalSword.ID.equals(cardId)
                || TrueSword.ID.equals(cardId)
                || GhostlySword.ID.equals(cardId)
                || MindSword.ID.equals(cardId)
                || NinefoldSword.ID.equals(cardId);
    }

    public static AbstractCard evolve(AbstractCard source) {
        AbstractCard evolved = createEvolution(source.cardID);
        if (evolved == null) {
            return null;
        }
        if (source.upgraded) {
            evolved.upgrade();
        }
        evolved.uuid = source.uuid;
        evolved.misc = source.misc;
        evolved.inBottleFlame = source.inBottleFlame;
        evolved.inBottleLightning = source.inBottleLightning;
        evolved.inBottleTornado = source.inBottleTornado;
        return evolved;
    }

    private static AbstractCard createEvolution(String cardId) {
        if (HiddenSword.ID.equals(cardId)) {
            return new UnsheathedBlade();
        }
        if (IllusorySword.ID.equals(cardId)) {
            return new SpacetimeLeap();
        }
        if (NonSword.ID.equals(cardId)) {
            return new VoidBladeStyle();
        }
        if (MyriadSword.ID.equals(cardId)) {
            return new AbsoluteImperialRule();
        }
        if (EmotionalSword.ID.equals(cardId)) {
            return new MortalBladeAllLivingFaces();
        }
        if (TrueSword.ID.equals(cardId)) {
            return new AzureRiverSwordDomain();
        }
        if (GhostlySword.ID.equals(cardId)) {
            return new SoulControlTrickery();
        }
        if (MindSword.ID.equals(cardId)) {
            return new UnderOnesGaze();
        }
        if (NinefoldSword.ID.equals(cardId)) {
            return new InfiniteNumeration();
        }
        return null;
    }
}
