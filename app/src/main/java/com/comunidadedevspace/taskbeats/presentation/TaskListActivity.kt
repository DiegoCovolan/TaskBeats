package com.comunidadedevspace.taskbeats.presentation

import android.app.Activity
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.comunidadedevspace.taskbeats.R
import com.comunidadedevspace.taskbeats.data.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.io.Serializable


//MAIN ACTIVITY É RESPONSAVEL POR EU FAZER A OPERAÇÃO, EXECUTAR A AÇÃO A TELA DA FRENTE QUE É A DETAIL ACTIVITY SÓ VAI ME FALAR OQ EU TENHO QUE FAZER
class TaskListActivity : AppCompatActivity() {

//aula lista tarefas KOTLIN
/*private var taskList = arrayListOf(    //ele precisa ser um arraylist e não listOf pq eu preciso mudar as informações dentro destas tarefas como adicionar e remover e editar
    Task(0,"DevSpace", "Fazer a aula do dia"),
    Task(1,"Title1", "Description1"),
    Task(2,"Title2", "Description2"),
    Task(3,"Title3", "Description3"),
    )*/

    private lateinit var ctnContent: LinearLayout

    //Adapter
    //val list = listOf<String>("Title 1", "Title 2", "Title 3")
    private val adapter: TaskListAdapter by lazy {
        TaskListAdapter (::onListItemClicked)
    }

    //VIEWMODEL
    private val viewModel: TaskListViewModel by lazy {
        TaskListViewModel.create(application)
    }

    /* BASE DE DADOSS
     lateinit var dataBase :AppDataBase //ROOM


     private val dao by lazy {
         dataBase.taskDao()
     }*/

    //BY LAZY é esse codigo só vai ser executado quando eu executar a variavel, isso evita de consumir a memoria

    //17.33 vamos abrir a activity pedindo um resultado https://devspace.alpaclass.com/c/cursos/x6KC8T?lessonSlug=gFwTHJ
    //Queremos que quando eu abra a minha proxima activity ela me devolva a ação de DELETAR que eu saiba que foi deletado alguma coisa aqui

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) { //SE A AÇÃO EXISTIR QUAL É A AÇÃO, AQUI ONDE VOU PEGAR A AÇÃO, Se eu cheguei nesse ok o dev escreveu o codigo certo para me devolver essa ação de tarefa
            // Pegando resultado
            val data = result.data
            val taskAction = data?.getSerializableExtra(TASK_ACTION_RESULT) as TaskAction // Se coloco ? vira opticional pode ou não existir ai posso remover o requireNotNull

            viewModel.execute(taskAction)
        }
    }
        //DELETE
            //NOVA LISTA com uma nova referencia 16:34 aula Android One - Comparando e animando com com diffutils
            /*val newList= arrayListOf<Task>()
                .apply {taskList
                    addAll(taskList)
                }

            //remove item da lista:
            newList.remove(task)

            showMessage(ctnContent, "Item deleted ${task.title}")

            //Se não tiver nenhum item na tela aparece a imagem somente quando estiver vazia
            if(newList.size == 0){
                ctnContent.visibility = View.VISIBLE
            }

            // Atualizar o Adapter (criar um novo adapter e mandar uma nova lista para o adapter, temos q tirar a lista do construtor do adapter e implementar o lateinit pq eu preciso modificar essa lista)
            adapter.submitList(newList)

            taskList = newList*/  // Ctrl + Shift / faz comentar na area selecionada

        //CREATE
            /* val newList= arrayListOf<Task>()
                    .apply {taskList
                        addAll(taskList)
                    }

                //remove item da lista:
                newList.add(task)
                showMessage(ctnContent, "Item added ${task.title}")

                // Atualizar o Adapter (criar um novo adapter e mandar uma nova lista para o adapter, temos q tirar a lista do construtor do adapter e implementar o lateinit pq eu preciso modificar essa lista)
                adapter.submitList(newList)
                taskList = newList */

        //UPDATE
            //Vamos ter que identificar o item da lista e remover ele para fazer o update
            //criamos uma lista vazia porem não é preciso quando usa base de dados

            /*val tempEmptyList = arrayListOf<Task>()

              taskList.forEach{
                if(it.id == task.id){
                    //preciso trocar a referencia dentro do item senão da erro
                val newItem = Task(it.id, task.title, task.description)
                    tempEmptyList.add(newItem)
                } else{
                    tempEmptyList.add(it)
                }
            }

            showMessage(ctnContent, "Item updated ${task.title}")
            adapter.submitList(tempEmptyList)
            taskList = tempEmptyList*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        setSupportActionBar(findViewById(R.id.toolbar))

        ctnContent = findViewById(R.id.ctn_content)


        // RecyclerView
        val rvTasks: RecyclerView = findViewById(R.id.rv_task_list)
        rvTasks.adapter = adapter

        //FloatActionButton 7:00 Android One - Float Action button
        val fab = findViewById<FloatingActionButton>(R.id.fab_add)
        fab.setOnClickListener {
            openTaskListDetail(null)
        }
    }

    override fun onStart() {
        super.onStart()
        //dataBase= (application as TaskBeatsApplication).getAppDataBase()
        // Log.d("DiegoTeste", dataBase.toString())
        listFromDataBase()
    }

    private fun deleteAll(){
        val taskAction = TaskAction(null, ActionType.DELETE_ALL.name)
        viewModel.execute(taskAction)
            //   listFromDataBase() isso vai atualizar a lista no adapter, se não trabalhar em serie a thread pode não atualizar na hora
        }



    private fun listFromDataBase(){

        //OBSERVER
            val listObserver = Observer<List<Task>>{ listTasks -> //TODA ATUALIZAÇÂO DO LIVE DATA VAI CAIR AQUI, diferente do for each do exemplo do kotlin playground
                if (listTasks.isEmpty()) {
                    ctnContent.visibility = View.VISIBLE
                }else {
                    ctnContent.visibility = View.GONE
                }
                    adapter.submitList(listTasks)
            }

        //LIVEDATA ele não fica só observando de quando ele tem que reagir de quando ele mudou ele fica observando mas ele pede quem que quer observar para eu saber quando que esse LifeCycle morreu ai nao vou entregar mais para ele e o LIVEDATA para o observer
        viewModel.taskListLiveData.observe(this@TaskListActivity, listObserver )

    //dao.getAll().observe(this@TaskListActivity, listObserver)

           /* val myDataBaseList: List<Task> = dao.getAll()     //READ "getAll"
            adapter.submitList(myDataBaseList)*/

            /*CoroutineScope(Main).launch {
                if (myDataBaseList.isEmpty()) {
                    ctnContent.visibility = View.VISIBLE
                } else {
                    ctnContent.visibility = View.GONE

                }
            }*/

           //Erro que sera tratado mais futuramente pois o codigo dao.insert(task) não está funcionando pois  não pode acessar o database na main thread ocasionando um bloqueio possivelmente na UI por um longo periodo, pois ele nao sabe o periodo de tempo que é necessario para acessar uma base de dados e inserir um objeto la dentro esse objeto pode ser mt grande ou nao...precisamos colocar isso para fazer em paralelo em threads como fala para funcionar corretamente
//


    }


// Mostrar para o usuario sempre quando ele deletar um item uma mensagem
        private fun showMessage(view:View, message: String){
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
        }




    private fun onListItemClicked(task: Task) { // isso para saber que eu to abrindo a detail activity quando vem da lista e quando vem da lista temos uma tarefa
        openTaskListDetail(task)
        //usar o start activity pedindo um resultado
       startForResult.launch(intent)
    }

    private fun openTaskListDetail(task: Task? = null) { // Essa tarefa pode existir como pode não existir, posso colocar q ela ja não existe com o "null"
        //"(task: Task? = null)" é um DEFAULT ARGUMENT, isso significa que posso passar uma tarefa sem colocar nada dentro como no caso da linha 84 "openTaskListDetail()"
        val intent = TaskDetailActivity.start(
            this,
            task
        )//Quero dar um start na TaskDetailActivity, passo quem é vc this, para a task e eu vou te dar uma intenção e vc consegue abrir
        startForResult.launch(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_task_list, menu)
        return true
    }
    //Acão do botão DeleteAll
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.delete_all_task -> {
                //Deletar todas as tarefas
                deleteAll()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}
//CRUD (CREATE, READ, UPDATE, DELETE)
enum class ActionType { //Serializable faz passar de uma tela para outra uma classe que nós criamos, quando é string int nao precisa o serializable
//Tivemos q mudar  object pq quando criamos o objeto temos a instancia dele e acaba ficando diferente um do outro e no ENUM (Enumerador), porem não consigo passar ele de uma tela para outra com o serializable por isso estou passando como string
    DELETE,
    DELETE_ALL,
    UPDATE,
    CREATE
}

data class TaskAction(
    val task: Task?,
    val actionType: String
    ) : Serializable


//Agora preciso passar um resultado para a tela anterior dessa maneira
const val TASK_ACTION_RESULT = "TASK_ACTION_RESULT"