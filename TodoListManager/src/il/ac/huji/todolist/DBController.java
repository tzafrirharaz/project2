package il.ac.huji.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * A controller object which manages the apps databases(SQLite and Parse).
 */
public class DBController {
	
	private TodoListSQLiteHelper dbHelper;
	private SQLiteDatabase database;
	
	/**
	 * Constructor.
	 * @param context - the apps context.
	 */
	public DBController(Context context) {
		this.dbHelper = new TodoListSQLiteHelper(context);
	}
	
	/**
	 * opens the apps databases(SQLite and Parse).
	 */
	public void open() {
		this.database = this.dbHelper.getWritableDatabase();
	}
	
	/**
	 * closes the apps database.
	 */
	public void close() {
		this.dbHelper.close();
	}
	
	public Cursor getData() {
		String[] columns = new String[] {TodoListSQLiteHelper.COLUMN_ID, 
										 TodoListSQLiteHelper.COLUMN_TITLE, 
										 TodoListSQLiteHelper.COLUMN_DUE};
		
		Cursor cursor = database.query(TodoListSQLiteHelper.TABLE_TODO, columns,
				null, null, null, null, null);
		if (cursor != null) cursor.moveToFirst();

		return cursor;
	}
	
	/**
	 * Adds a todo item with the given title and due date to the app's 
	 * databases(SQLite and Parse).
	 * @param title - the todo item's title.
	 * @param dueDate - the todo item's dueDate in long format(milliseconds).
	 * @return 
	 */
	public long addData(String title, long dueDate) {
		//add to the local database.
		ContentValues todoItem = new ContentValues();
		todoItem.put(TodoListSQLiteHelper.COLUMN_TITLE, title);
		todoItem.put(TodoListSQLiteHelper.COLUMN_DUE, dueDate);
		return this.database.insert(TodoListSQLiteHelper.TABLE_TODO, null, todoItem);
	}
	
	/**
	 * Removes the todo item with the given id from the app's databases(SQLite and Parse).
	 * @param id - the todo item to remove's id.
	 */
	public void removeData(long id) {
		//Removes from the local database.
		this.database.delete(TodoListSQLiteHelper.TABLE_TODO, 
				TodoListSQLiteHelper.COLUMN_ID + "=" + id, null);
	}
	
}
