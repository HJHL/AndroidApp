package me.lijiahui.androidapp.demo.serialization

import androidx.room.Room
import me.lijiahui.androidapp.MyApplication

object DatabaseHelper {
    val userDb by lazy {
        Room.databaseBuilder(MyApplication.app, UserDatabase::class.java, "user")
            .build()
    }
}