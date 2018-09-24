package com.example.formacio.shelterapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.formacio.shelterapp.database.Repository;
import com.example.formacio.shelterapp.domain.Animal;

import java.util.List;

public class DetailViewModel extends AndroidViewModel {
    private static final String TAG = DetailViewModel.class.getSimpleName();
    private Repository repository;
    private LiveData<List<Animal>> currentList;

    public DetailViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        Log.d(TAG, "Retrieving data from repository");
    }

    public void delete (Animal animal){
        repository.delete(animal);
    }
}
