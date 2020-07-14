package com.chizhikov.contactstest.extensions

import com.chizhikov.contactstest.contacts.Contact

fun filterContacts(list: List<Contact>, query: String): List<Contact> {
    if (validateQuery(query)) {
        return list.filter { contact ->
            val splittedName = contact.firstName.split(' ')
            val splittedPhone = contact.proneNumber.split(' ')
            var result = false
            splittedName.forEach {
                result = result || it.startsWith(query, true)
            }
            splittedPhone.forEach {
                result = result || it.startsWith(query, true)
            }
            return@filter result
        }
    } else {
        return emptyList()
    }
}

fun validateQuery(query: String?): Boolean {
    return if (query != null) {
        val phoneRegexUs = Regex("^[+]*[0-9]{0,3}[(]?[0-9]{1,4}[)]?[-0-9]*$")
        val nameRegex = Regex("^[A-Za-zА-Яа-я. ]*$")
        phoneRegexUs.matches(query) || nameRegex.matches(query)
    } else {
        false
    }
}