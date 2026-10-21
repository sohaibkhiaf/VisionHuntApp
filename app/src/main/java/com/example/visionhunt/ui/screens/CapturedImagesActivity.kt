package com.example.visionhunt.ui.screens

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.visionhunt.data.CapturedItemDbHelper
import com.example.visionhunt.databinding.ActivityCapturedImagesBinding
import com.example.visionhunt.ui.adapters.CapturedImagesAdapter

class CapturedImagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCapturedImagesBinding
    private lateinit var dbHelper: CapturedItemDbHelper
    private val adapter = CapturedImagesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCapturedImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = CapturedItemDbHelper(this)

        binding.toolbarTitle.text = "الصور المكتشفة"
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.rvCaptured.layoutManager = LinearLayoutManager(this)
        binding.rvCaptured.adapter = adapter

        loadCapturedImages()
    }

    private fun loadCapturedImages() {
        val items = dbHelper.getAllItems()
        adapter.submitList(items)
        if (items.isEmpty()) {
            binding.tvEmptyState.visibility = View.VISIBLE
            binding.rvCaptured.visibility = View.GONE
        } else {
            binding.tvEmptyState.visibility = View.GONE
            binding.rvCaptured.visibility = View.VISIBLE
        }
    }
}
