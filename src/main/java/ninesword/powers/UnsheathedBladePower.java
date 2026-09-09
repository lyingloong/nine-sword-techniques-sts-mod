package ninesword.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class UnsheathedBladePower extends AbstractPower {
    public static final String POWER_ID = "NineSwordTechniques:UnsheathedBladePower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final Texture TEXTURE_128 = ImageMaster.loadImage(
            "NineSwordResources/img/powers/UnsheathedBladePower128.png");
    private static final Texture TEXTURE_48 = ImageMaster.loadImage(
            "NineSwordResources/img/powers/UnsheathedBladePower48.png");

    public UnsheathedBladePower(AbstractCreature owner) {
        name = POWER_STRINGS.NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        amount = 1;
        region128 = PowerIcon.scaledRegion(TEXTURE_128, 84);
        region48 = PowerIcon.scaledRegion(TEXTURE_48, 32);
        updateDescription();
    }

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        if (card != null && card.type == AbstractCard.CardType.ATTACK) {
            return damage * (float) Math.pow(2, amount);
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        description = POWER_STRINGS.DESCRIPTIONS[0];
    }
}
