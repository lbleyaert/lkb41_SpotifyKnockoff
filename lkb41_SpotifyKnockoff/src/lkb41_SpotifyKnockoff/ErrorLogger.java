package lkb41_SpotifyKnockoff;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Class ErrorLogger
 * To use for pasting error message in the errorlog text file 
 * @author lbley
 * Created: 01/30/18
 */
public class ErrorLogger {

	public static void log(String errorMessage) {
		
		//link pasted at bottom of class for where I found DateTimeFormatter info
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime currentDateTime = LocalDateTime.now();
		
		//save the following information to errorlog.txt
		
		//date, time, errorMessage \n
		
		try {
			//"C:\\Users\\lbley\\OneDrive\\Documents\\IS1017-workspace\\lkb41_SpotifyKnockoff\\src\\lkb41_SpotifyKnockoff\\errorlog.txt"
			System.out.println(errorMessage);
			FileWriter fw = new FileWriter("src\\lkb41_SpotifyKnockoff\\errorlog.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			String msg = dtf.format(currentDateTime) + "," + errorMessage + "\n";
			System.out.println(msg);
			bw.write(msg);
			bw.close();
			fw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
//DateTimeFormatter link:	https://www.mkyong.com/java/java-how-to-get-current-date-time-date-and-calender/
}
