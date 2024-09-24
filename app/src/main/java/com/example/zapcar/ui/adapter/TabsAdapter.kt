package com.example.zapcar.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.eletriccarapp.ui.FavoriteFragment
import com.example.zapcar.ui.CarFragment
import com.example.zapcar.ui.MainActivity

class TabsAdapter(
    fragment: MainActivity,
) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> CarFragment()
            1 -> FavoriteFragment()
            else -> CarFragment()
        }

    override fun getItemCount(): Int = 2
}
