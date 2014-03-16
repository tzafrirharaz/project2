
package il.ac.huji.todolist;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * This class holds the implementation of a custom Adapter for my task Listview.
 * @author tzafrirharazy
 *
 */
public class TodoArrayAdapter extends ArrayAdapter<TodoItem>  {
	
	private Context context;
	private ArrayList<TodoItem> todoList;

	public TodoArrayAdapter(Context context, int resource, ArrayList<TodoItem> todoList) {
		super(context, resource, todoList);
		this.context = context;
		this.todoList = todoList;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View todoRowView = convertView;
		if (todoRowView == null) {
			todoRowView = inflater.inflate(R.layout.todo_item_row, parent, false);
		}
		TextView txtTodoTitle = (TextView) todoRowView.findViewById(R.id.txtTodoTitle);
		TextView txtTodoDueDate = (TextView) todoRowView.findViewById(R.id.txtTodoDueDate);
		TodoItem item = this.todoList.get(position);
		
		txtTodoTitle.setText(item.getTitle());
		txtTodoDueDate.setText(item.getDueDate());
		
		setTextColor(txtTodoTitle, txtTodoDueDate, item);
		
		return todoRowView;
	}
	
	/**
	 * Sets the color of the text in the given views. 
	 * @param titleView - the TextView which holds the todo item's title.
	 * @param dateView - the TextView which holds the todo item's due date.
	 * @param item - the todo item.
	 */
	private void setTextColor(TextView titleView, TextView dateView, TodoItem item) {
		int color = android.graphics.Color.BLACK;
		if (item.pastDueDate()) color = android.graphics.Color.RED;
		titleView.setTextColor(color);
		dateView.setTextColor(color);
	}

}
