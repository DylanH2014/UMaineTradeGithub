package Data;

import java.util.List;

/**
 * Created by abhinav on 1/28/16.
 */
public class Item {

    //public int itemId;
    public String itemName;
    public double itemPrice;
    public String itemOwner;
    public String itemCondition;
    public String itemCategory;

    public int itemPhoto;


    public Item(String itemName, double itemPrice, String itemCondition, String itemCategory, int itemPhoto ) {
        // this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        //this.itemOwner = itemOwner;
        this.itemCondition = itemCondition;
        this.itemPhoto = itemPhoto;
        this.itemCategory = itemCategory;

    }

    private List<Item> items;



}