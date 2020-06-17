package mymain;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import dao.StockDao;
import dao.userDao;
import dao.userMoneyDAO;
import util.MyUtil;
import vo.stockVo;
import vo.userMoneyVo;
import vo.userVo;

public class MyStock extends JFrame {

	//입출력 필드
	JTextField jtf_realize, jtf_stock, jtf_stock_qty, jtf_stock_price;
	
	//작업버튼
	JButton jbt_buy, jbt_sell, jbt_reset;
	
	//조회
	JTable jtb_user, jtb_stock, jtb_total;
	
	JPanel panel = new JPanel(new BorderLayout());
	JPanel top = new JPanel(new BorderLayout());
	
	//주식 리스트
	List<stockVo> stockList;
	
	//유저 리스트
	List<userVo> userList;
	
	//유저 돈
	List<userMoneyVo> moneyList;
	
	//판매했을 때 이익
	int sellProfit;
	
	//현재 출력되는 데이터 인덱스
	int current_user = -1;
	int current_stock = 0;
	
	//어디에서 접속했는지 구분하기
	boolean b_user = false;
	boolean b_stock = false;
	
	//주식명 배열 선언
	String[] stockN;
	
	//주식 현재 가격 배열 선언
	int[] cStockP;
	
	//잔고
	int userRate;
			
	//손익
	int userProfit = 0;
	
	public MyStock() {
		//타이틀
		super("주식");

		//입출력 필드 및 버튼 초기화
		init_inputs();
		
		//유저 금액 초기화
		init_user_money();
		
		//유저 초기화
		init_user();
		
		//주식 초기화
		init_stock();
		
		//user 출력
		display_user_list();
		
		//stock 출력
		display_stock_list();
		
		//난수 출력
		display_rand_stock();
		
		//배열 생성
		stockN = new String[stockList.size()];
		cStockP = new int[stockList.size()];
		
		//조회창 상단
		display_input_top();

		//조회창 하단 선택된 데이터를 출력한다.
		display_input_bottom();
		
		//버튼 비활성화
		enable_buttons();
		
		//위치
		super.setLocation(100, 100);

		//크기
		setResizable(false);
		
		pack();

		//보여줘라
		super.setVisible(true);

		//종료
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	//난수 ------------------------------------------------------------------ //
	//난수 발생
	private void display_rand_stock() {
		Timer timer = new Timer();
		
		//난수 발생한 내용을 배열로 받는다.
		List<Integer> randList = new ArrayList<Integer>();
		
		//System.out.println(stockList.size());
		
		boolean empty = true;
			
		for (int i = 0; i < stockList.size(); i++) {
			randList.add(0);
		}
		
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				Random random = new Random();
				
				for (int i = 0; i < stockList.size() ; i++) {
					int randInt = 1+random.nextInt(3000);
					String sing = random.nextBoolean() ? "+" : "-";
					
					int rand = Integer.parseInt(sing+randInt);
					
					//System.out.printf(" [%d = %d] ",i, rand);
					
					randList.set(i, rand);
					
				}
				
				//arraylist에 넣었던 내용을 배열로 옮긴 후 출력한다.
				Integer[] ob= new Integer[randList.size()];
				
				randList.toArray(ob);  
								
				for (int i = 0; i < stockList.size(); i++) {
					//System.out.println(ob[i].toString());
					
					int value1 = Integer.parseInt(ob[i].toString());
					
					int value2 = cStockP[i];
					
					int changeV = value2 - value1;
					
					if (changeV < 0) {
						changeV = 500000;
					}
					
					String name = stockN[i];
									
					System.out.printf(" [%d / %d] [%d %s %d] \n",value2, value1, i, name, changeV);
										
					//변경된 내용을 update한다.
					stockVo vo = new stockVo(name, changeV);
					
					int res = StockDao.getInstance().update(vo);

					/* 반복문 실행 시, Connection 닫을 시간이 없을 정도로 빨리 돌아 닫히지 않은 상태에서 실행문을
					 * 반복적으로 수행하여, Connection 수가 부족하다는 오류가 발생하게 된다.
					 * 
					 * 스레드를 발생 시켜 닫을 시간을 준다.*/
					try {
						
						Thread.sleep(5);
						//System.out.println("동작");
						
					} catch (InterruptedException e) {
						
						e.printStackTrace();
						
					}
					
				}
				
				System.out.println();
				
				display_print();
				
			}// run
		};
		
		//timer.schedule(task, 10000, 50000);
	}

	//버튼 클릭 / 비클릭 ------------------------------------------------------------------ //
	//초기화 버튼
	private void enable_buttons() {
		jbt_buy.setEnabled(false);
		jbt_reset.setEnabled(false);
		jbt_sell.setEnabled(false);
		
		if (b_stock) {
			//잔고가 0보다 적을 경우 구매할 수 없습니다.
			if (userRate > 0) {
				jbt_buy.setEnabled(true);
			}
			jbt_reset.setEnabled(true);
		}
		
		if (b_user) {
			//잔고가 0보다 적을 경우 구매할 수 없습니다.
			if (userRate > 0) {
				jbt_buy.setEnabled(true);
			}
			jbt_sell.setEnabled(true);
			jbt_reset.setEnabled(true);
		}
	}

	//입출력 필드 및 버튼 초기화
	private void init_inputs() {
		//상단 정보
		JPanel p = new JPanel(new GridLayout(2,4));
		JPanel p1 = new JPanel(new GridLayout(1,3));
		
		p.add(new JLabel("실현손익"));
		p.add(jtf_realize = new JTextField());
				
		p.add(new JLabel("종목명"));
		p.add(jtf_stock = new JTextField());
		
		p.add(new JLabel("수량"));
		p.add(jtf_stock_qty = new JTextField());
		
		p.add(new JLabel("가격"));
		p.add(jtf_stock_price = new JTextField());
		
		JPanel p2 = new JPanel(new GridLayout(1,3));
		p2.add(jbt_buy = new JButton("매수"));
		p2.add(jbt_sell = new JButton("매도"));
		p2.add(jbt_reset = new JButton("정정"));
		
		//버튼이벤트 초기화
		init_button_event();
		
		top.add(p, "Center");
		p1.add(p2, "Center");
		top.add(p1, "South");
		
		panel.add(top,"Center");
				
		//입력되지 않는다.
		jtf_realize.setEditable(false);
		jtf_stock.setEditable(false);
		jtf_stock_price.setEditable(false);
				
		//display_input();
	}
	
	//유저 상단 ui 출력 ------------------------------------------------------------------ //
	//유저 금액 초기화
	private void init_user_money() {
		//유저 총액 정보
		jtb_total = new JTable();
		JScrollPane jsp = new JScrollPane(jtb_total);
		jsp.setPreferredSize(new Dimension(450,45));
		top.add(jsp,"North");
	}
	
	private void init_user() {
		//유저 정보
		jtb_user = new JTable();
		JScrollPane jup = new JScrollPane(jtb_user);
		panel.add(jup,"South");
		
		this.add(panel, "West");
		
		jtb_user.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				//선택된 행 구하기
				current_user = jtb_user.getSelectedRow();
				
				b_user = true;
				b_stock = false;
				
				//버튼 비활성화
				enable_buttons();
				
				//입력창 출력
				display_bottom_user();
			}
			
		});
	}
	
	// 유저 총 금액 등 출력 ------------------------------------------------------------------ //
	// 유저 총 금액
	private void display_input_top() {
		moneyList = userMoneyDAO.getInstance().selectUserMoney();
		
		jtb_total.setModel(new UserMoneyTableModel());
		
		enable_buttons();	
	}
	
	class UserMoneyTableModel extends AbstractTableModel{
				
		String [] title = {"총매입", "총평가", "총수익", "잔고", "총수익률"};

		@Override
		public int getColumnCount() {
			return title.length;
		}
		
		@Override
		public String getColumnName(int column) {
			//System.out.println( title[column]);
			return title[column];
		}
		
		@Override
		public int getRowCount() {
			return moneyList.size();
		}

		@Override
		public Object getValueAt(int row, int col) {
			userMoneyVo vo = moneyList.get(row);
			
			if (col == 0) return vo.getTotalSum();
			else if (col == 1) return vo.getUserTotal();
			else if (col == 2) return vo.getTotalProfit();
			else if (col == 3) {
				userRate = vo.getRemain();
				return vo.getRemain();
			} 
			else if (col == 4) return vo.getTotalRate();
			
			return null;
		}		
	}
	
	// 유저 주식 정보
	private void display_user_list() {
		//1. DB에서 목록 가져오기 ( Model(Data) )
		userList = userDao.getInstance().selectUserList();
		
		// JTable 배치어댑터 설정 _ Controller
		jtb_user.setModel(new UserTableModel());
	}

	class UserTableModel extends AbstractTableModel{
		String [] title = {"종목", "매입가", "손익", "수량", "현재가", "총매매금액", "평가금액"};
		
		@Override
		public int getRowCount() {
			// 데이터 갯수 - 열
			return userList.size();
		}
		
		@Override
		public int getColumnCount() {
			// 행
			return title.length;
		}
		
		@Override
		public Object getValueAt(int row, int col) {
			
			//열을 가지고 온다.
			userVo vo = userList.get(row);
			
			if (col == 0) return vo.getStockName();
			else if (col == 1) return vo.getUserBuyMoney();
			else if (col == 2) {
				userProfit = vo.getProfit();
				
				return vo.getProfit();
			}
			else if (col == 3) return vo.getStockquantity();
			else if (col == 4) return vo.getStockMoney();
			else if (col == 5) return vo.getTotalSum();
			else if (col == 6) return vo.getAppraisal();
			
			return null;
		}
		
		@Override
		public String getColumnName(int col) {
			return title[col];
		}
	}
	
	// 주식 / 유저구매 정보 출력 ------------------------------------------------------------------ //
	private void init_stock() {
		//주식 정보
		jtb_stock = new JTable();
		JScrollPane jsp = new JScrollPane(jtb_stock);
		jsp.setPreferredSize(new Dimension(200,100));
		this.add(jsp,"East");
		
		jtb_stock.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				//선택된 행 구하기
				current_stock = jtb_stock.getSelectedRow();
				
				b_stock = true;
				b_user = false;
				
				//버튼 비활성화
				enable_buttons();
				
				//입력창 출력
				display_bottom_stock();
			}
			
		});
	}

	private void display_stock_list() {
		//1. DB에서 목록 가져오기 ( Model(Data) )
		stockList = StockDao.getInstance().selectStockList();
		
		// JTable 배치어댑터 설정 _ Controller
		jtb_stock.setModel(new StockTableModel());
		
	}

	// 주식 정보
	class StockTableModel extends AbstractTableModel{
		
		String [] title = {"종목", "가격"};
		
		@Override
		public int getRowCount() {
			// 데이터 갯수 - 열
			return stockList.size();
		}
		
		@Override
		public int getColumnCount() {
			// 행
			return title.length;
		}
		
		@Override
		public Object getValueAt(int row, int col) {
			
			//열을 가지고 온다.
			stockVo vo = stockList.get(row);
			
			if (col == 0)  {
				cStockP[row] = vo.getStockMoney();
				stockN[row] = vo.getStock();
								
				//System.out.printf(" [%d %d %s] ",row, stockN[row]);
				
				return vo.getStock();
			} else if (col == 1) return vo.getStockMoney();
			
			return null;
		}
		
		@Override
		public String getColumnName(int col) {
			return title[col];
		}
		
	}
	
	//선택된 정보가 출력된다.
	private void display_input_bottom() {
		if (b_user) {
			display_bottom_user();
		}
		
		if (b_stock) {
			display_bottom_stock();
		}			
	}

	
	protected void display_bottom_stock() {
		stockVo vo = stockList.get(current_stock);
		
		jtf_stock_qty.requestFocus();
		
		jtf_stock.setText(vo.getStock());
		jtf_stock_price.setText(vo.getStockMoney()+"");
		jtf_stock_qty.setText("");
				
		//System.out.println("선택 정보 stock "+current_stock);
	}
	
	protected void display_bottom_user() {
		if (current_user < 0) return;
		
		userVo vo = userList.get(current_user);
		
		jtf_stock_qty.requestFocus();
		
		jtf_stock.setText(vo.getStockName());
		jtf_stock_qty.setText(vo.getStockquantity()+"");
		jtf_stock_price.setText(vo.getStockMoney()+"");
		
		//System.out.println("선택 정보 user "+current_user);
	}
	
	//버튼 기능 및 출력 ------------------------------------------------------------------ //
	//버튼 이벤트
	private void init_button_event() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				Object evt = e.getSource();
				
				if (evt == jbt_buy) on_update();
				else if (evt == jbt_sell) on_delete();
				else if (evt == jbt_reset) on_reset();
				
			}
		};
		
		jbt_buy.addActionListener(listener);
		jbt_sell.addActionListener(listener);
		jbt_reset.addActionListener(listener);
		
	}
	
	//정정 버튼 클릭 시
	protected void on_reset() {
		jtf_stock.setText("");
		jtf_stock_price.setText("");
		jtf_stock_qty.setText("");
	}

	//판매
	protected void on_delete() {
		int result = JOptionPane.showConfirmDialog(this, "판매하시겠습니까?", "판매", JOptionPane.YES_NO_OPTION);
		
		String stockName = jtf_stock.getText();
		
		int res = userDao.getInstance().delete(stockName);
		
		if (result != JOptionPane.YES_OPTION) {
			//System.out.println("1");
			
			return;
		}
		
		//System.out.println(userProfit);
		jtf_realize.setText(userProfit+"");
		
		//출력
		display_print();
		
	}
		
	//구매
	protected void on_update() {
		//입력값 체크
		String s_qty = jtf_stock_qty.getText().trim();
		
		String stock = jtf_stock.getText();
		
        if (! MyUtil.isNumber(s_qty)) {
			
			//System.out.println("---s_qty-- ");
			JOptionPane.showMessageDialog(this, "숫자를 입력해주세요.");
			
			jtf_stock_qty.setText("");
			jtf_stock_qty.requestFocus();
			
			return;
		}

		//문자열을 숫자로 변환
		int qty = Integer.parseInt(s_qty);
		int stockquantity = qty > 0 ? qty : -qty;
		
		int stockMoney = Integer.parseInt(jtf_stock_price.getText());

		int compare = stockquantity * stockMoney ;
				
		//System.out.printf("%d * %d = %d", qty, c_price, compare);
				
		if (!(stockquantity > 0)) {
			JOptionPane.showMessageDialog(this, "0이상 입력하세요.");
			
			jtf_stock_qty.setText("");
			jtf_stock_qty.requestFocus();
			
			return;
		}
		
		if (compare > userRate) {
			JOptionPane.showMessageDialog(this, "잔고가 부족합니다.");
			
			jtf_stock_qty.setText("");
			jtf_stock_qty.requestFocus();
			
			return;
		}
		
		if(b_stock) {
			//데이터 포장
			userVo vo = new userVo("홍길동",stock, stockquantity, stockMoney);
			
			//DB Insert
			int res = userDao.getInstance().insert(vo);
		}
		
		if (b_user) {
			//데이터 포장
			userVo vo = new userVo(stockquantity, stockMoney, stock);
			
			//DB Insert
			int res = userDao.getInstance().update(vo);
		}
		
		//출력
		display_print();
		
	}
	
	// 프린트 -------------------------------------------------------- //
	private void display_print() {
		//비우기
		on_reset();
				
		//출력창 
		display_input_top();
		display_user_list();
		
		display_stock_list();
		display_rand_stock();
		
		//버튼
		enable_buttons();
	}
	
	// main() -------------------------------------------------------- //
	public static void main(String[] args) {
		new MyStock();
	}// main
}