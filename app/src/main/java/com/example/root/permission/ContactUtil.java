package com.example.root.permission;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ContactUtil {
    public static Pair<String, List<String>> retrievePhones(final Context context, final Intent intent) {
        if (intent == null) {
            return null;
        }
        String name = null;
        final List<String> phonesList = new ArrayList<>();

            Uri contactData = intent.getData();
            Cursor c = context.getContentResolver().query(contactData, null, null, null, null);

            if (c.moveToFirst()) {
                name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                c.close();
            }
            if (name == "" || name == null) {
                Log.i("xiong", "name is empty ");
            }
            ContentResolver cr = context.getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (cur.getCount() <= 0) {
            }
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String nameCheck =
                        cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (!nameCheck.equals(name)) {
                    continue;
                }
                if (Integer.parseInt(cur.getString(cur
                        .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) < 0) {
                    continue;
                }
                Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                while (pCur.moveToNext()) {
                    int phoneType =
                            pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                    String phoneNumber =
                            pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    switch (phoneType) {
                        case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                            phonesList.add("移动电话:" + phoneNumber);
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                            phonesList.add("家庭电话:" + phoneNumber);
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                            phonesList.add("工作电话:" + phoneNumber);
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                            phonesList.add("其他电话:" + phoneNumber);
                            break;
                        default:
                            break;
                    }
                }
                cur.close();
                pCur.close();
            }

        return new Pair<>(name, phonesList);
    }
}
