package com.advance_native;

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
import org.json.JSONObject;

public class ContactsInterface {
    private Context mContext;
    private static final int REQUEST_CONTACTS_PERMISSION = 123;

    public ContactsInterface(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public void requestContactsPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_CONTACTS_PERMISSION);
        } else {
            // Permission already granted
            fetchContacts();
        }
    }

    @JavascriptInterface
    public String getContacts() {
        // Check permission before accessing contacts
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request permission
            requestContactsPermission();
            // Return empty JSON array for now
            return new JSONArray().toString();
        } else {
            // Permission granted, fetch contacts
            return fetchContacts();
        }
    }

    private String fetchContacts() {
        JSONArray jsonArray = new JSONArray();
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                JSONObject contact = new JSONObject();
                try {
                    String idColumn = ContactsContract.Contacts._ID;
                    String nameColumn = ContactsContract.Contacts.DISPLAY_NAME;

                    int idIndex = cursor.getColumnIndex(idColumn);
                    int nameIndex = cursor.getColumnIndex(nameColumn);

                    if (idIndex != -1 && nameIndex != -1) {
                        String id = cursor.getString(idIndex);
                        String name = cursor.getString(nameIndex);

                        contact.put("id", id);
                        contact.put("name", name);

                        // Get phone numbers
                        Cursor phoneCursor = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id},
                                null);

                        JSONArray phoneNumbers = new JSONArray();
                        if (phoneCursor != null && phoneCursor.moveToFirst()) {
                            do {
                                int phoneNumberIndex = phoneCursor.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER);
                                if (phoneNumberIndex != -1) {
                                    String phoneNumber = phoneCursor.getString(phoneNumberIndex);
                                    phoneNumbers.put(phoneNumber);
                                }
                            } while (phoneCursor.moveToNext());
                            phoneCursor.close();
                        }

                        contact.put("phoneNumbers", phoneNumbers);
                        jsonArray.put(contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        return jsonArray.toString();
    }
}
