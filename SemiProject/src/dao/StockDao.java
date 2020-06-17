package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import service.StockService;
import vo.stockVo;

public class StockDao {

	static StockDao single = null;
	
	public static StockDao getInstance() {
		if (single == null) {
			single = new StockDao();
		}
		
		return single;
		
	}
	
	public StockDao() {
		
	}
	
	//�ֽ� ��ȸ
	public List<stockVo> selectStockList(){
		List<stockVo> list = new ArrayList<stockVo>();
		
		String sql = "select * from aa_stock";
		
		Connection conn = null;
		
		PreparedStatement pstmt = null;
		
		ResultSet rs = null;
		
		try {
			
			conn = StockService.getInstance().getConnection();
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				stockVo vo = new stockVo();
				
				vo.setStockNum(rs.getInt("stockNum"));
				
				vo.setStock(rs.getString("stock"));
				
				vo.setStockMoney(rs.getInt("stockMoney"));
				
				list.add(vo);
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}finally {
			
			try {
				if (rs != null) rs.close();
				
				if (pstmt != null) pstmt.close();
				
				if (conn != null) conn.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return list;
		
	}// selectStockList
	
public int update(stockVo vo) {
		
		int res = 0;
		
		Connection conn = null;
		
		PreparedStatement pstmt = null;
		
		String sql = "update stock set stockMoney = ? where stock = ?";
		
		try {
			
			//1. Connection ���
			conn = StockService.getInstance().getConnection();
			
			//2. PreparedStatment ��ü������(���ó�� ��ü)
			pstmt = conn.prepareStatement(sql);
			
			//3. pstmt�� parameter ����
			pstmt.setInt(1,  vo.getStockMoney());
			pstmt.setString(2, vo.getStock());
			
			//4. DB ó��
			res = pstmt.executeUpdate();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}finally {
			
			try {
				
				if(pstmt != null) pstmt.close();
				
				if(conn != null) conn.close();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				
			}
			
		}
		
		//���н� 0	
		return res;
	}
	
}
