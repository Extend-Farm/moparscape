/**
 * <p>Title: NPCDrops</p>
 *
 * <p>Description: Handles an Item drop from an NPC</p>
 *
 * <p>Copyright: Copyright (c) 2006 whitefang</p>
 *
 * <p>Company: Project HybridScape</p>
 *
 * @author whitefang
 * @version 1.0
 */

public class NPCDrops
{
    public int npcId;
    public int DropType;
    public int[] Items = new int[100];
    public int[] ItemsN = new int[100];

    /**
     * <p>Constructs a new NPCDrops with the specified npcID</p>
     * @param _npcId int
     */
    public NPCDrops(int _npcId)
    {
        npcId = _npcId;
        for(int i = 0; i < Items.length; i++){
            Items[i] = -1;
            ItemsN[i] = 0;
        }
    }
}
