package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import service.StockService;
import vo.userMoneyVo;

public class userMoneyDAO {

	static userMoneyDAO single = null;
	
	public static userMoneyDAO getInstance() {
		if (single == null) {
			single = new userMoneyDAO();
		}
		
		return single;
		
	}
	
	public userMoneyDAO() {
		
	}
	
	//���� �Ѿ� ��ȸ
	public List<userMoneyVo> selectUserMoney(){
		List<userMoneyVo> list = new ArrayList<userMoneyVo>();
		
		String sql = "select * from profit_view";
		
		Connection conn = null;
		
		PreparedStatement pstmt = null;
		
		ResultSet rs = null;
		
		try {
			
			conn = StockService.getInstance().getConnection();
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				userMoneyVo vo = new userMoneyVo();
				
				vo.setTotalSum(rs.getInt("totalSum")); //�Ѹ���
				vo.setUserTotal(rs.getInt("userTotal")); //����
				vo.setTotalProfit(rs.getInt("totalProfit")); //�Ѽ���
				vo.setRemain(rs.getInt("remain")); //�ܰ�
				vo.setTotalRate(rs.getInt("totalRate")); //�Ѽ��ͷ�
				
				
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
		
	}// selectUserMoney
	
	

}
