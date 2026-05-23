/**
 * <p>Title: Emotions</p>
 *
 * <p>Description: Contains all animations for NPC's and Players</p>
 *
 * <p>Copyright: Copyright (c) 2006 <b>&lt;unknown author&gt;</b></p>
 *
 * <p>Company: Project HybridScape</p>
 *
 * <p>The Animations are as follows:</p>
 * </p>
 * <p><b>[0] = Attacking emotions</b></p>
 * <blockquote>
 *   <p>[0]  = Whip Flick</p>
 *   <p>[1]  = Normal Stab</p>
 *   <p>[2]  = Dagger Stab</p>
 *   <p>[3]  = Crush</p>
 *   <p>[4]  = 2H Crush</p>
 *   <p>[5]  = Slash</p>
 *   <p>[6]  = 2H Slash</p>
 *   <p>[7]  = Spin Attack</p>
 *   <p>[8]  = Great Axe Attack</p>
 *   <p>[9]  = Un-armed</p>
 *   <p>[10] = Torag's Hammers</p>
 * </blockquote>
 * <p><b>[1] = Standing Emotes</b></p>
 * <blockquote>
 *   <p>[0]  = Normal</p>
 *   <p>[1]  = Two Handed Normal</p>
 * </blockquote>
 * <p><b>[2] = Walking Emotes</b></p>
 * <blockquote>
 *   <p>[0]  = Normal</p>
 *   <p>[1]  = Two Handed Normal</p>
 *   <p>[2]  = Maul Walk</p>
 * </blockquote>
 * <p><b>[3] = Running Emotes</p>
 * <blockquote>
 *   <p>[0] = Normal</p>
 *   <p>[1] = Maul Run</p>
 * </blockquote>
 * </p>
 * </p>
 * <p><b>DEV: </b>think we should rename it?</p>
 * @author not attributable
 * @version 1.0
 */

public class Emotions
{
    private Emotions()
    {}

    public static int[][] EmotionIDs = {{395, 381, 392, 385, 382, 377, 380, 409, 0x812, 0x326, 0x814}, {0x328, 0x811}, {0x333,
        0x810, 0x67F}, {0x338, 0x680}
    };

    // [0] = standing emote, [1] = Walking emote, [2] = running emote and [3] = Attacking emote

    /**
     * <p>Returns the int[] of animations available for that item</p>
     * @param itemName String
     * @return int[]
     */
    public static int[] getEmotions(String itemName)
    {

        int sta = 0, wal = 0, run = 0, att = 0;

        itemName = itemName.replaceAll("bronze", "");
        itemName = itemName.replaceAll("Iron", "");
        itemName = itemName.replaceAll("Steel", "");
        itemName = itemName.replaceAll("Black", "");
        itemName = itemName.replaceAll("Mithril", "");
        itemName = itemName.replaceAll("Adamant", "");
        itemName = itemName.replaceAll("Rune", "");
        itemName = itemName.replaceAll("Granite", "");
        itemName = itemName.replaceAll("Dragon", "");
        itemName = itemName.replaceAll("Crystal", "");
        itemName = itemName.trim();

        sta = EmotionIDs[1][0];
        wal = EmotionIDs[2][0];
        run = EmotionIDs[3][0];
        att = EmotionIDs[0][0];

        if(itemName.startsWith("sword") || itemName.startsWith("scimitar") || itemName.startsWith("longsword")){
            sta = EmotionIDs[1][0];
            wal = EmotionIDs[2][0];
            run = EmotionIDs[3][0];
            att = EmotionIDs[0][5];

        }
        if(itemName.startsWith("dagger")){
            sta = EmotionIDs[1][0];
            wal = EmotionIDs[2][0];
            run = EmotionIDs[3][0];
            att = EmotionIDs[0][2];
        }
        if(itemName.startsWith("mace") || itemName.startsWith("warhammer") || itemName.startsWith("battleaxe")){
            sta = EmotionIDs[1][0];
            wal = EmotionIDs[2][0];
            run = EmotionIDs[3][0];
            att = EmotionIDs[0][3];
        }
        if(itemName.startsWith("staff")){
            sta = EmotionIDs[1][0];
            wal = EmotionIDs[2][0];
            run = EmotionIDs[3][0];
            att = EmotionIDs[0][3];
        }
        if(itemName.startsWith("2h sword")){
            sta = EmotionIDs[1][1];
            wal = EmotionIDs[2][1];
            run = EmotionIDs[3][0];
            att = EmotionIDs[0][6];
        }
        if(itemName.startsWith("whip")){
            sta = EmotionIDs[1][0];
            wal = EmotionIDs[2][0];
            run = EmotionIDs[3][0];
            att = EmotionIDs[0][0];
        }
        if(itemName.startsWith("Dharoks greataxe")){
            sta = EmotionIDs[1][1];
            wal = EmotionIDs[2][1];
            run = EmotionIDs[3][0];
            att = EmotionIDs[0][8];
        }
        if(itemName.startsWith("Guthans warspear")){
            sta = EmotionIDs[1][0];
            wal = EmotionIDs[2][0];
            run = EmotionIDs[3][0];
            att = EmotionIDs[0][1];
        }
        if(itemName.startsWith("Torags hammers")){
            sta = EmotionIDs[1][0];
            wal = EmotionIDs[2][0];
            run = EmotionIDs[3][0];
            att = EmotionIDs[0][10];
        }
        if(itemName.startsWith("Veracs flail")){
            sta = EmotionIDs[1][0];
            wal = EmotionIDs[2][0];
            run = EmotionIDs[3][0];
            att = EmotionIDs[0][3];
        }

        return new int[]{sta, wal, run, att};
    }

}
