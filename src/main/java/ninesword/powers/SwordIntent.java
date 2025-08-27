package ninesword.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SwordIntent extends AbstractPower {
    public static final String POWER_ID = "NineSwordTechniques:SwordIntent";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String POWER_NAME = POWER_STRINGS.NAME;
    private static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;

    public SwordIntent(AbstractCreature owner) {
        this.name = POWER_NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF; // 负面减益
        this.isTurnBased = false;
        this.amount = 1;
        this.description = String.format(DESCRIPTIONS[0], this.amount);

        // 能力图
        String path128 = "NineSwordResources/img/powers/SwordIntent128.png";
        String path48 = "NineSwordResources/img/powers/SwordIntent48.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }
}