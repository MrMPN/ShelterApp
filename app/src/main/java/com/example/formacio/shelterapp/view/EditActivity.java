package com.example.formacio.shelterapp.view;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.formacio.shelterapp.R;
import com.example.formacio.shelterapp.domain.Animal;
import com.example.formacio.shelterapp.utils.DateUtils;
import com.example.formacio.shelterapp.utils.ImageUtils;
import com.example.formacio.shelterapp.viewmodel.EditViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static com.example.formacio.shelterapp.view.MainActivity.locationPermissionGranted;

public class EditActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "EditActivity";
    private final static int REQUEST_CHECK_SETTINGS = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    public static final String ANIMAL_DATA = "animalData";
    private final int ADD_MODE = 1;
    private final int EDIT_MODE = 2;
    private int ACTIVITY_MODE = ADD_MODE;
    private boolean connectionToApi = false;
    private List<EditText> validationList = new ArrayList<>();
    private ImageView picture;
    private EditText name;
    private EditText age;
    private TextView date;
    private CheckBox chip;
    private EditText typeAnimal;
    private TextView latitude;
    private TextView longitude;
    private String base64Pic;
    private int animalId;
    private long time = 0;
    private Location location;
    private EditViewModel mEditViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initViews();
        mEditViewModel = ViewModelProviders.of(this).get(EditViewModel.class);
        if (locationPermissionGranted) { initGoogleApi(); }
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(ANIMAL_DATA)) { changeToEditMode(intent); }
    }
    private void initViews() {
        picture = findViewById(R.id.picture);
        name = findViewById(R.id.nameEdit);
        age = findViewById(R.id.ageEdit);
        date = findViewById(R.id.dateEdit);
        chip = findViewById(R.id.chipCheckBox);
        typeAnimal = findViewById(R.id.typeEdit);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        Button buttonLocation = findViewById(R.id.button);
        FloatingActionButton fab = findViewById(R.id.addData);
        validationList.addAll(Arrays.asList(name, age, typeAnimal));

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

        buttonLocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionToApi) {checkLocationSettings();}
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFabClicked();
            }
        });
    }

    private void initGoogleApi() {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    private DatePickerDialog createDateDialog() {
        Calendar c = Calendar.getInstance();
        return new DatePickerDialog(this, new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.setText(DateUtils.getFormattedTime(year, month, dayOfMonth));
                time = DateUtils.dateToUnixTime(year, month, dayOfMonth);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    private void changeToEditMode(Intent intent) {
        ACTIVITY_MODE = EDIT_MODE;
        setTitle(R.string.edit);
        Animal animal = intent.getParcelableExtra(ANIMAL_DATA);
        storeData(animal);
        populateUi(animal);
    }

    private void storeData(Animal animal){
        animalId = animal.getAnimalID();
        base64Pic = animal.getPicture();
        time = animal.getDate();
        location = animal.getLocation();
    }

    private void populateUi(Animal animal) {
        name.setText(animal.getName());
        age.setText(String.valueOf(animal.getAge()));
        chip.setChecked(animal.isChip());
        typeAnimal.setText(animal.getTypeOfAnimal());
        date.setText(DateUtils.getFormattedTime(time));
        setCoordinates(location);
        setAndSaveImageToBitmap(base64Pic);
    }

    private void setCoordinates(Location location){
        latitude.setText(String.valueOf(location.getLatitude()));
        longitude.setText(String.valueOf(location.getLongitude()));
    }

    private boolean isInputValid() {
        for (EditText e : validationList) {
            if (TextUtils.isEmpty(e.getText().toString())) {
                return false;
            }
        }
        return !TextUtils.isEmpty(base64Pic) && (time != 0) && (location != null);
    }

    private Animal getInputData() {
        String mName = name.getText().toString();
        int mAge = Integer.parseInt(age.getText().toString());
        String mTypeAnimal = typeAnimal.getText().toString();
        boolean mChip = chip.isChecked();
        if (animalId != 0) {
            return new Animal(animalId, mName, mAge, mChip, mTypeAnimal, time, base64Pic, location);
        }
        return new Animal(mName, mAge, mChip, mTypeAnimal, time, base64Pic, location);
    }

    private void onFabClicked() {
        if (isInputValid()) {
            addData();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.input_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void addData() {
        switch (ACTIVITY_MODE) {
            case ADD_MODE:
                Log.d(TAG, "addData: AddMode");
                mEditViewModel.insert(getInputData());
                break;
            case EDIT_MODE:
                Log.d(TAG, "addData: EditMode");
                mEditViewModel.update(getInputData());
                break;
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void setAndSaveImageTo64(Intent data) {
        if (data.hasExtra("data")) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            if (bitmap != null) {
                base64Pic = ImageUtils.encodeTobase64(bitmap);
                picture.setImageBitmap(bitmap);
            }
        }
    }

    private void setAndSaveImageToBitmap(String base64) {
        Bitmap bitmap = ImageUtils.decodeBase64(base64);
        picture.setImageBitmap(bitmap);
    }

    private void checkLocationSettings(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
        setSuccessListener(result);
        setFailureListener(result);
    }

    private void setSuccessListener(Task<LocationSettingsResponse> task){
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getLocation();
            }
        });
    }

    private void setFailureListener(Task<LocationSettingsResponse> task) {
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(EditActivity.this, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException ignored) { }
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    location = task.getResult();
                    setCoordinates(location);
                }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        connectionToApi = true;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: ");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: " + connectionResult.toString());
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
            case REQUEST_CHECK_SETTINGS:
                if (resultCode == RESULT_OK) {
                    getLocation();
                    break;
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (ACTIVITY_MODE == EDIT_MODE) {
                    onBackPressed();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
