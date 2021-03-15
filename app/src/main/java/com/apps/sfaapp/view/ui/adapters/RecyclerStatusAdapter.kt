package com.apps.sfaapp.view.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.apps.sfaapp.R
import com.apps.sfaapp.databinding.AdapterStatusLayoutBinding
import com.apps.sfaapp.model.StatusModel
import com.apps.sfaapp.view.ui.activities.MainActivity
import com.apps.sfaapp.view.ui.fragments.StatusFragment

class RecyclerStatusAdapter(
    private val mainActivity: MainActivity,
    private val statusList: ArrayList<StatusModel.Binlist>,
    private val statusFragment: StatusFragment
) : RecyclerView.Adapter<RecyclerStatusAdapter.MyHolderClass>() {


    inner class MyHolderClass(var adapterStatusLayoutBinding: AdapterStatusLayoutBinding) :
        RecyclerView.ViewHolder(adapterStatusLayoutBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderClass {
        val view = AdapterStatusLayoutBinding.inflate(
            LayoutInflater.from(mainActivity),
            parent,
            false
        )
        return MyHolderClass(view)
    }

    override fun onBindViewHolder(holder: MyHolderClass, position: Int) {


        if (statusList[position].catType.equals("Toilet")) {
            holder.adapterStatusLayoutBinding.catTypeName.text =
                "Toilet Name : " + statusList[position].binName
        } else {
            holder.adapterStatusLayoutBinding.catTypeName.text =
                "Property Name : " + statusList[position].binName
        }
        holder.adapterStatusLayoutBinding.dateTimeTxt.text = "Date & Time : "+statusList[position].datetime
        if(statusList[position].isToiletCleaned.equals("No"))
        {
            holder.adapterStatusLayoutBinding.statusImg.setImageResource(R.drawable.ic_close)
        }else{
            holder.adapterStatusLayoutBinding.statusImg.setImageResource(R.drawable.ic_done)
        }

        if(statusList[position].cleanStatus.equals("2"))
        {
            holder.adapterStatusLayoutBinding.reasonTxt.text = "Reason for "+statusList[position].selChecklist
        }else{

        }
    }

    override fun getItemCount(): Int {
        return statusList.size
    }
}