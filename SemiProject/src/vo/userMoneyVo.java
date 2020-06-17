package vo;

public class userMoneyVo {
	//필드
	int totalRate, userTotal, totalProfit, totalSum, remain;
	
	//생성자
	public userMoneyVo() {
		
	}

	public userMoneyVo(int totalRate, int userTotal, int totalProfit, int totalSum, int remain) {
		super();
		this.totalRate = totalRate;
		this.userTotal = userTotal;
		this.totalProfit = totalProfit;
		this.totalSum = totalSum;
		this.remain = remain;
	}

	//setter / getter
	public int getTotalRate() {
		return totalRate;
	}

	public void setTotalRate(int totalRate) {
		this.totalRate = totalRate;
	}

	public int getUserTotal() {
		return userTotal;
	}

	public void setUserTotal(int userTotal) {
		this.userTotal = userTotal;
	}

	public int getTotalProfit() {
		return totalProfit;
	}

	public void setTotalProfit(int totalProfit) {
		this.totalProfit = totalProfit;
	}

	public int getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(int totalSum) {
		this.totalSum = totalSum;
	}

	public int getRemain() {
		return remain;
	}

	public void setRemain(int remain) {
		this.remain = remain;
	}
	
}
