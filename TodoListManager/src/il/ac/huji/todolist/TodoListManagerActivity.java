
package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

/**
 * The main activity of the Todo List app.
 * 
 * @author Tzafrir Harazy
 * HUJI 2014 - PostPC.
 */
public class TodoListManagerActivity extends Activity {
	
	private static final int ADD_TODO_ITEM_REQUEST = 1;
	
	private ArrayList<TodoItem> todoItems;
	private ListView lstTodoItems;
	private TodoArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		
		this.todoItems = new ArrayList<TodoItem>();
		this.lstTodoItems = (ListView) findViewById(R.id.lstTodoItems);
		this.adapter = new TodoArrayAdapter(getApplicationContext(), R.layout.todo_item_row,
				this.todoItems);
		
		this.lstTodoItems.setAdapter(this.adapter);
		
		registerForContextMenu(this.lstTodoItems);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.options_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.getItemId() == R.id.menuItemAdd) {
	    	Intent intent = new Intent(getApplicationContext(), AddNewTodoItemActivity.class);
	    	startActivityForResult(intent, ADD_TODO_ITEM_REQUEST);
	    	return true;
	    }
	    else {
	    	return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ADD_TODO_ITEM_REQUEST) {
			if (resultCode == RESULT_OK) {
				addNewTodo(data);
			}
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.context_menu, menu);
		
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;		
		TodoItem todo = this.todoItems.get(info.position);
		menu.setHeaderTitle(todo.getTitle());
		
		if (todo.hasDialIntent()) {
			menu.findItem(R.id.menuItemCall).setVisible(true);
			menu.findItem(R.id.menuItemCall).setTitle(todo.getTitle());
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
        case R.id.menuItemDelete:
        	removeTodo(info.position);
            return true;
        case R.id.menuItemCall:
        	initiateDialer(info.position);
        	return true;
        default:
            return super.onContextItemSelected(item);
		}
	}
	
	/**
	 * Starts a dial activity, with number according to the todo item in the given position.
	 * @param pos - the position of the todo item in the todo item's list.
	 */
	private void initiateDialer(int pos) {
		Intent dial = new Intent(Intent.ACTION_DIAL, 
					Uri.parse(this.todoItems.get(pos).getTelNum()));
		startActivity(dial);
	}

	/**
	 * Adds a new todo item to the this view.
	 * @param data - the intent which holds the new todo items data.
	 */
	private void addNewTodo(Intent data) {
		String title = data.getStringExtra("title");
		Date dueDate = (Date) data.getSerializableExtra("dueDate");
		this.todoItems.add(new TodoItem(title, dueDate));
		this.adapter.notifyDataSetChanged();	
	}
	
	/**
	 * Removes an existing task from the Todo list.
	 * @param position - the position of the task in the list.
	 */
	private void removeTodo(int position) {
		this.todoItems.remove(position);
		this.adapter.notifyDataSetChanged();
	}

}

	
