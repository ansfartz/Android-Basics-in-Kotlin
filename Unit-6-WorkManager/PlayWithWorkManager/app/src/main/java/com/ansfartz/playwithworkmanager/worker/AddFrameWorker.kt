package com.ansfartz.playwithworkmanager.worker

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ansfartz.playwithworkmanager.RobotAssemblerViewModel.Companion.WORKER_DATA_KEY

class AddFrameWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {
        Log.d("ANDYYY AddFrameWorker", "adding Frame")
        Thread.sleep(1000)

        val data = Data.Builder()
            .putString(WORKER_DATA_KEY, "| FRAME")
            .build()
        return Result.success(data)
    }

}