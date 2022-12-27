package com.example.myapplication

import android.app.Service
import android.content.Intent
import android.os.IBinder

class MyService : Service() {
    private var channel = ""
    private lateinit var thread: Thread
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //解析 Intent 取得字串訊息
        intent?.extras?.let {
            channel = it.getString("channel", "")
        }
        broadcast(
            when(channel) {
                "music" -> "歡迎來到音樂頻道"
                "new" -> "歡迎來到新聞頻道"
                "sport" -> "歡迎來到體育頻道"
                else -> "頻道錯誤"
            }
        )

        if (::thread.isInitialized && thread.isAlive)
            thread.interrupt()
        thread = Thread {
            try {
                Thread.sleep(3000) //延遲三秒
                broadcast(
                    when(channel) {
                        "music" -> "即將播放本月 TOP10 音樂"
                        "new" -> "即將為您提供獨家新聞"
                        "sport" -> "即將播報本週 NBA 賽事"
                        else -> "頻道錯誤"
                    }
                )
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        thread.start()
        return START_STICKY
    }
    override fun onBind(intent: Intent): IBinder? = null

    private fun broadcast(msg: String) =
        sendBroadcast(Intent(channel).putExtra("msg", msg))
}