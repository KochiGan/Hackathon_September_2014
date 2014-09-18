import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

/**
 * @author GAN KOCHI
 *
 */
public class FileBasedSmartProcess {

	private String inputChoice;
	private static String userID;
	private String password;
	private String mailID;
	private double availBalance;
	private double fare;

	/**
	 * reset
	 */
	private void reset(){
		inputChoice = "";
		userID = "";
		password = "";
		mailID = "";
		availBalance = 0;
		fare = 0;
	}

	/**
	 * displayScreen
	 */
	private void displayScreen(){
		System.out.println("\nWelcome to ‘My Metro Smart Card System’ (M2SCS)");
		System.out.println("Please select your option:");
		System.out.println("1. New User");
		System.out.println("2. Journey");   
		System.out.println("3. Recharge SmartCard");
		System.out.println("4. View Current Balance");
		System.out.println("5. Know My History ");
		System.out.println("6. Help");
		System.out.println("Press any other key to EXIT");
	}

	/**
	 * This method is to print the console
	 * @throws IOException
	 */
	private void home() throws IOException{
		this.displayScreen();
		try{
			inputChoice=(SmartUtility.readConsole());
			if(!SmartUtility.isInteger(inputChoice)){
				System.out.println(SmartUtility.exitMessage);
				return;
			}
			switch (Integer.parseInt(inputChoice)) {
			case 1:
				newUser();
				break;
			case 2:
				bookTicket();
				break;
			case 3:
				recharge();
				break;
			case 4:
				viewBalance();
				break;
			case 5:
				viewHistory();
				break;
			case 6:
				contactHelp();
				break;
			default:
				System.out.println(SmartUtility.exitMessage);
			break;
			}
		}catch(Exception e){				

			e.printStackTrace();
		}finally{
			this.reset();
			this.deleteAll();
		}
	}

	/**
	 * 
	 * @throws IOException
	 */
	private void createlostCard() throws IOException {
		if(new File(SmartUtility.lostCardFolder).mkdirs()){
			SmartUtility.writeFile(SmartUtility.lostCardFolder + SmartUtility.lostFile, "777#");
		}
	}

	/**
	 * Method to book ticket using Userid, source and destination.
	 * This method validates the user Id, source and destination
	 * The history of the journey is stored in history file of the user.
	 * @throws IOException 
	 * 
	 */
	private void bookTicket() throws IOException {
		try {
			System.out.println(SmartUtility.enterUserId);
			userID = SmartUtility.readConsole();
			if(!SmartUtility.isString(userID)){
				System.out.println(SmartUtility.stringMessage);
				this.home();			
				return;
			}
			if (!SmartUtility.isValid(userID)) {
				System.out.println(SmartUtility.userNotFound);
				this.home();			
				return;
			}
			if (SmartUtility.checkLostCard(userID)) {
				System.out.println("Smartcard with user id: "+userID+" reported as lost/damaged..!!\n");
				this.home();			
				return;
			}
			System.out.println("\nEnter SRC(1 - 75)");
			String source = SmartUtility.readConsole();

			if(!SmartUtility.isInteger(source)){
				System.out.println(SmartUtility.integerMessage);
				this.home();			
				return;
			}
			if (!SmartUtility.isValidStation(source)) {
				System.out.println(SmartUtility.invalidStation);
				this.home();			
				return;
			}
			System.out.println("\nEnter Destination(1-75)");
			String destination = SmartUtility.readConsole();

			if(!SmartUtility.isInteger(destination)){
				System.out.println(SmartUtility.integerMessage);
				this.home();			
				return;
			}
			if (destination.equalsIgnoreCase(source)) {
				System.out.println("Source and Destination are the same. Please try again..\n");
				this.home();			
				return;
			}
			if (!SmartUtility.isValidStation(destination)) {
				System.out.println(SmartUtility.invalidStation);
				this.home();			
				return;
			}
			availBalance = (SmartUtility.readFile(userID, "user"));
			if (SmartUtility.isWeekend()) {
				this.calculateBalance(3.50,source,destination);
			} else if (!SmartUtility.isWeekend()) {
				this.calculateBalance(5.0,source,destination);
			}
			writeHistory(SmartUtility.historyFolder + userID + SmartUtility.EXTN, source, destination, new Date());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to calculate balance of the user and 
	 * to check whether the user is allowed for the journey 
	 * @param source
	 * @param destination
	 * @throws IOException
	 */
	private void calculateBalance(double value,String source,String destination) throws IOException {
		fare = 40 * value;
		if (availBalance < (fare)) {
			System.out.println("Available Balance is : " + availBalance
					+ "\nBalance not sufficient to make a travel. Kindly recharge your card.\n\n");
			this.home();			
			return;
			
		}
		else {
			availBalance -= (double)Math.abs((Long.parseLong(source) - Long
					.parseLong(destination))) * 5;
			SmartUtility.writeFile(SmartUtility.userFolder + userID + SmartUtility.EXTN,
					String.valueOf(availBalance));
			System.out.println("Smart Card "+userID +" used to travel from station "+source +" to station "+ destination);
			System.out.println("\nHAPPY JOURNEY.. \n\nYour current balance is :"
					+ availBalance + "\n\n");
		}
	}

	/**
	 * This method is to print the contact details for customer support
	 * @throws IOException
	 */
	private void contactHelp() throws IOException{
		System.out.println("Kindly contact 999-9999999 for customer support");
		System.out.println("Mail your queries to: customerSupport@smartcard.com");
		this.home();
		return;
	}

	/**
	 * This method is to view the current balance of the user
	 * @throws IOException
	 */
	private void viewBalance() throws IOException{
		System.out.println(SmartUtility.enterUserId);
		userID = SmartUtility.readConsole();
		if(!SmartUtility.isString(userID)){
			System.out.println(SmartUtility.stringMessage);
			this.home();			
			return;
		}
		if (!SmartUtility.isValid(userID)) {
			System.out.println(SmartUtility.userNotFound);
			this.home();
			return;
		} else {
			System.out.println("Current Balance is:"+ (SmartUtility.readFile(userID, "user")) + "\n");
			this.home();
			return;
		}
	}


	/**
	 * This method is to view history of the user
	 * @throws IOException
	 */
	private void viewHistory() throws IOException{
		System.out.println(SmartUtility.enterUserId);
		userID = SmartUtility.readConsole();
		if(!SmartUtility.isString(userID)){
			System.out.println(SmartUtility.stringMessage);
			this.home();
			return;
		}
		if (!SmartUtility.isValid(userID)) {
			System.out.println(SmartUtility.userNotFound);
			this.home();			
			return;
		} else {
			System.out.println("Source \t    Destination \t\tDate Of Journey");
			System.out.println(SmartUtility.LINE);

			SmartUtility.readFile(userID, "history");
			this.home();
			return;
		}
	}


	/**
	 * This method is to write last five travel history details 
	 * of the user
	 * @param folderPath
	 * @param source
	 * @param destination
	 * @param date
	 * @throws IOException
	 */
	private void writeHistory(String folderPath, String source,
			String destination, Date date) throws IOException {
		String fileInfo = "";
		File file = new File(SmartUtility.historyFolder + userID
				+ SmartUtility.EXTN);
		int numberOfRecords = 0;
		FileInputStream fis = new FileInputStream(file);
		char current;
		while (fis.available() > 0) {
			current = (char) fis.read();
			if (current == SmartUtility.BREAK) {
				numberOfRecords++;
			}
		}
		if (numberOfRecords < 5) {
			fileInfo = readFileHistory();			
		} else {
			fileInfo = readFileHistory();
			int pos = fileInfo.indexOf(SmartUtility.BREAK);
			fileInfo = (fileInfo.substring(pos + "#".length()));
		}
		StringBuffer strbuf;
		strbuf = new StringBuffer(fileInfo);
		strbuf.append(source + SmartUtility.SEPARATOR + destination
				+ SmartUtility.SEPARATOR + date + SmartUtility.BREAK);
		SmartUtility.writeFile(SmartUtility.historyFolder + userID
				+ SmartUtility.EXTN, strbuf.toString());
		this.home();
		return;
	}

	/**
	 *	Method for reading file from history folder
	 * @return
	 * @throws IOException
	 */	      
	private String readFileHistory() throws IOException {
		BufferedReader br = null;
		String sCurrentLine = null;
		String userPath = null;
		String fileInfo = "";
		userPath = SmartUtility.historyFolder + userID + SmartUtility.EXTN;
		br = new BufferedReader(new FileReader(userPath));
		while ((sCurrentLine = br.readLine()) != null) {
			fileInfo += sCurrentLine;
		}
		return fileInfo;
	}

	/**
	 * Method for recharging the smartcard
	 * @throws IOException
	 */
	private void recharge() throws IOException {
		System.out.println(SmartUtility.enterUserId);
		userID = SmartUtility.readConsole();
		if(!SmartUtility.isString(userID)){
			System.out.println(SmartUtility.stringMessage);
			this.home();
			return;
		}
		if (!SmartUtility.isValid(userID)) {
			System.out.println("UserId doesnot exists..!!\n Please try again.");
			this.home();
			return;
		} else {
			System.out.println("Please enter the recharge amount :");
			String rechargeAmount = (SmartUtility.readConsole());
			if(!SmartUtility.isDecimal(rechargeAmount)){
				System.out.println(SmartUtility.decimalMessage);
				this.home();
				return;
			}
			if(Double.valueOf(rechargeAmount)<100 || Double.valueOf(rechargeAmount)>1000000){
				System.out.println(SmartUtility.rechargeMessage);
				this.home();
				return;
			}
			availBalance = (SmartUtility.readFile(userID, "user"));
			if (availBalance > 1000000) {
				System.out.println("Available Balance is : " + availBalance
						+ "\nBalance cant exceed 10 lakh INR.\n Recharge unsuccessful.\n");
				this.home();			
				return;
			}
			availBalance += Double.parseDouble(rechargeAmount);
			SmartUtility.writeFile(SmartUtility.userFolder + userID + SmartUtility.EXTN, String.valueOf(availBalance));
			System.out.println("Successfully Recharged..\nCurrent Balance is:"
					+ (SmartUtility.readFile(userID, "user")) + "\n");
			this.home();
			return;
		}
	}	

	/**
	 * Method for user registration and storing their  details in three different files     
	 * @throws IOException
	 */
	private void newUser() throws IOException {
		System.out.println(SmartUtility.enterUserId);
		userID = SmartUtility.readConsole();
		if(!SmartUtility.isString(userID)){
			System.out.println(SmartUtility.stringMessage);
			this.home();
			return;
		}
		if (SmartUtility.checkLostCard(userID)) {
			System.out.println("Smartcard with user id: "+userID+" reported as lost/damaged..!!\n");
			this.home();			
			return;
		}
		try{      
			new File(SmartUtility.userFolder).mkdirs();
			new File(SmartUtility.infoFolder).mkdirs();
			new File(SmartUtility.historyFolder).mkdirs();
		}catch(Exception e){
			e.printStackTrace();
		}
		if (SmartUtility.isValid(userID)) {
			System.out.println("UserId already exists..!!\n Please try again.");
			this.home();
			return;
		}
		System.out.println("Please enter your password");
		password = SmartUtility.readConsole();
		System.out.println("Please enter your mail id"); 
		mailID = SmartUtility.readConsole();
		if(!SmartUtility.isValidMail(mailID)){
			System.out.println(SmartUtility.validMailMessage);
			this.home();
			return;
		}
		System.out.println("Please enter the recharge amount :");
		String rechargeAmount = (SmartUtility.readConsole());
		if(!SmartUtility.isDecimal(rechargeAmount)){
			System.out.println(SmartUtility.rechargeMessage);
			this.home();
			return;
		}
		if(Double.valueOf(rechargeAmount)<100 || Double.valueOf(rechargeAmount)>1000000){
			System.out.println(SmartUtility.rechargeMessage);
			this.home();
			return;
		}
		availBalance = Double.parseDouble(rechargeAmount) +5.50;
		SmartUtility.writeFile(SmartUtility.userFolder + userID + SmartUtility.EXTN, String.valueOf(availBalance));
		SmartUtility.writeFile(SmartUtility.infoFolder + userID + SmartUtility.EXTN, password + "&"+ mailID);
		SmartUtility.writeFile(SmartUtility.historyFolder + userID + SmartUtility.EXTN," ");
		System.out.println("New user is registered.\n");
		this.home();
		return;
	}

	/**
	 * deletes all the file created for demo
	 */
	private void deleteAll(){
		SmartUtility.deleteAll();
	}

	/** 
	 * Main method
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		FileBasedSmartProcess sp = new FileBasedSmartProcess();
		sp.createlostCard();
		sp.home();
	}
}
