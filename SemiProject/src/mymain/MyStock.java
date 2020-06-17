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

	//����� �ʵ�
	JTextField jtf_realize, jtf_stock, jtf_stock_qty, jtf_stock_price;
	
	//�۾���ư
	JButton jbt_buy, jbt_sell, jbt_reset;
	
	//��ȸ
	JTable jtb_user, jtb_stock, jtb_total;
	
	JPanel panel = new JPanel(new BorderLayout());
	JPanel top = new JPanel(new BorderLayout());
	
	//�ֽ� ����Ʈ
	List<stockVo> stockList;
	
	//���� ����Ʈ
	List<userVo> userList;
	
	//���� ��
	List<userMoneyVo> moneyList;
	
	//�Ǹ����� �� ����
	int sellProfit;
	
	//���� ��µǴ� ������ �ε���
	int current_user = -1;
	int current_stock = 0;
	
	//��𿡼� �����ߴ��� �����ϱ�
	boolean b_user = false;
	boolean b_stock = false;
	
	//�ֽĸ� �迭 ����
	String[] stockN;
	
	//�ֽ� ���� ���� �迭 ����
	int[] cStockP;
	
	//�ܰ�
	int userRate;
			
	//����
	int userProfit = 0;
	
	public MyStock() {
		//Ÿ��Ʋ
		super("�ֽ�");

		//����� �ʵ� �� ��ư �ʱ�ȭ
		init_inputs();
		
		//���� �ݾ� �ʱ�ȭ
		init_user_money();
		
		//���� �ʱ�ȭ
		init_user();
		
		//�ֽ� �ʱ�ȭ
		init_stock();
		
		//user ���
		display_user_list();
		
		//stock ���
		display_stock_list();
		
		//���� ���
		display_rand_stock();
		
		//�迭 ����
		stockN = new String[stockList.size()];
		cStockP = new int[stockList.size()];
		
		//��ȸâ ���
		display_input_top();

		//��ȸâ �ϴ� ���õ� �����͸� ����Ѵ�.
		display_input_bottom();
		
		//��ư ��Ȱ��ȭ
		enable_buttons();
		
		//��ġ
		super.setLocation(100, 100);

		//ũ��
		setResizable(false);
		
		pack();

		//�������
		super.setVisible(true);

		//����
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	//���� ------------------------------------------------------------------ //
	//���� �߻�
	private void display_rand_stock() {
		Timer timer = new Timer();
		
		//���� �߻��� ������ �迭�� �޴´�.
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
				
				//arraylist�� �־��� ������ �迭�� �ű� �� ����Ѵ�.
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
										
					//����� ������ update�Ѵ�.
					stockVo vo = new stockVo(name, changeV);
					
					int res = StockDao.getInstance().update(vo);

					/* �ݺ��� ���� ��, Connection ���� �ð��� ���� ������ ���� ���� ������ ���� ���¿��� ���๮��
					 * �ݺ������� �����Ͽ�, Connection ���� �����ϴٴ� ������ �߻��ϰ� �ȴ�.
					 * 
					 * �����带 �߻� ���� ���� �ð��� �ش�.*/
					try {
						
						Thread.sleep(5);
						//System.out.println("����");
						
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

	//��ư Ŭ�� / ��Ŭ�� ------------------------------------------------------------------ //
	//�ʱ�ȭ ��ư
	private void enable_buttons() {
		jbt_buy.setEnabled(false);
		jbt_reset.setEnabled(false);
		jbt_sell.setEnabled(false);
		
		if (b_stock) {
			//�ܰ� 0���� ���� ��� ������ �� �����ϴ�.
			if (userRate > 0) {
				jbt_buy.setEnabled(true);
			}
			jbt_reset.setEnabled(true);
		}
		
		if (b_user) {
			//�ܰ� 0���� ���� ��� ������ �� �����ϴ�.
			if (userRate > 0) {
				jbt_buy.setEnabled(true);
			}
			jbt_sell.setEnabled(true);
			jbt_reset.setEnabled(true);
		}
	}

	//����� �ʵ� �� ��ư �ʱ�ȭ
	private void init_inputs() {
		//��� ����
		JPanel p = new JPanel(new GridLayout(2,4));
		JPanel p1 = new JPanel(new GridLayout(1,3));
		
		p.add(new JLabel("��������"));
		p.add(jtf_realize = new JTextField());
				
		p.add(new JLabel("�����"));
		p.add(jtf_stock = new JTextField());
		
		p.add(new JLabel("����"));
		p.add(jtf_stock_qty = new JTextField());
		
		p.add(new JLabel("����"));
		p.add(jtf_stock_price = new JTextField());
		
		JPanel p2 = new JPanel(new GridLayout(1,3));
		p2.add(jbt_buy = new JButton("�ż�"));
		p2.add(jbt_sell = new JButton("�ŵ�"));
		p2.add(jbt_reset = new JButton("����"));
		
		//��ư�̺�Ʈ �ʱ�ȭ
		init_button_event();
		
		top.add(p, "Center");
		p1.add(p2, "Center");
		top.add(p1, "South");
		
		panel.add(top,"Center");
				
		//�Էµ��� �ʴ´�.
		jtf_realize.setEditable(false);
		jtf_stock.setEditable(false);
		jtf_stock_price.setEditable(false);
				
		//display_input();
	}
	
	//���� ��� ui ��� ------------------------------------------------------------------ //
	//���� �ݾ� �ʱ�ȭ
	private void init_user_money() {
		//���� �Ѿ� ����
		jtb_total = new JTable();
		JScrollPane jsp = new JScrollPane(jtb_total);
		jsp.setPreferredSize(new Dimension(450,45));
		top.add(jsp,"North");
	}
	
	private void init_user() {
		//���� ����
		jtb_user = new JTable();
		JScrollPane jup = new JScrollPane(jtb_user);
		panel.add(jup,"South");
		
		this.add(panel, "West");
		
		jtb_user.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				//���õ� �� ���ϱ�
				current_user = jtb_user.getSelectedRow();
				
				b_user = true;
				b_stock = false;
				
				//��ư ��Ȱ��ȭ
				enable_buttons();
				
				//�Է�â ���
				display_bottom_user();
			}
			
		});
	}
	
	// ���� �� �ݾ� �� ��� ------------------------------------------------------------------ //
	// ���� �� �ݾ�
	private void display_input_top() {
		moneyList = userMoneyDAO.getInstance().selectUserMoney();
		
		jtb_total.setModel(new UserMoneyTableModel());
		
		enable_buttons();	
	}
	
	class UserMoneyTableModel extends AbstractTableModel{
				
		String [] title = {"�Ѹ���", "����", "�Ѽ���", "�ܰ�", "�Ѽ��ͷ�"};

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
	
	// ���� �ֽ� ����
	private void display_user_list() {
		//1. DB���� ��� �������� ( Model(Data) )
		userList = userDao.getInstance().selectUserList();
		
		// JTable ��ġ����� ���� _ Controller
		jtb_user.setModel(new UserTableModel());
	}

	class UserTableModel extends AbstractTableModel{
		String [] title = {"����", "���԰�", "����", "����", "���簡", "�ѸŸűݾ�", "�򰡱ݾ�"};
		
		@Override
		public int getRowCount() {
			// ������ ���� - ��
			return userList.size();
		}
		
		@Override
		public int getColumnCount() {
			// ��
			return title.length;
		}
		
		@Override
		public Object getValueAt(int row, int col) {
			
			//���� ������ �´�.
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
	
	// �ֽ� / �������� ���� ��� ------------------------------------------------------------------ //
	private void init_stock() {
		//�ֽ� ����
		jtb_stock = new JTable();
		JScrollPane jsp = new JScrollPane(jtb_stock);
		jsp.setPreferredSize(new Dimension(200,100));
		this.add(jsp,"East");
		
		jtb_stock.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				//���õ� �� ���ϱ�
				current_stock = jtb_stock.getSelectedRow();
				
				b_stock = true;
				b_user = false;
				
				//��ư ��Ȱ��ȭ
				enable_buttons();
				
				//�Է�â ���
				display_bottom_stock();
			}
			
		});
	}

	private void display_stock_list() {
		//1. DB���� ��� �������� ( Model(Data) )
		stockList = StockDao.getInstance().selectStockList();
		
		// JTable ��ġ����� ���� _ Controller
		jtb_stock.setModel(new StockTableModel());
		
	}

	// �ֽ� ����
	class StockTableModel extends AbstractTableModel{
		
		String [] title = {"����", "����"};
		
		@Override
		public int getRowCount() {
			// ������ ���� - ��
			return stockList.size();
		}
		
		@Override
		public int getColumnCount() {
			// ��
			return title.length;
		}
		
		@Override
		public Object getValueAt(int row, int col) {
			
			//���� ������ �´�.
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
	
	//���õ� ������ ��µȴ�.
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
				
		//System.out.println("���� ���� stock "+current_stock);
	}
	
	protected void display_bottom_user() {
		if (current_user < 0) return;
		
		userVo vo = userList.get(current_user);
		
		jtf_stock_qty.requestFocus();
		
		jtf_stock.setText(vo.getStockName());
		jtf_stock_qty.setText(vo.getStockquantity()+"");
		jtf_stock_price.setText(vo.getStockMoney()+"");
		
		//System.out.println("���� ���� user "+current_user);
	}
	
	//��ư ��� �� ��� ------------------------------------------------------------------ //
	//��ư �̺�Ʈ
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
	
	//���� ��ư Ŭ�� ��
	protected void on_reset() {
		jtf_stock.setText("");
		jtf_stock_price.setText("");
		jtf_stock_qty.setText("");
	}

	//�Ǹ�
	protected void on_delete() {
		int result = JOptionPane.showConfirmDialog(this, "�Ǹ��Ͻðڽ��ϱ�?", "�Ǹ�", JOptionPane.YES_NO_OPTION);
		
		String stockName = jtf_stock.getText();
		
		int res = userDao.getInstance().delete(stockName);
		
		if (result != JOptionPane.YES_OPTION) {
			//System.out.println("1");
			
			return;
		}
		
		//System.out.println(userProfit);
		jtf_realize.setText(userProfit+"");
		
		//���
		display_print();
		
	}
		
	//����
	protected void on_update() {
		//�Է°� üũ
		String s_qty = jtf_stock_qty.getText().trim();
		
		String stock = jtf_stock.getText();
		
        if (! MyUtil.isNumber(s_qty)) {
			
			//System.out.println("---s_qty-- ");
			JOptionPane.showMessageDialog(this, "���ڸ� �Է����ּ���.");
			
			jtf_stock_qty.setText("");
			jtf_stock_qty.requestFocus();
			
			return;
		}

		//���ڿ��� ���ڷ� ��ȯ
		int qty = Integer.parseInt(s_qty);
		int stockquantity = qty > 0 ? qty : -qty;
		
		int stockMoney = Integer.parseInt(jtf_stock_price.getText());

		int compare = stockquantity * stockMoney ;
				
		//System.out.printf("%d * %d = %d", qty, c_price, compare);
				
		if (!(stockquantity > 0)) {
			JOptionPane.showMessageDialog(this, "0�̻� �Է��ϼ���.");
			
			jtf_stock_qty.setText("");
			jtf_stock_qty.requestFocus();
			
			return;
		}
		
		if (compare > userRate) {
			JOptionPane.showMessageDialog(this, "�ܰ� �����մϴ�.");
			
			jtf_stock_qty.setText("");
			jtf_stock_qty.requestFocus();
			
			return;
		}
		
		if(b_stock) {
			//������ ����
			userVo vo = new userVo("ȫ�浿",stock, stockquantity, stockMoney);
			
			//DB Insert
			int res = userDao.getInstance().insert(vo);
		}
		
		if (b_user) {
			//������ ����
			userVo vo = new userVo(stockquantity, stockMoney, stock);
			
			//DB Insert
			int res = userDao.getInstance().update(vo);
		}
		
		//���
		display_print();
		
	}
	
	// ����Ʈ -------------------------------------------------------- //
	private void display_print() {
		//����
		on_reset();
				
		//���â 
		display_input_top();
		display_user_list();
		
		display_stock_list();
		display_rand_stock();
		
		//��ư
		enable_buttons();
	}
	
	// main() -------------------------------------------------------- //
	public static void main(String[] args) {
		new MyStock();
	}// main
}