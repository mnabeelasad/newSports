package com.go.sport.ui.dashboard.train.trainsingleitem

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.academies.AcademiesAcadmy
import com.go.sport.singleton.Singleton
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_train_item.*
import kotlinx.android.synthetic.main.row_train.*
import java.util.concurrent.TimeUnit

class TrainItemActivity : BaseActivity() {

    private var academy: AcademiesAcadmy? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train_item)

        academy = Singleton.academy
        initViews()
        listeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        Singleton.academy = null
    }

    private fun initViews() {
        academy?.let {
            val circularProgressDrawable = CircularProgressDrawable(this)
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            Glide.with(this).load(it.image).placeholder(circularProgressDrawable)
                .into(iv_sports)
            tv_name.text = it.name
            tv_desc.text = it.description
            tv_location.text = it.address
        }
    }

    @SuppressLint("CheckResult")
    private fun listeners() {
        RxView.clicks(iv_back).subscribe {
            super.onBackPressed()
        }

        RxView.clicks(iv_facebook).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            openBrowser(academy?.facebook ?: "")
        }

        RxView.clicks(iv_google).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            openBrowser(academy?.google ?: "")
        }

        RxView.clicks(iv_whatsapp).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            openWhatsApp(academy?.watsapp ?: "")
        }

        RxView.clicks(cont_share).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            shareAppExternally("I have found this academy ${academy!!.name} on Goplay")
        }
    }
}