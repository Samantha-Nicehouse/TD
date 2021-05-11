package com.example.treasuredetector;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.example.treasuredetector.view_model.ItemViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    Button button;

    Helper helper;
    DialogHelper dialogHelper;

    private long time;
    private double latitude;
    private double longitude;
    private Bitmap bitmap;

    ItemViewModel itemViewModel;

    Intent intent;
    Item item;
    String flow;

    List<CategoryIcon> categoryIconArrayList;

    MenuItem menuItemEdit;
    MenuItem menuItemSave;
    MenuItem menuItemDelete;

    ExecutorService executorService = Executors.newFixedThreadPool(4);
    Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);


        initViews();

        helper = new Helper(ItemActivity.this);
        dialogHelper = new DialogHelper(ItemActivity.this);
        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        categoryIconArrayList = getCategoryIconList();

        CategoryAdapter adapter = new CategoryAdapter(this, R.layout.item_category_icon, categoryIconArrayList);
        spinnerCategory.setAdapter(adapter);


        intent = getIntent();
        flow = intent.getStringExtra("flow");

        if (flow.equals("addItem")) {
            initializeDateAndTime(Calendar.getInstance());
            initializeLocation();
        } else if (flow.equals("viewItem")) {

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

            String location =
                    "Lat: " +
                            (helper.getLatitude().equals("0") ? "0" : helper.getLatitude().substring(0, 10)) +
                            "  |  " +
                            "Lng: " +
                            (helper.getLongitude().equals("0") ? "0" : helper.getLongitude().substring(0, 10));

            textViewLocation.setText(location);

            if (item.getImageName() != null) {
                //Getting image from stoarge then display is a heavy task hence we do it on a background thread
                loadImageOfSelectedItem(item);
            }
        }

        textViewDateAndTime.setOnClickListener(v -> showDateTimePicker());

        textViewLocation.setOnClickListener(v -> initializeLocation());

        imageView.setOnClickListener(v -> selectImage());

        button.setOnClickListener(v -> addToDB());
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

        //Inserting a new row in db is also time consuming hence we do it in a background thread so our ui don't lag
        addItemToDb(item);
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

        //Updating a row in db is a time consuming task hence we do it in a background thread so our ui don't lag
        updateItemInDb(this.item);
    }

    private void deleteFromDB() {
        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("YES", (dialog, which) -> {
                    dialogHelper.showDialog();
                    // Continue with delete operation
                    deleteFromDb(item);
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("NO", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
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
        latitude = Double.parseDouble(helper.getLatitude());
        longitude = Double.parseDouble(helper.getLongitude());

        if (longitude == 0 && latitude == 0) {
            helper.isLocationPermissionGranted();
        }

        String location =
                "Lat: " +
                        (helper.getLatitude().equals("0") ? "0" : helper.getLatitude().substring(0, 10)) +
                        "  |  " +
                        "Lng: " +
                        (helper.getLongitude().equals("0") ? "0" : helper.getLongitude().substring(0, 10));

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
        button = findViewById(R.id.button);
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
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

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

    private void addItemToDb(Item item){

        executorService.execute(() -> {
            //First we store the image in local storage get its path then seth this path in item object and at
            //we insert the item in db;

            //add image to db only if image is selected
            if (bitmap != null) {
                String imageName = item.getTitle() + "_" + item.getTime();
                String imagePath = helper.saveToInternalStorage(bitmap, imageName);

                if (imagePath != null && !imagePath.isEmpty()) {
                    item.setImagePath(imagePath);
                    item.setImageName(imageName);
                }
            }

            itemViewModel.insert(item);

            mainThreadHandler.post(() -> {
                dialogHelper.dismissDialog();
                Toast.makeText(ItemActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                onBackPressed();
            });
        });
    }

    private void loadImageOfSelectedItem(Item item){

        executorService.execute(() -> {

            String imagePath = item.getImagePath();
            String imageName = item.getImageName();
            bitmap = helper.getImageFromStorage(imagePath, imageName);

            mainThreadHandler.post(() -> {
                if(bitmap != null){
                    imageView.setImageBitmap(bitmap);
                }
            });
        });
    }

    private void updateItemInDb(Item item){
        executorService.execute(() -> {

            if (bitmap != null) {

                //If there was an image already associated with this entry we will delete it
                if(item.getImageName()!= null){
                    helper.deleteImageFromStorage(item.getImagePath(), item.getImageName());
                }

                String imageName = item.getTitle() + "_" + item.getTime();
                String imagePath = helper.saveToInternalStorage(bitmap, imageName);

                if (imagePath != null && !imagePath.isEmpty()) {
                    item.setImagePath(imagePath);
                    item.setImageName(imageName);
                }
            }

            itemViewModel.update(item);

            mainThreadHandler.post(() -> {
                dialogHelper.dismissDialog();
                Toast.makeText(ItemActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                editMode(false);
            });
        });
    }

    private void deleteFromDb(Item item){
        executorService.execute(() -> {

            //If there is an image associated with this entry we will delete it
            if(item.getImageName()!= null){
                helper.deleteImageFromStorage(item.getImagePath(), item.getImageName());
            }
            itemViewModel.delete(item);
            mainThreadHandler.post(() -> {
                dialogHelper.dismissDialog();
                Toast.makeText(ItemActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                onBackPressed();
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
        button.setVisibility(View.GONE);

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

}