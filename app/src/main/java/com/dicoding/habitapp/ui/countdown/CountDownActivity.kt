package com.dicoding.habitapp.ui.countdown

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit
import com.dicoding.habitapp.notification.NotificationWorker
import com.dicoding.habitapp.utils.HABIT
import com.dicoding.habitapp.utils.HABIT_ID
import com.dicoding.habitapp.utils.HABIT_TITLE
import com.dicoding.habitapp.utils.NOTIFICATION_CHANNEL_ID
import java.util.concurrent.TimeUnit

class CountDownActivity : AppCompatActivity() {
    private lateinit var workManager: WorkManager
    private lateinit var channelName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)
        supportActionBar?.title = "Count Down"

        workManager = WorkManager.getInstance(this)
        channelName = getString(R.string.notify_channel_name)

        val habit = intent.getParcelableExtra<Habit>(HABIT) as Habit
        val textCountDown = findViewById<TextView>(R.id.tv_count_down)
        findViewById<TextView>(R.id.tv_count_down_title).text = habit.title

        val viewModel = ViewModelProvider(this).get(CountDownViewModel::class.java)
        //TODO 10 : Set initial time and observe current time. Update button state when countdown is finished (DONE)
        viewModel.setInitialTime(habit.minutesFocus)
        viewModel.eventCountDownFinish.observe(this, Observer { isFinished ->
            updateButtonState(isFinished)
            if (isFinished) activateNotificationWorker(habit)
        })
        viewModel.currentTimeString.observe(this, Observer {
            textCountDown.text = it
        })
        //TODO 13 : Start and cancel One Time Request WorkManager to notify when time is up (DONE)

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            viewModel.startTimer()
        }

        findViewById<Button>(R.id.btn_stop).setOnClickListener {
            viewModel.resetTimer()
            cancelWork()
        }
    }

    private fun cancelWork() {
        workManager.cancelUniqueWork(channelName)
    }

    private fun activateNotificationWorker(habit: Habit) {
        val workerData = Data.Builder()
            .putInt(HABIT_ID, habit.id)
            .putString(HABIT_TITLE, habit.title)
            .build()

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(false)
            .build()

        val notificationRequest =
            OneTimeWorkRequestBuilder<NotificationWorker>().setConstraints(constraints)
                .setInputData(workerData)
                .build()

        workManager.enqueueUniqueWork(
            channelName,
            ExistingWorkPolicy.KEEP,
            notificationRequest
        )
    }

    private fun updateButtonState(isFinished: Boolean) {
        findViewById<Button>(R.id.btn_start).isEnabled = isFinished
        findViewById<Button>(R.id.btn_stop).isEnabled = !isFinished
    }
}