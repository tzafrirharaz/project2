
package il.ac.huji.todolist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
	private static final String DIAL_INTENT_PREFIX = "Call ";

	private ListView lstTodoItems;
	private DBController dbController;
	private TodoCursorAdapter adapter;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		
		this.lstTodoItems = (ListView) findViewById(R.id.lstTodoItems);
		this.dbController = new DBController(getApplicationContext());
		this.dbController.open();
				
		setAdapter();
		
		registerForContextMenu(this.lstTodoItems);
		
	}
	
	@Override
	public void onDestroy() {
		this.dbController.close();
	}
	
	/**
	 * Sets a CursorAdapter for the ListView.
	 */
	private void setAdapter() {
		Cursor cursor = this.dbController.getData();
		this.adapter = new TodoCursorAdapter(getApplicationContext(), R.layout.todo_item_row, 
				cursor, true);		
		this.lstTodoItems.setAdapter(this.adapter);		
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
		String title = this.dbController.getAttribute(info.id, TodoListSQLiteHelper.COLUMN_TITLE);
		menu.setHeaderTitle(title);
		
		if (hasDialIntent(title)) {
			menu.findItem(R.id.menuItemCall).setVisible(true);
			menu.findItem(R.id.menuItemCall).setTitle(title);
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
        case R.id.menuItemDelete:
        	removeTodo(info.id);
            return true;
        case R.id.menuItemCall:
        	initiateDialer(info.id);
        	return true;
        default:
            return super.onContextItemSelected(item);
		}
	}
	
	/**
	 * Starts a dial activity, with number according to the todo item in the given position.
	 * @param id - the id of the todo item in the database.
	 */
	private void initiateDialer(long id) {
		Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse(this.dbController.getTelNum(id)));
		startActivity(dial);
	}

	/**
	 * Adds a new todo item to the this view.
	 * @param data - the intent which holds the new todo items data.
	 */
	private void addNewTodo(Intent data) {
		String title = data.getStringExtra("title");
		long dueDate = data.getLongExtra("dueDate", System.currentTimeMillis());
		this.dbController.addData(title, dueDate);
		setAdapter();		
	}
	
	/**
	 * Removes an existing task from the Todo list.
	 * @param id - the id of the task in the database.
	 */
	private void removeTodo(long id) {
		this.dbController.removeData(id);
		setAdapter();
	}
	
	/**
	 * Returns true if the todo item with the given title has a dial intent, and false otherwise.
	 * @param title - the title of the todo item.
	 * @return true if the todo item with the given title has a dial intent, and false otherwise.
	 */
	private boolean hasDialIntent(String title) {
		if (title.startsWith(DIAL_INTENT_PREFIX)) {
			return true;
		}
		return false;
	}

}

	
