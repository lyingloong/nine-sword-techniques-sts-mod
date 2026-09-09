package ninesword.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class TheYousiSword extends CustomRelic {
    public static final String ID = "NineSwordTechniques:TheYousiSword";
    private static final String IMG_PATH =
            "NineSwordResources/img/relics/TheYousiSword_128.png";
    private static final String OUTLINE_PATH =
            "NineSwordResources/img/relics/MyRelic_Outline.png";

    public TheYousiSword() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH),
                RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public float atDamageModify(float damage, AbstractCard card) {
        if (card != null && card.type == AbstractCard.CardType.ATTACK) {
            return damage * 1.5F;
        }
        return damage;
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster monster) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            flash();
            addToBot(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, 1));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new TheYousiSword();
    }
}
