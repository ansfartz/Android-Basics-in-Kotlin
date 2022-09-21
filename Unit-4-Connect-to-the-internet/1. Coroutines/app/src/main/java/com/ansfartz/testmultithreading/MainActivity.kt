package com.ansfartz.testmultithreading

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ansfartz.testmultithreading.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)

        // Option 1:
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainActivity = this

        // Option 2:
        // binding = ActivityMainBinding.inflate(layoutInflater)
        // setContentView(binding.root)

    }

    fun doLoopThread() {
        Log.d("ANDYYY", "doLoopThread")
        var count = 0
        for (i in 1..50) {
            Thread {
                count += 1
                Log.d("ANDYYY", "Thread: $i count: $count")
            }.start()
        }
    }

    /**
     * Job - A cancelable unit of work, such as one created with the launch() function
     * CoroutineScope - Functions used to create new coroutines such as launch() and async() extend CoroutineScope
     * Dispatcher - Determines the thread the coroutine will use.
     *              The Main dispatcher will always run coroutines on the main thread, while
     *              dispatchers like Default, IO, or Unconfined will use other threads
     *
     * Dispatchers are one of the ways coroutines can be so performant. One avoids the performance cost of initializing new threads.
     *
     * The launch() function creates a coroutine from the enclosed code wrapped in a cancelable Job object.
     *     launch() is used when a return value is not needed outside the confines of the coroutine.
     * Behind the scenes, the block of code you passed to launch(..., block) is marked with the suspend keyword.
     *     Suspend signals that a block of code or function can be paused or resumed.
     */
    fun doCoroutines() {
        Log.d("ANDYYY", "doCoroutines")
        repeat(3) {
            GlobalScope.launch {
                Log.d("ANDYYY", "Hi from ${Thread.currentThread()}")
            }

        }
    }


    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME
    val time = { formatter.format(LocalDateTime.now()) }
    suspend fun getValue(): Double {
        Log.d("ANDYYY", "entering getValue() at ${time()}")
        delay(3000)
        Log.d("ANDYYY", "leaving getValue() at ${time()}")
        return Math.random()
    }

    /**
     * runBlocking - starts a new coroutine and blocks the current thread until completion.
     *               It is mainly used to bridge between blocking and non-blocking code in main functions and tests.
     *               You will not be using it often in typical Android code.
     */
    fun doRunBlockingCoroutines() {
        Log.d("ANDYYY", "doRunBlockingCoroutines")
        runBlocking {
            val num1 = getValue()
            val num2 = getValue()
            Log.d("ANDYYY", "result of num1 + num2 is ${num1 + num2}")
        }
    }

    /**
     * The two calls to getValue() are independent and don't necessarily need the coroutine to suspend.
     * Kotlin has an **async** function that's similar to **launch**.
     *
     * async() - returns a value of type Deferred. A Deferred is a cancelable Job that can hold a reference to a future value.
     *           By using a Deferred, you can still call a function as if it immediately returns a value.
     *           A Deferred just serves as a placeholder, since you can't be certain when an asynchronous task will return.
     *
     * A Deferred (also called a Promise or Future in other languages) guarantees that a value will be returned to this object at a later time.
     * An asynchronous task, on the other hand, will not block or wait for execution by default.
     * To initiate that the current line of code needs to wait for the output of a Deferred, you can call await() on it. It will return the raw value.
     */
    fun doRunBlockingCoroutinesAsync() {
        Log.d("ANDYYY", "doRunBlockingCoroutinesAsync")
        runBlocking {
            val num1 = async { getValue() }
            val num2 = async { getValue() }
            Log.d("ANDYYY", "result of num1 + num2 is ${num1.await() + num2.await()}")
        }
    }


    fun testMethod() {
        val states = arrayOf("Starting", "Doing Task1", "Doing Task2", "Ending")

        // Threads version
        // repeat(3) {
        //     Thread {
        //         Log.d("ANDYYY", "${Thread.currentThread()} has started")
        //         for (i in states) {
        //             Log.d("ANDYYY", "${Thread.currentThread()} - $i")
        //             Thread.sleep(50)
        //         }
        //     }.start()
        // }

        // Coroutines version
        repeat(3) {
            GlobalScope.launch {
                Log.d("ANDYYY", "${Thread.currentThread()} has started")
                for (i in states) {
                    Log.d("ANDYYY", "${Thread.currentThread()} - $i")
                }

            }
        }

    }


}