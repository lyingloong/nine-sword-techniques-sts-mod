package ninesword.muzixi.characters;

import basemod.BaseMod;
import basemod.abstracts.CustomPlayer;
import basemod.animations.SpineAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import ninesword.modspire.ModEnums;
import ninesword.muzixi.cards.MuzixiDefend;
import ninesword.muzixi.cards.LifeDrain;
import ninesword.muzixi.cards.MuzixiStrike;
import ninesword.muzixi.cards.RootGuard;
import ninesword.muzixi.cards.SapSurge;
import ninesword.muzixi.relics.DivineDemonicEyes;

import java.util.ArrayList;
import java.util.List;

public class MuzixiCharacter extends CustomPlayer {
    public static final String ID = "NineSwordTechniques:Muzixi";
    public static final Color CARD_COLOR = new Color(0.17F, 0.62F, 0.38F, 1.0F);

    private static final String ROOT = "NineSwordResources/img/muzixi/";
    private static final String UI_512 = ROOT + "ui/512/";
    private static final String UI_1024 = ROOT + "ui/1024/";
    private static final String ENERGY_ROOT = ROOT + "energy/";

    public static final String CHARACTER_BUTTON = ROOT + "character/button.png";
    public static final String CUSTOM_MODE_BUTTON = ROOT + "character/custom_mode_button.png";
    public static final String PORTRAIT = ROOT + "character/portrait.png";

    private static final String SPINE_ATLAS = ROOT + "character/spine/muzixi.atlas";
    private static final String SPINE_JSON = ROOT + "character/spine/muzixi.json";
    private static final String SHOULDER = ROOT + "character/shoulder.png";
    private static final String SHOULDER_2 = ROOT + "character/shoulder2.png";
    private static final String CORPSE = ROOT + "character/corpse.png";
    private static final String VICTORY_BG = ROOT + "character/victory_bg.png";
    private static final String VICTORY_1 = ROOT + "character/victory1.png";
    private static final String VICTORY_2 = ROOT + "character/victory2.png";
    private static final String VICTORY_3 = ROOT + "character/victory3.png";
    private static final String ENERGY_VFX = ENERGY_ROOT + "vfx.png";

    private static final String[] ORB_TEXTURES = new String[]{
            ENERGY_ROOT + "layer1.png",
            ENERGY_ROOT + "layer2.png",
            ENERGY_ROOT + "layer3.png",
            ENERGY_ROOT + "layer4.png",
            ENERGY_ROOT + "layer5.png",
            ENERGY_ROOT + "base.png",
            ENERGY_ROOT + "layer1d.png",
            ENERGY_ROOT + "layer2d.png",
            ENERGY_ROOT + "layer3d.png",
            ENERGY_ROOT + "layer4d.png",
            ENERGY_ROOT + "layer5d.png"
    };

    private static boolean colorRegistered;
    public MuzixiCharacter(String name, PlayerClass playerClass) {
        super(name, playerClass, ORB_TEXTURES, ENERGY_VFX,
                new SpineAnimation(SPINE_ATLAS, SPINE_JSON, 1.0F));
        initializeClass(null, SHOULDER_2, SHOULDER, CORPSE, getLoadout(),
                -8.0F, -8.0F, 220.0F, 290.0F, new EnergyManager(3));
        stateData.setMix("attack", "idle", 0.12F);
        stateData.setMix("hit", "idle", 0.1F);
        AnimationState.TrackEntry idle = state.setAnimation(0, "idle", true);
        idle.setTime(idle.getEndTime() * MathUtils.random());
    }

    public static void registerColor() {
        if (colorRegistered) {
            return;
        }
        BaseMod.addColor(ModEnums.MUZIXI_GREEN, CARD_COLOR,
                UI_512 + "attack.png",
                UI_512 + "skill.png",
                UI_512 + "power.png",
                UI_512 + "orb.png",
                UI_1024 + "attack.png",
                UI_1024 + "skill.png",
                UI_1024 + "power.png",
                UI_1024 + "orb.png",
                UI_512 + "card_orb.png");
        colorRegistered = true;
    }

    @Override
    public String getPortraitImageName() {
        return PORTRAIT;
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> cards = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            cards.add(MuzixiStrike.ID);
        }
        for (int i = 0; i < 4; i++) {
            cards.add(MuzixiDefend.ID);
        }
        cards.add(SapSurge.ID);
        cards.add(LifeDrain.ID);
        return cards;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> relics = new ArrayList<>();
        relics.add(DivineDemonicEyes.ID);
        UnlockTracker.markRelicAsSeen(DivineDemonicEyes.ID);
        return relics;
    }

    @Override
    public CharSelectInfo getLoadout() {
        CharacterStrings strings = CardCrawlGame.languagePack.getCharacterString(ID);
        return new CharSelectInfo(strings.NAMES[0], strings.TEXT[0],
                70, 70, 0, 99, 5, this,
                getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return getLocalizedCharacterName();
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return ModEnums.MUZIXI_GREEN;
    }

    @Override
    public Color getCardRenderColor() {
        return CARD_COLOR.cpy();
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new RootGuard();
    }

    @Override
    public Color getCardTrailColor() {
        return CARD_COLOR.cpy();
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 4;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_MAGIC_FAST_1", 0.0F);
    }

    @Override
    public void useFastAttackAnimation() {
        super.useFastAttackAnimation();
        playSpineAnimation("attack");
    }

    @Override
    public void damage(DamageInfo info) {
        int healthBefore = currentHealth;
        super.damage(info);
        if (currentHealth < healthBefore) {
            playSpineAnimation("hit");
        }
    }

    private void playSpineAnimation(String animationName) {
        state.setAnimation(0, animationName, false);
        state.addAnimation(0, "idle", true, 0.0F);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_MAGIC_FAST_1";
    }

    @Override
    public com.badlogic.gdx.graphics.g2d.BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontGreen;
    }

    @Override
    public String getLocalizedCharacterName() {
        return CardCrawlGame.languagePack.getCharacterString(ID).NAMES[0];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new MuzixiCharacter(name, ModEnums.MUZIXI);
    }

    @Override
    public String getSpireHeartText() {
        CharacterStrings strings = CardCrawlGame.languagePack.getCharacterString(ID);
        return strings.TEXT.length > 1 ? strings.TEXT[1] : "";
    }

    @Override
    public Color getSlashAttackColor() {
        return CARD_COLOR.cpy();
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                AbstractGameAction.AttackEffect.POISON,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                AbstractGameAction.AttackEffect.POISON,
                AbstractGameAction.AttackEffect.FIRE
        };
    }

    @Override
    public String getVampireText() {
        return Vampires.DESCRIPTIONS[1];
    }

    @Override
    public Texture getCutsceneBg() {
        return ImageMaster.loadImage(VICTORY_BG);
    }

    @Override
    public List<CutscenePanel> getCutscenePanels() {
        ArrayList<CutscenePanel> panels = new ArrayList<>();
        panels.add(new CutscenePanel(VICTORY_1, "ATTACK_MAGIC_FAST_1"));
        panels.add(new CutscenePanel(VICTORY_2));
        panels.add(new CutscenePanel(VICTORY_3));
        return panels;
    }
}
