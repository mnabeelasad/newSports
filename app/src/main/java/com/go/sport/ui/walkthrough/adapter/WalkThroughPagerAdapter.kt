package com.go.sport.ui.walkthrough.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.go.sport.ui.walkthrough.fragment.WalkThroughFragment

class WalkThroughPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = WalkThroughFragment.getInstance(position)
    override fun getCount(): Int = 3
}