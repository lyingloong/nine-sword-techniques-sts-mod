package ninesword.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class GhostlySwordPower extends AbstractPower {
    public static final String POWER_ID = "NineSwordTechniques:GhostlySwordPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final Texture TEXTURE_128 = ImageMaster.loadImage(
            "NineSwordResources/img/powers/GhostlySwordPower128.png");
    private static final Texture TEXTURE_48 = ImageMaster.loadImage(
            "NineSwordResources/img/powers/GhostlySwordPower48.png");

    public GhostlySwordPower(AbstractCreature owner, int amount) {
        name = POWER_STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        this.amount = amount;
        region128 = PowerIcon.scaledRegion(TEXTURE_128, 84);
        region48 = PowerIcon.scaledRegion(TEXTURE_48, 32);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(POWER_STRINGS.DESCRIPTIONS[0], amount);
    }
}
