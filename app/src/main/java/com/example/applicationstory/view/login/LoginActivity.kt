package com.example.applicationstory.view.login

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
import com.example.applicationstory.data.pref.UserModel
import com.example.applicationstory.data.pref.UserPreference
import com.example.applicationstory.data.pref.dataStore
import com.example.applicationstory.data.response.LoginResponse
import com.example.applicationstory.databinding.ActivityLoginBinding
import com.example.applicationstory.view.ViewModelFactory
import com.example.applicationstory.view.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this, UserPreference.getInstance(dataStore))
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun setupAction() {

        binding.loginButton.setOnClickListener {
            showLoading(true)
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.login(email, password).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        showLoading(false)
                        if (loginResponse != null && !loginResponse.error) {
                            val loginResult = loginResponse.loginResult!!
                            val user = UserModel(loginResult.userId, loginResult.name, loginResult.token)
                            viewModel.saveSession(user)
                            AlertDialog.Builder(this@LoginActivity).apply {
                                setTitle(R.string.yeah)
                                setMessage(R.string.berhasil_login)
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                create()
                                show()
                                Toast.makeText(this@LoginActivity, R.string.berhasil_login, Toast.LENGTH_SHORT).show()
                                showLoading(false)
                            }
                        } else {
                            AlertDialog.Builder(this@LoginActivity).apply {
                                setTitle(R.string.gagal_login)
                                setMessage(R.string.username_password_salah)
                                setPositiveButton(R.string.oke) { _, _ ->
                                }
                                create()
                                show()
                            }
                            Toast.makeText(this@LoginActivity, R.string.gagal_login, Toast.LENGTH_SHORT).show()
                            showLoading(false)
                        }
                    } else {
                        AlertDialog.Builder(this@LoginActivity).apply {
                            setTitle(R.string.gagal_login)
                            setMessage(R.string.username_password_salah)
                            setPositiveButton(R.string.oke) { _, _ ->
                            }
                            create()
                            show()
                        }
                        showLoading(false)
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    AlertDialog.Builder(this@LoginActivity).apply {
                        setTitle(R.string.gagal_login)
                        setMessage(R.string.gagal_memuat_data)
                        setPositiveButton(R.string.oke) { _, _ ->
                        }
                        create()
                        show()
                    }
                    showLoading(false)
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

            val text =
                ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
            val message =
                ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
            val email =
                ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
            val emailEdit =
                ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
            val password =
                ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
            val passwordEdit =
                ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f)
                    .setDuration(100)
            val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

            val together = AnimatorSet().apply {
                playTogether(login)
            }

            AnimatorSet().apply {
                playSequentially(text, message, email, emailEdit, password, passwordEdit, together)
                start()
            }
        }
}