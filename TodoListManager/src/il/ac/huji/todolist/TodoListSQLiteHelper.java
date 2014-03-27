package il.ac.huji.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoListSQLiteHelper extends SQLiteOpenHelper {

	protected static final String DATABASE_NAME = "todo_db";
	protected static final int DATABASE_VERSION = 1;
	
	protected static final String TABLE_TODO = "todo";
	protected static final String COLUMN_ID = "_id";
	protected static final String COLUMN_TITLE = "title";
	protected static final String COLUMN_DUE = "due";

	protected static final String DATABASE_TABLE_CREATE = "create table " + 
			TABLE_TODO + "(" +  COLUMN_ID + " integer primary key autoincrement, " +
			COLUMN_TITLE + " String, " + COLUMN_DUE + " long" + ")";


	public TodoListSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
		onCreate(db);
	}

}
