package ninesword.muzixi.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.HashMap;
import java.util.Map;

abstract class MuzixiPower extends AbstractPower {
    private static final String ROOT = "NineSwordResources/img/muzixi/powers/";
    private static final Map<String, IconRegions> ICON_CACHE = new HashMap<String, IconRegions>();

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
