import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GAN KOCHI
 *
 */
public class SmartProcess {
	private Map<String, SmartCard> smartCardUsers = new HashMap<String, SmartCard>();
	private static List<String> lostCards = new ArrayList<String>();

	/**
	 * reset
	 */
	private void reset(){
		this.smartCardUsers = new HashMap<String, SmartCard>();
		lostCards = new ArrayList<String>();
	}

	/**
	 * displayScreen
	 */
	private void displayScreen(){
		this.echo("\nWelcome to ‘My Metro Smart Card System’ (M2SCS)");
		this.echo("Please select your option:");
		this.echo("1. New User");
		this.echo("2. Journey");   
		this.echo("3. Recharge SmartCard");
		this.echo("4. View Current Balance");
		this.echo("5. Know My History ");
		this.echo("6. Help");
		this.echo("Press any other key to EXIT");
	}

	/**
	 * This method is to print the console
	 * @throws IOException
	 */
	private void home() throws IOException{
		this.displayScreen();
		try{
			String inputChoice=(SmartUtility.readConsole());
			if(!SmartUtility.isInteger(inputChoice)){
				this.echo(SmartUtility.exitMessage);
				return;
			}
			switch (Integer.parseInt(inputChoice)) {
			case 1:
				this.newUser();
				break;
			case 2:
				this.bookTicket();
				break;
			case 3:
				this.recharge();
				break;
			case 4:
				this.viewBalance();
				break;
			case 5:
				this.viewHistory();
				break;
			case 6:
				this.contactHelp();
				break;
			default:
				this.echo(SmartUtility.exitMessage);
				break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			this.reset();
		}

	}
	
	/**
	 * validateUser 
	 * @return
	 */
	private String validateUser(boolean newUser){
		this.echo(SmartUtility.enterUserId);
		String userID = SmartUtility.readConsole();
		if(!SmartUtility.isString(userID)){
			this.echo(SmartUtility.stringMessage);
			return "";
		}
		if(newUser){
			if (this.smartCardUsers.containsKey(userID)) {
				this.echo(SmartUtility.userExist);
				return "";
			}
		}else {
			if (!this.smartCardUsers.containsKey(userID)) {
				this.echo(SmartUtility.userNotFound);
				return "";
			}
		}			
		return userID;
	}
	
	/**
	 * validate Station
	 * @return
	 */
	private String validateStation(){
		String station = SmartUtility.readConsole();
		if(!SmartUtility.isInteger(station)){
			this.echo(SmartUtility.integerMessage);
			return "";
		}
		if (!SmartUtility.isValidStation(station)) {
			this.echo(SmartUtility.invalidStation);
			return "";
		}
		return station;
	}
	
	/**
	 * validateRechargeAmount
	 * @return
	 */
	private String validateRechargeAmount(){
		String rechargeAmount = (SmartUtility.readConsole());
		if (!SmartUtility.isDecimal(rechargeAmount)) {
			this.echo(SmartUtility.decimalMessage);
			return "";
		}
		if (Double.valueOf(rechargeAmount) < 100
				|| Double.valueOf(rechargeAmount) > 1000000) {
			this.echo(SmartUtility.rechargeMessage);
			return "";
		}
		return rechargeAmount;
	}

	/**
	 * 
	 * @throws IOException
	 */
	private void createlostCard() throws IOException {
		//this is just for testing
		lostCards.add("111");
		lostCards.add("222");
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
			String userID = this.validateUser(false);
			if(userID.isEmpty()){
				this.home();			
				return;
			}
			if (lostCards.contains(userID)) {
				this.echo(userID + " reported as lost/damaged..!!\n");
				this.home();			
				return;
			}
			this.echo("\nEnter SRC(1 - 75)");
			String source = this.validateStation();
			if(source.isEmpty()){
				this.home();
				return;
			}
			this.echo("\nEnter Destination(1-75)");
			String destination = this.validateStation();			
			if (destination.equalsIgnoreCase(source)) {
				this.echo(SmartUtility.sameSourceDest);
				this.home();			
				return;
			}
			SmartCard card = this.smartCardUsers.get(userID);
			if (SmartUtility.isWeekend()) {
				this.calculateBalance(3.50, source, destination, card);
			} else if (!SmartUtility.isWeekend()) {
				this.calculateBalance(5.0,source,destination, card);
			}
			this.writeHistory(card, source, destination);
			this.smartCardUsers.put(userID, card);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.home();
	}

	/**
	 * Method to calculate balance of the user and 
	 * to check whether the user is allowed for the journey 
	 * @param source
	 * @param destination
	 * @throws IOException
	 */
	private void calculateBalance(double value, String source, String destination, SmartCard card) throws IOException {
		double fare = 40 * value;
		if (Double.parseDouble(card.getCardBalance()) < (fare)) {
			this.echo("Available Balance is : " + card.getCardBalance()
					+ "\nBalance not sufficient to make a travel. Kindly recharge your card.\n\n");
			this.home();			
			return;			
		}
		else {
			fare = Double.parseDouble(card.getCardBalance());
			fare -= (double)Math.abs((Long.parseLong(source) - Long
					.parseLong(destination))) * value;
			card.setCardBalance(Double.toString(fare));			
			this.echo("Smart Card "+card.getUserID() +" used to travel from station "+ source +" to station "+ destination);
			this.echo("\nHAPPY JOURNEY.. \nYour current balance is :" + card.getCardBalance() + "\n\n");
		}
	}

	/**
	 * This method is to view the current balance of the user
	 * @throws IOException
	 */
	private void viewBalance() throws IOException {
		String userID = validateUser(false);
		if (userID.isEmpty()) {
			this.home();
			return;
		}
		SmartCard card = this.smartCardUsers.get(userID);
		this.echo("Current Balance is:"+ card.getCardBalance() + "\n");
		this.home();
	}


	/**
	 * This method is to view history of the user
	 * @throws IOException
	 */
	private void viewHistory() throws IOException {
		String userID = validateUser(false);
		if (userID.isEmpty()) {
			this.home();
			return;
		}
		this.echo("Source \t Destination \t Date Of Journey");
		this.echo(SmartUtility.LINE);
		SmartCard card = this.smartCardUsers.get(userID);
		if(card.getTravelHistory().isEmpty()){
			this.echo("No travel history available for "+ userID);
		}else{
			for(String history : card.getTravelHistory()){
				this.echo(history);
			}
		}
		this.home();
		return;
	}

	/**
	 * Method for recharging the smartcard
	 * @throws IOException
	 */
	private void recharge() throws IOException {
		String userID = validateUser(false);
		if (userID.isEmpty()) {
			this.home();
			return;
		}
		this.echo("Please enter the recharge amount :");
		String rechargeAmount = this.validateRechargeAmount();
		if(rechargeAmount.isEmpty()){
			this.home();
			return;
		}
		SmartCard card = smartCardUsers.get(userID);
		if (Double.parseDouble(card.getCardBalance()) > 1000000) {
			System.out
					.println("Available Balance is : "
							+ card.getCardBalance()
							+ "\nBalance cant exceed 10 lakh INR.\n Recharge unsuccessful.\n");
			this.home();
			return;
		}
		double balance = Double.parseDouble(card.getCardBalance());
		balance += Double.parseDouble(rechargeAmount);
		card.setCardBalance(Double.toString(balance));
		this.echo("Successfully Recharged..\nCurrent Balance is:"+ balance + "\n");
		this.home();
		return;

	}	

	/**
	 * Method for user registration and storing their  details in three different files     
	 * @throws IOException
	 */
	private void newUser() throws IOException {
		String userID = this.validateUser(true);
		if(userID.isEmpty()){
			this.home();			
			return;
		}
		if (lostCards.contains(userID)) {
			this.echo(userID + " reported as lost/damaged..!!\n");
			this.home();			
			return;
		}
		SmartCard card = new SmartCard();
		card.setUserID(userID);
		this.echo("Please enter your password");
		card.setPassword(SmartUtility.readConsole());
		this.echo("Please enter your mail id"); 
		String mailId = SmartUtility.readConsole();
		if(!SmartUtility.isValidMail(mailId)){
			this.echo(SmartUtility.validMailMessage);
			card.reset(); 
			this.home();
			return;
		}
		card.setMailID(mailId);
		this.echo("Please enter the recharge amount :");
		String rechargeAmount = (SmartUtility.readConsole());
		if(!SmartUtility.isDecimal(rechargeAmount)){
			this.echo(SmartUtility.rechargeMessage);
			card.reset(); this.home();
			return;
		}
		if(Double.valueOf(rechargeAmount)<100 || Double.valueOf(rechargeAmount)>1000000){
			this.echo(SmartUtility.rechargeMessage);
			card.reset(); this.home();
			return;
		}
		double availBalance = Double.parseDouble(rechargeAmount) + 5.50;
		card.setCardBalance(Double.toString(availBalance));		
		this.smartCardUsers.put(userID, card);
		this.echo("New user is registered.\n");
		this.home();		
	}

	/**
	 * This method is to print the contact details for customer support
	 * @throws IOException
	 */
	private void contactHelp() throws IOException{
		this.echo("\nKindly contact 999-9999999 for customer support");
		this.echo("Mail your queries to: customerSupport@smartcard.com \n");
		this.home();
		return;
	}
	
	/**
	 * 
	 * @param card
	 * @param source
	 * @param dest
	 * @throws IOException
	 */
	private void writeHistory(SmartCard card, String source, String dest) throws IOException {
		List<String> travelHistory = new ArrayList<String>();
		if (card.getTravelHistory().size() == 5) {
			card.getTravelHistory().remove(0);
		}
		travelHistory.add(source + " \t " + dest + " \t " + new Date());
		card.getTravelHistory().addAll(travelHistory);		
	}
	
	/**
	 * echo
	 * @param msg
	 */
	private void echo(final String msg){
		System.out.println(msg);
	}

	/** 
	 * Main method
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		SmartProcess proc = new SmartProcess();
		proc.createlostCard();
		SmartProcess sp = new SmartProcess();
		sp.home();
	}
}
