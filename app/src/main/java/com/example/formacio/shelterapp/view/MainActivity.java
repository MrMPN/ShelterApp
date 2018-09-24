package com.example.formacio.shelterapp.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.formacio.shelterapp.R;
import com.example.formacio.shelterapp.domain.Animal;
import com.example.formacio.shelterapp.viewmodel.MainViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private MainViewModel mMainViewModel;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initViewModel();
    }

    private void initViews(){
        recyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab_add);
        adapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViewModel(){
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.getCurrentList().observe(this, new Observer<List<Animal>>() {
            @Override
            public void onChanged(@Nullable List<Animal> animals) {
                Log.d(TAG, "Updating data from LiveData");
                adapter.setWords(animals);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                mMainViewModel.deleteAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
