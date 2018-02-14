package lkb41_SpotifyKnockoff;

import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Class Spotify
 * Spotify class implements methods that can search tables for certain instances based on the String input.  This class will be
 * utilized in the SpotifyGUI class, in which a textbox takes user input such as song title, album title, or artist name.
 * @author lbley
 * Created: 02/09/18
 */
public class Spotify {

	/**
	 * searchSongs method takes a String searchTerm.  The String that is entered will be incorporated into a SELECT statement. 
	 * The database is then searched for instances in the song table where the title is LIKE the String input.  A 
	 * DefaultTableModel is returned with the results.  If an error occurs when connecting to the database, the user is notified
	 * and the error message is logged in errorlog.txt
	 * @param searchTerm - the String that will be compared to song titles in the database 
	 * @return a DefaultTableModel with results from the SQL statement
	 */
	public DefaultTableModel searchSongs(String searchTerm) {
		
		String sql = "SELECT * FROM song";
		//if the searchTerm has anything in it, it will be added to the sql statement
		if(!searchTerm.equals("")) {
			sql += " WHERE title LIKE '%" + searchTerm + "%';";
		}	
		try {
			DbUtilities db = new DbUtilities();
			return db.getDataTable(sql);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to connect to database");
			ErrorLogger.log(e.getMessage());
		}
		return null;
	}
	
	/**
	 * searchAlbums method takes a String searchTerm.  The String that is entered will be incorporated into a SELECT statement. 
	 * The database is then searched for instances in the album table where the title is LIKE the String input.  A 
	 * DefaultTableModel is returned with the results.  If an error occurs when connecting to the database, the user is notified
	 * and the error message is logged in errorlog.txt
	 * @param searchTerm - the String that will be compared to album titles in the database 
	 * @return a DefaultTableModel with results from the SQL statement
	 */
	public DefaultTableModel searchAlbums(String searchTerm) {
		
		String sql = "SELECT * FROM album";
		//if the searchTerm has anything in it, that will be added as a LIKE condition to the sql statement
		if(!searchTerm.equals("")) {
			sql += " WHERE title LIKE '%" + searchTerm + "%';";
		}
		try {
			DbUtilities db = new DbUtilities();
			return db.getDataTable(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to connect to database");
			ErrorLogger.log(e.getMessage());
		}
		return null;
	
	}
	
	
	/**
	 * searchArtists method takes a String searchTerm.  The String that is entered will be incorporated into a SELECT statement. 
	 * The database is then searched for instances in the artist table where either the first_name OR last_name OR band_name is 
	 * LIKE the String input.  A DefaultTableModel is returned with the results.  If an error occurs when connecting to the 
	 * database, the user is notified and the error message is logged in errorlog.txt
	 * @param searchTerm - the String that will be compared to artist name options (first, last or band) in the database 
	 * @return a DefaultTableModel with results from the SQL statement
	 */
	public DefaultTableModel searchArtists(String searchTerm) {
		
		String sql = "SELECT * FROM artist";
		//if searchTerm has anything in it, LIKE conditions are added to statement - check first_name, last_name, and band_name
		if(!searchTerm.equals("")) {
			sql += " WHERE first_name LIKE '%" + searchTerm + "%' OR last_name LIKE '%" + searchTerm + "%' OR band_name LIKE '%"
					+ searchTerm + "%';";
		}
		try {
			DbUtilities db = new DbUtilities();
			return db.getDataTable(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to connect to database");
			ErrorLogger.log(e.getMessage());
		}
		return null;
	}
	
	
	
	
}//end of Spotify class
