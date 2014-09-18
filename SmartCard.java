import java.util.ArrayList;
import java.util.List;

/**
 * @author GAN KOCHI
 *
 */
public class SmartCard {

	private String userID;
	
	private String cardNumber;
	
	private String cardBalance;
	
	private String password;
	
	private String mailID;
	
	private List<String> travelHistory;
	
	/**
	 * constructor
	 */
	public SmartCard(){}
	
	
	/**
	 * Constructor for Smart Card
	 * @param id
	 * @param cardNo
	 * @param cardBal
	 */
	public SmartCard(final String id, final String cardNo,
			final String cardBal){
		super();
		this.userID = id;
		this.cardNumber = cardNo;
		this.cardBalance = cardBal;
	}

	/**
	 * @return the userID
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * @param userID the userID to set
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * @return the cardNumber
	 */
	public String getCardNumber() {
		return cardNumber;
	}

	/**
	 * @param cardNumber the cardNumber to set
	 */
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	/**
	 * @return the cardBalance
	 */
	public String getCardBalance() {
		return cardBalance;
	}

	/**
	 * @param cardBalance the cardBalance to set
	 */
	public void setCardBalance(String cardBalance) {
		this.cardBalance = cardBalance;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the mailID
	 */
	public String getMailID() {
		return mailID;
	}

	/**
	 * @param mailID the mailID to set
	 */
	public void setMailID(String mailID) {
		this.mailID = mailID;
	}

	/**
	 * @return the travelHistory
	 */
	public List<String> getTravelHistory() {
		if(this.travelHistory == null){
			this.travelHistory = new ArrayList<String>();
		}
		return travelHistory;
	}

	/**
	 * @param travelHistory the travelHistory to set
	 */
	public void setTravelHistory(List<String> travelHistory) {
		this.travelHistory = travelHistory;
	}
	
	/**
	 * reset
	 */
	public void reset(){
		this.userID = "";
		this.cardNumber = "";
		this.cardBalance = "";
		this.password = "";
		this.mailID = "";
		this.travelHistory = null;
	}
	
	/**
	 * display
	 */
	public void display(){
		System.out.println("userID : " + this.userID);
		System.out.println("cardBalance : " + this.cardBalance);
		System.out.println("password : " + this.password);
		System.out.println("mailID : " + this.mailID);
		System.out.println("getTravelHistory().size() : " + this.getTravelHistory().size());
		for(String hist : this.getTravelHistory()){
			System.out.println(hist);
		}
	}
}
