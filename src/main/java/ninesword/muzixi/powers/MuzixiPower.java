package ninesword.muzixi.powers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import ninesword.muzixi.vfx.PersonaAuraEffect;
import ninesword.muzixi.vfx.PersonaParticleEffect;

import java.util.HashMap;
import java.util.Map;

abstract class MuzixiPower extends AbstractPower {
    private static final String ROOT = "NineSwordResources/img/muzixi/powers/";
    private static final Map<String, IconRegions> ICON_CACHE = new HashMap<String, IconRegions>();
    private static final Color MUZIXI_PERSONA_COLOR = new Color(0.20F, 0.95F, 0.38F, 1.0F);
    private static final Color TEAR_PERSONA_COLOR = new Color(0.76F, 0.30F, 1.0F, 1.0F);

    // Match the cadence of the Watcher's visuals while keeping persona state
    // independent from AbstractPlayer.stance.
    private float personaParticleTimer;
    private float personaAuraTimer;

    protected void loadIcons(String iconName) {
        IconRegions icons = ICON_CACHE.get(iconName);
        if (icons == null) {
            icons = new IconRegions(
                    scaledRegion(ImageMaster.loadImage(ROOT + iconName + "128.png"), 84),
                    scaledRegion(ImageMaster.loadImage(ROOT + iconName + "48.png"), 32));
            ICON_CACHE.put(iconName, icons);
        }
        region128 = icons.region128;
        region48 = icons.region48;
    }

    /**
     * Emits the persistent in-combat feedback for a persona power.
     *
     * <p>Personas are powers rather than game stances, so assigning a custom
     * {@code AbstractPlayer.stance} would overwrite a real Watcher stance or
     * interfere when another character temporarily gains a persona. Power
     * particles give the same readable visual treatment without changing any
     * stance mechanics.</p>
     */
    protected final void updatePersonaVisuals(boolean tearPersona) {
        if (!canRenderPersonaVisuals()) {
            return;
        }

        float delta = Gdx.graphics.getDeltaTime();
        personaParticleTimer -= delta;
        if (personaParticleTimer <= 0.0F) {
            personaParticleTimer = tearPersona ? 0.05F : 0.04F;
            AbstractDungeon.effectsQueue.add(new PersonaParticleEffect(
                    (AbstractPlayer) owner, personaColor(tearPersona)));
        }

        personaAuraTimer -= delta;
        if (personaAuraTimer <= 0.0F) {
            personaAuraTimer = tearPersona
                    ? MathUtils.random(0.30F, 0.40F)
                    : MathUtils.random(0.45F, 0.55F);
            AbstractDungeon.effectsQueue.add(new PersonaAuraEffect(
                    (AbstractPlayer) owner, personaColor(tearPersona)));
        }
    }

    /** Plays the one-shot transition feedback when a persona is entered. */
    protected final void playPersonaEntryVisuals(boolean tearPersona) {
        if (!canRenderPersonaVisuals()) {
            return;
        }

        AbstractPlayer player = (AbstractPlayer) owner;
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(
                personaColor(tearPersona), true));
        for (int i = 0; i < 16; i++) {
            AbstractDungeon.effectsQueue.add(new PersonaParticleEffect(
                    player, personaColor(tearPersona), true));
        }
        AbstractDungeon.effectsQueue.add(new PersonaAuraEffect(
                player, personaColor(tearPersona)));
    }

    private static Color personaColor(boolean tearPersona) {
        return (tearPersona ? TEAR_PERSONA_COLOR : MUZIXI_PERSONA_COLOR).cpy();
    }

    private boolean canRenderPersonaVisuals() {
        return !Settings.DISABLE_EFFECTS
                && owner instanceof AbstractPlayer
                && owner == AbstractDungeon.player
                && AbstractDungeon.effectsQueue != null
                && Gdx.graphics != null
                && ((AbstractPlayer) owner).hb != null;
    }

    private static TextureAtlas.AtlasRegion scaledRegion(Texture texture, int displaySize) {
        TextureAtlas.AtlasRegion region = new TextureAtlas.AtlasRegion(
                texture, 0, 0, texture.getWidth(), texture.getHeight());
        region.packedWidth = displaySize;
        region.packedHeight = displaySize;
        region.originalWidth = displaySize;
        region.originalHeight = displaySize;
        return region;
    }

    private static final class IconRegions {
        private final TextureAtlas.AtlasRegion region128;
        private final TextureAtlas.AtlasRegion region48;

        private IconRegions(TextureAtlas.AtlasRegion region128,
                            TextureAtlas.AtlasRegion region48) {
            this.region128 = region128;
            this.region48 = region48;
        }
    }
}
