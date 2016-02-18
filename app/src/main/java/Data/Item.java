package Data;

import java.util.List;

/**
 * Created by abhinav on 1/28/16.
 */
public class Item {

    //public int itemId;
    public String itemName;
    public int itemPrice;
    public int itemOwner;
    public String itemCondition;
    public String itemCategory;
    public String itemDescription;


    public int itemPhoto;


    public Item(String itemName, int itemPrice, String itemCondition, String itemCategory, int itemPhoto, String itemDescription) {
        // this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        //this.itemOwner = itemOwner;
        this.itemCondition = itemCondition;
        this.itemPhoto = itemPhoto;
        this.itemCategory = itemCategory;
        this.itemDescription = itemDescription;


    }

    private List<Item> items;



}