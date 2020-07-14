package com.chizhikov.contactstest.integration

import android.app.Activity
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.filters.MediumTest
import com.chizhikov.contactstest.MainActivity
import com.chizhikov.contactstest.R
import com.chizhikov.contactstest.contacts.ContactListAdapter
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@MediumTest
@RunWith(RobolectricTestRunner::class)
class SearchViewTest {
    private lateinit var activity: Activity
    private lateinit var menu: Menu
    private lateinit var searchItem: MenuItem
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ContactListAdapter

    @Before
    fun setup() {
        activity =
            Robolectric.buildActivity(MainActivity::class.java).create().start().resume().visible()
                .get()
        menu = shadowOf(activity).optionsMenu
        searchItem = menu.findItem(R.id.search)
        searchView = searchItem.actionView as SearchView
        recyclerView = activity.findViewById(R.id.contact_recycler_view)
        adapter = recyclerView.adapter as ContactListAdapter
    }

    @Test
    fun `Perform search button click`() {
        activity.onOptionsItemSelected(searchItem)

        assertTrue(searchItem.expandActionView())
        assertTrue(searchItem.isActionViewExpanded)
    }

    @Test
    fun `Empty query text on start`() {
        assertEquals("", searchView.query.toString())
    }

    @Test
    fun `Background for empty list`() {
        if (adapter.contacts.isEmpty()) {
            assertEquals(
                R.drawable.no_contacts,
                shadowOf(recyclerView.background).createdFromResId
            )
        } else {
            assertEquals(
                0,
                shadowOf(recyclerView.background).createdFromResId
            )
        }
    }
}