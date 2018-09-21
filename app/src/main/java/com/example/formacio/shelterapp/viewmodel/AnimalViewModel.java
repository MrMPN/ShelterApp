package com.example.formacio.shelterapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.example.formacio.shelterapp.database.AppDatabase;
import com.example.formacio.shelterapp.domain.Animal;

import java.util.List;

public class AnimalViewModel extends AndroidViewModel{
    private static final String LOG = "AnimalViewModel";

    private LiveData<List<Animal>> currentList;
    private AppDatabase appDataBase;


    public AnimalViewModel(@NonNull Application application) {
        super(application);
        appDataBase = AppDatabase.getInstance(application.getApplicationContext());
        currentList = appDataBase.animalDao().getAllAnimals();
    }

    public LiveData<List<Animal>> getCurrentList() {
        return currentList;
    }

    public void insert(Animal animal){
        appDataBase.animalDao().insert(animal);
    }
}
