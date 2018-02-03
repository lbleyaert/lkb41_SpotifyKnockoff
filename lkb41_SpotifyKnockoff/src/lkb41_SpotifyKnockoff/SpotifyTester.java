package lkb41_SpotifyKnockoff;


public class SpotifyTester {

	public static void main(String[] args) {
		
		//song that will be deleted 
		Song mySong = new Song("TEST", 2.4, "1993-12-03", "1994-12-05");
		
		Song once = new Song("Once", 2.4, "1990-09-12", "1990-10-12");
		once.setFilePath("good//path//etc");
		
		//known songID from database:
		final String ALIVE_ID = "06984e3d-8d9f-4944-b24e-45ebf4deb913";
		Song alive = new Song(ALIVE_ID, "Alive", 3.4, "1990-08-27", "1990-05-04");
		//System.out.println("Song pulled with all info: " + alive.getTitle());
		
		//known songID from database:
		final String EVEN_FLOW_ID = "08209635-9fcb-4d23-b924-247f6f81296a";
		Song songThree = new Song(EVEN_FLOW_ID);
		//System.out.println("Song pulled with just ID: " + songThree.getTitle()); 

		Song pullSong = new Song(mySong.getSongID());
		pullSong.deleteSong(pullSong.getSongID());
		
		
		
		Artist myArtist = new Artist("TEST", "TEST", "TEST");
		myArtist.deleteArtist(myArtist.getArtistID());
		
		//known artistID from database:
		final String DUANE_ALLMAN_ID = "e9998612-0db0-446a-b293-27a2c21d41fc";
		Artist test = new Artist(DUANE_ALLMAN_ID, "Duane", "Allman", null);
	
		//known artistID from database:
		final String PEARL_JAM_ID = "47384f34-318d-4fed-93a6-d67205428161";
		Artist pj = new Artist(PEARL_JAM_ID);
		//System.out.println("Artist: " + pj.getBandName());
		pj.setBio("good artist");
		
		once.addArtist(pj);
		once.addArtist(test);

		//once.deleteArtist(pj.getArtistID());
		//once.deleteArtist(test);

	

		
		//create album with full constructor
		Album albumOne = new Album("Idlewild South", "1970-09-23", "Capricorn Records", 7, "rating", 30.8);
		
		albumOne.setCoverImagePath("path//path//etc");
		
		//create album object with the ID of the album above - this new Album object will be populated with the properties of
		//the above album
		Album albumTwo = new Album(albumOne.getAlbumID());
		//System.out.println("album info pulled from db:\nID: " + albumTwo.getAlbumID() + "\ntitle: " + albumTwo.getTitle());
		
		//create album - constructor that does not connect to db
		String TEN_ID = "110f5fca-6d84-4fdc-8b86-cfd71b850957";
		Album ten = new Album(TEN_ID,"Ten", "1991-08-27", "London Bridge Studios", 11, "rating", 53.33);
		
		
		
		//create songs to add to the album - test song will be deleted 
		Song songToAdd = new Song("Midnight Rider", 2.95, "1971-03-26", "1970-12-20");
		Song songToRemove = new Song("SONG TO DELETE FROM ALBUM", 2.00, "1970-10-03", "1960-03-21");
		Song songToRemoveTwo = new Song("OTHER SONG TO DELETE FROM ALBUM", 3.00, "1970-10-03", "1960-03-21");
		albumOne.addSong(songToAdd);
		albumOne.addSong(songToRemove);
		albumOne.addSong(songToRemoveTwo);
		
		
		//testing both delete methods
		albumOne.deleteSong(songToRemove);
		albumOne.deleteSong(songToRemoveTwo.getSongID());
		
		
	
		
		
	}//end of main method

}//end of SpotifyTester class
