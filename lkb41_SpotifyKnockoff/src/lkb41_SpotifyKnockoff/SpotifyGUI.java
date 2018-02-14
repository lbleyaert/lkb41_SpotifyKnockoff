package lkb41_SpotifyKnockoff;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.sql.SQLException;

import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
 * Class SpotifyGUI
 * A GUI will be launched and created.
 * @author lbley
 * Created: 02/13/18 
 */
public class SpotifyGUI {

	private JFrame frame;
	private JLabel lblSelectView;
	private JRadioButton radAlbums;
	private JTextField txtSearch;
	private JTable tblData;
	private JRadioButton radArtists;
	private JRadioButton radSongs;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JLabel lblSearch;
	private JButton btnSearch;
	//spotify object in order to call search methods
	private Spotify spfy = new Spotify();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SpotifyGUI window = new SpotifyGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					//e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Unable to launch the application");
					ErrorLogger.log(e.getMessage());
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SpotifyGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Spotify");
		frame.setBounds(100, 80, 1500, 800);
		frame.getContentPane().setLayout(null);
		
		lblSelectView = new JLabel("Select View");
		lblSelectView.setFont(new Font("Tahoma", Font.PLAIN, 26));
		lblSelectView.setBounds(51, 53, 151, 29);
		frame.getContentPane().add(lblSelectView);
		
		radAlbums = new JRadioButton("Albums");
		/*when the radAlbum button is clicked, this handler runs the Spotify searchAlbum method (with empty string as input so 
		 *that the return is a complete list of albums - a DefaultTableModel is returned to our new DefaultTableModel variable 
		 *--the updated DefaultTableModel is then set to our JTable object - repaint used to refresh GUI??*/ 
		radAlbums.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DefaultTableModel updatedTbl = spfy.searchAlbums("");
				 tblData.setModel(updatedTbl);
				 //tblData.repaint();
			}
		});
		buttonGroup.add(radAlbums);
		radAlbums.setSelected(true);
		radAlbums.setFont(new Font("Tahoma", Font.PLAIN, 24));
		radAlbums.setBounds(59, 96, 155, 29);
		frame.getContentPane().add(radAlbums);
		
		txtSearch = new JTextField();
		txtSearch.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtSearch.setBounds(51, 572, 199, 35);
		frame.getContentPane().add(txtSearch);
		txtSearch.setColumns(10);
		
		tblData = new JTable(spfy.searchAlbums(""));
		tblData.setBounds(336, 53, 1083, 634);
		tblData.setFillsViewportHeight(true);
		tblData.setShowGrid(true);
		tblData.setGridColor(Color.BLACK);
		frame.getContentPane().add(tblData);
		
		radArtists = new JRadioButton("Artists");
		/*when the radArtist button is clicked, this handler runs the Spotify searchArtists method (with empty string as input 
		 *so that the return is a complete list of artists - a DefaultTableModel is returned to our new updatedTbl variable 
		 *the updated DefaultTableModel is then set to our JTable object - repaint used to refresh GUI??*/ 
		radArtists.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel updatedTbl = spfy.searchArtists("");
				 tblData.setModel(updatedTbl);
				 //tblData.repaint();
			}
		});
		buttonGroup.add(radArtists);
		radArtists.setFont(new Font("Tahoma", Font.PLAIN, 24));
		radArtists.setBounds(59, 133, 155, 29);
		frame.getContentPane().add(radArtists);
		
		radSongs = new JRadioButton("Songs");
		/*when the radSongs button is clicked, this handler runs the Spotify searchSongs method (with empty string as input so 
		 *that the return is a complete list of songs - a DefaultTableModel is returned to our new updatedTbl variable 
		 *the updated DefaultTableModel is then set to our JTable object - repaint used to refresh GUI??*/ 
		radSongs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel updatedTbl = spfy.searchSongs("");
				 tblData.setModel(updatedTbl);
				 //tblData.repaint();
			}
		});
		buttonGroup.add(radSongs);
		radSongs.setFont(new Font("Tahoma", Font.PLAIN, 24));
		radSongs.setBounds(59, 170, 155, 29);
		frame.getContentPane().add(radSongs);
		
		lblSearch = new JLabel("Search");
		lblSearch.setFont(new Font("Tahoma", Font.PLAIN, 26));
		lblSearch.setBounds(51, 522, 151, 20);
		frame.getContentPane().add(lblSearch);
		
		btnSearch = new JButton("Search");
		/*if the search button is pressed, the following occurs: 
		 *  - depending on which radio button is selected (IF STATEMENT), the corresponding table will be searched (using 
		 *    corresponding search method from spotify class)
		 * 	- if info is entered into the txtSearch field, it will be concatenated to the SQL select statement - will be used
		 *    to find entries 'LIKE' the entered information 
		 *  - search is performed using searchSongs, searchAlbums, or searchArtists method (depend on the pressed radio button)
		 *    of the Spotify class which returns a DefaultTableModel
		 *  - the updated DefaultTableModel is then set to our JTable object - repaint used to refresh GUI??*/
	
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//a new table that stores the updated information - updates the JTable tblData
				DefaultTableModel updatedTbl = new DefaultTableModel();
	
				if(radSongs.isSelected()) {
					 updatedTbl = spfy.searchSongs(txtSearch.getText());
					 tblData.setModel(updatedTbl);
					 //tblData.repaint();
					 
				}else if(radAlbums.isSelected()) {
					updatedTbl = spfy.searchAlbums(txtSearch.getText());
					tblData.setModel(updatedTbl);
					//tblData.repaint();
				
				}else if(radArtists.isSelected()) {
					updatedTbl = spfy.searchArtists(txtSearch.getText());
					tblData.setModel(updatedTbl);
					//tblData.repaint();
				}
			}//end of btnSearch actionPerformed
		});//end of addActionListener
		
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnSearch.setBounds(135, 633, 115, 29);
		frame.getContentPane().add(btnSearch);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

/*
	private DefaultTableModel getSongData(String searchTerm) {
		String sql = "SELECT song_id, title, length, release_date, record_date FROM song;";
		if(!searchTerm.equals("")){
			sql += " WHERE title LIKE '%" + searchTerm + "%';";
		}

		try {
			DbUtilities db = new DbUtilities();
			
			String[] columnNames = {"Song ID", "Title", "Length", "Release Date", "Record Date"};
			return db.getDataTable(sql, columnNames);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Unable to connect to database");
			ErrorLogger.log(e.getMessage());
		}
		return null;
	}
*/	
	
}//end of SpotifyGUI class
