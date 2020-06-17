package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import service.StockService;
import vo.userVo;

public class userDao {

	static userDao single = null;
	
	public static userDao getInstance() {
		if (single == null) {
			single = new userDao();
		}
		
		return single;
		
	}
	
	public userDao() {
		
	}
	
	//유저 조회
	public List<userVo> selectUserList(){
		List<userVo> list = new ArrayList<userVo>();
		
		String sql = "select * from user_view";
		
		Connection conn = null;
		
		PreparedStatement pstmt = null;
		
		ResultSet rs = null;
		
		try {
			
			conn = StockService.getInstance().getConnection();
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				userVo vo = new userVo();
				
				vo.setStockquantity(rs.getInt("stockquantity"));
				vo.setStockMoney(rs.getInt("stockMoney"));
				vo.setTotalSum(rs.getInt("totalSum"));
				vo.setAppraisal(rs.getInt("appraisal"));
				vo.setStockName(rs.getString("stockName"));
				vo.setUserBuyMoney(rs.getInt("userBuyMoney"));
				vo.setProfit(rs.getInt("profit"));
				
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
		
	}// selectUserList
	
	public int insert(userVo vo) {
		int res = 0;
		
		Connection conn = null;
		
		PreparedStatement pstmt = null;
		
		String sql = "insert into aa_users values(seq_user_int.nextVal, ?, ?, ?, ?)"; 
		
		try {
			conn = StockService.getInstance().getConnection();
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1,  vo.getUserName());
			pstmt.setString(2,  vo.getStockName());
			pstmt.setInt(3,  vo.getStockquantity());
			pstmt.setInt(4, vo.getStockMoney());
			
			res = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return res;
	}// insert

	public int update(userVo vo) {
		int res = 0;
		
		Connection conn = null;
		
		PreparedStatement pstmt = null;
		
		String sql = "update aa_users set stockquantity = ?, userBuyMoney = ? where stock = ?";
		
		//System.out.printf("update users set stockquantity = %d, userBuyMoney = %d where stock = %s", vo.getStockquantity(),vo.getUserBuyMoney(),vo.getStockName());
		
		try {
			conn = StockService.getInstance().getConnection();
			
			pstmt = conn.prepareStatement(sql);
			
			//변수값 넣는다.
			pstmt.setInt(1,  vo.getStockquantity());
			pstmt.setInt(2, vo.getUserBuyMoney());
			pstmt.setString(3, vo.getStockName());
			
			res = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null) pstmt.close();
				
				if (conn != null) conn.close(); 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return res;
	}

	public int delete(String stockName) {
		
		int res = 0;
		
		Connection conn = null;
		
		PreparedStatement pstmt = null;
		
		String sql = "delete from aa_users where stock = ?";
		
		try {
			
			//1. Connection 얻기
			conn = StockService.getInstance().getConnection();
			
			//2. PreparedStatment 객체얻어오기(명령처리 객체)
			pstmt = conn.prepareStatement(sql);
			
			//3. pstmt에 parameter 설정
			pstmt.setString(1, stockName);
			
			//4. DB 처리
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
		
		//실패시 0	
		return res;
	}
	
	
}
