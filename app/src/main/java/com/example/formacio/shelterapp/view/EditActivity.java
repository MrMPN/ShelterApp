package com.example.formacio.shelterapp.view;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.formacio.shelterapp.R;
import com.example.formacio.shelterapp.domain.Animal;
import com.example.formacio.shelterapp.utils.ImageUtils;
import com.example.formacio.shelterapp.viewmodel.EditViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class EditActivity extends AppCompatActivity {
    private final String TAG = EditActivity.class.getSimpleName();
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    public static final String ANIMAL_DATA = "animalData";
    private final int ADD_MODE = 1;
    private final int EDIT_MODE = 2;
    private int ACTIVITY_MODE = ADD_MODE;
    private ImageView picture;
    private EditText name;
    private EditText age;
    private TextView date;
    private CheckBox chip;
    private EditText typeAnimal;
    private String base64Pic;
    private int animalId;
    private long time = 0;
    private List<EditText> validationList = new ArrayList<>();
    private EditViewModel mEditViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initViews();
        mEditViewModel = ViewModelProviders.of(this).get(EditViewModel.class);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(ANIMAL_DATA)){
            changeToEditMode(intent);
        }
    }

    private void changeToEditMode(Intent intent){
        ACTIVITY_MODE = EDIT_MODE;
        setTitle("Edit");

        Animal animal = intent.getParcelableExtra(ANIMAL_DATA);
        populateUi(animal);
    }

    private void initViews(){
        picture = findViewById(R.id.picture);
        name = findViewById(R.id.nameEdit);
        age = findViewById(R.id.ageEdit);
        date = findViewById(R.id.dateEdit);
        chip = findViewById(R.id.chipCheckBox);
        typeAnimal = findViewById(R.id.typeEdit);
        FloatingActionButton fab = findViewById(R.id.addData);
        validationList.addAll(Arrays.asList(name,age,typeAnimal));

        date.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createDateDialog().show();
            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFabClicked();
            }
        });
    }

    private DatePickerDialog createDateDialog() {
        Calendar c = Calendar.getInstance();
        return new DatePickerDialog(this, new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                setTime(year, month, dayOfMonth);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    private void setTime(int year, int month, int dayOfMonth){
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        @SuppressLint("SimpleDateFormat") DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        date.setText(sdf.format(c.getTime()));
        time = c.getTimeInMillis();
    }

    private void setTime(long time){
        @SuppressLint("SimpleDateFormat") DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        date.setText(sdf.format(new Date(time)));
        this.time = time;
    }

    private void populateUi(Animal animal) {
        name.setText(animal.getName());
        age.setText(String.valueOf(animal.getAge()));
        chip.setChecked(animal.isChip());
        typeAnimal.setText(animal.getTypeOfAnimal());
        animalId = animal.getAnimalID();
        setAndSaveImageToBitmap(animal.getPicture());
        setTime(animal.getDate());
    }


    private boolean isInputValid(){
        for (EditText e: validationList){
            if (TextUtils.isEmpty(e.getText().toString())){
                return false;
            }
        }
        return !TextUtils.isEmpty(base64Pic) && (time != 0);
    }

    private Animal getInputData(){
        String mName = name.getText().toString();
        int mAge = Integer.parseInt(age.getText().toString());
        String mTypeAnimal = typeAnimal.getText().toString();
        boolean mChip = chip.isChecked();
        return new Animal(mName, mAge, mChip, mTypeAnimal, time, base64Pic);
    }

    private void onFabClicked(){
        if (isInputValid()){
            addData();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else{
            Toast.makeText(this, R.string.input_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void addData(){
        switch (ACTIVITY_MODE){
            case ADD_MODE:
                Log.i(TAG, "AddMode");
                mEditViewModel.insert(getInputData());
                break;
            case EDIT_MODE:
                Log.i(TAG, "EditMode");
                Animal animal = getInputData();
                animal.setAnimalID(animalId);
                mEditViewModel.update(animal);
                break;
        }
    }

    private void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void setAndSaveImageTo64(Intent data){
        if (data.hasExtra("data")){
            Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            if (bitmap != null) {
                base64Pic = ImageUtils.encodeTobase64(bitmap);
                picture.setImageBitmap(bitmap);
            }
        }
    }

    private void setAndSaveImageToBitmap(String base64){
        base64Pic = base64;
        Bitmap bitmap = ImageUtils.decodeBase64(base64Pic);
        picture.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    setAndSaveImageTo64(data);
                    break;
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (ACTIVITY_MODE == EDIT_MODE) {
                    onBackPressed();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
