package com.chizhikov.contactstest.ui

import android.widget.EditText
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.chizhikov.contactstest.MainActivity
import com.chizhikov.contactstest.R
import com.chizhikov.contactstest.contacts.Contact
import com.chizhikov.contactstest.contacts.ContactListAdapter
import com.chizhikov.contactstest.contacts.fetchAllContacts
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@MediumTest
@RunWith(AndroidJUnit4::class)
class SearchQueryTest {
    private val TEST_CONTACT_NAME = "Test"
    private val TEST_CONTACT_NUMBER = "1 23"
    private val TEST_CORRECT_QUERY_NAME = "Tes"
    private val TEST_CORRECT_QUERY_NUMBER = "1"
    private val TEST_INVALID_QUERY_NAME = "No such name"
    private val TEST_INVALID_QUERY_NUMBER = "***123"
    private lateinit var contactsList: List<Contact>
    private lateinit var contactListAdapter: ContactListAdapter

    @get:Rule
    var permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.READ_CONTACTS)

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setup() {
        contactsList = activityRule.activity.applicationContext.fetchAllContacts()
        contactListAdapter =
            (activityRule.activity.findViewById(R.id.contact_recycler_view) as RecyclerView).adapter as ContactListAdapter
    }

    @Test
    fun empty_query_returns_same_list() {
        onView(withId(R.id.search)).perform(click())

        onView(isAssignableFrom(EditText::class.java)).perform(typeText(""))

        assertEquals(contactsList, (contactListAdapter.contacts))
    }

    @Test
    fun correct_query_contains_correct_contact_name() {
        onView(withId(R.id.search)).perform(click())

        onView(isAssignableFrom(EditText::class.java)).perform(typeText(TEST_CONTACT_NAME))

        assertEquals(TEST_CONTACT_NAME, contactListAdapter.contacts[0].firstName)
    }

    @Test
    fun correct_query_contains_correct_contact_number() {
        onView(withId(R.id.search)).perform(click())

        onView(isAssignableFrom(EditText::class.java)).perform(typeText(TEST_CORRECT_QUERY_NUMBER))

        assertEquals(TEST_CONTACT_NUMBER, contactListAdapter.contacts[0].proneNumber)
    }

    @Test
    fun invalid_query_name_returns_empty_list() {
        onView(withId(R.id.search)).perform(click())

        onView(isAssignableFrom(EditText::class.java)).perform(typeText(TEST_INVALID_QUERY_NAME))

        assertEquals(emptyList<Contact>(), contactListAdapter.contacts)
    }

    @Test
    fun invalid_query_number_returns_empty_list() {
        onView(withId(R.id.search)).perform(click())

        onView(isAssignableFrom(EditText::class.java)).perform(typeText(TEST_INVALID_QUERY_NUMBER))

        assertEquals(emptyList<Contact>(), contactListAdapter.contacts)
    }

    @Test
    fun invalid_query_displays_background_image() {
        val noResultsBackground = activityRule.activity.getDrawable(R.drawable.no_results)

        onView(withId(R.id.search)).perform(click())

        onView(isAssignableFrom(EditText::class.java)).perform(typeText(TEST_INVALID_QUERY_NAME))

        assertEquals(
            noResultsBackground.constantState,
            (activityRule.activity.findViewById(R.id.contact_recycler_view) as RecyclerView).background.constantState
        )
    }

    @Test
    fun onClick_after_query_works_properly(){
        onView(withId(R.id.search)).perform(click())

        onView(isAssignableFrom(EditText::class.java)).perform(typeText(TEST_CORRECT_QUERY_NAME))

        onView(isAssignableFrom(CardView::class.java)).perform(click())
    }
}