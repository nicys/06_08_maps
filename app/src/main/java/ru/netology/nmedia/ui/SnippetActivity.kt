package ru.netology.nmedia.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityNewSnippetBinding

class SnippetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewSnippetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.edit.requestFocus();
        binding.ok.setOnClickListener {
            val intent = Intent(this, MapsFragment::class.java)
            if (binding.edit.text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val snippet = binding.edit.text.toString()
                intent.putExtra("newSnippet", snippet)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }
}
