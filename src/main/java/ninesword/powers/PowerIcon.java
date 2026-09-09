package ninesword.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

final class PowerIcon {
    private PowerIcon() {
    }

    static TextureAtlas.AtlasRegion scaledRegion(Texture texture, int displaySize) {
        TextureAtlas.AtlasRegion region = new TextureAtlas.AtlasRegion(
                texture, 0, 0, texture.getWidth(), texture.getHeight());
        region.packedWidth = displaySize;
        region.packedHeight = displaySize;
        region.originalWidth = displaySize;
        region.originalHeight = displaySize;
        return region;
    }
}
