package com.chizhikov.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chizhikov.R

class ContactListAdapter(
    private val contacts: List<Contact>,
    val onClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactListVIewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListVIewHolder {
        return ContactListVIewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.contact_list_item,
                parent,
                false
            )
        ).apply {
            root.setOnClickListener {
                onClick(contacts[adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: ContactListVIewHolder, position: Int) {
        holder.contactFirstName.text = contacts[position].firstName
        holder.contactLastName.text = contacts[position].proneNumber
    }

}