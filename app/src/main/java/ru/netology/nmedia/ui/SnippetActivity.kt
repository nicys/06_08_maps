package ru.netology.nmedia.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityNewSnippetBinding


class SnippetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewSnippetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edit.requestFocus()
        binding.ok.setOnClickListener {
            if (binding.edit.text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val snippet = binding.edit.text.toString()
                intent.putExtra("newSnippet", snippet)
                Log.i("TAG", "intent.putExtra")
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }
}
