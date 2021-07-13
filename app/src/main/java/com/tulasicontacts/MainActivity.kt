package com.tulasicontacts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private var contactList: ArrayList<Contact> = ArrayList<Contact>()
    private var recyclerView: RecyclerView? = null
    private var mAdapter: ContactListAdaptar? = null

    companion object {
        val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadContacts()

        recyclerView = findViewById(R.id.contactRecylerView) as RecyclerView


        //loadContacts.setOnClickListener { loadContacts() }

        mAdapter = ContactListAdaptar(contactList)
        recyclerView!!.setAdapter(mAdapter)
        mAdapter?.notifyDataSetChanged();

        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.setLayoutManager(mLayoutManager)
        recyclerView!!.setItemAnimator(DefaultItemAnimator())


        getcontacts.setOnClickListener {
            recyclerView!!.setAdapter(mAdapter)
            mAdapter?.notifyDataSetChanged();

        }
        addcontacts.setOnClickListener {
            openactivity("1","","","0")
        }

    }
    fun openactivity(type : String, id : String,name : String, phone: String){
        val intent = Intent(this, AddActivity::class.java)

        startActivity(intent)
    }
    private fun loadContacts() {
        var builder = StringBuilder()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
            //callback onRequestPermissionsResult
        } else {
            //builder =
            getContacts()
            //listContacts.text = builder.toString()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()
            } else {
                //  toast("Permission must be granted in order to display contacts information")
            }
        }
    }

    private fun getContacts() {
        val cursor: Cursor?
        var name: String
        var phonenumber: String
        var image_uri: String
        var count: String
        val order = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            order
        )

        var i = 0
        while (cursor!!.moveToNext() && i < 500) {
            name =
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            phonenumber =
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            image_uri =
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val removeSpecialChar = phonenumber.replace("[-+.^:*#_/, ]".toRegex(), "")
            var mobileNumber: String? = null
            if (removeSpecialChar.length == 13) {
                mobileNumber = removeSpecialChar.substring(2, 13)
                //searchView.setText(mobileNumber);
                phonenumber = mobileNumber
                val a = Contact(name, image_uri, phonenumber, "")
                contactList.add(a)
            } else if (removeSpecialChar.length == 11) {
                //searchView.setText(removeSpecialChar);
                phonenumber = removeSpecialChar
                val a = Contact(name, image_uri, phonenumber, "")
                contactList.add(a)
            } else if (removeSpecialChar.length == 15) {
                mobileNumber = removeSpecialChar.substring(4, 15)
                //searchView.setText(mobileNumber);
                phonenumber = mobileNumber
                val (name1, image, phone, count1) = Contact(name, image_uri, phonenumber, "")
            }
            i++
        }

        cursor.close()




    }

}