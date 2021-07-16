/**
 * <p>Title: NPCList</p>
 *
 * <p>Description: Holds the values of an npc</p>
 *
 * <p>Copyright: Copyright (c) 2006 whitefang</p>
 *
 * <p>Company: Project HybridScape</p>
 *
 * @author whitefang
 * @version 1.0
 */

public class NPCList
{
    public int npcId;
    public String npcName;
    public int npcCombat;
    public int npcHealth;

    public NPCList(int _npcId)
    {
        npcId = _npcId;
    }
}
