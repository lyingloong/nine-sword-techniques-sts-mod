package ninesword.modcore;

import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostCreateStartingRelicsSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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
import ninesword.modspire.ModEnums;
import ninesword.muzixi.cards.AncientBark;
import ninesword.muzixi.cards.Awakening;
import ninesword.muzixi.cards.CreepingRoots;
import ninesword.muzixi.cards.DivineDemonicGaze;
import ninesword.muzixi.cards.EyeOfLife;
import ninesword.muzixi.cards.GiantTree;
import ninesword.muzixi.cards.LeafDance;
import ninesword.muzixi.cards.LifeDrain;
import ninesword.muzixi.cards.MindGarden;
import ninesword.muzixi.cards.MuzixiDefend;
import ninesword.muzixi.cards.MuzixiStrike;
import ninesword.muzixi.cards.NaturalCycle;
import ninesword.muzixi.cards.ParalysisFlower;
import ninesword.muzixi.cards.ParasiticSeed;
import ninesword.muzixi.cards.PersonaShift;
import ninesword.muzixi.cards.RootGuard;
import ninesword.muzixi.cards.SapSurge;
import ninesword.muzixi.cards.SoulReflection;
import ninesword.muzixi.cards.TearOfGod;
import ninesword.muzixi.cards.TenThousandFlowers;
import ninesword.muzixi.cards.ThornWhip;
import ninesword.muzixi.cards.VerdantRebirth;
import ninesword.muzixi.cards.VineLash;
import ninesword.muzixi.cards.Vinesnare;
import ninesword.muzixi.cards.WorldTree;
import ninesword.muzixi.characters.MuzixiCharacter;
import ninesword.muzixi.relics.DivineDemonicEyes;
import ninesword.muzixi.powers.VitalityPower;
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
        EditStringsSubscriber, EditCharactersSubscriber, EditKeywordsSubscriber,
        PostCreateStartingRelicsSubscriber, PostUpdateSubscriber {
    private static final NineSwordRunState RUN_STATE = new NineSwordRunState();

    public NineSword() {
        BaseMod.subscribe(this);
        BaseMod.addSaveField(NineSwordRunState.SAVE_KEY, RUN_STATE);
    }

    @Override
    public void receiveEditCards() {
        // Register the color before creating any cards that use it.
        MuzixiCharacter.registerColor();
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
        BaseMod.addCard(new MuzixiStrike());
        BaseMod.addCard(new MuzixiDefend());
        BaseMod.addCard(new PersonaShift());
        BaseMod.addCard(new ThornWhip());
        BaseMod.addCard(new ParalysisFlower());
        BaseMod.addCard(new RootGuard());
        BaseMod.addCard(new SapSurge());
        BaseMod.addCard(new VineLash());
        BaseMod.addCard(new LifeDrain());
        BaseMod.addCard(new LeafDance());
        BaseMod.addCard(new CreepingRoots());
        BaseMod.addCard(new Vinesnare());
        BaseMod.addCard(new GiantTree());
        BaseMod.addCard(new Awakening());
        BaseMod.addCard(new EyeOfLife());
        BaseMod.addCard(new ParasiticSeed());
        BaseMod.addCard(new AncientBark());
        BaseMod.addCard(new NaturalCycle());
        BaseMod.addCard(new MindGarden());
        BaseMod.addCard(new VerdantRebirth());
        BaseMod.addCard(new DivineDemonicGaze());
        BaseMod.addCard(new WorldTree());
        BaseMod.addCard(new TenThousandFlowers());
        BaseMod.addCard(new TearOfGod());
        BaseMod.addCard(new SoulReflection());
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
        BaseMod.addRelicToCustomPool(new DivineDemonicEyes(), ModEnums.MUZIXI_GREEN);

        BaseMod.addEvent(WhiteCaveEvent.ID, WhiteCaveEvent.class, Exordium.ID);
        BaseMod.addEvent(VoidIslandEvent.ID, VoidIslandEvent.class, TheCity.ID, TheBeyond.ID);
        BaseMod.addEvent(BazunanEvent.ID, BazunanEvent.class);
        BaseMod.logger.info("Nine Sword Techniques relics registered");
    }

    @Override
    public void receivePostCreateStartingRelics(
            AbstractPlayer.PlayerClass playerClass, ArrayList<String> relics) {
        RUN_STATE.clear();
        if (playerClass != ModEnums.MUZIXI && !relics.contains(SwordAncestorsLegacy.ID)) {
            relics.add(SwordAncestorsLegacy.ID);
        }
    }

    @Override
    public void receiveEditCharacters() {
        MuzixiCharacter.registerColor();
        BaseMod.addCharacter(new MuzixiCharacter("Muzixi", ModEnums.MUZIXI),
                MuzixiCharacter.CHARACTER_BUTTON,
                MuzixiCharacter.PORTRAIT,
                ModEnums.MUZIXI,
                MuzixiCharacter.CUSTOM_MODE_BUTTON);
        BaseMod.logger.info("Muzixi character registered");
    }

    @Override
    public void receiveEditKeywords() {
        if (Settings.language == Settings.GameLanguage.ZHS) {
            BaseMod.addKeyword("NineSwordTechniques", "生机", new String[]{"生机"},
                    "木子汐的战斗资源。它会在战斗结束时消失，可被泪汐儿人格和部分卡牌消耗。");
            BaseMod.addKeyword("NineSwordTechniques", "人格", new String[]{"人格"},
                    "木子汐与泪汐儿共享肉身，但拥有不同的回合开始效果。通过人格交替可在两者之间切换。");
        } else {
            BaseMod.addKeyword("NineSwordTechniques", "Vitality", new String[]{"Vitality"},
                    "Muzixi's combat resource. It disappears after combat and can be spent by Leixier's form and several cards.");
            BaseMod.addKeyword("NineSwordTechniques", "persona", new String[]{"persona"},
                    "Muzixi and Leixier share one body but have different start-of-turn effects. Persona Shift changes between them.");
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
        if (AbstractDungeon.player != null) {
            VitalityPower.removeExpired(AbstractDungeon.player);
        }
    }

    public static void initialize() {
        new NineSword();
    }
}
