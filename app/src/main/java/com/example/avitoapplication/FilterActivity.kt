package com.example.avitoapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.avitoapplication.adapters.SwitchViewAdapter
import kotlinx.android.synthetic.main.activity_filter.*


class FilterActivity : AppCompatActivity() {
    private val RESULT_CODE = 5
    private lateinit var services: HashMap<String, Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        try {
            services = intent.getSerializableExtra("services") as HashMap<String, Boolean>
            val layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter =
                SwitchViewAdapter(services.toList() as ArrayList<Pair<String, Boolean>>)
            val dividerItemDecoration = DividerItemDecoration(
                recyclerView.context,
                layoutManager.orientation
            )
            recyclerView.addItemDecoration(dividerItemDecoration)
        } catch (e: Exception) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        val adapter = recyclerView.adapter as SwitchViewAdapter
        intent.putExtra("services", adapter.data.toMap() as LinkedHashMap<String, Boolean>)
        setResult(RESULT_CODE, intent)
        super.onBackPressed()
    }
}
