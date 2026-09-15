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

/** A soft vertical glow that rises behind the current persona. */
public class PersonaAuraEffect extends AbstractGameEffect {
    private final TextureAtlas.AtlasRegion image;
    private final float startingDuration;
    private final float maxAlpha;
    private float x;
    private float y;
    private float velocityY;

    public PersonaAuraEffect(AbstractPlayer player, Color personaColor) {
        image = ImageMaster.VERTICAL_AURA;
        if (player == null || player.hb == null || image == null) {
            startingDuration = 0.0F;
            maxAlpha = 0.0F;
            isDone = true;
            return;
        }

        startingDuration = MathUtils.random(0.85F, 1.25F);
        duration = startingDuration;
        maxAlpha = MathUtils.random(0.22F, 0.36F);
        scale = Settings.scale * MathUtils.random(0.85F, 1.25F);
        velocityY = MathUtils.random(40.0F, 85.0F) * Settings.scale;
        renderBehind = true;

        float bottom = player.hb.cY - player.hb.height * 0.5F;
        x = player.hb.cX + MathUtils.random(-player.hb.width * 0.52F,
                player.hb.width * 0.52F) - image.packedWidth * 0.5F;
        y = bottom - image.packedHeight * 0.5F;

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
        y += velocityY * delta;
        duration -= delta;
        float progress = MathUtils.clamp(1.0F - duration / startingDuration, 0.0F, 1.0F);
        if (progress < 0.20F) {
            color.a = Interpolation.fade.apply(0.0F, maxAlpha, progress / 0.20F);
        } else if (progress > 0.72F) {
            color.a = Interpolation.fade.apply(maxAlpha, 0.0F,
                    (progress - 0.72F) / 0.28F);
        } else {
            color.a = maxAlpha;
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
                0.0F);
        spriteBatch.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {
        // The texture belongs to ImageMaster and must not be disposed here.
    }
}
