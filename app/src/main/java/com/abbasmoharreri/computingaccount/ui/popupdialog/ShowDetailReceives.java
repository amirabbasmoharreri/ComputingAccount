package com.abbasmoharreri.computingaccount.ui.popupdialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;
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
import com.abbasmoharreri.computingaccount.module.AMoneyReceive;
import com.abbasmoharreri.computingaccount.module.AWork;

public class ShowDetailReceives extends Dialog implements View.OnClickListener {

    private TextView price, date;
    private TextView reportNumber, receiveNumber;
    private AMoneyReceive aMoneyReceive;
    private Context context;
    private Button ok;

    public ShowDetailReceives(@NonNull Context context, AMoneyReceive aMoneyReceive) {
        super( context );
        this.context = context;
        this.aMoneyReceive = aMoneyReceive;
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.dialog_show_detail_receive );

        reportNumber = findViewById( R.id.dialog_show_detail_receive_report_number_work );
        price = findViewById( R.id.dialog_show_detail_receive_price_work );
        date = findViewById( R.id.dialog_show_detail_receive_date_work );
        receiveNumber = findViewById( R.id.dialog_show_detail_receive_work_number_work );
        ok = findViewById( R.id.dialog_show_detail_receive_button_ok );
        ok.setOnClickListener( this );

        setItemsView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.dialog_show_detail_receive_button_ok) {
            dismiss();
        }
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void setItemsView() {
        reportNumber.setText( String.valueOf( aMoneyReceive.getReportNumber() ) );
        receiveNumber.setText( String.valueOf( aMoneyReceive.getNumber() ) );
        price.setText( String.format( "%,d", aMoneyReceive.getPrice() ) );
        date.setText( aMoneyReceive.getStringIranianDate() );


        if (aMoneyReceive.getAttaches().size() != 0) {

            float imageViewSize = TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 130, context.getResources().getDisplayMetrics() );

            for (int i = 0; i < aMoneyReceive.getAttaches().size(); i++) {
                LinearLayout mainLayout = findViewById( R.id.dialog_show_detail_receive_container_image_work );
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( (int) imageViewSize, (int) imageViewSize );
                ImageView imageView = new ImageView( context );
                imageView.setLayoutParams( params );

                mainLayout.addView( imageView );

                Bitmap bitmap1 = BitmapFactory.decodeByteArray( aMoneyReceive.getAttaches().get( i ).getPicture(), 0, aMoneyReceive.getAttaches().get( i ).getPicture().length );
                imageView.setImageBitmap( bitmap1 );

            }
        }

    }


}
