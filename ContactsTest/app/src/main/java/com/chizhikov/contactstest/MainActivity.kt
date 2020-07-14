package com.chizhikov.contactstest

import android.Manifest
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.widget.SearchView
import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.chizhikov.contactstest.extensions.*
import com.google.android.material.snackbar.Snackbar
import android.widget.Toast
import com.chizhikov.contactstest.contacts.*

const val MY_PERMISSIONS_REQUEST_READ_CONTACTS: Int = 1
const val TYPE_CONTACTS = "CONTACTS"
const val TYPE_RESULTS = "RESULTS"
val PERMISSIONS_CONTACT = arrayOf(Manifest.permission.READ_CONTACTS)

class MainActivity : AppCompatActivity() {
    private var contactList: List<Contact> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initContacts()
        if (savedInstanceState == null) {
            updateContacts()
        } else {
            setContacts(lastCustomNonConfigurationInstance as List<Contact>)
        }

        contact_main_layout.setOnRefreshListener {
            contact_main_layout.isRefreshing = true
            updateContacts()
            contact_main_layout.isRefreshing = false
        }
    }


    override fun onRetainCustomNonConfigurationInstance(): Any {
        return contactList
    }

    private fun updateContacts() {
        if (!isPermissionGranted(Manifest.permission.READ_CONTACTS)) {
            requestContactsPermissions()
        } else {
            fetchContacts()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        initSearchView(menu)

        return super.onCreateOptionsMenu(menu)
    }

    private fun initSearchView(menu: Menu) {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searchView: SearchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.setIconifiedByDefault(false)

        searchView.setOnSearchClickListener {

        }

        searchView.setOnCloseListener {
            searchView.clearFocus()
            updateContacts()
            true
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                updateForSearch(filterContacts(contactList, query.toString()))
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                updateForSearch(filterContacts(contactList, newText.toString()))
                return true
            }
        })
    }

    private fun initContacts() {
        contact_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ContactListAdapter(emptyList()) {
                startActivity(Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${it.proneNumber}")
                })
            }
        }
        setBackNoContacts(emptyList(), TYPE_CONTACTS)
    }

    private fun fetchContacts() {
        setContacts(fetchAllContacts())
        Toast.makeText(
            this@MainActivity,
            resources.getQuantityString(
                R.plurals.contacts_count_plurals,
                contactList.size,
                contactList.size
            ),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setContacts(list: List<Contact>) {
        contactList = list
        val contactAdapter = contact_recycler_view.adapter as ContactListAdapter
        contactAdapter.apply {
            contacts = contactList
            notifyDataSetChanged()
        }
        setBackNoContacts(list, TYPE_CONTACTS)
    }

    private fun setBackNoContacts(list: List<Contact>, type: String) {
        if (list.isEmpty()) {
            when (type) {
                TYPE_RESULTS -> {
                    contact_recycler_view.background = getDrawable(R.drawable.no_results)
                }
                TYPE_CONTACTS -> {
                    contact_recycler_view.background = getDrawable(R.drawable.no_contacts)
                }
            }
        } else {
            contact_recycler_view.setBackgroundResource(0)
        }
    }


    private fun updateForSearch(list: List<Contact>) {
        val contactAdapter = contact_recycler_view.adapter as ContactListAdapter
        contactAdapter.apply {
            contacts = list
            notifyDataSetChanged()
        }
        setBackNoContacts(list, TYPE_RESULTS)
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
                        fetchContacts()
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
