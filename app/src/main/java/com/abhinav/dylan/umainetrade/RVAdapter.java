package com.abhinav.dylan.umainetrade;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Data.Person;
import com.R;


/**
 * Created by abhinav on 1/28/16.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{

    List<Person> persons;

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, final int position) {
        personViewHolder.personName.setText(persons.get(position).name);
        personViewHolder.personAge.setText(persons.get(position).age);
        personViewHolder.personPhoto.setImageResource(persons.get(position).photoId);
        personViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ListingsPage.listingContext, "Recycle Click" + persons.get(position).name, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



    @Override
    public int getItemCount() {
        return persons.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView personName;
        TextView personAge;
        ImageView personPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            //personName = (TextView)itemView.findViewById(R.id.person_name);
            //personAge = (TextView)itemView.findViewById(R.id.person_age);
            //personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }
    }

    RVAdapter(List<Person> persons){
        this.persons = persons;
    }


}