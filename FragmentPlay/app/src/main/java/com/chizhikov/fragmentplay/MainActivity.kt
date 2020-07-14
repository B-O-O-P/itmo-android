package com.chizhikov.fragmentplay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.chizhikov.fragmentplay.swipeListener.OnSwipeTouchListener
import com.chizhikov.fragmentplay.fragments.RootFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            switchTab(TAG_HOME)
        } else {
            val tabState = getTabTag(savedInstanceState.getInt(TAB_STATE))
            updateSelect(tabState)
            showLastChild(tabState)
        }

        nav_menu.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                showLastChild(getTabTag(tab!!.position))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                hidePage(getTabTag(tab!!.position))
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                switchTab(getTabTag(tab!!.position))
            }
        })

        fragment_container.setOnTouchListener(object : OnSwipeTouchListener(this) {
            override fun onSwipeRight() {
                val currentPosition = nav_menu.selectedTabPosition
                if (currentPosition > 0) {
                    switchTab(getTabTag(currentPosition - 1))
                }
            }

            override fun onSwipeLeft() {
                val currentPosition = nav_menu.selectedTabPosition
                if (currentPosition < 2) {
                    switchTab(getTabTag(currentPosition + 1))
                }
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(TAB_STATE, nav_menu.selectedTabPosition)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            val currentTag = getTabTag(nav_menu.selectedTabPosition)
            val currentRoot = getRootFragment(currentTag)
            if (currentRoot != null) {
                currentRoot.childFragmentManager.popBackStackImmediate()
                if (currentRoot.childFragmentManager.backStackEntryCount == 0) {
                    currentRoot.fragmentManager?.popBackStackImmediate(
                        currentTag,
                        POP_BACK_STACK_INCLUSIVE
                    )
                    val lastTag = getLastBackStackTag()
                    if (lastTag != NO_TAB && lastTag != null) {
                        switchTab(lastTag)
                    } else {
                        finish()
                    }
                }
            }
        }
    }


    private fun getLastBackStackTag(): String? {
        if (supportFragmentManager.backStackEntryCount == 0) {
            return NO_TAB
        } else {
            val lastTag =
                supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1)
                    .name ?: return NO_TAB
            return if (supportFragmentManager.findFragmentByTag(lastTag)?.childFragmentManager!!.backStackEntryCount == 0) {
                supportFragmentManager.popBackStackImmediate(lastTag, POP_BACK_STACK_INCLUSIVE)
                getLastBackStackTag()
            } else {
                lastTag
            }

        }
    }

    private fun getRootFragment(tab: String): Fragment? {
        return supportFragmentManager.findFragmentByTag(tab)
    }

    private fun switchTab(tag: String) {
        showPage(tag)
        updateSelect(tag)
    }

    private fun showPage(tag: String) {
        title = getTitle(tag)
        if (getRootFragment(tag) == null || getRootFragment(tag)!!.childFragmentManager.backStackEntryCount == 0) {
            val newRoot = RootFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, newRoot, tag).addToBackStack(tag).commit()
            supportFragmentManager.executePendingTransactions()
            newRoot.addFirstChild()
        } else {
            getRootFragment(tag)?.apply {
                fragmentManager?.beginTransaction()?.addToBackStack(tag)?.show(this)?.commit()
                showLastChild(tag)
            }
        }
    }

    private fun showLastChild(tag: String) {
        getRootFragment(tag)?.apply {
            val latestChild = childFragmentManager.findFragmentByTag(tag)
            if (latestChild != null) {
                childFragmentManager.beginTransaction()
                    .show(latestChild).commitNow()
            }
        }
    }

    private fun updateSelect(tab: String) {
        val position = getTabPosition(tab)
        for (i in 0 until 3) {
            nav_menu.getTabAt(i)?.apply {
                if (i == position) {
                    select()
                }
            }
        }
    }

    private fun hidePage(tag: String) {
        getRootFragment(tag)?.apply {
            val latestChild = childFragmentManager.findFragmentByTag(tag)
            if (latestChild != null) {
                childFragmentManager.beginTransaction()
                    .hide(latestChild).commitNow()
            }
        }
    }

    private fun getTabTag(position: Int): String {
        return when (position) {
            0 -> TAG_HOME
            1 -> TAG_DASHBOARD
            2 -> TAG_NOTIFICATIONS
            else -> NO_TAB
        }
    }

    private fun getTabPosition(tag: String): Int {
        return when (tag) {
            TAG_HOME -> 0
            TAG_DASHBOARD -> 1
            TAG_NOTIFICATIONS -> 2
            else -> 0
        }
    }

    private fun getTitle(tag: String) : String {
        return when (tag) {
            TAG_HOME -> "Home"
            TAG_DASHBOARD -> "Dashboard"
            TAG_NOTIFICATIONS -> "Notifications"
            else -> "NO TAB?"
        }
    }

    companion object {
        const val NO_TAB = "none"
        const val TAG_HOME = "home"
        const val TAG_DASHBOARD = "dashboard"
        const val TAG_NOTIFICATIONS = "notifications"
        const val TAB_STATE = "tab"
    }
}
