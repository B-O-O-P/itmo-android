package com.chizhikov.contacts

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contact_list_item.view.*

class ContactListVIewHolder(val root: View) : RecyclerView.ViewHolder(root) {
    val contactFirstName: TextView = root.contact_name
    val contactLastName: TextView = root.contact_phone_number
}