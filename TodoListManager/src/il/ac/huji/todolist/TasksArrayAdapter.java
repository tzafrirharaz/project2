
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
public class TasksArrayAdapter extends ArrayAdapter<String>  {
	
	private Context context;
	private ArrayList<String> tasksList;

	public TasksArrayAdapter(Context context, int resource, ArrayList<String> tasksList) {
		super(context, resource, tasksList);
		this.context = context;
		this.tasksList = tasksList;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View taskRowView = convertView;
		if (taskRowView == null) {
			taskRowView = inflater.inflate(R.layout.list_view_task_row, parent, false);
		}
		TextView taskTextView = (TextView) taskRowView.findViewById(R.id.taskTextView);
		taskTextView.setText(this.tasksList.get(position));
		setTextColor(taskTextView, position);
		return taskRowView;
	}
	
	/**
	 * Sets a task text color (Red or Blue).
	 * @param v - The TextView in which the task exists.
	 * @param pos - the task position in the list.
	 */
	private void setTextColor(TextView v, int pos) {
		if (pos % 2 == 0) {
			//Could not send a color to the method setTextColor the normal way, 
			//i.e - R.color.COLOR_ID.
			v.setTextColor(this.context.getResources().getColor(R.color.red));
		}
		else {
			v.setTextColor(this.context.getResources().getColor(R.color.blue));
		}
	}

}
