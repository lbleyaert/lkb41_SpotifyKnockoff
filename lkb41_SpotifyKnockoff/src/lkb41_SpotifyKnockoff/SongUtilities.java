package lkb41_SpotifyKnockoff;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
/**
 * SongUtilities Class
 * Provides static methods to create, update and delete songs.  This class utilizes JPA to connect to the database and perform
 * any additions, updates, or deletions within our database tables.
 * @author lbley
 * Created: 02/22/18
 */
public class SongUtilities {
	/**
	 * Default constructor makes call to super 
	 */
	public SongUtilities() {
		super();	
	}
	
	/**
	 * CreateSong method takes parameters and creates a song object that also inserts the song instance into the database
	 * utilizing JPA.  It returns a Song object to the user.
	 * @param title			the title of the song
	 * @param length		the length of the song (in minutes)
	 * @param releaseDate	the date the song was released
	 * @param recordDate	the date the song was recorded
	 * @return the Song object created
	 */
	public static Song CreateSong(String title, double length, String releaseDate, String recordDate) {
		
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("lkb41_SpotifyKnockoff");
		EntityManager emanager = emfactory.createEntityManager();
		
		emanager.getTransaction().begin();
		
		Song s = new Song();
		s.setSongID(UUID.randomUUID().toString());
		s.setTitle(title);
		s.setLength(length);
		s.setReleaseDate(releaseDate);
		s.setRecordDate(recordDate);
		s.setFilePath("");
		
		emanager.persist(s);
		emanager.getTransaction().commit();
		
		emanager.close();
		emfactory.close();
		
		return s;

	}
		
	/**
	 * UpdateSong method takes a songID as input.  The user is prompted to enter changes to the song properties - if there are 
	 * no changes to make to a property, they are instructed to leave the field blank.  the EntityManager finds this song instance,
	 * creates a Song object with it, adjusts the properties IF they were changed, and then commits these changes to the database.
	 * @param songID	id of the song you want to update
	 */
	public static void UpdateSong(String songID) {
		
		Double length = null;
		String title = JOptionPane.showInputDialog("Please enter what you would like the title to be changed to OR leave blank if no changes");
		String lengthStr = JOptionPane.showInputDialog("Please enter new (numerical) duration of the song, OR leave blank if no changes");
		if(!lengthStr.equals("")) {
			length = Double.parseDouble(lengthStr);
		}
		String releaseDate = JOptionPane.showInputDialog("Please enter new release date of the song OR leave blank if no changes");
		String recordDate = JOptionPane.showInputDialog("Please enter the new record date of the song OR leave blank if no changes");
		String filePath = JOptionPane.showInputDialog("Please enter the new file path OR leave blank if no changes");
		
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("lkb41_SpotifyKnockoff");
		EntityManager emanager = emfactory.createEntityManager();
		
		emanager.getTransaction().begin();
		
		Song s = emanager.find(Song.class, songID);
		
		if(!title.equals("")) {
			s.setTitle(title);	
		}
		if(length != null) {
			s.setLength(length);
		}
		if(!releaseDate.equals("")) {
			s.setReleaseDate(releaseDate);
		}
		if(!recordDate.equals("")) {
			s.setRecordDate(recordDate);
		}
		if(!filePath.equals("")) {
			s.setFilePath(filePath);	
		}
	
		
		emanager.persist(s);
		emanager.getTransaction().commit();
		
		emanager.close();
		emfactory.close();
			
	}
	
	/**
	 * DeleteSong method takes a songID as parameter and uses that id to find the song instance in the database (utilizing the find
	 * method of emanager).  This song will then be removed from the database.
	 * @param songID	id of the song you want to delete
	 */
	public static void DeleteSong(String songID) {
		
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("lkb41_SpotifyKnockoff");
		EntityManager emanager = emfactory.createEntityManager();
		
		emanager.getTransaction().begin();
		
		Song s = emanager.find(Song.class, songID);
		emanager.remove(s);
		
		
		emanager.getTransaction().commit();
		
		emanager.close();
		emfactory.close();

	
	}
	
		
		
		
}//end of SongUtilities class
	
