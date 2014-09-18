import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
/**
 * @author GAN KOCHI
 *
 */
public class SmartUtility {

	public static String parentFolder = "D:/Smartcard/";
	public static String userFolder = parentFolder + "user/";
	public static String infoFolder = parentFolder + "info/";
	public static String historyFolder = parentFolder + "history/";
	public static String lostCardFolder = parentFolder + "lostCard/";
	public static final String EMPTY = "";
	public static final char BREAK = '#';
	public static final char SEPARATOR = '&';
	public static final String EXTN = ".txt";
	public static String lostFile = "lostFile" + EXTN;
	//Messages
	public static String userExist = "User ID already exists...";
	public static String userNotFound = "UserId does not exists..!!\n Please try again.";
	public static String invalidNumber = "Please enter a number.";
	public static String invalidStation = "Invalid Station Code";
	public static String enterUserId = "Please enter your User id";
	public static String stringMessage = "Please enter alphanumeric characters. \nNo other characters are allowed.";
	public static String integerMessage = "Please enter integer .\nNo other characters are allowed.";
	public static String decimalMessage = "Please enter valid number .\nNo other characters are allowed.";
	public static String validMailMessage = "Please enter valid email address";
	public static String exitMessage = "Thank You for using My Metro Smart Card System";
	public static String rechargeMessage = "Please enter recharge value between 100 and 10 lakh ";
	public static String sameSourceDest = "Source and Destination are the same. Please try again..\n";
	public static String LINE = "------------------------------------------------------------";


	/**
	 * 
	 * @return
	 */
	public static String readConsole() {
		StringBuffer buff = new StringBuffer();
		try {
			BufferedReader bufferRead = new BufferedReader(
					new InputStreamReader(System.in));
			buff.append(bufferRead.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buff.toString();
	}

	/**
	 * Method to read user file and history file based on the input
	 * @param userId
	 * @param userFile
	 * @return
	 * @throws IOException
	 * @throws NullPointerException
	 */
	public static double readFile(String userId, String userFile)
	throws IOException, NullPointerException {
		BufferedReader br = null;
		String sCurrentLine = null;
		String userPath = null;
		double d = 0;
		if (userFile == "user") {
			userPath = SmartUtility.userFolder + userId + EXTN;
			br = new BufferedReader(new FileReader(userPath));
			while ((sCurrentLine = br.readLine()) != null) {
				d = Double.parseDouble(sCurrentLine);
			}
		} else if (userFile == "history") {
			userPath = SmartUtility.historyFolder + userId + EXTN;
			br = new BufferedReader(new FileReader(userPath));
			int value = 0;
			while ((value = br.read()) != -1) {
				char c = (char) value;
				if (c == SEPARATOR)
					System.out.print("\t\t");
				else if (c == BREAK)
					System.out.print("\n");
				else
					System.out.print(c);
			}
		} 
		return d;
	}

	/**
	 * Method to write a file
	 * @param folderPath
	 * @param writeValue
	 * @throws IOException
	 */
	public static void writeFile(String folderPath, String writeValue)
	throws IOException {
		PrintWriter infoFolderCreate = new PrintWriter(new BufferedWriter(
				new FileWriter(folderPath)));
		infoFolderCreate.println(writeValue);
		infoFolderCreate.close();
		return;
	}

	/**
	 * Method to check whether a user id exists or not
	 * @param userId
	 * @return
	 */
	public static boolean isValid(String userId) {
		boolean flag = false;
		String userPath = SmartUtility.userFolder + userId + EXTN;
		File f = null;
		f = new File(userPath);
		if (f.exists()) {
			flag = true;
		}// Print msg here
		return flag;
	}

	/**
	 * Method to check whether the station is valid or nor
	 * @param x
	 * @return
	 */
	public static boolean isValidStation(String station) {
		return (Integer.parseInt(station) > 0 && Integer.parseInt(station) <= 75);
	}

	/**
	 * Method to check whether the current day is weekend or not
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static boolean isWeekend() {
		boolean flag = false;
		Calendar dt = new GregorianCalendar();
		switch (dt.getTime().getMonth()) {// january - 0, aug - 7, oct - 9
		case 0:
			if (dt.getTime().getDate() == 26)
				return true;
			else
				return false;
		case 7:
			if (dt.getTime().getDate() == 15)
				return true;
			else
				return false;
		case 9:
			if (dt.getTime().getDate() == 2)
				return true;
			else
				return false;
		}
		int dayOfWeek = dt.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1 || dayOfWeek == 7) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 
	 * @param userID
	 * @return
	 * @throws NullPointerException
	 * @throws IOException
	 */
	public static boolean checkLostCard(String userID)
	throws NullPointerException, IOException {
		boolean flag = false;
		String userPath = lostCardFolder + lostFile;
		BufferedReader br = null;
		String sCurrentLine = null;
		File f = new File(userPath);
		if (f.exists()) {
			br = new BufferedReader(new FileReader(userPath));
			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.contains(userID)) {
					flag = true;
				}
			}
		}
		return flag;
	}

	/**
	 * deletes all files from a location
	 */
	public static void deleteAll(){		
		deleteAll(userFolder);
		deleteAll(infoFolder);
		deleteAll(historyFolder);
		deleteAll(lostCardFolder);
		deleteAll(parentFolder);
	}

	/**
	 * deletes all files from a location
	 * @param location
	 */
	private static void deleteAll(final String location){
		File file = new File(location);
		if(file.exists() && file.list() != null){
			if (file.list().length == 0) {
				file.delete();
				//System.out.println("Directory is deleted : "+ file.getAbsolutePath());
			}else{
				String files[] = file.list();
				for (String temp : files) {
					File fileDelete = new File(file, temp);
					fileDelete.delete();
				}
				if (file.list().length == 0) {
					file.delete();
					//System.out.println("Directory is deleted : " + file.getAbsolutePath());
				}
			}
		}		
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		return str.matches("^[0-9]+$");
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isString(String str) {
		return str.matches("^[a-zA-Z0-9]+$");
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDecimal(String str) {
		return str.matches("^[0-9]+[.]?[0-9]?[0-9]$");
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isValidMail(String str) {
		return str
		.matches("^[a-zA-Z][a-zA-Z0-9.]*[@][a-zA-Z0-9]+[.][a-zA-Z]{2,3}$");
	}
}
