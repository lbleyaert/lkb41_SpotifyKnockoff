package lkb41_SpotifyKnockoff;

import javax.swing.JOptionPane;

/**
 * TesterJPA Class
 * This class tests the methods of the SongUtilities, ArtistUtilities, and AlbumUtilities classes.
 * @author lbley
 * Created: 02/22/18
 */
public class TesterJPA {

	public static void main(String[] args) {
		
		
		Song newSong = SongUtilities.CreateSong("Listen To The Music", 3.4 , "1970-02-02", "1970-12-12");
		
		SongUtilities.UpdateSong(newSong.getSongID());
		
		Song songToDelete = SongUtilities.CreateSong("Song to be deleted", 0.35, "1990-04-16", "1990-04-19");
		SongUtilities.DeleteSong(songToDelete.getSongID());
		 
		
		
		
		Artist newArtist = ArtistUtilities.CreateArtist("Eddie", "Vedder", "Pearl Jam");
		
		ArtistUtilities.UpdateArtist(newArtist.getArtistID());
		
		Artist artistToDelete = ArtistUtilities.CreateArtist("Artist to be deleted", "last name", "band name");
		ArtistUtilities.DeleteArtist(artistToDelete.getArtistID());
		
		
		
		
		Album newAlbum = AlbumUtilities.CreateAlbum("Busted Stuff", "1998-04-04", "rec company", 11, "rating", 40.30);
		
		AlbumUtilities.UpdateAlbum(newAlbum.getAlbumID());
		
		Album albumToDelete = AlbumUtilities.CreateAlbum("Album to be deleted", "1990-01-20", "a company", 12, "rating", 40.1);
		AlbumUtilities.DeleteAlbum(albumToDelete.getAlbumID());

		
		
	}//end of main method

}
