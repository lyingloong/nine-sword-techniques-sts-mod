package ninesword.neow;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.neow.NeowReward;
import ninesword.cards.SwordCardPool;
import ninesword.modspire.ModEnums;

import java.util.ArrayList;

/** Adds the sword-technique choice to the normal Neow reward pool. */
public final class NineSwordNeowEvent {
    private static final String ID = "NineSwordTechniques:NeowReward";

    private NineSwordNeowEvent() {
    }

    @SpirePatch(clz = NeowReward.class, method = "getRewardOptions")
    public static class AddSwordRewardToPool {
        @SpirePostfixPatch
        public static ArrayList<NeowReward.NeowRewardDef> postfix(
                ArrayList<NeowReward.NeowRewardDef> __result,
                NeowReward __instance,
                int category) {
            if (category == 0 && ModEnums.SwordTechniqueReward != null) {
                boolean alreadyPresent = false;
                for (NeowReward.NeowRewardDef definition : __result) {
                    if (definition.type == ModEnums.SwordTechniqueReward) {
                        alreadyPresent = true;
                        break;
                    }
                }
                if (!alreadyPresent) {
                    __result.add(new NeowReward.NeowRewardDef(
                            ModEnums.SwordTechniqueReward,
                            CardCrawlGame.languagePack.getCharacterString(ID).TEXT[0]));
                }
            }
            return __result;
        }
    }

    @SpirePatch(clz = NeowReward.class, method = "activate")
    public static class ActivateSwordReward {
        @SpirePrefixPatch
        public static SpireReturn<Void> prefix(NeowReward __instance) {
            if (__instance.type != ModEnums.SwordTechniqueReward) {
                return SpireReturn.Continue();
            }

            ReflectionHacks.setPrivate(__instance, NeowReward.class, "activated", true);
            ArrayList<AbstractCard> cards = SwordCardPool.create();
            String header = CardCrawlGame.languagePack
                    .getUIString("CardRewardScreen").TEXT[1];
            AbstractDungeon.cardRewardScreen.open(cards, null, header);
            CardCrawlGame.metricData.addNeowData(
                    __instance.type.name(), __instance.drawback.name());
            return SpireReturn.Return(null);
        }
    }
}
