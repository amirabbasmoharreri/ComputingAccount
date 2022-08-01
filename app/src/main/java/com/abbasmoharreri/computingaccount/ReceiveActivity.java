package com.abbasmoharreri.computingaccount;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.database.reports.FetchLatestInventory;
import com.abbasmoharreri.computingaccount.database.reports.FetchWalletCard;
import com.abbasmoharreri.computingaccount.database.reports.InsettingData;
import com.abbasmoharreri.computingaccount.filemanager.PictureCompression;
import com.abbasmoharreri.computingaccount.module.AAccount;
import com.abbasmoharreri.computingaccount.module.AMoneyReceive;
import com.abbasmoharreri.computingaccount.module.APicture;
import com.abbasmoharreri.computingaccount.persiandatepicker.PersianDatePicker;
import com.abbasmoharreri.computingaccount.persiandatepicker.util.PersianCalendar;
import com.abbasmoharreri.computingaccount.text.NumberTextWatcherForThousand;
import com.abbasmoharreri.computingaccount.text.TextProcessing;
import com.abbasmoharreri.computingaccount.ui.adapters.SpinnerAccountAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReceiveActivity extends AppCompatActivity implements View.OnClickListener, TimePicker.OnTimeChangedListener, PersianDatePicker.OnDateChangedListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_PICTURE = 2;
    private TextView reportNumber, receivedNumber, remained;
    private EditText price, attachCount;
    private Spinner account;
    private PersianDatePicker datePicker;
    private TimePicker timePicker;
    private ImageView addPicture;
    private Button save;
    private Date date;
    private PictureCompression pictureCompression;
    private String dateString, timeString;
    private PersianCalendar persianCalendar;
    private AAccount aAccount;
    private boolean isUseDatePicker = false;
    private int countAttach = 0;
    private ArrayList<APicture> attaches;
    String imageFilePath;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("darkMode", true)) {
            setTheme(R.style.Dark_AppTheme);
        } else {
            setTheme(R.style.Light_AppTheme);
        }
        setContentView(R.layout.activity_receive);

        pictureCompression = new PictureCompression();
        date = new Date();


        reportNumber = findViewById(R.id.activity_received_report_number);
        receivedNumber = findViewById(R.id.activity_received_number);
        remained = findViewById(R.id.activity_received_remained);
        datePicker = findViewById(R.id.activity_received_date_picker);
        timePicker = findViewById(R.id.activity_received_time_picker);
        attachCount = findViewById(R.id.activity_received_attach_count);
        price = findViewById(R.id.activity_received_price);
        account = findViewById(R.id.activity_received_account_name);
        save = findViewById(R.id.activity_received_save_button);
        addPicture = findViewById(R.id.activity_received_add_picture_button);


        datePicker.setOnDateChangedListener(this);

        save.setOnClickListener(this);
        addPicture.setOnClickListener(this);

        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(this);

        date = new Date();

        persianCalendar = new PersianCalendar();
        attaches = new ArrayList<>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSystemDate();
        setDataToolBar();
        setSpinner();
        setEditTextWatcher();
    }


    private void getSystemDate() {

        Calendar calendar = Calendar.getInstance();
        isUseDatePicker = false;
        TextProcessing textProcessing = new TextProcessing();
        Date date1 = calendar.getTime();
        timeString = textProcessing.convertDateToStringWithoutDate(date1);

        datePicker.setDisplayPersianDate(persianCalendar);
        dateString = textProcessing.convertDateToStringWithoutTime(date1);

    }

    @SuppressLint("DefaultLocale")
    private void setDataToolBar() {

        try {
            DataBaseController dataBaseController = new DataBaseController(this);
            FetchLatestInventory fetchLatestInventory = new FetchLatestInventory(this);
            remained.setText(String.format("%,d", fetchLatestInventory.getRemainedReport()));
            reportNumber.setText(String.valueOf(fetchLatestInventory.getReportNumber()));
            receivedNumber.setText(String.valueOf(dataBaseController.max_receiveNumber() + 1));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setSpinner() {
        FetchWalletCard fetchWalletCard = new FetchWalletCard(this);

        SpinnerAccountAdapter spinnerAccountAdapter = new SpinnerAccountAdapter(this, fetchWalletCard.getList());
        account.setAdapter(spinnerAccountAdapter);
        account.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                aAccount = (AAccount) adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        aAccount = (AAccount) account.getSelectedItem();
    }


    private void setEditTextWatcher() {

        price.addTextChangedListener(new NumberTextWatcherForThousand(price));
        attachCount.addTextChangedListener(new NumberTextWatcherForThousand(attachCount));

    }

    private boolean showMassage() {


        if (price.getText().toString().equals("")) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.massage_title_price)
                    .setMessage(R.string.massage_inputPrice)
                    .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        }

        if (attachCount.getText().toString().equals("")) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.massage_title_attachCount)
                    .setMessage(R.string.massage_inputAttachCount)
                    .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        }


        return true;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_received_save_button:

                if (showMassage()) {
                    AMoneyReceive aMoneyReceive = new AMoneyReceive();

                    getDate();

                    String paidPrice = NumberTextWatcherForThousand.trimCommaOfString(price.getText().toString());

                    aMoneyReceive.setReportNumber(Integer.parseInt(reportNumber.getText().toString()))
                            .setPrice(Integer.parseInt(paidPrice))
                            .setNumber(Integer.parseInt(receivedNumber.getText().toString()))
                            .setAccountName(aAccount.getName())
                            .setAccountNumber(aAccount.getAccountNumber())
                            .setAttachCount(Integer.parseInt(attachCount.getText().toString()))
                            .setAttaches(attaches);

                    if (isUseDatePicker) {
                        aMoneyReceive.setIranianDate(date);
                    } else {
                        aMoneyReceive.setGregorianDate(date);
                    }

                    InsettingData insettingData = new InsettingData(this);
                    insettingData.insertMoney(aMoneyReceive);

                    Toast.makeText(this, R.string.toast_saved, Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }

                break;

            case R.id.activity_received_add_picture_button:

                selectImage();

                break;
        }
    }

    private void getDate() {
        TextProcessing textProcessing = new TextProcessing();
        date = textProcessing.convertStringToDate(this.dateString + " " + this.timeString);
    }

    private void selectImage() {
        final CharSequence[] options = {getString(R.string.camera_menu_take_photo), getString(R.string.camera_menu_choose_from_gallery), getString(R.string.camera_menu_cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.massage_title_add_photo);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getString(R.string.camera_menu_take_photo))) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("asdasdada", "" + e);
                        }
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                                    "com.abbasmoharreri.computingaccount.FileProvider", photoFile);
                            takePictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        }

                    }
                    takePictureIntent.putExtra(MediaStore.ACTION_IMAGE_CAPTURE, imageUri);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                } else if (options[item].equals(getString(R.string.camera_menu_choose_from_gallery))) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_PICK_PICTURE);
                } else if (options[item].equals(getString(R.string.camera_menu_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    public Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }


    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        countAttach++;
        float imageViewSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        float buttonSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        float imageViewMarginStart = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());

        final LinearLayout mainLayout = findViewById(R.id.activity_received_container_image_attach);
        LinearLayout subLayout = new LinearLayout(this);

        LinearLayout.LayoutParams subLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        subLayoutParams.setMargins(0, 30, 0, 0);
        subLayout.setLayoutParams(subLayoutParams);
        subLayout.setGravity(Gravity.CENTER);
        subLayout.setOrientation(LinearLayout.HORIZONTAL);
        subLayout.setId(R.id.subLayoutDynamic);

        LinearLayout.LayoutParams paramsImage = new LinearLayout.LayoutParams((int) imageViewSize, (int) imageViewSize);
        paramsImage.setMarginStart((int) imageViewMarginStart);

        ImageView button = new ImageView(this);
        button.setLayoutParams(new LinearLayout.LayoutParams((int) buttonSize, (int) buttonSize));
        button.setImageResource(R.drawable.ic_delete_24dp);
        button.setId(R.id.buttonDynamic);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup parent = (ViewGroup) v.getParent();
                ViewGroup parent1 = (ViewGroup) parent.getParent();
                int index = parent1.indexOfChild(parent);
                mainLayout.removeViewAt(index);
                countAttach--;
                attachCount.setText(String.valueOf(countAttach));
                attaches.remove(index);
                if (countAttach == 0) {
                    addPicture.setImageResource(R.drawable.ic_attach_file_24dp);
                }
            }
        });
        subLayout.addView(button);

        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(paramsImage);
        imageView.setBackgroundColor(Color.GRAY);
        subLayout.addView(imageView);


        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            int rotation = 0;
            int rotationInDegrees = 0;
            try {
                ExifInterface exifInterface = new ExifInterface(imageFilePath);
                rotation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                rotationInDegrees = exifToDegrees(rotation);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Bundle extras = data.getExtras();
            // Sample data cast to  thumbnail
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageFilePath);
            // Do whatever you want with the thumbnail such as setting it to image view
            imageBitmap = rotateBitmap(imageBitmap, rotationInDegrees);
            imageView.setImageBitmap(imageBitmap);
            attaches.add(new APicture().setPicture(pictureCompression.DrawableToByteArray(imageView.getDrawable())));


            //deleting image from storage after saving to db
            File fdelete = new File(imageFilePath);
            if (fdelete.exists()) {
                if (fdelete.delete()) {
                    Log.e("file Deleted :", imageFilePath);
                } else {
                    Log.e("file not Deleted :", imageFilePath);
                }
            }

        } else if (requestCode == REQUEST_PICK_PICTURE) {
            assert data != null;
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imageView.setImageBitmap(bitmap);
                attaches.add(new APicture().setPicture(pictureCompression.DrawableToByteArray(imageView.getDrawable())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        mainLayout.addView(subLayout);

        addPicture.setImageResource(R.drawable.ic_add_24dp);

        attachCount.setText(String.valueOf(countAttach));
    }


    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.timeString = hourOfDay + ":" + minute;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onDateChanged(int newYear, int newMonth, int newDay) {
        this.dateString = newYear + "-" + String.format("%02d", newMonth) + "-" + String.format("%02d", newDay);
        isUseDatePicker = true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
