package com.example.formacio.shelterapp.view;

import android.app.Dialog;
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
import android.widget.Toast;

import com.example.formacio.shelterapp.R;
import com.example.formacio.shelterapp.domain.Animal;
import com.example.formacio.shelterapp.viewmodel.MainViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private final int ERROR_DIALOG_REQUEST = 9001;
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
        isServiceOk();
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

    public boolean isServiceOk(){
        Log.d(TAG, "isServiceOk: Checking Google services");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServiceOk: Everything's fine");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServiceOk: Service error, but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, available, ERROR_DIALOG_REQUEST );
            dialog.show();
        } else{
            Toast.makeText(this, "Can't make Map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
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
