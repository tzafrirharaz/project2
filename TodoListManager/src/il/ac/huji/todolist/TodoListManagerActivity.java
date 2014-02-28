

package il.ac.huji.todolist;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;

/**
 * The main activity of the Todo List app.
 * 
 * @author Tzafrir Harazy
 * HUJI 2014 - PostPC.
 */
public class TodoListManagerActivity extends Activity {
	
	private ArrayList<String> tasks;
	private ListView tasksListView;
	private EditText taskInputField;
	private TasksArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		
		this.tasks = new ArrayList<String>();
		this.tasksListView = (ListView) findViewById(R.id.tasksListView);
		this.taskInputField = (EditText) findViewById(R.id.taskInputField);
		this.adapter = new TasksArrayAdapter(this, R.layout.list_view_task_row, this.tasks);
		
		this.tasksListView.setAdapter(this.adapter);
		registerForContextMenu(this.tasksListView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.options_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.getItemId() == R.id.add) {
	    	addNewTask();
	    	return true;
	    }
	    else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
	    menu.setHeaderTitle(this.tasks.get(info.position));
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.context_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    if (item.getItemId() == R.id.delete) {
	    	removeTask(info.position);
	    	return true;
	    }
	    else {
	    	return super.onContextItemSelected(item);
	    }
	}
	
	/**
	 * Adds a new task to the Todo list.
	 */
	private void addNewTask() {
		String newTask = this.taskInputField.getText().toString();
		if (newTask.length() > 0) {
			this.taskInputField.setText("");
			this.tasks.add(newTask.trim());
			this.adapter.notifyDataSetChanged();	
		}
	}
	
	/**
	 * Removes an existing task from the Todo list.
	 * @param position - the position of the task in the list.
	 */
	private void removeTask(int position) {
		this.tasks.remove(position);
		this.adapter.notifyDataSetChanged();
	}

}

	
