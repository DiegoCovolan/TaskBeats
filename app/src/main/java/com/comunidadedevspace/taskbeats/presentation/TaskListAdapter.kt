package com.comunidadedevspace.taskbeats.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.comunidadedevspace.taskbeats.R
import com.comunidadedevspace.taskbeats.data.Task


class TaskListAdapter(                                                  // private val listTask : List<Task>,     tiramos a lista do construtor do adapter, pq essa lista nao existe no momento mas ela vai existir em algum momento por isso o lateinit var
/*CALLBACK*/ private val openTaskDetailView: (task: Task) -> Unit
//Cuidado ao selecionar o preenchimento automatico do CODIGO pois pode selecionar o mesmo NOME porem com OUTRA FUNÇÃO!
                    ) : androidx.recyclerview.widget.ListAdapter<Task, TaskListViewHolder>(
    TaskListAdapter
)  {

    /*
    private var listTask: List<Task> = emptyList()

    fun submit(list : List<Task>){
        listTask = list
         ()
    }*/


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {

        /*
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.text_row_item, viewGrop, false)
        */

        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskListViewHolder(view)
    }


    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task, openTaskDetailView)
    }

    //TAMANHO DA MINHA LISTA
    /*
    override fun getItemCount(): Int {
        return listTask.size
    }*/

    companion object : DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.title == newItem.title &&
                     oldItem.description == newItem.description

        }

    }

}
// View Holder é o responsável por recuperar a view pelo id e está setando a descrição e o titulo
class TaskListViewHolder(
    private val view:View)
    : RecyclerView.ViewHolder(view){

    private val tvTaskTitle = view.findViewById<TextView>(R.id.tv_task_title)
    private val tvTaskDescription = view.findViewById<TextView>(R.id.tv_task_description)

    fun bind(task: Task,
             openTaskDetailView:(tak: Task) -> Unit){
        tvTaskTitle.text = task.title
        tvTaskDescription.text = "${task.id}- ${task.description}"

        view.setOnClickListener{
            openTaskDetailView.invoke(task)
        }
    }

}
