package com.abbasmoharreri.computingaccount.ui.popupdialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.abbasmoharreri.computingaccount.R;
import com.abbasmoharreri.computingaccount.filemanager.PictureCompression;
import com.abbasmoharreri.computingaccount.module.AAccount;
import com.abbasmoharreri.computingaccount.module.AWork;

public class ShowDetailWorks extends Dialog implements View.OnClickListener {

    private TextView price, attachCount, comment, date, time;
    private TextView reportNumber, workName, person, workNumber;
    private TextView accountName;
    private AWork aWork;
    private Context context;
    private Button ok;

    public ShowDetailWorks(@NonNull Context context, AWork aWork) {
        super( context );
        this.context = context;
        this.aWork = aWork;
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.dialog_show_detail_work );

        reportNumber = findViewById( R.id.dialog_show_detail_report_number_work );
        workName = findViewById( R.id.dialog_show_detail_work_name_work );
        price = findViewById( R.id.dialog_show_detail_price_work );
        date = findViewById( R.id.dialog_show_detail_date_work );
        person = findViewById( R.id.dialog_show_detail_person_work );
        attachCount = findViewById( R.id.dialog_show_detail_attach_count_work );
        comment = findViewById( R.id.dialog_show_detail_comment_work );
        workNumber = findViewById( R.id.dialog_show_detail_work_number_work );
        accountName = findViewById( R.id.dialog_show_detail_account );
        ok = findViewById( R.id.dialog_show_detail_button_ok );
        ok.setOnClickListener( this );

        setItemsView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.dialog_show_detail_button_ok) {
            dismiss();
        }
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void setItemsView() {
        reportNumber.setText( String.valueOf( aWork.getReportNumber() ) );
        workNumber.setText( String.valueOf( aWork.getNumber() ) );
        workName.setText( aWork.getName() );
        price.setText( String.format( "%,d", aWork.getPrice() ) );
        date.setText( aWork.getStringIranianDate() );
        person.setText( aWork.getPersonName() );
        attachCount.setText( String.valueOf( aWork.getAttachCount() ) );
        comment.setText( aWork.getComment() );
        accountName.setText( aWork.getAccountNumber() + "  " + aWork.getAccountName() );


        if (aWork.getAttaches().size() != 0) {

            float imageViewSize = TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 130, context.getResources().getDisplayMetrics() );

            for (int i = 0; i < aWork.getAttaches().size(); i++) {
                LinearLayout mainLayout = findViewById( R.id.dialog_show_detail_container_image_work );
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( (int) imageViewSize, (int) imageViewSize );
                ImageView imageView = new ImageView( context );
                imageView.setLayoutParams( params );

                mainLayout.addView( imageView );

                Bitmap bitmap1 = BitmapFactory.decodeByteArray( aWork.getAttaches().get( i ).getPicture(), 0, aWork.getAttaches().get( i ).getPicture().length );
                imageView.setImageBitmap( bitmap1 );

            }
        }

    }


}
