package lkb41_SpotifyKnockoff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Class Album
 * Allows you to create instances of the Album class (as well as an instance in the album table of database) using a variety of
 * parameters as well as delete albums from the database.  You can also add or delete songs from the albums's list of songs.
 * This will add or delete both within the Album object's Hashtable and the database album_song table.
 * @author lbley
 * Created: 1/23/18
 */
public class Album {

	private String albumID;
	private String title;
	private String releaseDate;
	private String coverImagePath;
	private String recordingCompany;
	private int numberOfTracks;
	private String pmrcRating;
	private double length;
	Map<String, Song> albumSongs;
	
	
	/**
	 * This constructor creates an instance of class Album using the parameters passed through.  The constructor 
	 * also assigns a random UUID to the Album object which will be its albumID.  It then creates an SQL INSERT 
	 * statement using these parameters and attempts to insert the record into the album table of the database 
	 * @param title				the title of the album
	 * @param releaseDate		the date the album was release
	 * @param recordingCompany	the company the album was recorded with
	 * @param numOfTracks		the number of tracks on the album
	 * @param pmrcRating		the parental rating of the album
	 * @param length			the length of the album (in minutes)
	 */
	public Album(String title, String releaseDate, String recordingCompany, int numOfTracks, String pmrcRating, double length) {
		
		this.albumID = UUID.randomUUID().toString();
		this.title = title;
		this.releaseDate = releaseDate;
		this.recordingCompany = recordingCompany;
		this.numberOfTracks = numOfTracks;
		this.pmrcRating = pmrcRating;
		this.length = length;
		albumSongs = new Hashtable<String, Song>();
		
		
		String sql = "INSERT INTO album(album_id, title, release_date, cover_image_path, recording_company_name, number_of_tracks, "
				+ "PMRC_rating, length) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
		

		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.albumID);
			ps.setString(2, this.title);
			ps.setString(3, this.releaseDate);
			ps.setString(4, "");
			ps.setString(5, this.recordingCompany);
			ps.setInt(6, this.numberOfTracks);
			ps.setString(7, this.pmrcRating);
			ps.setDouble(8, this.length);
			ps.executeUpdate();
			db.closeDbConnection();
			//destroy db object (memory cleanup)
			db = null; 
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}//end of Album constructor
	
	/**
	 * This constructor creates an Album object if you already know all the property values.  Because all the values
	 * are known and passed as parameters, a connection to the database does not need to be created.  This constructor must also
	 * search the album_song table of the database for the album_id to find any associated songs.  Songs associated with this album
	 * will be added to the albumSongs hashtable of the object
	 * @param albumID			the known UUID of the album
	 * @param title				the title of the album
	 * @param releaseDate		the date the album was released
	 * @param recordingCompany	the company the album was recorded with
	 * @param numOfTracks		the number of tracks on the album
	 * @param pmrcRating		the parental rating of the album
	 * @param length			the length of the album (in minutes)
	 */
	public Album(String albumID, String title, String releaseDate, String recordingCompany, int numOfTracks, String pmrcRating, double length) {
		
		this.albumID = albumID;
		this.title = title;
		this.releaseDate = releaseDate;
		this.recordingCompany = recordingCompany;
		this.numberOfTracks = numOfTracks;
		this.pmrcRating = pmrcRating;
		this.length = length;
		albumSongs = new Hashtable<String, Song>();
		//SHOULD THIS CONSTRUCTOR CONNECT TO DB TO POPULATE THE ALBUMSONGS HASHTABLE WITH ANY RELATED SONGS?
	
	}
	
	
	/**
	 * This constructor creates an instance of class Album by taking a known albumID as a parameter.  The program
	 * searches for this albumID within the album table of the database and attempts to return properties associated 
	 * with that ID.  These returned values are then used to populate the Album object being created.  The constructor
	 * also searches the album_song table to see if this albumID is associated with songs.  Those songs will need to
	 * be added to the albumSongs Hashtable
	 * @param albumID	the UUID that was generated for a specific album
	 */
	public Album(String albumID) {
		
		albumSongs = new Hashtable<String, Song>();
		
		String sql = "SELECT * FROM album WHERE album_id = '" + albumID + "';";
		String hashtableInfo = "SELECT * FROM album_song WHERE fk_album_id = '" + albumID + "';";

		try {
			DbUtilities db = new DbUtilities();
			ResultSet rs = db.getResultSet(sql);
			while(rs.next()) {
				this.albumID = rs.getString("album_id");
				this.title = rs.getString("title");
				this.releaseDate = rs.getDate("release_date").toString();
				this.coverImagePath = rs.getString("cover_image_path");
				this.recordingCompany = rs.getString("recording_company_name");
				this.numberOfTracks = rs.getInt("number_of_tracks");
				this.pmrcRating = rs.getString("PMRC_rating");
				this.length = rs.getDouble("length");
			}
			
			
			/*if an album is created and info is pulled from the db, we need to pull info from the album_song table about that
			 * ablum and what songs are associated with it - we have to populate this created Album object's hashtable property
			 * with this information.  The following code gets a ResultSet that searches for instances in the album_song table
			 * that include both album_id and song_id - if no rows are returned, the while loop is skipped.  If rows are returned,
			 * the while loop goes by each row, creates an Song object with the song_id from the table that matched with the 
			 * album_id.  It then inserts the song_id (key) and Song object (value) into the hashtable.  Repeats until no rows
			 * are left.
			 *  */
			ResultSet hashResults = db.getResultSet(hashtableInfo);
			while(hashResults.next()) {
				//extra db connection in next step????????
				Song thisSong = new Song(hashResults.getString("fk_song_id"));
				albumSongs.put(thisSong.getSongID(), thisSong);
			}
			
			db.closeDbConnection();
			db = null;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	/**
	 * deleteAlbum method deletes the album specified by the albumID input.  It deletes the instance from the album table in the 
	 * database.  The Album object with that albumID is then emptied (variables all set to null and 0?).  This method also sets the 
	 * fk_album_id in the song table to an empty string IF the fk_album_id matches the one being deleted.
	 * @param albumID	the UUID of the album to be deleted 
	 */
	public void deleteAlbum(String albumID) {
		
		String sql = "DELETE FROM album WHERE album_id = '" + albumID + "';";
		
		String removeFromSongTable = "DELETE FROM album_song WHERE fk_album_id = '" + albumID + "';";
	
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			Statement sqlSong = conn.createStatement();
			sqlSong.executeUpdate(removeFromSongTable);
			
			Statement s = conn.createStatement();
			int rowsAffected = s.executeUpdate(sql);
			if(rowsAffected != 0) {
				
				//destroying object by reverting its properties back to default values so that it will be removed by garbage collector
				this.albumID = null;
				this.title = null;
				this.releaseDate = null;
				this.coverImagePath = null;
				this.recordingCompany = null;
				this.numberOfTracks = 0;
				this.pmrcRating = null;
				this.length = 0.0;
				albumSongs = null;
				
				System.out.println("Album deletion successful");
			}else {
				System.out.println("Album was not deleted");
			}
			db.closeDbConnection();
			db = null;
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}//end of deleteAlbum method
	
	/**
	 * addSong method receives a song object and adds that song to the Album object's albumSongs hashtable.  It also adds an instance 
	 * to the album_song table with this song's id and this album's id to relate them (M:N junction table)
	 * @param song	Song object to be added to the album
	 */
	public void addSong(Song song) {
		
		albumSongs.put(song.getSongID(), song);
		
		
		String sql = "INSERT INTO album_song(fk_album_id, fk_song_id) VALUES (?, ?);";

		try {		
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.albumID);
			ps.setString(2, song.getSongID());
			ps.executeUpdate();
			db.closeDbConnection();
			db = null;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		Set<String> keys = albumSongs.keySet();
		System.out.println("LIST OF SONGS:");
		for(String key: keys) {
			System.out.println(albumSongs.get(key).getTitle());
		}
		*/
		
	}//end of addSong method
	
	
	/**
	 * deleteSong method deletes a Song from the albumSong hashtable using the songID input.  It also removes any instance from the 
	 * album_songs junction table that relates this album with the inputted songID 
	 * @param songID	id of song to be deleted from the album
	 */
	public void deleteSong(String songID) {
		
		Song removeSong = new Song(songID);
		albumSongs.remove(songID, removeSong);
		
		String sql = "DELETE FROM album_song WHERE fk_album_id = ? AND fk_song_id = ?;";
	
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.albumID);
			ps.setString(2, songID);
			ps.executeUpdate();
			db.closeDbConnection();
			db = null;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * deleteSong method takes a Song object as a parameter and removes that song from the albumSong hashtable of the specific album.
	 * It also deletes any instance in the album_song table of the database that relates the song and the album (remove from M:N 
	 * junction table)
	 * @param song	the Song object to be deleted from the album's albumSong hashtable 
	 */
	public void deleteSong(Song song) {
		
		albumSongs.remove(song.getSongID(), song);
		
		String sql = "DELETE FROM album_song WHERE fk_album_id = ? AND fk_song_id = ?;";
			
		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();		
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.albumID);
			ps.setString(2, song.getSongID());
			ps.executeUpdate();
			db.closeDbConnection();
			db = null;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	/*define getters and setters for appropriate properties
	getters allow access to the property its made for - returns the property value 
	setters allow the property value to be set 
	*/
	public String getCoverImagePath() {
		return coverImagePath;
	}

	public void setCoverImagePath(String coverImagePath) {
		this.coverImagePath = coverImagePath;
		
		String sql = "UPDATE album SET cover_image_path = ? WHERE album_id = ?;";
		

		try {
			DbUtilities db = new DbUtilities();
			Connection conn = db.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, coverImagePath);
			ps.setString(2, this.albumID);
			ps.executeUpdate();
			db.closeDbConnection();
			//destroy db object (memory cleanup)
			db = null; 
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	public String getAlbumID() {
		return albumID;
	}

	public String getTitle() {
		return title;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public String getRecordingCompany() {
		return recordingCompany;
	}

	public int getNumberOfTracks() {
		return numberOfTracks;
	}

	public String getPmrcRating() {
		return pmrcRating;
	}

	public double getLength() {
		return length;
	}

	public Map<String, Song> getAlbumSongs() {
		return albumSongs;
	}
	
	
	
	
}//end of Album class 
