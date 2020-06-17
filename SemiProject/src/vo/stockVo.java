package vo;

public class stockVo {
	//필드
	int stockNum, stockMoney;
	String stock;
	
	//생성자
	public stockVo() {
		
	}

	public stockVo(String stock, int stockMoney) {
		super();
		this.stock = stock;
		this.stockMoney = stockMoney;
	}

	public stockVo(int stockNum, int stockMoney, String stock) {
		super();
		this.stockNum = stockNum;
		this.stockMoney = stockMoney;
		this.stock = stock;
	}

	//setter / getter
	public int getStockNum() {
		return stockNum;
	}

	public void setStockNum(int stockNum) {
		this.stockNum = stockNum;
	}

	public int getStockMoney() {
		return stockMoney;
	}

	public void setStockMoney(int stockMoney) {
		this.stockMoney = stockMoney;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}
}
