package com.abbasmoharreri.computingaccount.filemanager;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class FileManager extends File{

    public FileManager(@NonNull String pathname) {
        super(pathname);
    }

    public FileManager(@Nullable String parent, @NonNull String child) {
        super(parent, child);
    }

    public FileManager(@Nullable File parent, @NonNull String child) {
        super(parent, child);
    }

    public FileManager(@NonNull URI uri) {
        super(uri);
    }

    public static String getPath(String path) throws Exception {
        String result = "";
        String[] pro = path.split(":");

        result = "/" + pro[1];


        return result;
    }

}
