package com.abhinav.dylan.umainetrade;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.R;

import java.util.List;

import Data.Item;
import Data.Person;


/**
 * Created by abhinav on 1/28/16.
 */
public class RVAdapterItems extends RecyclerView.Adapter<RVAdapterItems.ItemViewHolder>{

    List<Item> items;





    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        ItemViewHolder ivh = new ItemViewHolder(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int position) {
        final String itemName = items.get(position).itemName;
        final String itemPrice = Integer.toString(items.get(position).itemPrice);
        final String itemCondition = items.get(position).itemCondition;
        final String itemCategory = items.get(position).itemCategory;
        final String itemDescription = items.get(position).itemDescription;

        itemViewHolder.itemName.setText(itemName);
        itemViewHolder.itemPrice.setText("$"+itemPrice);
        itemViewHolder.itemCondition.setText(itemCondition);
        itemViewHolder.itemCategory.setText(itemCategory);
        //itemViewHolder.itemDescription.setText(itemDescription);

        itemViewHolder.itemPhoto.setImageResource(items.get(position).itemPhoto);

        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ListingsPage.listingContext, "Recycle Click" + items.get(position).itemName, Toast.LENGTH_SHORT).show();
                Intent viewItem = new Intent(ListingsPage.listingContext, ItemSummary.class);
                viewItem.putExtra("itemName", itemName);
                viewItem.putExtra("itemPrice", itemPrice);
                viewItem.putExtra("itemCondition", itemCondition);
                viewItem.putExtra("itemCategory", itemCategory);
                viewItem.putExtra("itemDescription", itemDescription);
                viewItem.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ListingsPage.listingContext.startActivity(viewItem);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



    @Override
    public int getItemCount() {


        return items.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView itemName;
        TextView itemPrice;
        TextView itemCondition;
        TextView itemCategory;
        TextView itemDescription;
        ImageView itemPhoto;

        ItemViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            itemName = (TextView)itemView.findViewById(R.id.item_name);
            itemPrice = (TextView)itemView.findViewById(R.id.item_price);
            itemCondition = (TextView)itemView.findViewById(R.id.item_condition);
            itemCategory = (TextView)itemView.findViewById(R.id.item_category);
            itemPhoto = (ImageView)itemView.findViewById(R.id.item_photo);
            itemDescription = (TextView) itemView.findViewById(R.id.item_description);

        }
    }

    RVAdapterItems(List<Item> items){
        this.items = items;
    }


}