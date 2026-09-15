package ninesword.muzixi.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

/** A small persona spark that rises from the player's feet. */
public class PersonaParticleEffect extends AbstractGameEffect {
    private final TextureAtlas.AtlasRegion image;
    private final float startingDuration;
    private final float drift;
    private float x;
    private float y;
    private float velocityX;
    private float velocityY;

    public PersonaParticleEffect(AbstractPlayer player, Color personaColor) {
        this(player, personaColor, false);
    }

    public PersonaParticleEffect(AbstractPlayer player, Color personaColor, boolean burst) {
        image = ImageMaster.GLOW_SPARK;
        if (player == null || player.hb == null || image == null) {
            startingDuration = 0.0F;
            drift = 0.0F;
            isDone = true;
            return;
        }

        startingDuration = burst
                ? MathUtils.random(0.55F, 0.90F)
                : MathUtils.random(0.75F, 1.15F);
        duration = startingDuration;
        scale = Settings.scale * MathUtils.random(0.55F, burst ? 1.10F : 0.90F);
        drift = MathUtils.random(-48.0F, 48.0F) * Settings.scale;
        velocityX = MathUtils.random(-38.0F, 38.0F) * Settings.scale;
        velocityY = MathUtils.random(burst ? 150.0F : 95.0F,
                burst ? 260.0F : 180.0F) * Settings.scale;
        rotation = MathUtils.random(360.0F);
        renderBehind = MathUtils.randomBoolean(0.65F);

        float halfWidth = player.hb.width * 0.78F;
        float bottom = player.hb.cY - player.hb.height * 0.5F;
        x = player.hb.cX + MathUtils.random(-halfWidth, halfWidth)
                - image.packedWidth * 0.5F;
        y = bottom + MathUtils.random(-12.0F, 14.0F) * Settings.scale
                - image.packedHeight * 0.5F;

        color = personaColor == null ? Color.WHITE.cpy() : personaColor.cpy();
        color.a = 0.0F;
    }

    @Override
    public void update() {
        if (isDone) {
            return;
        }
        if (Gdx.graphics == null) {
            isDone = true;
            return;
        }

        float delta = Gdx.graphics.getDeltaTime();
        x += velocityX * delta;
        y += velocityY * delta;
        velocityX += drift * delta;
        velocityY += 18.0F * Settings.scale * delta;
        rotation += 90.0F * delta;
        duration -= delta;

        float progress = MathUtils.clamp(1.0F - duration / startingDuration, 0.0F, 1.0F);
        if (progress < 0.22F) {
            color.a = Interpolation.fade.apply(0.0F, 1.0F, progress / 0.22F);
        } else {
            color.a = Interpolation.fade.apply(1.0F, 0.0F,
                    (progress - 0.22F) / 0.78F);
        }
        if (duration <= 0.0F) {
            isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        if (isDone || image == null) {
            return;
        }
        spriteBatch.setColor(color);
        spriteBatch.setBlendFunction(770, 1);
        spriteBatch.draw(image,
                x,
                y,
                image.packedWidth * 0.5F,
                image.packedHeight * 0.5F,
                image.packedWidth,
                image.packedHeight,
                scale,
                scale,
                rotation);
        spriteBatch.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {
        // The texture belongs to ImageMaster and must not be disposed here.
    }
}
