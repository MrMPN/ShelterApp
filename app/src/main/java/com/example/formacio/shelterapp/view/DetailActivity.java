package com.example.formacio.shelterapp.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.formacio.shelterapp.R;
import com.example.formacio.shelterapp.domain.Animal;
import com.example.formacio.shelterapp.utils.DateUtils;
import com.example.formacio.shelterapp.utils.ImageUtils;
import com.example.formacio.shelterapp.view.DeleteDialogFragment.DeleteDialogListener;
import com.example.formacio.shelterapp.viewmodel.DetailViewModel;

import static com.example.formacio.shelterapp.view.EditActivity.ANIMAL_DATA;

public class DetailActivity extends AppCompatActivity implements DeleteDialogListener {
    public static final String SELECTED_ANIMAL = "selectedAnimal";
    private TextView detailName;
    private TextView detailAge;
    private TextView detailType;
    private TextView detailDate;
    private CheckBox detailCheck;
    private ImageView detailPicture;
    private Animal animal;
    private DetailViewModel mDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initViews();
        mDetailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(SELECTED_ANIMAL)) {
            populateUI(intent);
        }
    }

    private void initViews() {
        detailName = findViewById(R.id.detailName);
        detailAge = findViewById(R.id.detailAge);
        detailType = findViewById(R.id.detailType);
        detailDate = findViewById(R.id.detailDate);
        detailCheck = findViewById(R.id.detailCheck);
        Button detailButton = findViewById(R.id.detailButton);
        detailPicture = findViewById(R.id.detailPicture);
        detailButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        DeleteDialogFragment deleteDialog = new DeleteDialogFragment();
        deleteDialog.show(getSupportFragmentManager(), "DeleteDialog");
    }

    private void populateUI(Intent intent){
        animal = intent.getExtras().getParcelable("selectedAnimal");
        detailName.setText(animal.getName());
        detailAge.setText(getString(R.string.age_details, animal.getAge()));
        detailType.setText(animal.getTypeOfAnimal());
        detailDate.setText(DateUtils.getFormattedTime(animal.getDate()));
        detailCheck.setChecked(animal.isChip());
        detailPicture.setImageBitmap(ImageUtils.decodeBase64(animal.getPicture()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.editOption:
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                intent.putExtra(ANIMAL_DATA, animal);
                getApplicationContext().startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPositiveResult() {
        mDetailViewModel.delete(animal);
        finish();
    }

    @Override
    public void onNegativeResult() {
    }
}
