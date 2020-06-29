package com.chase.kotlincoroutines

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chase.kotlincoroutines.adapters.ToDoAdapter
import com.chase.kotlincoroutines.model.Data
import com.chase.kotlincoroutines.network.RetrofitFactory
import com.kotlin.myapplication.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.jetbrains.anko.toast
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            CoroutineScope(Dispatchers.Default).launch {
                callNewAPI()
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
        failedParent.visibility = View.GONE
        recyclerview.visibility = View.VISIBLE

        buttonRetry.setOnClickListener({ v -> (callNewAPI()) })
    }

    private fun initRecyclerView(list: List<Data>) {
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = ToDoAdapter(list, this)
    }

    fun callNewAPI(){
        GlobalScope.launch(Dispatchers.Main) {
            val service =  RetrofitFactory.makeRetrofitService()
            try {
                val res = withContext(Dispatchers.IO) {
                    service.getPosts()
                }
                res.body()?.let { initRecyclerView(it) }
                failedParent.visibility = View.GONE
                recyclerview.visibility = View.VISIBLE
            }
            catch (e: Exception) {
                failedParent.visibility = View.VISIBLE
                recyclerview.visibility = View.GONE
                toast("Error network operation failed with ${e}")
            }
        }
    }
}
