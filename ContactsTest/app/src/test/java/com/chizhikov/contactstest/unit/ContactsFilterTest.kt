package com.chizhikov.contactstest.unit

import androidx.test.filters.SmallTest
import com.chizhikov.contactstest.contacts.Contact
import com.chizhikov.contactstest.extensions.filterContacts
import org.junit.Test

import org.junit.Assert.*


@SmallTest
class ContactsFilterTest {
    private val CONTACT_FIRST_TYPE_NAME = "Pasha"
    private val CONTACT_SECOND_TYPE_NAME = "Masha Ivanova"
    private val CONTACT_FIRST_TYPE_NUMBER = "020-666-9999"
    private val CONTACT_SECOND_TYPE_NUMBER = "8(800)555-35-35"
    private val LIST_OF_CONTACTS = listOf<Contact>(
        Contact(CONTACT_FIRST_TYPE_NAME, CONTACT_FIRST_TYPE_NUMBER),
        Contact(CONTACT_FIRST_TYPE_NAME, CONTACT_SECOND_TYPE_NUMBER),
        Contact(CONTACT_SECOND_TYPE_NAME, CONTACT_FIRST_TYPE_NUMBER),
        Contact(CONTACT_SECOND_TYPE_NAME, CONTACT_SECOND_TYPE_NUMBER)
    )


    @Test
    fun `Empty query returns list back`() {
        val contactsList = LIST_OF_CONTACTS
        val query = ""

        assertEquals(contactsList, filterContacts(contactsList, query))
    }

    @Test
    fun `All elements of non-empty list contain query name`() {
        val contactList = LIST_OF_CONTACTS
        val query = CONTACT_FIRST_TYPE_NAME.subSequence(0, 3)

        filterContacts(contactList, query.toString()).forEach {
            assertTrue(it.firstName.contains(query))
        }
    }

    @Test
    fun `All elements of non-empty list contain query number`() {
        val contactList = LIST_OF_CONTACTS
        val query = CONTACT_FIRST_TYPE_NUMBER.subSequence(0, 3)

        filterContacts(contactList, query.toString()).forEach {
            assertTrue(it.proneNumber.contains(query))
        }
    }

    @Test
    fun `Each element has part which start with query`() {
        val contactList = LIST_OF_CONTACTS
        val query = "Iva"

        filterContacts(contactList, query).forEach {
            val splittedName = it.firstName.split(' ')
            var assert = false

            splittedName.forEach { part ->
                assert = part.startsWith(query) || assert
            }

            assertTrue(assert)
        }
    }

    @Test
    fun `Query is not prefix of any part of any name or number returns empty list`() {
        val contactList = LIST_OF_CONTACTS
        val query = CONTACT_SECOND_TYPE_NUMBER.subSequence(1,4).toString()

        assertEquals(emptyList<Contact>(), filterContacts(contactList, query))
    }

    @Test
    fun `Invalid query returns empty list`() {
        val contactList = LIST_OF_CONTACTS
        val query = "*******"

        assertEquals(emptyList<Contact>(), filterContacts(contactList, query))
    }
}
