package com.hacktivators.mentalhealth.Chat.Bots

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.hacktivators.mentalhealth.Adapter.MessageAdapter
import com.hacktivators.mentalhealth.BackEnd.Service
import com.hacktivators.mentalhealth.Constants
import com.hacktivators.mentalhealth.Model.Message
import com.hacktivators.mentalhealth.R
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SarcasticActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var chat = null
    var welcomeTextView: TextView? = null
    var messageEditText: EditText? = null
    var sendButton: ImageView? = null
    var messageList: MutableList<Message>? = null
    var messageAdapter: MessageAdapter? = null
    var service: Service? = null
    var constants: Constants? = null
    private val mainThreadScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sarcastic)
        messageList = ArrayList()
        recyclerView = findViewById(R.id.recycler_view)
        welcomeTextView = findViewById(R.id.welcome_text)
        messageEditText = findViewById(R.id.message_edit_text)
        sendButton = findViewById(R.id.send_btn)
        constants = Constants()

        //setup recycler view
        messageAdapter = MessageAdapter(messageList)
        recyclerView?.setAdapter(messageAdapter)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        recyclerView?.setLayoutManager(layoutManager)
        sendButton?.setOnClickListener(View.OnClickListener { v: View? ->
            val question = messageEditText?.getText().toString().trim { it <= ' ' }
            addToChat(question, Message.SENT_BY_USER)
            messageEditText?.setText("")
            mainThreadScope.launch {
                runGemini(question)
            }
            welcomeTextView?.setVisibility(View.GONE)
        })
    }

    suspend fun runGemini(question: String) {
        val generativeModel = GenerativeModel(
            // The Gemini 1.5 models are versatile and work with most use cases
            modelName = "gemini-1.5-flash",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = "AIzaSyDFJKIK3lLXQkX5KVSrkYu-vS3_ejHpYv8",
            systemInstruction = content { text("This model will be used as a sarcastic chatbot to relieve stress in a mental-healthcare app.Be sarcastic without being mean.") }
        )
        val chat = generativeModel.startChat(
            history = listOf(
                content(role = "user") { text("hey") },
                content(role = "model") { text("Oh, hey there. Just taking a break from my intense schedule of existential pondering and existential dread. You know, the usual. What brings you to my corner of the internet? Did you just get out of bed or are you a professional at procrastination?") }
            )
        )
        val response = chat.sendMessage(question)
        println(response.text)
        addToChat(response.text, Message.SENT_BY_BOT)
    }

    fun addToChat(message: String?, sentBy: String?) {
        runOnUiThread {
            messageList!!.add(Message(message, sentBy))
            messageAdapter!!.notifyDataSetChanged()
            recyclerView!!.smoothScrollToPosition(messageAdapter!!.itemCount)
        }
    }
}