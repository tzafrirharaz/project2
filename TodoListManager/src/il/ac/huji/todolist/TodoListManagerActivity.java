
package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
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
	
	private static final int ADD_TODO_ITEM_REQUEST = 10;

	private ListView lstTodoItems;
	private DBController dbController;
	private TodoArrayAdapter adapter;
	private ArrayList<TodoItem> todoItems;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		
		this.lstTodoItems = (ListView) findViewById(R.id.lstTodoItems);
		this.dbController = new DBController(getApplicationContext());
		this.dbController.open();
		this.todoItems = new ArrayList<TodoItem>();
		this.adapter = new TodoArrayAdapter(getApplicationContext(), R.layout.todo_item_row, 
				this.todoItems);
		this.lstTodoItems.setAdapter(this.adapter);
			
		registerForContextMenu(this.lstTodoItems);
		
		new TodoAsyncTask().execute();
	}
	
	@Override
	public void onDestroy() {
		this.dbController.close();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.options_menu, menu);
		//s
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
		TodoItem item = this.todoItems.get(info.position);
		menu.setHeaderTitle(item.getTitle());
		
		if (item.hasDialIntent()) {
			menu.findItem(R.id.menuItemCall).setVisible(true);
			menu.findItem(R.id.menuItemCall).setTitle(item.getTitle());
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
	 * @param id - the id of the todo item in the database.
	 */
	private void initiateDialer(int pos) {
		Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse(
				this.todoItems.get(pos).getTelNum()));
		startActivity(dial);
	}

	/**
	 * Adds a new todo item to the this view.
	 * @param data - the intent which holds the new todo items data.
	 */
	private void addNewTodo(Intent data) {
		String title = data.getStringExtra("title");
		long dueDate = data.getLongExtra("dueDate", System.currentTimeMillis());
		long id = this.dbController.addData(title, dueDate);
		this.todoItems.add(new TodoItem(id, title, new Date(dueDate)));
		this.adapter.notifyDataSetChanged();
	}
	
	/**
	 * Removes an existing task from the Todo list.
	 * @param id - the id of the task in the database.
	 */
	private void removeTodo(int pos) {
		this.dbController.removeData(this.todoItems.get(pos).getId());
		this.todoItems.remove(pos);
		this.adapter.notifyDataSetChanged();
	}
	
	private class TodoAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			Cursor cursor = TodoListManagerActivity.this.dbController.getData();
			int counter = 0;
			while (cursor.moveToNext()) {
				TodoItem item = new TodoItem(cursor.getLong(0), cursor.getString(1), 
						new Date(cursor.getLong(2)));
				TodoListManagerActivity.this.todoItems.add(item);
				if (counter % 5 == 0) {
					publishProgress();
				}
				counter++;
			}
			publishProgress();
			return null; 
		}
		
		@Override
		protected void onProgressUpdate(Void... params) {
			TodoListManagerActivity.this.adapter.notifyDataSetChanged();
		}
		
	}

}

	
