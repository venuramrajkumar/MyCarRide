package com.raj.databindinglibrary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.raj.databindinglibrary.databinding.ActivityDataBindingDemoBinding

class DataBindingDemoActivity : AppCompatActivity() {

    val dataViewModel by lazy {
        ViewModelProvider(this).get(DataViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Remember this
        val databinding : ActivityDataBindingDemoBinding  = DataBindingUtil.setContentView(this,R.layout.activity_data_binding_demo)

        databinding.lifecycleOwner = this
        databinding.viewModelData = dataViewModel
    }


}