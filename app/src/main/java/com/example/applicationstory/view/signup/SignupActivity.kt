package com.example.applicationstory.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.applicationstory.R
import com.example.applicationstory.data.pref.UserPreference
import com.example.applicationstory.data.pref.dataStore
import com.example.applicationstory.data.response.RegisterResponse
import com.example.applicationstory.databinding.ActivitySignupBinding
import com.example.applicationstory.view.ViewModelFactory
import com.example.applicationstory.view.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    private val viewModel by viewModels<SignupViewModel>{
        ViewModelFactory.getInstance(this, UserPreference.getInstance(dataStore))
    }
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            showLoading(true)
            val email = binding.emailEditText.text.toString()
            val name = binding.nameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.register(name, email, password).enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful) {
                        val registerResponse = response.body()
                        if (registerResponse != null && !registerResponse.error) {
                            AlertDialog.Builder(this@SignupActivity).apply {
                                setTitle("Yeah!")
                                setMessage("Akun dengan $email sudah jadi nih. Yuk, login dan belajar coding.")
                                setPositiveButton(R.string.oke) { _, _ ->
                                    val intent = Intent(context, LoginActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                                showLoading(false)
                            }
                        } else {
                            showLoading(false)
                            Toast.makeText(this@SignupActivity, R.string.pendaftaran_gagal, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        AlertDialog.Builder(this@SignupActivity).apply {
                            setTitle(R.string.pendaftaran_gagal)
                            setMessage(R.string.periksa_kembali_data)
                            setPositiveButton(R.string.oke) { _, _ ->
                            }
                            create()
                            show()
                        }
                        showLoading(false)
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    showLoading(false)
                    AlertDialog.Builder(this@SignupActivity).apply {
                        setTitle(R.string.pendaftaran_gagal)
                        setMessage(R.string.gagal_memuat_data)
                        setPositiveButton(R.string.oke) { _, _ ->
                        }
                        create()
                        show()
                        showLoading(false)
                    }
                }
            })
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingSpinner.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val text = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEdit = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEdit = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val password = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEdit = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(signup)
        }

        AnimatorSet().apply {
            playSequentially(text, name, nameEdit, email, emailEdit, password, passwordEdit, together)
            start()
        }
    }
}