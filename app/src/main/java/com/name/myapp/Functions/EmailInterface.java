package com.eroinnovations.qelmedistore;
import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import org.json.JSONObject;
public class EmailInterface {
    private Context context;
    private static final int PERMISSION_REQUEST_CODE = 124;
    public EmailInterface(Context context) {
        this.context = context;
    }
    @JavascriptInterface
    public void requestEmailPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            
            ActivityCompat.requestPermissions((Activity) context,
                new String[]{Manifest.permission.READ_CONTACTS},
                PERMISSION_REQUEST_CODE);
        }
    }
    @JavascriptInterface
    public String getEmails() {
        if (!checkEmailPermission()) {
            requestEmailPermission();
            return new JSONArray().toString();
        } else {
            return fetchEmails();
        }
    }
    private boolean checkEmailPermission() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }
    private String fetchEmails() {
        JSONArray jsonArray = new JSONArray();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                String idColumn = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
                String emailColumn = ContactsContract.CommonDataKinds.Email.DATA;
                int idIndex = cursor.getColumnIndex(idColumn);
                int emailIndex = cursor.getColumnIndex(emailColumn);
                do {
                    JSONObject jsonObject = new JSONObject();
                    String id = cursor.getString(idIndex);
                    String email = cursor.getString(emailIndex);
                    jsonObject.put("id", id);
                    jsonObject.put("email", email);
                    jsonArray.put(jsonObject);
                } while (cursor.moveToNext());
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
        return jsonArray.toString();
    }

    private void showToast(String message) {
        ((Activity) context).runOnUiThread(() ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }
}
