package il.ac.huji.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * A controller object which manages the apps databases(SQLite and Parse).
 */
public class DBController {
	
	private static final String PARSE_APP_ID = "4DgX5hL0ZA3ROQbDdnpafk5vHFVq5eEAar3Rn3So";
	private static final String PARSE_CLIENT_KEY = "D5wcjp7PFT6JowUVDWKk9Et9zXU1JagULlSIXITm";
	private static final String PARSE_ID_FIELD_NAME = "SQL" + TodoListSQLiteHelper.COLUMN_ID;

	private static final String DIAL_INTENT_PREFIX = "Call ";
	private static final String TEL_URL_PREFIX = "tel:";
	private static final String ID_QUERY_SQL = "select * from " + 
						TodoListSQLiteHelper.TABLE_TODO + " where " + 
						TodoListSQLiteHelper.COLUMN_ID + "=";
	
	private TodoListSQLiteHelper dbHelper;
	private SQLiteDatabase database;
	
	/**
	 * Constructor.
	 * @param context - the apps context.
	 */
	public DBController(Context context) {
		this.dbHelper = new TodoListSQLiteHelper(context);
		Parse.initialize(context, PARSE_APP_ID, PARSE_CLIENT_KEY);
	}
	
	/**
	 * opens the apps databases(SQLite and Parse).
	 */
	public void open() {
		this.database = this.dbHelper.getWritableDatabase();
		ParseUser.enableAutomaticUser();
		ParseACL.setDefaultACL(new ParseACL(ParseUser.getCurrentUser()), true);
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
	 */
	public void addData(String title, long dueDate) {
		//add to the local database.
		ContentValues todoItem = new ContentValues();
		todoItem.put(TodoListSQLiteHelper.COLUMN_TITLE, title);
		todoItem.put(TodoListSQLiteHelper.COLUMN_DUE, dueDate);
		long id = this.database.insert(TodoListSQLiteHelper.TABLE_TODO, null, todoItem);
		
		
		//add to Parse database.
		if (id != -1) {
			final ParseObject parseTodo = new ParseObject(TodoListSQLiteHelper.TABLE_TODO);
			parseTodo.put(PARSE_ID_FIELD_NAME, id);
			parseTodo.put(TodoListSQLiteHelper.COLUMN_TITLE, title);
			parseTodo.put(TodoListSQLiteHelper.COLUMN_DUE, dueDate);
			parseTodo.saveInBackground(new SaveCallback() {
				
				@Override
				public void done(ParseException e) {
					if (e == null) {
			            Log.v("Add Parse object", "Success");
					}
					else {
			            Log.v("Remove Parse query", "Error: " + e.getMessage());
			            parseTodo.saveEventually();
					}
				}
			});
		}
	}
	
	/**
	 * Removes the todo item with the given id from the app's databases(SQLite and Parse).
	 * @param id - the todo item to remove's id.
	 */
	public void removeData(long id) {
		//Removes from the local database.
		this.database.delete(TodoListSQLiteHelper.TABLE_TODO, 
				TodoListSQLiteHelper.COLUMN_ID + "=" + id, null);
		
		//Removes from the Parse database.
		ParseQuery<ParseObject> query = ParseQuery.getQuery(TodoListSQLiteHelper.TABLE_TODO);
		query.whereEqualTo(PARSE_ID_FIELD_NAME, id);
		query.getFirstInBackground(new GetCallback<ParseObject>() {
		    
			@Override
			public void done(ParseObject item, ParseException e) {
		        if (e == null) {
		            item.deleteEventually();
		            Log.v("Remove Parse query", "Success");
		        }
		        else {
		            Log.v("Remove Parse query", "Error: " + e.getMessage());
		        }
		    }
		});
	}
	
	/**
	 * Returns the tel number in the title of the todo item with the given id.
	 * @param id - the todo item's id.
	 * @return - the tel number in the title of the todo item with the given id.
	 */
	public String getTelNum(long id) {
		String title = getAttribute(id, TodoListSQLiteHelper.COLUMN_TITLE);
		return title.replace(DIAL_INTENT_PREFIX, TEL_URL_PREFIX);
	}
	
	/**
	 * Returns the value of the given database attribute, for the todo item with the given id.
	 * @param id - the todo item's id.
	 * @param attFlag - the attribute to retrieve.
	 * @return - the value of the given database attribute, for the todo item with the given id.
	 */
	public String getAttribute(long id, String attFlag) {
		Cursor cursor = this.database.rawQuery(ID_QUERY_SQL + id, null);
		String att = "";		
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				att += cursor.getString(cursor.getColumnIndex(attFlag));
			}
		}
		cursor.close();
		return String.valueOf(att);
	}
}
