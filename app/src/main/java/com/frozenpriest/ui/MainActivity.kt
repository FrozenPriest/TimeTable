package com.frozenpriest.ui

import android.os.Bundle
import com.frozenpriest.R
import com.frozenpriest.ui.common.BaseActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }
}
