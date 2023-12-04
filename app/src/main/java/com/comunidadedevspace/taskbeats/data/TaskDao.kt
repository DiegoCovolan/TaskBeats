package com.comunidadedevspace.taskbeats.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

    @Query("Select * from task")
    fun getAll(): LiveData<List<Task>>


    //Update precisa encontrar a tarefa que queremos alterar

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(task: Task)

    //DETELE para deletar nós precisamos encontrar por ID

    //DELETE ALL
    @Query("DELETE from task")
    fun deleteAll()

    //DELETE pelo ID
    @Query("DELETE from task WHERE id=:id")
    fun deleteById(id: Int)

}