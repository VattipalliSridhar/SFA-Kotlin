package com.apps.sfaapp.view.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apps.sfaapp.databinding.AdapterChecklistLayoutBinding
import com.apps.sfaapp.model.CheckListModel
import com.apps.sfaapp.view.ui.activities.ToiletActivity

class RecyclerCheckAdapter(
    private val activity: Context,
    val arrayList: ArrayList<CheckListModel.CheckList>,
    var toiletFragment: ToiletActivity
) : RecyclerView.Adapter<RecyclerCheckAdapter.MyHolderClass>() {


    inner class MyHolderClass(var adapterChecklistLayoutBinding: AdapterChecklistLayoutBinding) :
        RecyclerView.ViewHolder(adapterChecklistLayoutBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderClass {
        val view = AdapterChecklistLayoutBinding.inflate(
            LayoutInflater.from(activity),
            parent,
            false
        )
        return MyHolderClass(view)

    }

    override fun onBindViewHolder(holder: MyHolderClass, position: Int) {


        holder.adapterChecklistLayoutBinding.checkboxTxt.text = arrayList[position].description
        holder.adapterChecklistLayoutBinding.checkBox.isChecked = arrayList[position].selected
        holder.adapterChecklistLayoutBinding.checkBox.tag = position

        holder.adapterChecklistLayoutBinding.checkBox.setOnClickListener {


            val pos = holder.adapterChecklistLayoutBinding.checkBox.getTag()

            if (arrayList.get(pos as Int).getSelected()) {
                arrayList.get(pos).setSelected(false)
            } else {
                arrayList.get(pos).setSelected(true)
            }
            toiletFragment.clickData();
        }


    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}