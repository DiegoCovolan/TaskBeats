 package com.comunidadedevspace.taskbeats

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar

 class TaskDetailActivity : AppCompatActivity() {

     private var task: Task? = null  //A tarefa vai existir só não inicializei ela ainda, Eu coloco que a task vai ser inicializada depois, ñ se preocupa vou ter uma tarefa em algum momento
    private lateinit var btnDone: Button

     companion object{
          private const val TASK_DETAIL_EXTRA = "task.extra.detail"

         //Função para TaskDetailActivity quem for chamar ela passar uma tarefa para ela, deixei como opcional pois apertando no floatbutton pode vir como pode nao vir tarefa, no caso de eu querer add uma tarefa e não tiver nenhuma pelo eu add pelo float
           fun start(context: Context, task: Task?): Intent{
             val intent = Intent(context, TaskDetailActivity::class.java)
                 .apply {
                     putExtra(TASK_DETAIL_EXTRA, task) // COLOCO O VALOR DO TASK_ACTION_RESULT
                 }
             return intent
         }
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)
        setSupportActionBar(findViewById(R.id.toolbar))//Assim colocamos o toolbar


        //Recuperar uma tarefa inteira TASK (quando eu passar aqui eu falo para o dev. me passar uma tarefa, pq se não passar o app vai crashar, se se me passar uma tarefa e ela não for nula eu vou conseguir passar essa tarefa para a fun onOptionsItemSelected
        task = intent.getSerializableExtra(TASK_DETAIL_EXTRA) as Task? //Ponto de ? é que a tarefa pode não estar ali  "" //?: "" é caso a variavel venha vazia esse é o valor default p/ string

        //Recuperei os campos do xml de adicionar task                                                                            //// PEGO O VALOR DO TASK_ACTION_RESULT
        val edtTitle = findViewById<EditText>(R.id.edt_task_title)
        val edtDescription = findViewById<EditText>(R.id.edt_task_description)
        btnDone = findViewById<Button>(R.id.btn_done)

        if (task != null){
            edtTitle.setText(task!!.title)
            edtDescription.setText(task!!.description)
        }


        //Atribui uma função ao clicar
        btnDone.setOnClickListener{
            val title= edtTitle.text.toString()//toda vez que eu clicar no botão quero recuperar o Title
            val desc= edtDescription.text.toString()//toda vez que eu clicar no botão quero recuperar o Description

            // Se ao clicar no botao os dados estao preenchidos a tarefa não vai ser nula, senão vai mostrar uma menssagem
            if(title.isNotEmpty() && desc.isNotEmpty()){
                if (task == null){ //Se não existir uma tarefa e eu quiser add uma tarefa vou para o CREATE
                    addOrUpdateTask(0,title, desc, ActionType.CREATE)
                }else{
                    addOrUpdateTask(task!!.id,title, desc, ActionType.UPDATE) //Se a tarefa existe eu pego o ID da tarefa e faço o Update
                }
            }else{
                showMessage(it, "Fields are required")
            }

        }



    //Recuperar campo do XML
        //tvTitle = findViewById(R.id.tv_task_title_detail)
        //Setar um novo texto na tela
        //tvTitle.text = task?.title ?: "Adicione uma Tarefa"
    }


     //Função para mandar para a tela uma nova tarefa ou fazer update
     private fun addOrUpdateTask(
         id:Int,
         title: String,
         description: String,
         actionType: ActionType
     ) {
         val task = Task(id,title,description)
        returnAction(task, actionType)
     }




     //Criando o menu com a função delete - Cliclo de vida da Activity (no caso quando temos o oncreate não precisamos chamar a função novamente no inflater.inflate pq a activity ja chama em determinado momento)
     override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         val inflater : MenuInflater = menuInflater
         inflater.inflate(R.menu.menu_task_detail, menu)
         return true
     }

     //Ação do botão Delete OnoptionItemSelected checa qual opção do menu foi clicada
     override fun onOptionsItemSelected(item: MenuItem): Boolean {
         return when (item.itemId){
             R.id.delete_task -> {
//Quando clicamos no FloatinButton e tentamos deletar ira cair nesse codigo onde if é tem tarefa e else nao tem tarefa e exibe uma mensagem informando o usuario caso ele tente deletar evitando de craschar o app
                 if (task != null) {
                     returnAction(task!!, ActionType.DELETE)
                 } else {
                     showMessage(btnDone, "Item not found")

                 }
                 true
             }
             else -> super.onOptionsItemSelected(item)
         }
     }

     private fun returnAction(task: Task, actionType: ActionType){
         val intent = Intent()
             .apply {
                 val taskAction = TaskAction(task, actionType.name)//!! é perigoso e ele garante que tem q ter uma tarefa, porem nao tem e ele crasha pq nao tem quando vai pelo floatbutton, para isso nao craschar utilizo o if e else
                 putExtra(TASK_ACTION_RESULT, taskAction)// COLOCO O VALOR DO TASK_ACTION_RESULT E EXECUTO A AÇÃO taskAction
             }
         setResult(Activity.RESULT_OK, intent)
         finish()
     }

         private fun showMessage(view: View, message: String){
             Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                 .setAction("Action", null)
                 .show()
         }
}