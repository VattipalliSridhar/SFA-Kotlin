package com.apps.sfaapp.view.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.sfaapp.R
import com.apps.sfaapp.databinding.ActivityMainBinding
import com.apps.sfaapp.model.MenuModel
import com.apps.sfaapp.view.base.BaseActivity
import com.apps.sfaapp.view.base.SharedPreferConstant
import com.apps.sfaapp.view.ui.adapters.RecyclerMenuAdapter
import com.apps.sfaapp.view.ui.fragments.HomeFragment
import com.apps.sfaapp.view.ui.fragments.StatusFragment


class MainActivity : BaseActivity(),RecyclerMenuAdapter.OnItemClickListener {


    lateinit var binding: ActivityMainBinding
    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var recyclerMenuAdapter: RecyclerMenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()
        defaultFragment()


        val menuModel = ArrayList<MenuModel>()
        menuModel.add(MenuModel("Home", "Home"))
        menuModel.add(MenuModel("Status", "Status"))
        menuModel.add(MenuModel("Logout", "Logout"))

        //adding a layoutmanager
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.setHasFixedSize(true)
        recyclerMenuAdapter = RecyclerMenuAdapter(applicationContext, menuModel, this)
        binding.recyclerview.adapter = recyclerMenuAdapter







    }


    fun titleTextChange(title: String) {
        binding.title.text = title
    }

    private fun defaultFragment() {

        val homFrag = HomeFragment()
        putFragment(homFrag, SharedPreferConstant.DASH_FRG_TAG)
    }

    private fun putFragment(homFrag: Fragment?, dashFrgTag: String) {
        if (homFrag != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, homFrag, SharedPreferConstant.DASH_FRG_TAG)
            transaction.commit()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }


    }


    private fun removeAllFragmentsInBackStack() {
        for (i in 0 until fragmentManager.backStackEntryCount) {
            fragmentManager.popBackStack()
        }

    }




    override fun onItemClick(position: Int) {
        binding.drawerLayout.closeDrawers()

        if(position == 0)
        {
            this@MainActivity.viewModelStore.clear()
            removeAllFragmentsInBackStack()
            defaultFragment()
        }
        if(position == 1)
        {
            val fragment1: Fragment = StatusFragment()
            val beginTransaction1 = supportFragmentManager.beginTransaction()
            beginTransaction1.replace(R.id.frame_layout, fragment1)
            beginTransaction1.addToBackStack(null)
            beginTransaction1.commitAllowingStateLoss()
        }
        if(position == 2)
        {

        }
    }


    override fun onBackPressed() {

        this@MainActivity.viewModelStore.clear()
        val fm = supportFragmentManager
        if (fm.backStackEntryCount > 0) {
            Log.e("MainActivity", "popping backstack")
            fm.popBackStack()
        } else {
            //popupExist("Do you want exit from application?")
            finish()
        }

    }

    fun qrImg(s: String) {

        if(s == "Status")
        {
            binding.menuScan.visibility = View.GONE

        }else{
            binding.menuScan.visibility = View.VISIBLE
        }

    }


}