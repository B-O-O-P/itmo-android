package com.chizhikov.contactstest.integration

import android.app.Activity
import android.widget.SearchView
import androidx.core.view.isNotEmpty
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.test.filters.MediumTest
import com.chizhikov.contactstest.MainActivity
import com.chizhikov.contactstest.R
import com.chizhikov.contactstest.contacts.ContactListAdapter
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@MediumTest
@RunWith(RobolectricTestRunner::class)
class ActivityTest {
    private lateinit var activity: Activity

    @Before
    fun setup() {
        activity = Robolectric.buildActivity(MainActivity::class.java).create().start().visible().get()
    }

    @Test
    fun `Activity not null`() {
        assertNotNull(activity)
    }

    @Test
    fun `Recycler not null`() {
        assertNotNull(activity.findViewById(R.id.contact_recycler_view))
    }

    @Test
    fun `Recycler adapter not null`() {
        val adapter: ContactListAdapter =
            (activity.findViewById(R.id.contact_recycler_view) as RecyclerView).adapter as ContactListAdapter
        assertNotNull(adapter)
    }

    @Test
    fun `Recycler adapter list not null`() {
        val adapter: ContactListAdapter =
            (activity.findViewById(R.id.contact_recycler_view) as RecyclerView).adapter as ContactListAdapter

        assertNotNull(adapter.contacts)
    }

    @Test
    fun `Menu shown correctly`(){
        val menu = shadowOf(activity).optionsMenu

        assertNotNull(menu)
        assertTrue(menu.isNotEmpty())
        assertTrue(menu.hasVisibleItems())
    }

    @Test
    fun `SearchView shown correctly`(){
        val searchView = shadowOf(activity).optionsMenu.findItem(R.id.search).actionView

        assertNotNull(searchView)
        assertTrue(searchView is SearchView)
        assertTrue(searchView.isVisible)
    }
}

