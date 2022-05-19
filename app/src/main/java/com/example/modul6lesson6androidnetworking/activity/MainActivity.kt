package com.example.modul6lesson6androidnetworking.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.modul6lesson6androidnetworking.R
import com.example.modul6lesson6androidnetworking.adapter.PostAdapter
import com.example.modul6lesson6androidnetworking.databinding.ActivityMainBinding
import com.example.modul6lesson6androidnetworking.model.Post
import com.example.modul6lesson6androidnetworking.model.PostResponse
import com.example.modul6lesson6androidnetworking.retrofit.RetrofitHttp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"
    private lateinit var postList : ArrayList<Post>
    private lateinit var postAdapter: PostAdapter
    private lateinit var context: Context
    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this
        postList = ArrayList()
        initViews()
    }

    private fun initViews() {
        apiRetrofitList()

        binding.btnFloating.setOnClickListener {
            addPost()
        }
    }

    private fun addPost() {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_log_out)

        val etTitle : EditText = dialog.findViewById(R.id.et_title)
        val etBody : EditText = dialog.findViewById(R.id.et_body)
        val buttonYes: Button = dialog.findViewById(R.id.btn_yes)
        val buttonNo: Button = dialog.findViewById(R.id.btn_no)

        buttonYes.setOnClickListener{
            val post = Post(id, id, etTitle.text.toString().trim(), etBody.text.toString().trim())
            id++
            apiRetrofitCreate(post)
            dialog.dismiss()
        }

        buttonNo.setOnClickListener{
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(getDrawable(R.drawable.ic_launcher_foreground))
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.show()

    }

    private fun apiRetrofitList() {
        showProgressBar()
        RetrofitHttp.posterService.listPost()
            .enqueue(object : Callback<ArrayList<PostResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<PostResponse>>,
                    response: Response<ArrayList<PostResponse>>
                ) {
                    postList.clear()
                    val items = response.body()
                    if (items != null) {
                        for (item in items){
                            val post = Post(item.userId, item.id, item.title!!, item.body!!)
                            postList.add(post)
                        }
                    }
                    refreshAdapter(postList)
                }

                override fun onFailure(
                    call: Call<ArrayList<PostResponse>>,
                    t: Throwable
                ) {
                    Log.e(TAG, t.message.toString())
                }
            })

    }

    private fun apiRetrofitDelete(post: Post) {
        RetrofitHttp.posterService.deletePost(post.id).enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                Log.d(TAG, response.body().toString())
                apiRetrofitList()
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }
        })
    }

    private fun apiRetrofitUpdate(post: Post) {
        RetrofitHttp.posterService.updatePost(post.id, post)
            .enqueue(object : Callback<PostResponse> {
                override fun onResponse(
                    call: Call<PostResponse>,
                    response: Response<PostResponse>
                ) {
                    Log.d(TAG, response.body().toString())

                }

                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    Log.e(TAG, t.message.toString())
                }
            })

    }

    private fun apiRetrofitCreate(post: Post) {
        RetrofitHttp.posterService.createPost(post).enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                Log.d(TAG, response.body().toString())
                val item = response.body()
                val post : Post = Post(item!!.userId, item.id, item.title!!, item.body!!)

                postList.add(post)
                postAdapter.notifyItemInserted(postList.size)
                Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())

                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun refreshAdapter(postList: ArrayList<Post>) {
        postAdapter = PostAdapter(context as MainActivity, postList)
        binding.recyclerView.adapter = postAdapter
        hideProgressBar()
    }

    private fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        binding.progressBar.visibility = View.GONE
    }

    fun dialogPost(post: Post?){
        val builder = AlertDialog.Builder(context)
            .setTitle("Delete Post")
            .setMessage("Are you sure you want to delete this post?")
            .setPositiveButton(android.R.string.yes){ dialog, which->
//                apiVolleyDelete(post!!)
                apiRetrofitDelete(post!!)
            }
            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
        builder.show()
    }

}