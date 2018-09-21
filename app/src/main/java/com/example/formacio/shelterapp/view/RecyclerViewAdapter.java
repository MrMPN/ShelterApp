package com.example.formacio.shelterapp.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.formacio.shelterapp.R;
import com.example.formacio.shelterapp.domain.Animal;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String LOG = "RecyclerViewAdapter";
    private List<Animal> mAnimals;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context mContext) {
        inflater = LayoutInflater.from(mContext);
    }

    void setWords(List<Animal> animals){
        mAnimals = animals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Animal animal = mAnimals.get(position);

        holder.nameTextView.setText(animal.getName());
        holder.dateTextView.setText(String.valueOf(animal.getDate()));
    }

    @Override
    public int getItemCount() {
        if (mAnimals != null)
            return mAnimals.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameEdit);
            dateTextView = itemView.findViewById(R.id.date);
        }
    }
}
