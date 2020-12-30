package com.abbasmoharreri.computingaccount.ui.popupdialog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.abbasmoharreri.computingaccount.R;
import com.abbasmoharreri.computingaccount.database.DataBaseController;
import com.abbasmoharreri.computingaccount.database.reports.FetchPersonNames;
import com.abbasmoharreri.computingaccount.database.reports.FetchWalletCard;
import com.abbasmoharreri.computingaccount.database.reports.FetchWorkNames;
import com.abbasmoharreri.computingaccount.database.reports.UpdateAllData;
import com.abbasmoharreri.computingaccount.filemanager.PictureCompression;
import com.abbasmoharreri.computingaccount.module.AAccount;
import com.abbasmoharreri.computingaccount.module.AList;
import com.abbasmoharreri.computingaccount.module.APerson;
import com.abbasmoharreri.computingaccount.module.APicture;
import com.abbasmoharreri.computingaccount.module.AWork;
import com.abbasmoharreri.computingaccount.module.AWorkName;
import com.abbasmoharreri.computingaccount.persiandatepicker.PersianDatePicker;
import com.abbasmoharreri.computingaccount.text.NumberTextWatcherForThousand;
import com.abbasmoharreri.computingaccount.text.TextProcessing;
import com.abbasmoharreri.computingaccount.ui.adapters.SpinnerAccountAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class UpdateWorkActivity extends AppCompatActivity implements View.OnClickListener, PersianDatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_PICTURE = 2;
    private EditText price, attachCount, comment;
    private int oldPrice = 0;
    private AutoCompleteTextView reportNumber, workName, person;
    private Spinner accountName;
    private AWork aWork;
    private ImageView addPicture;
    private int position;
    private Context context;
    private PersianDatePicker date;
    private TimePicker time;
    private PictureCompression pictureCompression;
    private String dateString;
    private String timeString;
    private Date workDate;
    private Button update, cancel;
    private List<AWorkName> workNames;
    private List<Integer> reportNumbers;
    private List<APerson> personNames;
    private AAccount aAccount;
    private int countAttach = 0;
    private ArrayList<APicture> attaches;
    private ArrayList<APicture> attachesDelete;
    private boolean isUseDatePicker = false;
    String imageFilePath;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_update_work);

        context = getApplicationContext();
        pictureCompression = new PictureCompression();

        reportNumber = findViewById(R.id.dialog_update_report_number_work);
        reportNumber.setEnabled(false);
        workName = findViewById(R.id.dialog_update_work_name_work);
        price = findViewById(R.id.dialog_update_price_work);
        date = findViewById(R.id.dialog_update_date_work);
        time = findViewById(R.id.dialog_update_time_work);
        time.setIs24HourView(true);
        date.setOnDateChangedListener(this);
        time.setOnTimeChangedListener(this);
        person = findViewById(R.id.dialog_update_person_work);
        attachCount = findViewById(R.id.dialog_update_attach_count);
        comment = findViewById(R.id.dialog_update_comment_work);
        accountName = findViewById(R.id.dialog_update_account_name_work);
        accountName.setEnabled(false);
        update = findViewById(R.id.dialog_update_button_update_work);
        update.setOnClickListener(this);
        cancel = findViewById(R.id.dialog_update_button_cancel_work);
        cancel.setOnClickListener(this);
        addPicture = findViewById(R.id.dialog_update_add_picture_button);
        addPicture.setOnClickListener(this);
        workDate = new Date();
        attaches = new ArrayList<>();
        attachesDelete = new ArrayList<>();
        aWork = (AWork) Objects.requireNonNull(getIntent().getExtras()).getParcelable(AList.KEY_PARCELABLE_Work);
        attaches = aWork.getAttaches();

        isUseDatePicker = false;
        setSpinner();
        getList();
        setAutoCompleteTextView();
        setDataView();
        setEditTextWatcher();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.dialog_update_button_update_work:
                getDate();

                resetAttaches();

                String updatePrice = NumberTextWatcherForThousand.trimCommaOfString(price.getText().toString());
                DataBaseController dataBaseController = new DataBaseController(this);

                aWork.setReportNumber(Integer.parseInt(reportNumber.getText().toString()))
                        .setReportId(dataBaseController.get_Id_Report(Integer.parseInt(reportNumber.getText().toString())))
                        .setName(workName.getText().toString())
                        .setWorkNameId(dataBaseController.get_Id_WorkName(workName.getText().toString()))
                        .setPersonName(person.getText().toString())
                        .setPersonId(dataBaseController.get_Id_Person(person.getText().toString()))
                        .setAccountName(aAccount.getName())
                        .setAccountNumber(aAccount.getAccountNumber())
                        .setPrice(Integer.parseInt(updatePrice))
                        .setAttachCount(Integer.parseInt(attachCount.getText().toString()))
                        .setComment(comment.getText().toString())
                        .setAttaches(attaches);

                if (isUseDatePicker) {
                    aWork.setIranianDate(workDate);
                } else {
                    aWork.setGregorianDate(workDate);
                }

                int differencePrice = oldPrice - aWork.getPrice();

                UpdateAllData updateAllData = new UpdateAllData(context);
                updateAllData.updateAll(aWork, differencePrice);

                onBackPressed();

                break;
            case R.id.dialog_update_button_cancel_work:

                onBackPressed();

                break;
            case R.id.dialog_update_add_picture_button:

                selectImage();

                break;
        }
    }

    private void resetAttaches() {
        attachesDelete.addAll(attaches);
        attaches = new ArrayList<>();
        attaches = attachesDelete;
    }

    private void getList() {

        FetchWorkNames fetchWorkNames = new FetchWorkNames(context);
        workNames = fetchWorkNames.getList();
        FetchPersonNames fetchPersonNames = new FetchPersonNames(context);
        personNames = fetchPersonNames.getList();
    }


    private void setAutoCompleteTextView() {

        ArrayAdapter<String> adapter;


        String[] StringWNames = new String[workNames.size()];
        for (int i = 0; i < workNames.size(); i++) {
            StringWNames[i] = workNames.get(i).getName();
        }
        adapter = new ArrayAdapter<String>(context, R.layout.popup_autocomplete, StringWNames);
        workName.setThreshold(1);
        workName.setAdapter(adapter);

        String[] StringPNames = new String[personNames.size()];
        for (int i = 0; i < personNames.size(); i++) {
            StringPNames[i] = personNames.get(i).getName();
        }
        adapter = new ArrayAdapter<String>(context, R.layout.popup_autocomplete, StringPNames);
        person.setThreshold(1);
        person.setAdapter(adapter);

    }

    private void setSpinner() {
        FetchWalletCard fetchWalletCard = new FetchWalletCard(context);
        SpinnerAccountAdapter spinnerAccountAdapter = new SpinnerAccountAdapter(context, fetchWalletCard.getList());
        accountName.setAdapter(spinnerAccountAdapter);
        accountName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                aAccount = (AAccount) adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        int index = 0;
        for (int i = 0; i < fetchWalletCard.getList().size(); i++) {
            if (fetchWalletCard.getList().get(i).getAccountNumber().equals(aWork.getAccountNumber())) {
                index = i;
            }
        }

        accountName.setSelection(index);

        aAccount = (AAccount) accountName.getSelectedItem();
    }


    private void setEditTextWatcher() {
        price.addTextChangedListener(new NumberTextWatcherForThousand(price));
        attachCount.addTextChangedListener(new NumberTextWatcherForThousand(attachCount));
    }

    @SuppressLint({"NewApi", "DefaultLocale"})
    private void setDataView() {
        TextProcessing textProcessing = new TextProcessing();
        reportNumber.setText(String.valueOf(aWork.getReportNumber()));
        workName.setText(aWork.getName());
        price.setText(String.format("%,d", aWork.getPrice()));
        oldPrice = Integer.parseInt(NumberTextWatcherForThousand.trimCommaOfString(price.getText().toString()));
        person.setText(aWork.getPersonName());
        attachCount.setText(String.valueOf(aWork.getAttachCount()));
        countAttach = aWork.getAttachCount();
        comment.setText(aWork.getComment());

        this.dateString = textProcessing.convertDateToString(aWork.getGregorianDate());
        this.timeString = textProcessing.convertDateToStringWithoutDate(aWork.getGregorianDate());

        date.setDisplayDate(aWork.getGregorianDate());
        time.setHour(aWork.getIranianDate().getHours());
        time.setMinute(aWork.getIranianDate().getMinutes());

        if (aWork.getAttaches().size() != 0) {

            addPicture.setImageResource(R.drawable.ic_add_white_24dp);

            for (int i = 0; i < aWork.getAttaches().size(); i++) {
                float imageViewSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, context.getResources().getDisplayMetrics());
                float buttonSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, context.getResources().getDisplayMetrics());
                float imageViewMarginStart = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, context.getResources().getDisplayMetrics());

                final LinearLayout mainLayout = findViewById(R.id.dialog_update_container_image_work);
                LinearLayout subLayout = new LinearLayout(context);

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
                button.setImageResource(R.drawable.ic_delete_white_24dp);
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
                        attachesDelete.add(new APicture().setId(attaches.get(index).getId()));
                        attaches.remove(index);
                        if (countAttach == 0) {
                            addPicture.setImageResource(R.drawable.ic_attach_file_white_24dp);
                        }
                    }
                });
                subLayout.addView(button);

                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(paramsImage);
                imageView.setBackgroundColor(Color.GRAY);
                Bitmap bitmap = BitmapFactory.decodeByteArray(aWork.getAttaches().get(i).getPicture(), 0, aWork.getAttaches().get(i).getPicture().length);
                imageView.setImageBitmap(bitmap);
                subLayout.addView(imageView);
                mainLayout.addView(subLayout);

            }
        }
    }

    private void getDate() {
        TextProcessing textProcessing = new TextProcessing();
        workDate = textProcessing.convertStringToDate(this.dateString + " " + this.timeString);
    }


    @SuppressLint("DefaultLocale")
    @Override
    public void onDateChanged(int newYear, int newMonth, int newDay) {
        this.dateString = newYear + "-" + String.format("%02d", newMonth) + "-" + String.format("%02d", newDay);
        isUseDatePicker = true;
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
        this.timeString = hourOfDay + ":" + minute;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        countAttach++;
        float imageViewSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, context.getResources().getDisplayMetrics());
        float buttonSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, context.getResources().getDisplayMetrics());
        float imageViewMarginStart = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, context.getResources().getDisplayMetrics());

        final LinearLayout mainLayout = findViewById(R.id.dialog_update_container_image_work);
        LinearLayout subLayout = new LinearLayout(context);

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
        button.setImageResource(R.drawable.ic_delete_white_24dp);
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
                    addPicture.setImageResource(R.drawable.ic_attach_file_white_24dp);
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

        addPicture.setImageResource(R.drawable.ic_add_white_24dp);

        attachCount.setText(String.valueOf(countAttach));
    }
}
