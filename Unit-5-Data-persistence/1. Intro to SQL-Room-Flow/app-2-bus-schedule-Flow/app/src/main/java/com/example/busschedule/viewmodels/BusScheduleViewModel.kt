package com.example.busschedule.viewmodels

import androidx.lifecycle.ViewModel
import com.example.busschedule.database.schedule.Schedule
import com.example.busschedule.database.schedule.ScheduleDao
import kotlinx.coroutines.flow.Flow

class BusScheduleViewModel(private val scheduleDao: ScheduleDao): ViewModel() {

    fun fullSchedule(): Flow<List<Schedule>> {
      return scheduleDao.getAll()
    }

    fun scheduleForStopName(name: String): Flow<List<Schedule>> {
        return scheduleDao.getByStopName(name)
    }

}

