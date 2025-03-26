package com.eroinnovations.qelmedistore;
import android.Manifest;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
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
import java.util.ArrayList;
public class ContactsInterface {
    private Context context;
    private static final int PERMISSION_REQUEST_CODE = 123;
    public ContactsInterface(Context context) {
        this.context = context;
    }
    @JavascriptInterface
    public void requestContactsPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            
            ActivityCompat.requestPermissions((Activity) context,
                new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS},
                PERMISSION_REQUEST_CODE);
        }
    }
    @JavascriptInterface
    public String getContacts() {
        if (!checkContactsPermission()) {
            requestContactsPermission();
            return new JSONArray().toString();
        } else {
            return fetchContacts();
        }
    }
    private boolean checkContactsPermission() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }
    private String fetchContacts() {
        JSONArray jsonArray = new JSONArray();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                String idColumn = ContactsContract.Contacts._ID;
                String nameColumn = ContactsContract.Contacts.DISPLAY_NAME;
                int idIndex = cursor.getColumnIndex(idColumn);
                int nameIndex = cursor.getColumnIndex(nameColumn);
                do {
                    JSONObject jsonObject = new JSONObject();
                    String id = cursor.getString(idIndex);
                    String name = cursor.getString(nameIndex);
                    jsonObject.put("id", id);
                    jsonObject.put("name", name);
                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    JSONArray phoneNumbers = new JSONArray();
                    if (phoneCursor != null && phoneCursor.moveToFirst()) {
                        int phoneNumberIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        do {
                            String phoneNumber = phoneCursor.getString(phoneNumberIndex);
                            phoneNumbers.put(phoneNumber);
                        } while (phoneCursor.moveToNext());
                        phoneCursor.close();
                    }
                    jsonObject.put("phoneNumbers", phoneNumbers);
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
    @JavascriptInterface
    public void addContact(String name, String phoneNumber) {
        if (!checkContactsPermission()) {
            requestContactsPermission();
            return;
        }
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        int rawContactInsertIndex = operations.size();
        operations.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
            .build());
        operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
            .build());
        operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
            .build());
        try {
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, operations);
            showToast("Contact created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            showToast("Failed to create contact: " + e.getMessage());
        }
    }
    @JavascriptInterface
    public void updateContact(String contactId, String newName, String newPhoneNumber) {
        if (!checkContactsPermission()) {
            requestContactsPermission();
            return;
        }
        ContentValues nameValues = new ContentValues();
        nameValues.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, newName);
        ContentValues phoneValues = new ContentValues();
        phoneValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, newPhoneNumber);
        int nameRowsUpdated = context.getContentResolver().update(
            ContactsContract.Data.CONTENT_URI, nameValues,
            ContactsContract.Data.MIMETYPE + "=? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + "=?",
            new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, contactId});
        int phoneRowsUpdated = context.getContentResolver().update(
            ContactsContract.Data.CONTENT_URI, phoneValues,
            ContactsContract.Data.MIMETYPE + "=? AND " + ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
            new String[]{ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, contactId});
        if (nameRowsUpdated > 0 || phoneRowsUpdated > 0) {
            showToast("Contact updated successfully");
        } else {
            showToast("Failed to update contact");
        }
    }
    @JavascriptInterface
    public void deleteContact(String contactId) {
        if (!checkContactsPermission()) {
            requestContactsPermission();
            return;
        }
        int rowsDeleted = context.getContentResolver().delete(
            ContactsContract.RawContacts.CONTENT_URI,
            ContactsContract.RawContacts._ID + "=?",
            new String[]{contactId});
        if (rowsDeleted > 0) {
            showToast("Contact deleted successfully");
        } else {
            showToast("Failed to delete contact");
        }
    }
    private void showToast(String message) {
        ((Activity) context).runOnUiThread(() -> 
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }
}
