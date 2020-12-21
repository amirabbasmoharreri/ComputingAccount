package com.abbasmoharreri.computingaccount.filemanager;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class PictureCompression {


    public Bitmap compression(Bitmap picture) {
        Bitmap compress = picture;

        return compress;
    }


    public byte[] DrawableToByteArray(Drawable picture) {
        byte[] convertedPicture = null;

        Bitmap bitmap = ((BitmapDrawable) picture).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        bitmap.compress( Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream );

        convertedPicture = byteArrayOutputStream.toByteArray();

        return convertedPicture;
    }

}
