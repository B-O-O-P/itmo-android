package com.chizhikov

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.chizhikov.extensions.batchRequestPermissions
import com.chizhikov.extensions.isPermissionGranted
import com.chizhikov.extensions.shouldShowPermissionRationale
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import android.view.Menu
import android.widget.Toast
import com.chizhikov.contacts.Contact
import com.chizhikov.contacts.ContactListAdapter
import com.chizhikov.contacts.fetchAllContacts


const val MY_PERMISSIONS_REQUEST_READ_CONTACTS: Int = 1
val PERMISSIONS_CONTACT = arrayOf(Manifest.permission.READ_CONTACTS)

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showContacts()
        contact_main_layout.setOnRefreshListener {
            contact_main_layout.isRefreshing = true
            showContacts()
            contact_main_layout.isRefreshing = false
        }
    }

    private fun showContacts() {
        if (!isPermissionGranted(Manifest.permission.READ_CONTACTS)) {
            requestContactsPermissions()
        } else {
            showContactsList()
        }
    }


    private fun showContactsList() {
        val contactsList: List<Contact> = fetchAllContacts()
        contact_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ContactListAdapter(contactsList) {
                startActivity(Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${it.proneNumber}")
                })
            }
        }
        Toast.makeText(
            this@MainActivity,
            resources.getQuantityString(
                R.plurals.contacts_count_plurals,
                contactsList.size,
                contactsList.size
            ),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun requestContactsPermissions() {
        if (shouldShowPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            Snackbar.make(
                contact_main_layout, R.string.permission_contacts_rationale,
                Snackbar.LENGTH_INDEFINITE
            ).setAction(R.string.ok) {
                batchRequestPermissions(
                    PERMISSIONS_CONTACT,
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS
                )
            }.show()
        } else {
            batchRequestPermissions(PERMISSIONS_CONTACT, MY_PERMISSIONS_REQUEST_READ_CONTACTS)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Snackbar.make(
                        contact_main_layout,
                        getString(R.string.contact_permissions_granted),
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(getString(R.string.update)) {
                        showContactsList()
                    }.show()
                } else {
                    Snackbar.make(
                        contact_main_layout, getString(R.string.permissions_not_granted),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                return
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }
}
