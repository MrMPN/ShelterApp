package com.example.formacio.shelterapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.formacio.shelterapp.domain.Animal;

import java.util.List;

@Dao
public interface AnimalDao {

    @Insert
    void insert(Animal animal);

    @Delete
    void deleteAnimal(Animal animal);

    @Update
    void update(Animal animal);

    @Query("SELECT COUNT(*) FROM animal")
    int countItems();

    @Query("SELECT * FROM animal WHERE animalID = :id")
    Animal findById(int id);

    @Query("DELETE FROM animal")
    void deleteAll();

    @Query("SELECT * FROM animal ORDER BY date DESC")
    LiveData<List<Animal>> getAllAnimals();
}
