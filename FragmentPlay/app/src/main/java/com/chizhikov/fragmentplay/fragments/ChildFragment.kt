package com.chizhikov.fragmentplay.fragments

import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chizhikov.fragmentplay.MainActivity.Companion.TAG_DASHBOARD
import com.chizhikov.fragmentplay.MainActivity.Companion.TAG_HOME
import com.chizhikov.fragmentplay.MainActivity.Companion.TAG_NOTIFICATIONS
import com.chizhikov.fragmentplay.R
import kotlinx.android.synthetic.main.child_fragment.*


class ChildFragment : Fragment() {
    private lateinit var depth: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        depth = arguments?.getString(KEY_TXT_DEPTH) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.child_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val text = "$tag: $depth"
        txt_depth.text = text

        btn_next.setOnClickListener {
            val newFragment =
                newInstance(
                    fragmentManager?.backStackEntryCount.toString()
                )
            val gravityIn = when (tag){
                TAG_HOME -> Gravity.START
                TAG_DASHBOARD -> Gravity.TOP
                TAG_NOTIFICATIONS -> Gravity.END
                else -> Gravity.BOTTOM
            }
            newFragment.enterTransition = Slide(gravityIn).apply {
                duration = 600
            }
            newFragment.exitTransition = Slide(Gravity.BOTTOM).apply { duration = 300 }

            fragmentManager?.beginTransaction()?.add(R.id.child_container, newFragment, tag)
                ?.hide(this)?.addToBackStack(tag)?.commit()
        }
    }

    companion object {
        private const val KEY_TXT_DEPTH = "depth"

        fun newInstance(depth: String?): ChildFragment {
            return ChildFragment().apply {
                arguments = Bundle(1).apply {
                    putString(KEY_TXT_DEPTH, depth)
                }
            }
        }
    }
}