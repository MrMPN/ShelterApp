package com.example.formacio.shelterapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.formacio.shelterapp.database.Repository;
import com.example.formacio.shelterapp.domain.Animal;

import java.util.List;

public class MainViewModel extends AndroidViewModel{
    private static final String TAG = MainViewModel.class.getSimpleName();
    private Repository repository;
    private LiveData<List<Animal>> currentList;


    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        Log.d(TAG, "Retrieving data from repository");
        currentList = repository.getAllAnimals();
    }

    public LiveData<List<Animal>> getCurrentList() {
        return currentList;
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public LiveData<Animal> findById(int id){
        return repository.findById(id);
    }
}
