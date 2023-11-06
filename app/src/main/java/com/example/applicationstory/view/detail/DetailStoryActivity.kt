package com.example.applicationstory.view.detail

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.applicationstory.R
import com.example.applicationstory.databinding.ActivityDetailStoryBinding
import com.example.applicationstory.formatter.DateFormatter
import com.example.applicationstory.view.main.MainActivity
import kotlinx.coroutines.launch
import java.util.TimeZone

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var viewModel: DetailStoryViewModel
    private val dateFormatter = DateFormatter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val token = intent.getStringExtra(EXTRA_TOKEN)
        val id = intent.getStringExtra(EXTRA_ID)

        viewModel = ViewModelProvider(this).get(DetailStoryViewModel::class.java)

        if (token != null && id != null) {
            lifecycleScope.launch {
                viewModel.setDetailStory("$id", "Bearer $token")
                showLoading(true)
            }
        } else {
            AlertDialog.Builder(this@DetailStoryActivity).apply {
                setTitle(R.string.gagal_memuat)
                setMessage(R.string.gagal_memuat_data)
                setPositiveButton(R.string.oke) { _, _ ->
                }
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
                showLoading(false)
            }
        }


        viewModel.getDetailStory().observe(this, {
            if (it != null) {
                    binding.apply {
                        tvItemName.text = it.name
                        tvItemDescription.text = it.description
                        val formattedDate = dateFormatter.formatDate(it.createdAt, TimeZone.getDefault().id)
                        tvItemDate.text = formattedDate
                        Glide.with(this@DetailStoryActivity)
                            .load(it.photoUrl)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(tvItemPhoto)
                        showLoading(false)
                    }
            }
        })
        setupView()
        playAnimation()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingSpinner.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object{
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TOKEN = "extra_token"
    }

    private fun setupView() {
        supportActionBar?.apply {
            title = getString(R.string.detail_story)
            setDisplayHomeAsUpEnabled(true)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun playAnimation() {

        val photo = ObjectAnimator.ofFloat(binding.cardViewPhoto, View.ALPHA, 1f).setDuration(200)
        val name = ObjectAnimator.ofFloat(binding.cardViewName, View.ALPHA, 1f).setDuration(200)
        val description = ObjectAnimator.ofFloat(binding.cardViewDescription, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(photo, name, description)
            start()
        }
    }
}