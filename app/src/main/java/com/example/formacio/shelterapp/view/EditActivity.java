package com.example.formacio.shelterapp.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.formacio.shelterapp.R;
import com.example.formacio.shelterapp.domain.Animal;
import com.example.formacio.shelterapp.utils.ImageUtils;

public class EditActivity extends AppCompatActivity {
    final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView picture;
    EditText name;
    EditText age;
    EditText date;
    CheckBox chip;
    EditText typeAnimal;
    FloatingActionButton fab;
    String base64Pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initViews();
    }

    private void initViews(){
        picture = findViewById(R.id.picture);
        name = findViewById(R.id.nameEdit);
        age = findViewById(R.id.ageEdit);
        date = findViewById(R.id.dateEdit);
        chip = findViewById(R.id.chipCheckBox);
        typeAnimal = findViewById(R.id.typeEdit);
        fab = findViewById(R.id.addData);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });
    }

    private void addData(){
        //TODO
        String mName = name.getText().toString();
        int mAge = Integer.parseInt(age.getText().toString());
        int mDate = Integer.parseInt(date.getText().toString());
        String mTypeAnimal = typeAnimal.getText().toString();
        boolean mChip = chip.isChecked();

        Animal animal = new Animal(mName, mAge, mChip, mTypeAnimal, mDate, base64Pic);

        Intent replyIntent = new Intent();
        replyIntent.putExtra("animalItem",animal);
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    private void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void setImage(int resultCode, Intent data){
        if (resultCode == RESULT_OK && data.hasExtra("data")){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            base64Pic = ImageUtils.encodeTobase64(bitmap);
            if (bitmap != null) {
                picture.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_IMAGE_CAPTURE:
                setImage(resultCode, data);
                break;
        }
    }
}
