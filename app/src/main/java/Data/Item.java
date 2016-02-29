package Data;

import java.util.List;

/**
 * Created by abhinav on 1/28/16.
 */
public class Item {

    //public int itemId;

    public String itemName;
    public int itemPrice;
    public String itemOwner;
    public String itemCondition;
    public String itemCategory;
    public String itemDescription;
    //public int itemPhoto;
    public byte[] itemImage;


    public Item(String itemName, int itemPrice, String itemCondition, String itemCategory, String itemOwner, byte[] itemImage, String itemDescription) {
        // this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        //this.itemOwner = itemOwner;
        this.itemImage = itemImage;
        this.itemCondition = itemCondition;
        this.itemOwner = itemOwner;
        //this.itemPhoto = itemPhoto;
        this.itemCategory = itemCategory;
        this.itemDescription = itemDescription;


    }

    private List<Item> items;



}