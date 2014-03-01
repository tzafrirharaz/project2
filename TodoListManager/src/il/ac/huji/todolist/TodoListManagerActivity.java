
package il.ac.huji.todolist;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;

/**
 * The main activity of the Todo List app.
 * 
 * @author Tzafrir Harazy
 * HUJI 2014 - PostPC.
 */
public class TodoListManagerActivity extends Activity implements OnItemLongClickListener {
	
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
		this.tasksListView.setOnItemLongClickListener(this);
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
	public boolean onItemLongClick(AdapterView<?> adapterView, View v, final int pos, long id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(this.tasks.get(pos));
		builder.setPositiveButton(R.string.dialog_delete, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int id) {
				removeTask(pos);
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.show();
		return false;
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

	
