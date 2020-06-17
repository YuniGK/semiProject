package vo;

public class userVo {
	//필드
	int userInt, userBuyMoney, profit, stockquantity, stockMoney, totalSum, appraisal, userTotal; 
	String stockName, userName;
	
	//생성자
	public userVo() {
	}

	public userVo(int stockquantity, int userBuyMoney, String stockName) {
		super();
		this.userBuyMoney = userBuyMoney;
		this.stockquantity = stockquantity;
		this.stockName = stockName;
	}

	public userVo(String userName, String stockName, int stockquantity, int stockMoney) {
		super();
		this.userName = userName;
		this.stockName = stockName;
		this.stockquantity = stockquantity;
		this.stockMoney = stockMoney;
	}
	
	public userVo(int userBuyMoney, int profit, int stockquantity, int stockMoney, int totalSum,
			int appraisal, String stockName, String userName) {
		super();
		this.userBuyMoney = userBuyMoney;
		this.profit = profit;
		this.stockquantity = stockquantity;
		this.stockMoney = stockMoney;
		this.totalSum = totalSum;
		this.appraisal = appraisal;
		this.stockName = stockName;
		this.userName = userName;
	}

	//setter / getter
	public int getUserInt() {
		return userInt;
	}

	public void setUserInt(int userInt) {
		this.userInt = userInt;
	}

	public int getUserBuyMoney() {
		return userBuyMoney;
	}

	public void setUserBuyMoney(int userBuyMoney) {
		this.userBuyMoney = userBuyMoney;
	}

	public int getProfit() {
		return profit;
	}

	public void setProfit(int profit) {
		this.profit = profit;
	}

	public int getStockquantity() {
		return stockquantity;
	}

	public void setStockquantity(int stockquantity) {
		this.stockquantity = stockquantity;
	}

	public int getStockMoney() {
		return stockMoney;
	}

	public void setStockMoney(int stockMoney) {
		this.stockMoney = stockMoney;
	}

	public int getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(int totalSum) {
		this.totalSum = totalSum;
	}

	public int getAppraisal() {
		return appraisal;
	}

	public void setAppraisal(int appraisal) {
		this.appraisal = appraisal;
	}

	public int getUserTotal() {
		return userTotal;
	}

	public void setUserTotal(int userTotal) {
		this.userTotal = userTotal;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
