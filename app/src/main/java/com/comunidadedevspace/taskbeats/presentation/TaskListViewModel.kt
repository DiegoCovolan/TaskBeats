package com.comunidadedevspace.taskbeats.presentation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ServiceLifecycleDispatcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comunidadedevspace.taskbeats.TaskBeatsApplication
import com.comunidadedevspace.taskbeats.data.Task
import com.comunidadedevspace.taskbeats.data.TaskDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskListViewModel(
    private val taskDao: TaskDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): ViewModel() {

    //Livedata no ViewModel
    val taskListLiveData: LiveData<List<Task>> = taskDao.getAll()

    //CRUD
    fun execute(taskAction: TaskAction){

        when (taskAction.actionType) {
            ActionType.DELETE.name -> deleteById(taskAction.task!!.id)
            ActionType.CREATE.name -> insertintoDataBase(taskAction.task!!)
            ActionType.UPDATE.name -> updateintoDataBase(taskAction.task!!)
            ActionType.DELETE_ALL.name -> deleteAll()

        }
        }

    //DELETE pelo ID
    private fun deleteById(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            ///CoroutineScope(Dispatchers.IO).launch {
            taskDao.deleteById(id)
            }

        }

    //CREATE
    private fun insertintoDataBase(task: Task){
        viewModelScope.launch (dispatcher){
            taskDao.insert(task)
        }
    }

    //UPDATE
    private fun updateintoDataBase(task: Task){
        viewModelScope.launch(dispatcher) {
            taskDao.update(task)
        }
    }

    //DELETE ALL
    private fun deleteAll(){
        viewModelScope.launch (dispatcher){
            taskDao.deleteAll()
            //   listFromDataBase() isso vai atualizar a lista no adapter, se não trabalhar em serie a thread pode não atualizar na hora
        }
    }


    companion object {

        fun create(application: Application):TaskListViewModel {
            val dataBaseInstance = (application as TaskBeatsApplication).getAppDataBase()
            val dao = dataBaseInstance.taskDao()
            return TaskListViewModel(dao)
        }
    }
}