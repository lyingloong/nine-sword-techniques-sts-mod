package ninesword.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class VoidBladeStylePower extends AbstractPower {
    public static final String POWER_ID = "NineSwordTechniques:VoidBladeStylePower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final Texture TEXTURE_128 = ImageMaster.loadImage(
            "NineSwordResources/img/powers/VoidBladeStylePower128.png");
    private static final Texture TEXTURE_48 = ImageMaster.loadImage(
            "NineSwordResources/img/powers/VoidBladeStylePower48.png");

    public VoidBladeStylePower(AbstractCreature owner, int amount) {
        name = POWER_STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        region128 = PowerIcon.scaledRegion(TEXTURE_128, 84);
        region48 = PowerIcon.scaledRegion(TEXTURE_48, 32);
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount), amount));
        addToBot(new ApplyPowerAction(owner, owner, new DexterityPower(owner, amount), amount));
        addToBot(new ApplyPowerAction(owner, owner, new FocusPower(owner, amount), amount));
        addToBot(new ApplyPowerAction(owner, owner, new MetallicizePower(owner, amount), amount));
    }

    @Override
    public void updateDescription() {
        description = String.format(POWER_STRINGS.DESCRIPTIONS[0], amount);
    }
}
