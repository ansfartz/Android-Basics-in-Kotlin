package com.ansfartz.playwithworkmanager.worker

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ansfartz.playwithworkmanager.RobotAssemblerViewModel
import com.ansfartz.playwithworkmanager.RobotAssemblerViewModel.Companion.WORKER_DATA_KEY

class AddLegsWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {
        Log.d("ANDYYY AddLegsWorker", "adding Legs")
        Thread.sleep(1000)

        var currentRobotParts = inputData.getString(WORKER_DATA_KEY)
        currentRobotParts += " | LEGS"

        val data = Data.Builder()
            .putString(WORKER_DATA_KEY, currentRobotParts)
            .build()
        return Result.success(data)
    }

}