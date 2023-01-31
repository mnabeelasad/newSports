package com.go.sport.ui.signup.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.constants.Constants
import com.go.sport.ui.signup.SignUpActivity
import com.go.sport.ui.signup.SignUpSingleton
import kotlinx.android.synthetic.main.fragment_email.view.*

/**
 * A simple [Fragment] subclass.
 */
class EmailFragment : Fragment() {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_email, container, false)

        rootView.et_email.isFocusable = (requireActivity() as SignUpActivity).emailAddress.isEmpty()
        rootView.et_email.setText((requireActivity() as SignUpActivity).emailAddress)

        setFonts()

        return rootView
    }

    private fun setFonts() {
        (activity as BaseActivity).setFont(Constants.REGULAR, rootView.til_email)
    }

    fun initEmailValidation(): Boolean {
        val email = rootView.et_email.text.toString()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            rootView.et_email.requestFocus()
            (requireActivity() as BaseActivity).warningToast("Invalid Email Address")
            return false
        }

        SignUpSingleton.emailAddress = email

        return true
    }
}