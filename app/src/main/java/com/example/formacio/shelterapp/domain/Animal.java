package com.example.formacio.shelterapp.domain;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "animal")
public class Animal implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int animalID;
    private String name;
    private int age;
    private boolean chip;
    private String typeOfAnimal;
    private long date;
    private String picture;
    private double latitude;
    private double longitude;

    @Ignore
    public Animal(String name, int age, boolean chip, String typeOfAnimal, long date,
                  String picture, Location location) throws IllegalArgumentException {
        this.name = name;
        this.age = age;
        this.chip = chip;
        this.typeOfAnimal = typeOfAnimal;
        this.date = date;
        this.picture = picture;
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    @Ignore
    public Animal(int animalID, String name, int age, boolean chip, String typeOfAnimal, long date,
                  String picture, Location location) throws IllegalArgumentException {
        this.animalID = animalID;
        this.name = name;
        this.age = age;
        this.chip = chip;
        this.typeOfAnimal = typeOfAnimal;
        this.date = date;
        this.picture = picture;
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    public Animal(int animalID, String name, int age, boolean chip, String typeOfAnimal, long date,
                  String picture, double latitude, double longitude) throws IllegalArgumentException {
        this.animalID = animalID;
        this.name = name;
        this.age = age;
        this.chip = chip;
        this.typeOfAnimal = typeOfAnimal;
        this.date = date;
        this.picture = picture;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Location getLocation() {
        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    //Aquests mètodes s'han generat automàticament amb un plugin (Android Parcelable code generator)
    //per tal de poder crear un Parcelable facilment

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
        dest.writeLong(this.date);
        dest.writeString(this.picture);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    protected Animal(Parcel in) {
        this.animalID = in.readInt();
        this.name = in.readString();
        this.age = in.readInt();
        this.chip = in.readByte() != 0;
        this.typeOfAnimal = in.readString();
        this.date = in.readLong();
        this.picture = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Creator<Animal> CREATOR = new Creator<Animal>() {
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
