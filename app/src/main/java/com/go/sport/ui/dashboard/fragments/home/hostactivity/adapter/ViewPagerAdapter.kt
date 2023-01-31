package com.go.sport.ui.dashboard.fragments.home.hostactivity.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager, behavior: Int) :
    FragmentPagerAdapter(fm, behavior) {

    private val fragments: ArrayList<Fragment> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    fun addFragment(index: Int, fragment: Fragment?) {
        fragments.add(index, fragment!!)
    }
}