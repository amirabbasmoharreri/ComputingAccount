package com.abbasmoharreri.computingaccount.filemanager;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class Storage extends FileManager {

    public static final int INTERNAL_STORAGE = 1;
    public static final int SD_CARD = 2;
    public static final int USB_DRIVE = 3;
    private Context context;
    private File file;

    public String name;
    public int type;

    public Storage(String path, String name, int type) {
        super(path);
        this.name = name;
        this.type = type;
    }

    public Storage(Context context, File file) {
        super(file.getPath());
        this.file = file;
        this.context = context;
    }


    public String getRealPathStorage() {
        String result = "";

        result = getRootDirStorageSd() + removeUnnecessaryPath();

        return result;
    }

    private boolean detectStorage() {

        // 'true' means it is internal storage and 'false' means External storage

        if (file.getPath().contains(getStorageName().get(0)) || file.getPath().contains("primary")) {
            return true;
        } else {
            return false;
        }
    }

    private List<String> getStorageName() {
        List<String> list = new ArrayList<>();
        List<File> fileList = new ArrayList<>();

        fileList.addAll(Arrays.asList(context.getExternalFilesDirs("SECONDARY_STORAGE")));

        String[] internalSplit = fileList.get(0).getAbsolutePath().split("/");
        String[] externalSplit = fileList.get(1).getAbsolutePath().split("/");

        list.add(internalSplit[2]);
        list.add(externalSplit[2]);

        Log.e("internal name: ", list.get(0));
        Log.e("external name: ", list.get(1));
        return list;
    }

    private String getRootDirStorageSd() {
        String result = "";
        String[] pro;
        ArrayList<File> fileList = new ArrayList<>();
        fileList.addAll(Arrays.asList(context.getExternalFilesDirs("SECONDARY_STORAGE")));

        Log.e("internal: ", fileList.get(0).getPath());
        Log.e("external: ", fileList.get(1).getPath());

        if (detectStorage()) {
            //internal storage
            pro = fileList.get(0).getAbsolutePath().split("/");
            result = pro[0] + "/" + pro[1] + "/" + pro[2] + "/" + pro[3] + "/";
        } else {
            //external storage
            pro = fileList.get(1).getAbsolutePath().split("/");
            result = pro[0] + "/" + pro[1] + "/" + pro[2] + "/";
        }

        Log.e("external: ", result);
        return result;
    }

    private String removeUnnecessaryPath() {

        /* remove unnecessary items of path for example

        input: /tree/7429-E3B7:Android/
        output: Android/

         */

        String result = "";

        String[] pro = file.getAbsolutePath().split(":");

        if (pro.length < 2) {
            result = "";
        } else {
            result = pro[1];
        }


        return result;
    }


    public static ArrayList<Storage> getStorages(Context context) {
        ArrayList<Storage> storages = new ArrayList<>();

        // Internal storage
        storages.add(new Storage(Environment.getExternalStorageDirectory().getPath(),
                "Internal Storage", Storage.INTERNAL_STORAGE));

        // SD Cards
        ArrayList<File> extStorages = new ArrayList<>();
        extStorages.addAll(Arrays.asList(context.getExternalFilesDirs(null)));
        extStorages.remove(0); // Remove internal storage
        String secondaryStoragePath = System.getenv("SECONDARY_STORAGE");
        for (int i = 0; i < extStorages.size(); i++) {
            String path = extStorages.get(i).getPath().split("/Android")[0];
            if (Environment.isExternalStorageRemovable(extStorages.get(i)) || secondaryStoragePath != null && secondaryStoragePath.contains(path)) {
                String name = "SD Card" + (i == 0 ? "" : " " + String.valueOf(i + 1));
                storages.add(new Storage(path, name, Storage.SD_CARD));
            }
        }

        // USB Drives
        ArrayList<String> drives = new ArrayList<>();
        String reg = "(?i).*vold.*(vfat|ntfs|exfat|fat32|ext3|ext4).*rw.*";
        String s = "";
        try {
            final Process process = new ProcessBuilder().command("mount")
                    .redirectErrorStream(true).start();
            process.waitFor();
            final InputStream is = process.getInputStream();
            final byte[] buffer = new byte[1024];
            while (is.read(buffer) != -1) {
                s += new String(buffer);
            }
            is.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        final String[] lines = s.split("\n");
        for (String line : lines) {
            if (!line.toLowerCase(Locale.US).contains("asec") && line.matches(reg)) {
                String[] parts = line.split(" ");
                for (String path : parts) {
                    if (path.startsWith(File.separator) && !path.toLowerCase(Locale.US).contains("vold")) {
                        drives.add(path);
                    }
                }
            }
        }

        // Remove SD Cards from found drives (already found)
        ArrayList<String> ids = new ArrayList<>();
        for (Storage st : storages) {
            String[] parts = st.getPath().split(File.separator);
            ids.add(parts[parts.length - 1]);
        }
        for (int i = drives.size() - 1; i >= 0; i--) {
            String[] parts = drives.get(i).split(File.separator);
            String id = parts[parts.length - 1];
            if (ids.contains(id)) drives.remove(i);
        }

        // Get USB Drive name
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        Collection<UsbDevice> dList = usbManager.getDeviceList().values();
        ArrayList<UsbDevice> deviceList = new ArrayList<>();
        deviceList.addAll(dList);
        for (int i = 0; i < deviceList.size(); i++) {
            storages.add(new Storage(drives.get(i), deviceList.get(i).getProductName(), Storage.USB_DRIVE));
        }

        return storages;
    }
}