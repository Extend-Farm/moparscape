/**
 * <p>Title: ItemHandler</p>
 *
 * <p>Description: Holds information about items</p>
 * <p>(anyone else think we should rename this to Item?)</p>
 *
 * <p>Copyright: Copyright (c) 2006 whitefang</p>
 *
 * <p>Company: Project HybridScape</p>
 *
 * @author whitefang
 * @version 1.0
 */

public class ItemList
{
    public int itemId;
    public String itemName;
    public String itemDescription;
    public double ShopValue;
    public double LowAlch;
    public double HighAlch;
    public int[] Bonuses = new int[100];

    /**
     * <p>Creates a new ItemList with the specified ItemID</p>
     * @param _itemId int
     */
    public ItemList(int _itemId)
    {
        itemId = _itemId;
    }
}
