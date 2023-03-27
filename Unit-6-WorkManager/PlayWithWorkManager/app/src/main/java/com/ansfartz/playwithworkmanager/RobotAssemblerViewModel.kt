package com.ansfartz.playwithworkmanager

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.ansfartz.playwithworkmanager.worker.AddArmsWorker
import com.ansfartz.playwithworkmanager.worker.AddFrameWorker
import com.ansfartz.playwithworkmanager.worker.AddHeadWorker
import com.ansfartz.playwithworkmanager.worker.AddLegsWorker

class RobotAssemblerViewModel(application: Application) : ViewModel() {

    private val workManager = WorkManager.getInstance(application)
    val frameWorkInfo: LiveData<List<WorkInfo>> = workManager.getWorkInfosByTagLiveData(TAG_FRAME)
    val armsWorkInfo: LiveData<List<WorkInfo>> = workManager.getWorkInfosByTagLiveData(TAG_ARMS)
    val legsWorkInfo: LiveData<List<WorkInfo>> = workManager.getWorkInfosByTagLiveData(TAG_LEGS)
    val headWorkInfo: LiveData<List<WorkInfo>> = workManager.getWorkInfosByTagLiveData(TAG_HEAD)

    fun assemble(hasArms: Boolean,
                 hasLegs: Boolean,
                 hasHead: Boolean
    ) {
        val addFrameWorkerRequest = OneTimeWorkRequestBuilder<AddFrameWorker>()
            .addTag(TAG_FRAME)
            .build()
        val addArmsWorkerRequest = OneTimeWorkRequestBuilder<AddArmsWorker>()
            .addTag(TAG_ARMS)
            .build()
        val addLegsWorkerRequest = OneTimeWorkRequestBuilder<AddLegsWorker>()
            .addTag(TAG_LEGS)
            .build()
        val addHeadWorkerRequest = OneTimeWorkRequestBuilder<AddHeadWorker>()
            .addTag(TAG_HEAD)
            .build()

        var continuation = workManager.beginUniqueWork(
            "unique_work_name",
            ExistingWorkPolicy.REPLACE,
            addFrameWorkerRequest)

        if (hasArms) continuation = continuation.then(addArmsWorkerRequest)
        if (hasLegs) continuation = continuation.then(addLegsWorkerRequest)
        if (hasHead) continuation = continuation.then(addHeadWorkerRequest)

        continuation.enqueue()
    }

    class RobotAssemblerViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(RobotAssemblerViewModel::class.java)) {
                RobotAssemblerViewModel(application) as T
            } else {
                throw java.lang.IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    companion object {
        const val WORKER_DATA_KEY: String = "WORKER_DATA_KEY"

        const val TAG_FRAME: String = "TAG_FRAME"
        const val TAG_ARMS: String = "TAG_ARMS"
        const val TAG_LEGS: String = "TAG_LEGS"
        const val TAG_HEAD: String = "TAG_HEAD"
    }

}