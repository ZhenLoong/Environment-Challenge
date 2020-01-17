package com.example.environmentchallenge.ui.friends

import android.widget.CompoundButton
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior.setTag
import android.content.Context
import android.widget.CheckBox
import android.widget.TextView
import android.view.ViewGroup
import java.nio.file.Files.size
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.widget.BaseAdapter
import com.example.environmentchallenge.R
import java.nio.file.Files.size


class MultiSelectionAdapter<T>(internal var mContext: Context, list: ArrayList<T>) : BaseAdapter() {
    internal var mInflater: LayoutInflater
    internal var mList: ArrayList<T>
    internal var mSparseBooleanArray: SparseBooleanArray

    init {
        mInflater = LayoutInflater.from(mContext)
        mSparseBooleanArray = SparseBooleanArray()
        mList = ArrayList()
        this.mList = list
    }// TODO Auto-generated constructor stub

    val checkedItems: ArrayList<T>
        get() {
            val mTempArry = ArrayList<T>()
            for (i in 0 until mList.size) {
                if (mSparseBooleanArray.get(i)) {
                    mTempArry.add(mList[i])
                }
            }
            return mTempArry


        }

    internal var mCheckedChangeListener: CompoundButton.OnCheckedChangeListener = object : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
            // TODO Auto-generated method stub
            mSparseBooleanArray.put(buttonView.tag as Int, isChecked)
        }
    }

    override fun getCount(): Int {
        // TODO Auto-generated method stub
        return mList.size
    }

    override fun getItem(position: Int): T {
        // TODO Auto-generated method stub
        return mList[position]
    }

    override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return position.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row, null)
        }

        val tvTitle = convertView!!.findViewById(R.id.title) as TextView
        tvTitle.text = mList[position].toString()
        val mCheckBox = convertView!!.findViewById(R.id.checkbox) as CheckBox
        mCheckBox.tag = position
        mCheckBox.isChecked = mSparseBooleanArray.get(position)
        mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener)
        return convertView
    }


}