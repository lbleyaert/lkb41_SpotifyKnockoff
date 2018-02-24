package lkb41_SpotifyKnockoff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.swing.JOptionPane;

/**
 * Class Artist
 * Allows you to create instances of the Artist class (as well as an instance in the artist table of database) using a variety of
 * parameters as well as delete artists from the database. 
 * @author lbley
 * Created: 01/23/18
 */
@Entity
@Table (name = "artist")
public class Artist {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	@Column (name = "artist_id")
	private String artistID;
	
	@Column (name = "first_name")
	private String firstName;
	
	@Column (name = "last_name")
	private String lastName;
	
	@Column (name = "band_name")
	private String bandName;
	
	@Column (name = "bio")
	private String bio;
	
	/**
	 * Artist Constructor that takes no arguments.  It makes a call to the super class for JPA purposes.
	 */
	public Artist() {
		super();
	}
	
	
	/**
	 * This constructor creates an instance of class Artist using the parameters passed through.  The constructor 
	 * also assigns a random UUID to the Artist object which will be its artistID.  It then creates an SQL INSERT 
	 * statement using these parameters and attempts to insert the record into the artist table of the database 
	 * @param firstName		first name of the artist (if needed)
	 * @param lastName		last name of the artist (if needed)
	 * @param bandName		name of the the band (if needed)
	 */
	public Artist(String firstName, String lastName, String bandName) {
		
		super();
		this.artistID = UUID.randomUUID().toString();
		this.firstName = firstName;
		this.lastName = lastName;
		this.bandName = bandName;
		this.bio = null;
		
		String sql = "INSERT INTO artist(artist_id, first_name, last_name, band_name) ";
		sql += "VALUES (?, ?, ?, ?);";
		//System.out.println(sql);
		
		
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.artistID);
			ps.setString(2, this.firstName);
			ps.setString(3, this.lastName);
			ps.setString(4, this.bandName);
			ps.executeUpdate();
			db.closeDbConnection();
			//destroy db object (memory cleanup)
			db = null; 
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}//end of Artist constructor
	
	
	/**
	 * This constructor creates an Artist object if you already know all the property values.  Because all the values
	 * are known and passed as parameters, a connection to the database does not need to be created.
	 * @param artistID		the known UUID of the artist
	 * @param firstName		the artist's first name (if needed)
	 * @param lastName		the artist's last name (if needed)
	 * @param bandName		the name of the band (if needed)
	 */
	public Artist(String artistID, String firstName, String lastName, String bandName) {
		
		super();
		this.artistID = artistID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.bandName = bandName;
		this.bio = null;
	
	}
	
	
	/**
	 * This constructor creates an instance of class Artist by taking a known artistID as a parameter.  The program
	 * searches for this artistID within the artist table of the database and attempts to return properties associated 
	 * with that ID.  These returned values are then used to populate the Artist object being created.
	 * @param artistID	the UUID that was generated for a specific Artist
	 */
	public Artist(String artistID) {
		super();
		String sql = "SELECT * FROM artist WHERE artist_id = '" + artistID + "';";
		//System.out.println(sql);
		
		try {
			DbUtilities db = new DbUtilities();
			ResultSet rs = db.getResultSet(sql);
			while(rs.next()) {
				this.artistID = rs.getString("artist_id");
				this.firstName = rs.getString("first_name");
				this.lastName = rs.getString("last_name");
				this.bandName = rs.getString("band_name");
				this.bio = rs.getString("bio");
			}
			db.closeDbConnection();
			db = null;
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}//end of Artist constructor

	/**
	 * deleteArtist method deletes an artist from the database using the artist_id as input.  It then empties the properties of the
	 * Artist object for that specific artist
	 * @param artistID	id of the artist you want to delete
	 */
	public void deleteArtist(String artistID) {
		String sql = "DELETE FROM artist WHERE artist_id = '" + artistID + "';";
		
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			Statement s = conn.createStatement();
			int rowsAffected = s.executeUpdate(sql);
			
			if(rowsAffected != 0) {
				//destroying object by reverting its properties back to default values so that it will be removed by garbage collector
				this.artistID = null;
				this.firstName = null;
				this.lastName = null;
				this.bandName = null;
				this.bio = null;
				
				System.out.println("Artist deletion successful");
			}else {
				System.out.println("Artist was not deleted");
			}
			db.closeDbConnection();
			db = null;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}//end of deleteArtist method 
	

	
	/*define getters and setters for appropriate properties
	getters allow access to the property its made for - returns the property value 
	setters allow the property value to be set 
	*/
	public String getBio() {
		return bio;
	}



	public void setBio(String bio) {
		this.bio = bio;
		
		String sql = "UPDATE artist SET bio = ? WHERE artist_id = ?;";
		
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();		
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, bio);
			ps.setString(2, this.artistID);
			ps.executeUpdate();
			db.closeDbConnection();
			//destroy db object (memory cleanup)
			db = null; 
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to set the bio");
			ErrorLogger.log(e.getMessage());
		}
		
		
	}



	public String getArtistID() {
		return artistID;
	}



	public String getFirstName() {
		return firstName;
	}



	public String getLastName() {
		return lastName;
	}



	public String getBandName() {
		return bandName;
	}


	public void setArtistID(String artistID) {
		this.artistID = artistID;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public void setBandName(String bandName) {
		this.bandName = bandName;
	}
	
	

	
	
	
}//end of Artist class
