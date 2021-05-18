package com.example.treasuredetector;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treasuredetector.adapter.CategoryAdapter;
import com.example.treasuredetector.helper.Constants;
import com.example.treasuredetector.helper.DialogHelper;
import com.example.treasuredetector.helper.Helper;
import com.example.treasuredetector.helper.LocationHelper;
import com.example.treasuredetector.model.Category;
import com.example.treasuredetector.model.Item;
import com.example.treasuredetector.view_model.ItemViewModel;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.treasuredetector.helper.Helper.LOCATION_PERMISSION;

//In this activity we add, view, edit and delete items

public class ItemActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 123;
    EditText editTextTitle;
    EditText editTextDescription;
    Spinner spinnerCategory;
    TextView textViewDateAndTime;
    TextView textViewLocation;
    ImageView imageView;
    ImageView imageViewEdit;
    Button buttonAdd;

    Helper helper;
    DialogHelper dialogHelper;
    LocationHelper locationHelper;

    private long time;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private Bitmap bitmap;

    ItemViewModel itemViewModel;

    Intent intent;
    Item item;
    String flow;

    List<Category> categoryIconArrayList;

    MenuItem menuItemEdit;
    MenuItem menuItemSave;
    MenuItem menuItemDelete;

    ExecutorService executorService = Executors.newFixedThreadPool(2);
    Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());


    private boolean recordedSizeOfOriginalList = false;
    private int sizeOfOriginalList = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        initViews();

        helper = new Helper(ItemActivity.this);
        dialogHelper = new DialogHelper(ItemActivity.this);
        locationHelper = new LocationHelper(ItemActivity.this);
        locationHelper.setOnLocationChange(location -> {
            if (location != null) {
                Toast.makeText(ItemActivity.this, "Current location updated", Toast.LENGTH_SHORT).show();
                setupLocationTextView(location.getLatitude(), location.getLongitude());
            } else {
                Toast.makeText(ItemActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
            }
            dialogHelper.dismissDialog();
        });

        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        itemViewModel.getAllItems().observe(this, items -> {

            if (!recordedSizeOfOriginalList) {
                sizeOfOriginalList = items.size();
                recordedSizeOfOriginalList = true;
                return;
            }

            //Item added
            if (items.size() > sizeOfOriginalList) {
                dialogHelper.dismissDialog();
                Toast.makeText(ItemActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
            //Item updated
            else if (items.size() == sizeOfOriginalList) {
                dialogHelper.dismissDialog();
                Toast.makeText(ItemActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                editMode(false);
            }
            //Item Deleted
            else if (items.size() < sizeOfOriginalList) {
                dialogHelper.dismissDialog();
                Toast.makeText(ItemActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

        categoryIconArrayList = Constants.getCategoryIconList();

        CategoryAdapter adapter = new CategoryAdapter(this, R.layout.item_category_icon, categoryIconArrayList);
        spinnerCategory.setAdapter(adapter);

        intent = getIntent();
        flow = intent.getStringExtra("flow");

        if (flow.equals("addItem")) {
            initializeDateAndTime(Calendar.getInstance());
            setupLocationTextView(latitude, longitude);
            getDeviceLocation();
        } else if (flow.equals("viewItem")) {
            buttonAdd.setVisibility(View.GONE);

            editMode(false);

            item = (Item) getIntent().getSerializableExtra("data");

            editTextTitle.setText(item.getTitle());
            editTextDescription.setText(item.getDescription());

            for (int i = 0; i < categoryIconArrayList.size(); i++) {
                if (item.getCategory().equals(categoryIconArrayList.get(i).getName())) {
                    spinnerCategory.setSelection(i);
                    break;
                }
            }

            textViewDateAndTime.setText(helper.getFormattedDate(item.getTime()));

            setupLocationTextView(item.getLatitude(), item.getLongitude());

            if (item.getImageName() != null) {
                //Getting image from stoarge then display is a heavy task hence we do it on a background thread
                loadImageOfSelectedItemInBackground(item);
            }
        }

        textViewDateAndTime.setOnClickListener(v -> showDateTimePicker());

        textViewLocation.setOnClickListener(v -> getDeviceLocation());

        imageView.setOnClickListener(v -> selectImage());

        buttonAdd.setOnClickListener(v -> addToDB());
    }

    private void addToDB() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String category = ((Category) spinnerCategory.getSelectedItem()).getName();

        if (title.isEmpty()) {
            Toast.makeText(ItemActivity.this, "Please enter title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (description.isEmpty()) {
            Toast.makeText(ItemActivity.this, "Please enter description", Toast.LENGTH_SHORT).show();
            return;
        }

        if (category.equals("Select")) {
            Toast.makeText(ItemActivity.this, "Please select some category", Toast.LENGTH_SHORT).show();
            return;
        }

        Item item = new Item(title, description, category, time, latitude, longitude);


        dialogHelper.showDialog();
        itemViewModel.insert(item, bitmap);

    }

    private void updateInDB() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String category = ((Category) spinnerCategory.getSelectedItem()).getName();

        if (title.isEmpty()) {
            Toast.makeText(ItemActivity.this, "Please enter title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (description.isEmpty()) {
            Toast.makeText(ItemActivity.this, "Please enter description", Toast.LENGTH_SHORT).show();
            return;
        }

        if (category.equals("Select")) {
            Toast.makeText(ItemActivity.this, "Please select some category", Toast.LENGTH_SHORT).show();
            return;
        }

        this.item.setTitle(title);
        this.item.setDescription(description);
        this.item.setCategory(category);
        this.item.setTime(time);
        this.item.setLatitude(latitude);
        this.item.setLongitude(longitude);

        dialogHelper.showDialog();
        itemViewModel.update(item, bitmap);
    }

    private void deleteFromDB() {
        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton("YES", (dialog, which) -> {
                    dialogHelper.showDialog();
                    itemViewModel.delete(item);
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("NO", null)
                .show();
    }

    private void selectImage() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    private void initializeDateAndTime(Calendar calendar) {
        time = calendar.getTime().getTime();
        textViewDateAndTime.setText(helper.getFormattedDate(time));
    }

    private void setupLocationTextView(double latitude, double longitude) {

        this.latitude = latitude;
        this.longitude = longitude;

        String lat = String.valueOf(latitude).length() <= 10 ? String.valueOf(latitude) : String.valueOf(latitude).substring(0, 10);
        String lng = String.valueOf(longitude).length() <= 10 ? String.valueOf(longitude) : String.valueOf(longitude).substring(0, 10);
        String location =
                "Lat: " +
                        lat +
                        "  |  " +
                        "Lng: " +
                        lng;

        textViewLocation.setText(location);
    }

    private void initViews() {
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        textViewDateAndTime = findViewById(R.id.textViewDateAndTime);
        textViewLocation = findViewById(R.id.textViewLocation);
        imageView = findViewById(R.id.imageView);
        imageViewEdit = findViewById(R.id.imageViewEdit);
        buttonAdd = findViewById(R.id.button);
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        Calendar date = Calendar.getInstance();
        new DatePickerDialog(ItemActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
            date.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(ItemActivity.this, (view1, hourOfDay, minute) -> {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);
                initializeDateAndTime(date);
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    private void loadImageInBackground(Uri uri) {
        executorService.execute(() -> {
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri); //100ms
                bitmap = BitmapFactory.decodeStream(inputStream); // 200ms
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                bitmap = null;
            }
            mainThreadHandler.post(() -> {
                if (bitmap == null) {
                    Toast.makeText(ItemActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                    return;
                }
                imageView.setImageBitmap(bitmap);
            });
        });
    }

    private void loadImageOfSelectedItemInBackground(Item item) {
        executorService.execute(() -> {
            String imagePath = item.getImagePath();
            String imageName = item.getImageName();
            Bitmap bitmap = helper.getImageFromStorage(imagePath, imageName);
            mainThreadHandler.post(() -> {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    /*Since getting image from gallery then converting it into bitmap is a time consuming task therefore
                        we do it in a background thread*/
                    loadImageInBackground(data.getData());
                }
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == LOCATION_PERMISSION) {
            if (resultCode == RESULT_OK) {
                getDeviceLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Show action bar menu only in viewItemMode
        if (flow.equals("viewItem")) {
            new MenuInflater(this).inflate(R.menu.item_detail_menu, menu);
            menuItemEdit = menu.findItem(R.id.actionEdit);
            menuItemSave = menu.findItem(R.id.actionSave);
            menuItemDelete = menu.findItem(R.id.actionDelete);
        }
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.actionEdit) {
            time = this.item.getTime();
            latitude = this.item.getLatitude();
            longitude = this.item.getLongitude();

            menuItemEdit.setVisible(false);
            menuItemDelete.setVisible(false);
            menuItemSave.setVisible(true);
            editMode(true);
            Toast.makeText(this, "Edit mode", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.actionSave) {

            menuItemEdit.setVisible(true);
            menuItemDelete.setVisible(true);
            menuItemSave.setVisible(false);
            updateInDB();
            return true;
        } else if (item.getItemId() == R.id.actionDelete) {
            deleteFromDB();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    private void editMode(boolean isEditable) {
        editTextTitle.setEnabled(isEditable);
        editTextDescription.setEnabled(isEditable);
        spinnerCategory.setEnabled(isEditable);
        textViewDateAndTime.setEnabled(isEditable);
        textViewLocation.setEnabled(isEditable);
        imageView.setEnabled(isEditable);
        imageViewEdit.setEnabled(isEditable);

        int colorId = R.color.edit_text_background_color;

        if (!isEditable) {
            colorId = R.color.gray;
        }

        editTextTitle.setBackgroundTintList(getResources().getColorStateList(colorId));
        editTextDescription.setBackgroundTintList(getResources().getColorStateList(colorId));
        spinnerCategory.setBackgroundTintList(getResources().getColorStateList(colorId));
        textViewDateAndTime.setBackgroundTintList(getResources().getColorStateList(colorId));
        textViewLocation.setBackgroundTintList(getResources().getColorStateList(colorId));
        imageView.setBackgroundTintList(getResources().getColorStateList(colorId));
    }

    private void getDeviceLocation() {
        if (helper.isLocationPermissionGranted(ItemActivity.this)) {
            Toast.makeText(this, "Getting device location...", Toast.LENGTH_SHORT).show();
            dialogHelper.showDialog();
            locationHelper.startLocationChangeListener();
        }
        else {
            Toast.makeText(this, "Kindly grant location permission", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationHelper.removeLocationChangeListener();
    }
}