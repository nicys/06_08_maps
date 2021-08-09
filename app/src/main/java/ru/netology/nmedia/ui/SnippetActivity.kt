package ru.netology.nmedia.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityNewSnippetBinding


class SnippetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewSnippetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val myFragment = MapsFragment()
        binding.edit.requestFocus()
        val ok = binding.ok
        val edit = binding.edit
        ok?.setOnClickListener{
            val newSnippet = Bundle()
            newSnippet.putString("message", edit?.text.toString())
            myFragment.arguments = newSnippet


            val snippet = binding.edit.text.toString()
            intent.putExtra("newSnippet", snippet)
            setResult(Activity.RESULT_OK, intent)
            finish()

//            fragmentTransaction.add(R.id.frameLayout, MapsFragment()).commit()
        }
    }
}

//binding.ok.setOnClickListener {
//    if (binding.edit.text.isNullOrBlank()) {
//        setResult(Activity.RESULT_CANCELED, intent)
//    } else {
//        val snippet = binding.edit.text.toString()
//        intent.putExtra("newSnippet", snippet)
//        Log.i("TAG", "intent.putExtra")
//        setResult(Activity.RESULT_OK, intent)
//    }
//    finish()
//}