package lkb41_SpotifyKnockoff;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;

/**
 * AlbumUtilities Class
 * Provides static methods to create, update and delete albums.  This class utilizes JPA to connect to the database and perform
 * any additions, updates, or deletions within our database tables.
 * @author lbley
 * Created: 02/22/18
 */
public class AlbumUtilities {

	
	/**
	 * Default constructor makes call to super 
	 */
	public AlbumUtilities() {
		super();
	}
	
	/**
	 * CreateAlbum method takes parameters and creates an Album object that also inserts the album instance into the database
	 * utilizing JPA.  It returns an Album object to the user.
	 * @param title				the title of the album
	 * @param releaseDate		the date the album was release
	 * @param recordingCompany	the company the album was recorded with
	 * @param numOfTracks		the number of tracks on the album
	 * @param pmrcRating		the parental rating of the album
	 * @param length			the length of the album (in minutes)
	 * @return the Album object created 
	 */
	public static Album CreateAlbum(String title, String releaseDate, String recordingCompany, int numOfTracks, String pmrcRating, double length) {
		
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("lkb41_SpotifyKnockoff");
		EntityManager emanager = emfactory.createEntityManager();
		
		emanager.getTransaction().begin();
		
		Album alb = new Album();
		alb.setAlbumID(UUID.randomUUID().toString());
		alb.setTitle(title);
		alb.setReleaseDate(releaseDate);
		alb.setRecordingCompany(recordingCompany);
		alb.setNumberOfTracks(numOfTracks);
		alb.setPmrcRating(pmrcRating);
		alb.setLength(length);
		
		
		emanager.persist(alb);
		emanager.getTransaction().commit();
		
		emanager.close();
		emfactory.close();
		
		
		return alb;
	}
	
	/**
	 * UpdateAlbum method takes an albumID as input.  The user is prompted to enter changes to the album properties - if there are 
	 * no changes to make to a property, they are instructed to leave the field blank.  the EntityManager finds this album instance,
	 * creates an Album object with it, adjusts the properties IF they were changed, and then commits these changes to the database.
	 * @param albumID	id of the album to be updated
	 */
	public static void UpdateAlbum(String albumID) {
		
		Double length = null;
		Integer numOfTracks = null;
		
		String title = JOptionPane.showInputDialog("Please enter what you would like the title to be changed "
				+ "to OR leave blank if no changes");
		String releaseDate = JOptionPane.showInputDialog("Please enter the new release date OR leave blank if no changes");
		String recordingCompany = JOptionPane.showInputDialog("Please enter the new recording company OR leave blank if no changes");
		String numOfTracksStr = JOptionPane.showInputDialog("Please enter the new number (integer) of tracks OR leave blank if no changes");
		if(!numOfTracksStr.equals("")) {
			numOfTracks = Integer.parseInt(numOfTracksStr);
		}
		String pmrcRating = JOptionPane.showInputDialog("Please enter the new PMRC rating OR leave blank if no changes");
		String lengthStr = JOptionPane.showInputDialog("Please enter new (numerical) duration of the album, OR leave blank if no changes");
		if(!lengthStr.equals("")) {
			length = Double.parseDouble(lengthStr);
		}
		
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("lkb41_SpotifyKnockoff");
		EntityManager emanager = emfactory.createEntityManager();
		emanager.getTransaction().begin();
		
		Album alb = emanager.find(Album.class, albumID);
		
		if(!title.equals("")) {
			alb.setTitle(title);
		}
		if(!releaseDate.equals("")) {
			alb.setReleaseDate(releaseDate);
		}
		if(!recordingCompany.equals("")) {
			alb.setRecordingCompany(recordingCompany);
		}
		if(numOfTracks != null) {
			alb.setNumberOfTracks(numOfTracks);
		}
		if(!pmrcRating.equals("")) {
			alb.setPmrcRating(pmrcRating);
		}
		if(length != null) {
			alb.setLength(length);
		}
		
		
		emanager.persist(alb);
		emanager.getTransaction().commit();
		
		emanager.close();
		emfactory.close();
		
		
	}
	
	/**
	 * DeleteAlbum method takes an albumID as parameter and uses that id to find the album instance in the database (utilizing the
	 * find method of emanager).  This album will then be removed from the database.
	 * @param albumID	id of the album you want to delete
	 */
	public static void DeleteAlbum(String albumID) {
		
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("lkb41_SpotifyKnockoff");
		EntityManager emanager = emfactory.createEntityManager();
		
		emanager.getTransaction().begin();
		
		
		Album alb = emanager.find(Album.class, albumID);
		emanager.remove(alb);
		
		emanager.getTransaction().commit();
		
		emanager.close();
		emfactory.close();


		
	}
	
	
	
	
	
}//end of AlbumUtilities class
