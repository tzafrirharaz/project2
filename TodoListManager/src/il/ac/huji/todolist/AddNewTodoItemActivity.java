
package il.ac.huji.todolist;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddNewTodoItemActivity extends Activity {

	private EditText edtNewItem;
	private DatePicker datePicker;
	private Button btnCancel;
	private Button btnOK;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.title_activity_add_new_todo_item);
		setContentView(R.layout.activity_add_new_todo_item);
		
		this.edtNewItem = (EditText) findViewById(R.id.edtNewItem);
		this.datePicker = (DatePicker) findViewById(R.id.datePicker);
		this.btnCancel = (Button) findViewById(R.id.btnCancel);
		this.btnOK = (Button) findViewById(R.id.btnOK);
		
		setButtonListeners();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_new_todo_item, menu);
		return true;
	}

	/**
	 * Sets the listeners for the buttons in this activity's view.
	 */
	private void setButtonListeners() {
		this.btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		this.btnOK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getNewItem();
			}
		});		
	}
	
	/**
	 * Gets the data for creating a new todo item from the user.
	 */
	private void getNewItem() {
		String title = this.edtNewItem.getText().toString().trim();		
		@SuppressWarnings("deprecation")
		Date dueDate = new Date(this.datePicker.getYear() - 1900, this.datePicker.getMonth(), 
														this.datePicker.getDayOfMonth());
		sendData(title, dueDate);
	}
	
	/**
	 * Sends the given title and dueDate to the initiating app activity.
	 * @param title - the todo item's title to send.
	 * @param dueDate - the todo item's due date to send.
	 */
	private void sendData(String title, Date dueDate) {
		Intent result = new Intent();
		result.putExtra("title", title);
		result.putExtra("dueDate", dueDate);
		setResult(RESULT_OK, result);
		finish();
	}

}
