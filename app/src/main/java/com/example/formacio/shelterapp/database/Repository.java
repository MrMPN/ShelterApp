package com.example.formacio.shelterapp.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.example.formacio.shelterapp.AppExecutors;
import com.example.formacio.shelterapp.domain.Animal;

import java.util.List;

public class Repository {
    private AnimalDao animalDao;
    private LiveData<List<Animal>> allAnimals;
    private AppExecutors mExecutors = AppExecutors.getInstance();

    public Repository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application.getApplicationContext());
        animalDao = db.animalDao();
        allAnimals = animalDao.getAllAnimals();
    }

    public LiveData<List<Animal>> getAllAnimals() {
        return allAnimals;
    }

    public LiveData<Animal> findById(int id){
        return animalDao.findById(id);
    }

    public void insert (final Animal animal) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                animalDao.insert(animal);
            }
        });
    }

    public void update(final Animal animal){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                animalDao.update(animal);
            }
        });

    }

    public void delete (final Animal animal){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                animalDao.deleteAnimal(animal);
            }
        });
    }

    public void deleteAll (){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                animalDao.deleteAll();
            }
        });
    }

    public int countItems (){
        return animalDao.countItems();
    }
}

