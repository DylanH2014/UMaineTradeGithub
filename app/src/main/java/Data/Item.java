package Data;

import java.util.List;

/**
 * Created by abhinav on 1/28/16.
 */
public class Item {

    public int itemId;
    public String itemName;
    public double itemPrice;
    public String itemOwner;
    public String itemCondition;


    public Item(int itemId, String itemName, double itemPrice, String itemOwner, String itemCondition) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemOwner = itemOwner;
        this.itemCondition = itemCondition;

    }

    private List<Item> items;



}
