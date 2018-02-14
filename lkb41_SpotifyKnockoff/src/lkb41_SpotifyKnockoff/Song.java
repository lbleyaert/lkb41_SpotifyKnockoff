package lkb41_SpotifyKnockoff;

import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Class Song
 * Allows you to create instances of the Song class (as well as an instance in the song table of database) using a variety of
 * parameters as well as delete songs from the database.  You can also add or delete artists from the song's list of collaborators.
 * This will add or delete them both within the object Hashtable and the database song_artist table.
 * @author lbley
 * Created: 01/23/18
 */
public class Song {

	private String songID;
	private String title;
	private double length;
	private String filePath;
	private String releaseDate; 
	private String recordDate;
	private Map<String, Artist> songArtists;
	
	/**
	 * This constructor creates an instance of class Song using the parameters passed through.  The constructor 
	 * also assigns a random UUID to the Song object which will be its songID.  It then creates an SQL INSERT 
	 * statement using these parameters and attempts to insert the record into the song table of the database 
	 * @param title			the title of the song
	 * @param length		the length of the song (in minutes)
	 * @param releaseDate	the date the song was released
	 * @param recordDate	the date the song was recorded
	 */
	public Song(String title, double length, String releaseDate, String recordDate) {
		this.title = title;
		this.length = length;
		this.releaseDate = releaseDate;
		this.recordDate = recordDate;
		//need to generate unique identifier for each Song that is entered
		this.songID = UUID.randomUUID().toString();
		
		this.songArtists = new Hashtable<String, Artist>();

		String sql = "INSERT INTO song(song_id, title, length, file_path, release_date, record_date) ";
		sql += "VALUES (?, ?, ?, ?, ?, ?);";
		
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.songID);
			ps.setString(2, this.title);
			ps.setDouble(3, this.length);
			ps.setString(4, "");
			ps.setString(5, this.releaseDate);
			ps.setString(6, this.recordDate);
			ps.executeUpdate();
			
			db.closeDbConnection();
			//destroy db object (memory cleanup)
			db = null; 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}//end of Song constructor
	
	
	
	/**
	 * This constructor creates a Song object if you already know all the property values.  Because all the values
	 * are known and passed as parameters, a connection to the database does not need to be created.
	 * @param songID		the known UUID for the song
	 * @param title			the title of the song
	 * @param length		the length of the song (in minutes)
	 * @param releaseDate	the date the song was released
	 * @param recordDate	the date the song was recorded
	 */
	public Song(String songID, String title, double length, String releaseDate, String recordDate) {
		this.songID = songID;
		this.title = title;
		this.length = length;
		this.releaseDate = releaseDate;
		this.recordDate = recordDate;
		
		this.songArtists = new Hashtable<String, Artist>();
	}
	
	
	/**
	 * This constructor creates an instance of class Song by taking a known songID as a parameter.  The program
	 * searches for this songID within the song table of the database and attempts to return properties associated 
	 * with that ID.  These returned values are then used to populate the Song object being created.  The constructor
	 * also searches the song_artist table for this songID in order to populate the songArtist Hashtable property
	 * @param songID	the UUID that was generated for a specific song
	 */
	public Song(String songID) {
		
		this.songArtists = new Hashtable<String, Artist>();
		
		String sql = "SELECT * FROM song WHERE song_id = '" + songID + "';";
		//want to populate the songArtist Hashtable of the object as well - need to look at song_artist table for this song
		String hashtableInfo = "SELECT * FROM song_artist WHERE fk_song_id = '" + songID + "';";
		
		//System.out.println(sql);
		try {
			DbUtilities db = new DbUtilities();
			ResultSet rs = db.getResultSet(sql);
			while(rs.next()) {
				this.songID = rs.getString("song_id");
				this.title = rs.getString("title");
				this.filePath = rs.getString("file_path");
				this.releaseDate = rs.getDate("release_date").toString();
				this.recordDate = rs.getDate("record_date").toString();
				this.length = rs.getDouble("length");
			}
			
			/*if a song is created and info is pulled from the db, we need to pull info from the song_artist table about that
			 * song and what artists are associated with it - we have to populate this created Song object's hashtable property
			 * with this information.  The following code gets a ResultSet that searches for instances in the song_artist table
			 * that include both song_id and artist_id - if no rows are returned, the while loop is skipped.  If rows are returned,
			 * the while loop goes by each row, creates an Artist object with the artist_id from the table that matched with the 
			 * song_id.  It then inserts the artist_id (key) and Artist object (value) into the hashtable.  Repeats until no rows
			 * are left.
			 *  */
			ResultSet hashResults = db.getResultSet(hashtableInfo);
			while(hashResults.next()) {
				Artist thisArtist = new Artist(hashResults.getString("fk_artist_id"));
				songArtists.put(thisArtist.getArtistID(), thisArtist);
			}
			
			db.closeDbConnection();
			db = null;
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//end of Song constructor
	
	
	
	/**
	 * deleteSong method deletes a song from the database using the song_id.  It also deletes any instances of the song within
	 * the song_artist table of the database (so no orphans are left).  The Song object with that songID is then emptied (set to
	 * null)
	 * @param songID	the UUID of the song to be deleted 
	 */
	public void deleteSong(String songID) {
		
		//Song toDelete = new Song(songID);
		
		String sql = "DELETE FROM song WHERE song_id = '" + songID + "';";
		//need to delete from song_artist table as well? or will the delete cascade?
		String sqlSongArtistTable = "DELETE FROM song_artist WHERE fk_song_id = '" + songID + "';";
		
		try {

			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			Statement sa = conn.createStatement();
			sa.executeUpdate(sqlSongArtistTable);
			
			Statement s = conn.createStatement();
			int rowsAffected = s.executeUpdate(sql);
			if(rowsAffected != 0) {
				
				//destroying object by reverting its properties back to default values so that it will be removed by garbage collector
				this.songID = null;
				this.title = null;
				this.length = 0.0;
				this.filePath = null;
				this.releaseDate = null;
				this.recordDate = null;
				this.songArtists = null;
				
				System.out.println("Song deletion successful");
			}else {
				System.out.println("Song was not deleted");
			}
			db.closeDbConnection();
			db = null;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}//end of deleteSong method

	
	/**
	 * This method first adds a new Artist object to the list of the song's Artists (add to hashtable where key is artistID string
	 * and value is Artist object.  The method then adds a row to the song_artist table of the database with the songID and artistID
	 * to acknowledge the relationship between the song and the artist
	 * @param artist	the Artist object 
	 */
	public void addArtist(Artist artist) {
		
		songArtists.put(artist.getArtistID(), artist);
		
		String sql = "INSERT INTO song_artist(fk_song_id,fk_artist_id) VALUES (?, ?);";
		
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.songID);
			ps.setString(2, artist.getArtistID());
			ps.executeUpdate();
			db.closeDbConnection();
			db = null;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		Set<String> keys = songArtists.keySet();
		System.out.println("LIST OF COLLABORATORS:");
		for(String key: keys) {
			System.out.println(songArtists.get(key).getBandName() + " " + songArtists.get(key).getFirstName() 
					+ " "  + songArtists.get(key).getLastName()); 
		}
		*/
		
	}//end of addArtist method
	
	
	/**
	 * This method deletes an artist from the list of the song's artists (deletes it from the songArtist hashtable) based on the 
	 * artist ID. It then deletes the instance in the song_artist table of the database that relates the songID with the artistID. 
	 * This does not delete the Artist from the database (will still be found in artist table)
	 * @param artistID	the assigned string ID of an Artist object
	 */
	public void deleteArtist(String artistID) {
		
		Artist removeArtist = new Artist(artistID);
		songArtists.remove(artistID, removeArtist);
		
		
		String sql = "DELETE FROM song_artist WHERE fk_song_id = ? AND fk_artist_id = ?;";
		
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.songID);
			ps.setString(2, artistID);
			ps.executeUpdate();
			db.closeDbConnection();
			db = null;
			
			System.out.println("Artist with ID " + artistID + " is no longer associated with song ID " + this.getSongID());
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	/**
	 * This method deletes an artist from the list of the song's artists (deletes it from the songArtist hashtable) using an Artist 
	 * object as input. It then deletes the instance in the song_artist table of the database that relates the songID with the 
	 * artistID. This does not delete the Artist from the database (will still be found in artist table)
	 * @param artist	Artist object that you wish to be removed from the songArtist hashtable and song_artist relationship
	 */
	public void deleteArtist(Artist artist) {
		
		this.songArtists.remove(artist.getArtistID(), artist);
		
		String sql = "DELETE FROM song_artist WHERE fk_song_id = ? AND fk_artist_id = ?;";
		
		
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.songID);
			ps.setString(2, artist.getArtistID());
			ps.executeUpdate();
			db.closeDbConnection();
			db = null;
			
			System.out.println("Artist with ID " + artist.getArtistID() + " is no longer associated with song ID " + this.getSongID());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

	/*define getters and setters for appropriate properties
	getters allow access to the property its made for - returns the property value 
	setters allow the property value to be set 
	*/
	
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Along with updating the Object property, the setter needs to also update the file_path property within song's database record 
	 * @param filePath	the file path you would like to add to a specific song 
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
		
		String sql = "UPDATE song SET file_path = ? WHERE song_id = ?;";

		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, filePath);
			ps.setString(2, this.songID);
			ps.executeUpdate();
			db.closeDbConnection();
			//destroy db object (memory cleanup)
			db = null; 		
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}


	public Map<String, Artist> getSongArtists() {
		return songArtists;
	}


	public String getSongID() {
		return songID;
	}


	public String getTitle() {
		return title;
	}


	public double getLength() {
		return length;
	}


	public String getReleaseDate() {
		return releaseDate;
	}


	public String getRecordDate() {
		return recordDate;
	}

	
	
	
}//end of class Song
