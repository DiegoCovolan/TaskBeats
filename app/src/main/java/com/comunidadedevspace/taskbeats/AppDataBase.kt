package com.comunidadedevspace.taskbeats

import androidx.room.Database
import androidx.room.RoomDatabase

//ESSA É A NOSSA TABLE


@Database(entities = [Task::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
