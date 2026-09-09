package ninesword.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class FlameSwordYanmang extends CustomRelic {
    public static final String ID = "NineSwordTechniques:FlameSwordYanmang";
    private static final String IMG_PATH =
            "NineSwordResources/img/relics/FlameSwordYanmang_128.png";
    private static final String OUTLINE_PATH =
            "NineSwordResources/img/relics/MyRelic_Outline.png";
    private static final int DAMAGE = 4;
    private boolean triggeredThisTurn;

    public FlameSwordYanmang() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH),
                RelicTier.COMMON, LandingSound.SOLID);
    }

    @Override
    public void atBattleStart() {
        triggeredThisTurn = false;
    }

    @Override
    public void atTurnStart() {
        triggeredThisTurn = false;
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster monster) {
        if (!triggeredThisTurn && card.type == AbstractCard.CardType.ATTACK) {
            triggeredThisTurn = true;
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, DAMAGE,
                    DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FlameSwordYanmang();
    }
}
