package ninesword.modspire;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.neow.NeowReward.NeowRewardType;

public class ModEnums {
    @SpireEnum(name = "SwordTechniqueReward")
    public static NeowRewardType SwordTechniqueReward;

    @SpireEnum(name = "MUZIXI")
    public static AbstractPlayer.PlayerClass MUZIXI;

    @SpireEnum(name = "MUZIXI_GREEN")
    public static AbstractCard.CardColor MUZIXI_GREEN;

    /**
     * BaseMod's compendium tabs resolve custom CardColor names through this
     * parallel enum.  Keep the names identical so the card library can open
     * safely when the character color is present.
     */
    @SpireEnum(name = "MUZIXI_GREEN")
    public static CardLibrary.LibraryType MUZIXI_LIBRARY;
}
