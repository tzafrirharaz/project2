package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.TextView;

public class TodoCursorAdapter extends ResourceCursorAdapter {
	
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	
	public TodoCursorAdapter(Context context, int layout, Cursor c, boolean autoRequery) {
		super(context, layout, c, autoRequery);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView txtTodoTitle = (TextView) view.findViewById(R.id.txtTodoTitle);
		TextView txtTodoDueDate = (TextView) view.findViewById(R.id.txtTodoDueDate);
		
		String title = cursor.getString(cursor.getColumnIndex
				(TodoListSQLiteHelper.COLUMN_TITLE));
		Date dueDate = new Date(cursor.getLong(cursor.getColumnIndex
				(TodoListSQLiteHelper.COLUMN_DUE)));
		
		txtTodoTitle.setText(title);
		txtTodoDueDate.setText(getDate(dueDate));
		
		setTextColor(txtTodoTitle, txtTodoDueDate, dueDate);

	}
	
	/**
	 * Sets the color for the given TextViews - Red if the todo's dueDate is over due, 
	 * Black otherwise.
	 * @param titleView - the title's TextView.
	 * @param dateView - the due date's TextView.
	 * @param dueDate - the todo item's date.
	 */
	private void setTextColor(TextView titleView, TextView dateView, Date dueDate) {
		int color = android.graphics.Color.BLACK;
		if (dueDate.before(new Date())) {
			color = android.graphics.Color.RED;
		}
		titleView.setTextColor(color);
		dateView.setTextColor(color);
	}
	
	/**
	 * Returns a String representation of the given Date object.
	 * @param dueDate - the given Date object.
	 * @return - a String representation of the given Date object.
	 */
	private String getDate(Date dueDate) {
		if (dueDate == null) return "No Due Date";
		return (new SimpleDateFormat(DATE_FORMAT)).format(dueDate);
	}
}
