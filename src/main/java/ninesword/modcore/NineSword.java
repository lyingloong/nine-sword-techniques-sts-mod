package ninesword.modcore;

import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostCreateStartingRelicsSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import ninesword.cards.EmotionalSword;
import ninesword.cards.GhostlySword;
import ninesword.cards.HiddenSword;
import ninesword.cards.IllusorySword;
import ninesword.cards.MindSword;
import ninesword.cards.MyriadSword;
import ninesword.cards.NinefoldSword;
import ninesword.cards.NonSword;
import ninesword.cards.SwordCardChoiceManager;
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
import ninesword.enlightenment.EnlightenmentManager;
import ninesword.events.BazunanEvent;
import ninesword.events.VoidIslandEvent;
import ninesword.events.WhiteCaveEvent;
import ninesword.relics.EmberSeed;
import ninesword.relics.EmberWhiteFlame;
import ninesword.relics.FlameSwordYanmang;
import ninesword.relics.SwordAncestorsLegacy;
import ninesword.relics.TheCanonOfSwordObservation;
import ninesword.relics.TheYousiSword;
import ninesword.relics.VoidCrystal;
import ninesword.relics.evolution.RelicEvolutionManager;

import java.util.ArrayList;

@SpireInitializer
public class NineSword implements EditCardsSubscriber, EditRelicsSubscriber,
        EditStringsSubscriber, PostCreateStartingRelicsSubscriber, PostUpdateSubscriber {
    private static final NineSwordRunState RUN_STATE = new NineSwordRunState();

    public NineSword() {
        BaseMod.subscribe(this);
        BaseMod.addSaveField(NineSwordRunState.SAVE_KEY, RUN_STATE);
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new HiddenSword());
        BaseMod.addCard(new IllusorySword());
        BaseMod.addCard(new NonSword());
        BaseMod.addCard(new MyriadSword());
        BaseMod.addCard(new EmotionalSword());
        BaseMod.addCard(new TrueSword());
        BaseMod.addCard(new GhostlySword());
        BaseMod.addCard(new MindSword());
        BaseMod.addCard(new NinefoldSword());
        BaseMod.addCard(new UnsheathedBlade());
        BaseMod.addCard(new SpacetimeLeap());
        BaseMod.addCard(new VoidBladeStyle());
        BaseMod.addCard(new AbsoluteImperialRule());
        BaseMod.addCard(new MortalBladeAllLivingFaces());
        BaseMod.addCard(new AzureRiverSwordDomain());
        BaseMod.addCard(new SoulControlTrickery());
        BaseMod.addCard(new UnderOnesGaze());
        BaseMod.addCard(new InfiniteNumeration());
        BaseMod.logger.info("Nine Sword Techniques cards registered");
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelic(new SwordAncestorsLegacy(), RelicType.SHARED);
        BaseMod.addRelic(new TheCanonOfSwordObservation(), RelicType.SHARED);
        BaseMod.addRelic(new TheYousiSword(), RelicType.SHARED);
        BaseMod.addRelic(new FlameSwordYanmang(), RelicType.SHARED);
        BaseMod.addRelic(new EmberSeed(), RelicType.SHARED);
        BaseMod.addRelic(new EmberWhiteFlame(), RelicType.SHARED);
        BaseMod.addRelic(new VoidCrystal(), RelicType.SHARED);

        BaseMod.addEvent(WhiteCaveEvent.ID, WhiteCaveEvent.class, Exordium.ID);
        BaseMod.addEvent(VoidIslandEvent.ID, VoidIslandEvent.class, TheCity.ID, TheBeyond.ID);
        BaseMod.addEvent(BazunanEvent.ID, BazunanEvent.class);
        BaseMod.logger.info("Nine Sword Techniques relics registered");
    }

    @Override
    public void receivePostCreateStartingRelics(
            AbstractPlayer.PlayerClass playerClass, ArrayList<String> relics) {
        RUN_STATE.clear();
        if (!relics.contains(SwordAncestorsLegacy.ID)) {
            relics.add(SwordAncestorsLegacy.ID);
        }
    }

    @Override
    public void receiveEditStrings() {
        String language = Settings.language == Settings.GameLanguage.ZHS ? "ZHS" : "ENG";
        String base = "NineSwordResources/localization/" + language + "/";
        BaseMod.loadCustomStringsFile(CardStrings.class, base + "cards.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, base + "powers.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, base + "characters.json");
        BaseMod.loadCustomStringsFile(EventStrings.class, base + "events.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, base + "relics.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, base + "ui.json");
    }

    @Override
    public void receivePostUpdate() {
        EnlightenmentManager.update();
        RelicEvolutionManager.update();
        SwordCardChoiceManager.update();
    }

    public static void initialize() {
        new NineSword();
    }
}
