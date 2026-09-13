package ninesword.muzixi.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

abstract class MuzixiPower extends AbstractPower {
    private static final String ROOT = "NineSwordResources/img/muzixi/powers/";

    protected void loadIcons(String iconName) {
        region128 = scaledRegion(ImageMaster.loadImage(ROOT + iconName + "128.png"), 84);
        region48 = scaledRegion(ImageMaster.loadImage(ROOT + iconName + "48.png"), 32);
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
}
