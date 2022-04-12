package com.example.contactsintent;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button pickContacts = (Button) findViewById(R.id.ContactButton);
        pickContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent implicitIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(implicitIntent, REQUEST_CODE);
            }
        });
    }

    @SuppressLint("Range")
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode){
            case (REQUEST_CODE):
                if (resultCode == Activity.RESULT_OK){
                    Uri contactData = data.getData();

                    Cursor cursor = getContentResolver().query(contactData, null, null, null, null);

                    if (cursor.moveToFirst()){
                        @SuppressLint("Range") String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        @SuppressLint("Range") String hasNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String num = "";

                        if (Integer.valueOf(hasNumber) == 1){
                            Cursor numbers = getContentResolver()
                                    .query(
                                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);

                            while (numbers.moveToNext()){
                                num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                Toast.makeText(MainActivity.this, "Number = " + num, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    break;
                }
        }
    }

}