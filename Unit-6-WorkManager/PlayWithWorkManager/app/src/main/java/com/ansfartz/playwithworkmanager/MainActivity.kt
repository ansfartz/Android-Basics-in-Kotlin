package com.ansfartz.playwithworkmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import com.ansfartz.playwithworkmanager.RobotAssemblerViewModel.Companion.WORKER_DATA_KEY
import com.ansfartz.playwithworkmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewModel: RobotAssemblerViewModel by viewModels {
        RobotAssemblerViewModel.RobotAssemblerViewModelFactory(application)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        // setContentView(R.layout.activity_main)
        setContentView(binding.root)

        binding.buildRobotButton.setOnClickListener {
            viewModel.assemble(
                binding.armsBtn.isChecked,
                binding.legsBtn.isChecked,
                binding.headBtn.isChecked,)
        }

        viewModel.frameWorkInfo.observe(this, workInfoObserver("frameWorkInfo"))
        viewModel.armsWorkInfo.observe(this, workInfoObserver("armsWorkInfo"))
        viewModel.legsWorkInfo.observe(this, workInfoObserver("legsWorkInfo"))
        viewModel.headWorkInfo.observe(this, workInfoObserver("headWorkInfo"))
    }

    private fun workInfoObserver(source: String): Observer<List<WorkInfo>> {

        val observer: Observer<List<WorkInfo>> = object : Observer<List<WorkInfo>> {
            override fun onChanged(workInfoList: List<WorkInfo>?) {
                if (workInfoList.isNullOrEmpty()) {
                    return
                }

                val workInfo = workInfoList[0]
                if (workInfo.state.isFinished) {
                    val data = workInfo.outputData.getString(WORKER_DATA_KEY)
                    Log.d("ANDYYY $source-Observer", "onChanged: data = $data")
                    updateStatusText(data)
                }

            }
        }

        return observer
    }

    private fun updateStatusText(data: String?) {
        val text = "${binding.statusText.text}\n$data"
        binding.statusText.text = text
    }
}