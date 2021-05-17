package com.example.treasuredetector;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treasuredetector.helper.DialogHelper;
import com.example.treasuredetector.helper.Helper;
import com.example.treasuredetector.model.Item;
import com.example.treasuredetector.repository.ItemRepository;
import com.example.treasuredetector.view_model.ItemViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.widget.Toast.LENGTH_LONG;

//In this activity we add, view, edit and delete items

public class ItemActivity extends AppCompatActivity {

    private static final String TAG = "ItemActivity";
    private Boolean mLocationPermissionGranted = false;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
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

    private long time;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private Bitmap bitmap;

    ItemViewModel itemViewModel;

    Intent intent;
    Item item;
    String flow;

    List<CategoryIcon> categoryIconArrayList;

    MenuItem menuItemEdit;
    MenuItem menuItemSave;
    MenuItem menuItemDelete;

    ExecutorService executorService = Executors.newFixedThreadPool(2);
    Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());

    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        initViews();

        helper = new Helper(ItemActivity.this);
        dialogHelper = new DialogHelper(ItemActivity.this);
        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        itemViewModel.setCallback(new ItemRepository.Callback() {

            @Override
            public void onItemAdded() {
                dialogHelper.dismissDialog();
                Toast.makeText(ItemActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

            @Override
            public void onItemUpdated() {
                dialogHelper.dismissDialog();
                Toast.makeText(ItemActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                editMode(false);
//                onBackPressed();
            }

            @Override
            public void onItemDeleted() {
                dialogHelper.dismissDialog();
                Toast.makeText(ItemActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

        categoryIconArrayList = getCategoryIconList();

        CategoryAdapter adapter = new CategoryAdapter(this, R.layout.item_category_icon, categoryIconArrayList);
        spinnerCategory.setAdapter(adapter);

        intent = getIntent();
        flow = intent.getStringExtra("flow");

        if (flow.equals("addItem")) {
            initializeDateAndTime(Calendar.getInstance());
            initializeLocation();
            if(isLocationPermissionGranted()){
                getDeviceLocation();
            }
        } else if (flow.equals("viewItem")) {
            buttonAdd.setVisibility(View.GONE);

            editMode(false);

            item = (Item) getIntent().getSerializableExtra("data");

            editTextTitle.setText(item.getTitle());
            editTextDescription.setText(item.getDescription());

            for (int i = 0; i < categoryIconArrayList.size(); i++) {
                if (item.getCategory().equals(categoryIconArrayList.get(i).category)) {
                    spinnerCategory.setSelection(i);
                    break;
                }
            }

            textViewDateAndTime.setText(helper.getFormattedDate(item.getTime()));

            String lat = String.valueOf(item.getLatitude()).length() <= 10 ? String.valueOf(item.getLatitude()) : String.valueOf(item.getLatitude()).substring(0,10);
            String lng = String.valueOf(item.getLongitude()).length() <= 10 ? String.valueOf(item.getLongitude()) : String.valueOf(item.getLongitude()).substring(0,10);
            String location =
                    "Lat: " +
                            lat +
                            "  |  " +
                            "Lng: " +
                                    lng;

            textViewLocation.setText(location);

            if (item.getImageName() != null) {
                //Getting image from stoarge then display is a heavy task hence we do it on a background thread
                loadImageOfSelectedItem(item);
            }
        }

        textViewDateAndTime.setOnClickListener(v -> showDateTimePicker());

        textViewLocation.setOnClickListener(v -> {
            if(isLocationPermissionGranted()){
                getDeviceLocation();
            }
        });

        imageView.setOnClickListener(v -> selectImage());

        buttonAdd.setOnClickListener(v -> addToDB());
    }

    private void addToDB() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String category = ((CategoryIcon) spinnerCategory.getSelectedItem()).getCategory();

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
        String category = ((CategoryIcon) spinnerCategory.getSelectedItem()).getCategory();

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

    private void initializeLocation() {

//        if (longitude == 0.0 && latitude == 0.0) {
//            helper.isLocationPermissionGranted();
//            getDeviceLocation();
//        }

        String lat = String.valueOf(latitude).length() <= 10 ? String.valueOf(latitude) : String.valueOf(latitude).substring(0,10);
        String lng = String.valueOf(longitude).length() <= 10 ? String.valueOf(longitude) : String.valueOf(longitude).substring(0,10);
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

    static class CategoryAdapter extends ArrayAdapter<CategoryIcon> {

        Context context;
        List<CategoryIcon> list;


        public CategoryAdapter(Context context, int resource, List<CategoryIcon> list) {
            super(context, resource, list);
            this.context = context;
            this.list = list;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    @NotNull ViewGroup parent) {
            return getCustomView(position, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, parent);
        }

        public View getCustomView(int position, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_category_icon, parent, false);

            TextView textView = view.findViewById(R.id.textView);
            ImageView imageView = view.findViewById(R.id.imageView);

            textView.setText(list.get(position).getCategory());
            imageView.setImageResource(list.get(position).getIcon());

            return view;
        }
    }

    private List<CategoryIcon> getCategoryIconList() {
        List<CategoryIcon> list = new ArrayList<>();
        list.add(new CategoryIcon("Select", R.drawable.ic_baseline_arrow_drop_down_24));
        list.add(new CategoryIcon("Bottle Cap", R.drawable.ic_bottlecap));
        list.add(new CategoryIcon("Bow and Arrow", R.drawable.ic_bow_and_arrow));
        list.add(new CategoryIcon("Bullets", R.drawable.ic_bullets));
        list.add(new CategoryIcon("Coins", R.drawable.ic_coins));
        list.add(new CategoryIcon("Jewelry", R.drawable.ic_jewelry));
        list.add(new CategoryIcon("Key", R.drawable.ic_key));
        list.add(new CategoryIcon("Miscellaneous", R.drawable.ic_miscellaneous));
        list.add(new CategoryIcon("Quiver", R.drawable.ic_quiver));
        list.add(new CategoryIcon("Sword", R.drawable.ic_sword));
        return list;
    }

    static class CategoryIcon {
        private final String category;
        private final int icon;

        public CategoryIcon(String category, int icon) {
            this.category = category;
            this.icon = icon;
        }

        public String getCategory() {
            return category;
        }

        public int getIcon() {
            return icon;
        }
    }

    private void loadImageInBackground(Uri uri){
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

    private void loadImageOfSelectedItem(Item item){

        executorService.execute(() -> {


            String imagePath = item.getImagePath();
            String imageName = item.getImageName();
            Bitmap bitmap = helper.getImageFromStorage(imagePath, imageName);

            mainThreadHandler.post(() -> {
                if(bitmap != null){
                    imageView.setImageBitmap(bitmap);
                }
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {

            if (data == null) {
                //Display an error
                return;
            }

            /*Since getting image from gallery then converting it into bitmap is a time consuming task therefore
            we do it in a background thread*/
            loadImageInBackground(data.getData());

        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
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
        }
        else if(item.getItemId() == R.id.actionDelete){
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
        Log.d(TAG, "getDeviceLocation: getting the device current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        try {
            if (true) {
                @SuppressLint("MissingPermission") Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Found location");
//                            Toast.makeText(ItemActivity.this ,"found current location", LENGTH_LONG).show();
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                latitude = currentLocation.getLatitude();
                                longitude = currentLocation.getLongitude();

                                String lat = String.valueOf(latitude).length() <= 10 ? String.valueOf(latitude) : String.valueOf(latitude).substring(0,10);
                                String lng = String.valueOf(longitude).length() <= 10 ? String.valueOf(longitude) : String.valueOf(longitude).substring(0,10);
                                String location =
                                        "Lat: " +
                                                lat +
                                                "  |  " +
                                                "Lng: " +
                                                lng;

                                textViewLocation.setText(location);
                            }
                        } else {
                            Log.d(TAG, "onComplete: Current location is null");
                            Toast.makeText(ItemActivity.this, " unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException" + e.getMessage());
        }
    }

    private boolean isLocationPermissionGranted(){
        String[] permission = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if(isLocationServiceOn()){
                    return true;
                }
            } else {
                ActivityCompat.requestPermissions(ItemActivity.this, permission, LOCATION_PERMISSION_REQUEST_CODE);
                return false;
            }
        } else {
            ActivityCompat.requestPermissions(ItemActivity.this, permission, LOCATION_PERMISSION_REQUEST_CODE);
            return false;
        }
        return false;
    }

    private boolean isLocationServiceOn() {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Build the alert dialog
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
            return false;
        }
        return true;
    }
}