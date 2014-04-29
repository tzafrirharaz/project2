
package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoItem {
	
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	private static final String DIAL_INTENT_PREFIX = "Call ";
	private static final String TEL_URL_PREFIX = "tel:";
	
	private long id;
	private String title;
	private Date dueDate;
	
	/**
	 * Consructor.
	 * @param title - the todo item's title.
	 * @param dueDate - a date object which represents the todo item's due date.
	 */
	public TodoItem(long id, String title, Date dueDate) {
		this.id = id;
		this.title = title;
		this.dueDate = dueDate;
	}
	
	public long getId() {
		return this.id;
	}
	
	/**
	 * Returns this todo item's title.
	 * @return - this todo item's title.
	 */
	public String getTitle() {
		if (this.title.length() == 0) {
			return " ";
		}
		return this.title;
	}
	
	/**
	 * Returns this todo items's due date in string format.
	 * @return - this todo items's due date in string format.
	 */
	public String getDueDate() {
		if (this.dueDate == null) {
			return "No Due Date";
		}
		return (new SimpleDateFormat(DATE_FORMAT)).format(this.dueDate);
	}
	
	/**
	 * Returns true if this todo item's due date < today's date, and false otherwise.
	 * @return - true if this todo item's due date < today's date, and false otherwise.
	 */
	public boolean pastDueDate() {
		return this.dueDate.before(new Date());
	}
	
	/**
	 * Returns true if this todo item has a dial intent, meaning it start with the string "Call ",
	 * and false otherwise.
	 * @return - true if this todo item has a dial intent, meaning it start with the string 
	 * "Call ", and false otherwise.
	 */
	public boolean hasDialIntent() {
		if (this.title.startsWith(DIAL_INTENT_PREFIX)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the telephone number in the title of this todo item, including the systems prefix
	 * "tel:", ready for dial.
	 * @return - the telephone number in the title of this todo item.
	 */
	public String getTelNum() {
		if (this.hasDialIntent()) {
			return this.title.replace(DIAL_INTENT_PREFIX, TEL_URL_PREFIX);
		}
		return null;
	}
	
}
