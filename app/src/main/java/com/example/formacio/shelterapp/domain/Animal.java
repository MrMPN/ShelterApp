package com.example.formacio.shelterapp.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "animal")
public class Animal implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int animalID;
    private String name;
    private int age;
    private boolean chip;
    private String typeOfAnimal;
    private int date;
    private String picture;

    public Animal(String name, int age, boolean chip, String typeOfAnimal, int date, String picture) {
        this.name = name;
        this.age = age;
        this.chip = chip;
        this.typeOfAnimal = typeOfAnimal;
        this.date = date;
        this.picture = picture;
    }

    public int getAnimalID() {
        return animalID;
    }

    public void setAnimalID(int animalID) {
        this.animalID = animalID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isChip() {
        return chip;
    }

    public void setChip(boolean chip) {
        this.chip = chip;
    }

    public String getTypeOfAnimal() {
        return typeOfAnimal;
    }

    public void setTypeOfAnimal(String typeOfAnimal) {
        this.typeOfAnimal = typeOfAnimal;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.animalID);
        dest.writeString(this.name);
        dest.writeInt(this.age);
        dest.writeByte(this.chip ? (byte) 1 : (byte) 0);
        dest.writeString(this.typeOfAnimal);
        dest.writeInt(this.date);
        dest.writeString(this.picture);
    }

    protected Animal(Parcel in) {
        this.animalID = in.readInt();
        this.name = in.readString();
        this.age = in.readInt();
        this.chip = in.readByte() != 0;
        this.typeOfAnimal = in.readString();
        this.date = in.readInt();
        this.picture = in.readString();
    }

    public static final Parcelable.Creator<Animal> CREATOR = new Parcelable.Creator<Animal>() {
        @Override
        public Animal createFromParcel(Parcel source) {
            return new Animal(source);
        }

        @Override
        public Animal[] newArray(int size) {
            return new Animal[size];
        }
    };
}
