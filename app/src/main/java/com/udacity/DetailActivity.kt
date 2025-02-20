package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.OpenableColumns
import android.webkit.URLUtil
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import com.udacity.databinding.ActivityDetailBinding
import java.util.logging.Handler

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val notificationManager =
            ContextCompat.getSystemService(
                this,
                NotificationManager::class.java
            ) as NotificationManager
        notificationManager.cancelNotifications()

        infoById()

        binding.contentDetail.button.setOnClickListener() {
            val transferIntent = Intent(applicationContext, MainActivity::class.java)
            startActivity(transferIntent)
            finish()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

    }

    private fun infoById() {
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val id = intent.getLongExtra("id_extra", -1)
        if (id != -1L) {
            val query = DownloadManager.Query().setFilterById(id)
            val cursor = downloadManager.query(query)

            if (cursor.moveToFirst()) {
                val status =
                    cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                val fileUri =
                    cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI))
                val fileUriParsed = Uri.parse(fileUri)
                val title =
                    cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TITLE))


                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    binding.contentDetail.statusShowed.text = "SUCCESSFUL"
                } else if (status == DownloadManager.STATUS_FAILED) {
                    binding.contentDetail.statusShowed.text = "FAILED"
                } else if (status == DownloadManager.STATUS_PAUSED) {
                    binding.contentDetail.statusShowed.text = "PAUSED"
                } else if (status == DownloadManager.STATUS_PENDING) {
                    binding.contentDetail.statusShowed.text = "PENDING"
                } else if (status == DownloadManager.STATUS_RUNNING) {
                    binding.contentDetail.statusShowed.text = "RUNNING"
                }
                binding.contentDetail.fileNameChoosen.text = title
            }
            cursor.close()
        }
    }


}
