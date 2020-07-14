package com.chizhikov.fragmentplay.fragments

import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.chizhikov.fragmentplay.R


class RootFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.root_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun addFirstChild() {
        val firstChild = ChildFragment.newInstance("0")
        firstChild.enterTransition = Fade().apply { duration = 600 }
        firstChild.exitTransition = Slide(Gravity.BOTTOM).apply { duration = 300 }
        childFragmentManager.beginTransaction()
            .add(R.id.child_container, firstChild, tag).addToBackStack(tag)
            .commit()
    }
}