package com.eroinnovations.qelmedistore;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.app.Activity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
public class FileManager {
    private static final int REQUEST_PERMISSION_CODE = 100;
    private static final int REQUEST_CODE_OPEN_DIRECTORY = 101;
    private Context context;
    public FileManager(Context context) {
        this.context = context;
    }
    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                context.startActivity(intent);
                return false;
            }
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                
                if (context instanceof Activity) {
                    ActivityCompat.requestPermissions((Activity) context, 
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 
                        REQUEST_PERMISSION_CODE);
                }
                return false;
            }
        }
        return true;
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted
            } else {
                // Redirect to app settings
                if (context instanceof Activity) {
                    Activity activity = (Activity) context;
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                    intent.setData(uri);
                    activity.startActivity(intent);
                }
            }
        }
    }
    public void openDirectoryPicker(Activity activity) {
        if (checkAndRequestPermissions()) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            activity.startActivityForResult(intent, REQUEST_CODE_OPEN_DIRECTORY);
        }
    }
    public void handleDirectoryPickerResult(int requestCode, int resultCode, Intent data, DirectoryPickerCallback callback) {
        if (requestCode == REQUEST_CODE_OPEN_DIRECTORY && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (callback != null) {
                callback.onDirectoryPicked(uri);
            }
        }
    }
    public boolean createDirectory(String dirPath) {
        if (checkAndRequestPermissions()) {
            File dir = new File(dirPath);
            return dir.mkdirs();
        }
        return false;
    }
    public boolean writeFile(String filePath, String data) {
        if (checkAndRequestPermissions()) {
            try {
                File file = new File(filePath);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] dataBytes = Base64.getDecoder().decode(data);
                fileOutputStream.write(dataBytes);
                fileOutputStream.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
    public String readFileAsBase64(String filePath) {
        if (checkAndRequestPermissions()) {
            try {
                File file = new File(filePath);
                byte[] fileData = new byte[(int) file.length()];
                FileInputStream fileInputStream = new FileInputStream(file);
                fileInputStream.read(fileData);
                fileInputStream.close();
                return Base64.getEncoder().encodeToString(fileData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
    public String readFileAsUTF(String filePath) {
        if (checkAndRequestPermissions()) {
            try {
                File file = new File(filePath);
                byte[] fileData = new byte[(int) file.length()];
                FileInputStream fileInputStream = new FileInputStream(file);
                fileInputStream.read(fileData);
                fileInputStream.close();
                return new String(fileData, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
    public boolean deleteFile(String filePath) {
        if (checkAndRequestPermissions()) {
            File file = new File(filePath);
            return file.delete();
        }
        return false;
    }
    public boolean deleteDirectory(String dirPath) {
        if (checkAndRequestPermissions()) {
            File dir = new File(dirPath);
            if (dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isDirectory()) {
                            deleteDirectory(file.getPath());
                        } else {
                            file.delete();
                        }
                    }
                }
            }
            return dir.delete();
        }
        return false;
    }
    public boolean moveFile(String sourceFilePath, String destDirPath) {
        if (checkAndRequestPermissions()) {
            File sourceFile = new File(sourceFilePath);
            File destDir = new File(destDirPath);
            File destFile = new File(destDir, sourceFile.getName());
            if (sourceFile.renameTo(destFile)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    public interface DirectoryPickerCallback {
        void onDirectoryPicked(Uri uri);
    }
}
