package com.example.formacio.shelterapp.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.formacio.shelterapp.R;
import com.example.formacio.shelterapp.domain.Animal;
import com.example.formacio.shelterapp.utils.DateUtils;

import java.util.List;

import static com.example.formacio.shelterapp.view.DetailActivity.*;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private List<Animal> mAnimals;
    private LayoutInflater inflater;
    private Context mContext;

    RecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
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
        Log.i(TAG, "OnBindViewHolder method called");
        final Animal animal = mAnimals.get(position);

        cleanHolder(holder);
        setViews(holder, animal);
        holder.parentLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(SELECTED_ANIMAL, animal);
                mContext.startActivity(intent);
            }
        });
    }

    private void cleanHolder(ViewHolder holder){
        holder.nameTextView.setTypeface(Typeface.DEFAULT);
        holder.nameTextView.setTextColor(Color.DKGRAY);
    }

    private void setViews(ViewHolder holder, Animal animal){
        holder.nameTextView.setText(animal.getName());
        holder.dateTextView.setText(DateUtils.getFormattedTime(animal.getDate()));
        if (animal.isChip()){
            holder.nameTextView.setTextColor(Color.parseColor("#000099"));
            holder.nameTextView.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    @Override
    public int getItemCount() {
        if (mAnimals != null)
            return mAnimals.size();
        else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView dateTextView;
        ConstraintLayout parentLayout;

        ViewHolder(View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            nameTextView = itemView.findViewById(R.id.nameEdit);
            dateTextView = itemView.findViewById(R.id.date);
        }
    }
}
