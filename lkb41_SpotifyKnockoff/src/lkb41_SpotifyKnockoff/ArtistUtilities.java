package lkb41_SpotifyKnockoff;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;

/**
 * ArtistUtilities Class
 * Provides static methods to create, update and delete artists.  This class utilizes JPA to connect to the database and perform
 * any additions, updates, or deletions within our database tables.
 * @author lbley
 * Created: 02/22/18
 */
public class ArtistUtilities {

	/**
	 * Default constructor makes call to super
	 */
	public ArtistUtilities() {
		super();
	}

	/**
	 * CreateArtist method takes parameters and creates an artist object that also inserts the artist instance into the database
	 * utilizing JPA.  It returns an Artist object to the user.
	 * @param firstName	first name of the artist (if needed)
	 * @param lastName	last name of the artist (if needed)
	 * @param bandName	name of the band (if needed)
	 * @return the Artist object created
	 */
	public static Artist CreateArtist(String firstName, String lastName, String bandName) {
		
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("lkb41_SpotifyKnockoff");
		EntityManager emanager = emfactory.createEntityManager();
		
		emanager.getTransaction().begin();
		
		Artist a = new Artist();
		a.setArtistID(UUID.randomUUID().toString());
		a.setFirstName(firstName);
		a.setLastName(lastName);
		a.setBandName(bandName);
		a.setBio("");
		
		emanager.persist(a);
		emanager.getTransaction().commit();
		
		emanager.close();
		emfactory.close();
		
		return a;
		
	}
	
	
	/**
	 * UpdateArtist method takes an artistID as input.  The user is prompted to enter changes to the artist properties - if there are 
	 * no changes to make to a property, they are instructed to leave the field blank.  the EntityManager finds this artist instance,
	 * creates an Artist object with it, adjusts the properties IF they were changed, and then commits these changes to the database.
	 * @param artistID	id of the artist to be updated
	 */
	public static void UpdateArtist(String artistID) {
		
		String firstName = JOptionPane.showInputDialog("Please enter the new first name of this artist OR leave blank if no changes");
		String lastName = JOptionPane.showInputDialog("Please enter new last name of this artist OR leave blank if no changes");
		String bandName = JOptionPane.showInputDialog("Please enter new band name of this artist OR leave blank if no changes");
		
		
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("lkb41_SpotifyKnockoff");
		EntityManager emanager = emfactory.createEntityManager();
		
		emanager.getTransaction().begin();
		
		Artist a = emanager.find(Artist.class, artistID);
		
		if(!firstName.equals("")) {
			a.setFirstName(firstName);
		}
		if(!lastName.equals("")) {
			a.setLastName(lastName);
		}
		if(!bandName.equals("")) {
			a.setBandName(bandName);
		}
		
		emanager.persist(a);
		emanager.getTransaction().commit();
		
		emanager.close();
		emfactory.close();
	}
	
	
	/**
	 * DeleteArtist method takes an artistID as parameter and uses that id to find the artist instance in the database (utilizing
	 * the find method of emanager).  This artist will then be removed from the database.
	 * @param artistID	id of the artist you want to delete
	 */
	public static void DeleteArtist(String artistID) {
		
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("lkb41_SpotifyKnockoff");
		EntityManager emanager = emfactory.createEntityManager();
		
		emanager.getTransaction().begin();
		
		Artist a = emanager.find(Artist.class, artistID);
		emanager.remove(a);
		
		emanager.getTransaction().commit();
		
		emanager.close();
		emfactory.close();
	}
	



}//end of ArtistUtilities class
