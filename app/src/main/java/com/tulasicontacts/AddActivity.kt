package com.tulasicontacts

import android.content.ContentProviderOperation

import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class AddActivity : AppCompatActivity() {

    lateinit var txtname : EditText
    lateinit var txtphone : EditText
    lateinit var buttonadd : Button
    lateinit var titleactivity : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

         buttonadd = findViewById(R.id.bttadd)


        buttonadd.setOnClickListener {



            val etContactName: EditText = findViewById(R.id.edittextname)
            val etContactNumber: EditText = findViewById(R.id.edittextphone)
            val name: String = etContactName.text.toString()
            val phone = etContactNumber.text.toString()
            /*val intent = Intent(ContactsContract.Intents.Insert.ACTION)
            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE)
            intent.putExtra(ContactsContract.Intents.Insert.NAME, name)
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone)
            startActivityForResult(intent, 1)*/
            val contact = ArrayList<ContentProviderOperation>();
            contact.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.RawContacts.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                    )
                    .withValue(
                        ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                        name
                    )
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, name)
                    .build()

            );
            contact.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                    )
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
                    .withValue(
                        ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                    )
                    .build()
            );
            try {
                val results = contentResolver.applyBatch(ContactsContract.AUTHORITY, contact)
                Toast.makeText(applicationContext,"results "+results,Toast.LENGTH_SHORT).show();
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Toast.makeText(applicationContext,"Contact Added Successfully ",Toast.LENGTH_SHORT).show();

        }
    }
}