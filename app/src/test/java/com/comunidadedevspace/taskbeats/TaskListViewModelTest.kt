import com.comunidadedevspace.taskbeats.data.Task
import com.comunidadedevspace.taskbeats.data.TaskDao
import com.comunidadedevspace.taskbeats.presentation.ActionType
import com.comunidadedevspace.taskbeats.presentation.TaskAction
import com.comunidadedevspace.taskbeats.presentation.TaskListViewModel
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


class TaskListViewModelTest {

    private val taskDao: TaskDao = mock()

    private val underTest: TaskListViewModel by lazy {
        TaskListViewModel(
            taskDao,
            UnconfinedTestDispatcher()
        )
    }

    //Testes cases Delete_all

    @Test

    fun delete_all() = runTest {

//GIVEN
        val taskAction = TaskAction(
            task = null,
            actionType = ActionType.DELETE_ALL.name
        )
        //WHEN
        underTest.execute(taskAction)

        //Then
        verify(taskDao).deleteAll()
    }

    @Test
    fun update_task() = runTest{
        //Given
        val task = Task(
            id = 1,
        title = "title",
        description = "description"
        )
        val taskAction = TaskAction(
            task = task,
            actionType = ActionType.UPDATE.name
        )
        //When
        underTest.execute(taskAction)

        //Then
        verify(taskDao).update(task)
    }
}