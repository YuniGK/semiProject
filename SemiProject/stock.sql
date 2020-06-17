/*
    
    create user test identified by test
    grant connect, resource to test;
    
    create sequence seq_user_int;
    create sequence seq_user_money;
    create sequence seq_stock_int;
    
    create table aa_userMoney
    (	
    	moneyInt int, 
    	userName varchar2(100),	
    	constraint pk_user_name primary key(userName),
    	userTotal int
    );
    
    create table aa_users
    (
        userInt int,
        userName varchar2(100), 
        constraint pk_user primary key(userInt),
        stock varchar2(100),	
        stockquantity int check(stockquantity > 0),	
        userBuyMoney int check(userBuyMoney > 0)
    );
        
    create table aa_stock
    (
    	stockNum int, 
    	stock varchar2(100),
    	constraint pk_stock primary key(stock),
        stockMoney int check(stockMoney > 0)
    );
        
    -- FK ����
	alter table aa_users
		add constraint fk_users_stock foreign key(stock) references aa_stock(stock);
		
	alter table aa_users
		add constraint fk_user_name foreign key(userName) references aa_userMoney(userName);
		
	-- Insert
	insert into aa_stock values(seq_stock_int.nextVal, '�Ｚ', 55500);
	insert into aa_stock values(seq_stock_int.nextVal, 'ȭ�¿�����������', 14650);
	insert into aa_stock values(seq_stock_int.nextVal, '�����͹��̿�',128400);
    insert into aa_stock values(seq_stock_int.nextVal, '��Ʈ����',268500);
    insert into aa_stock values(seq_stock_int.nextVal, '���̷θŵ�', 155000);
	insert into aa_stock values(seq_stock_int.nextVal, '����', 7500);
	insert into aa_stock values(seq_stock_int.nextVal, 'īī��', 262000);
	insert into aa_stock values(seq_stock_int.nextVal, '��ǳ����', 29450);
	insert into aa_stock values(seq_stock_int.nextVal, '������', 32800);
	insert into aa_stock values(seq_stock_int.nextVal, '�̽�Ÿ�װ�', 15550);
	
	insert into aa_userMoney values(seq_user_money.nextVal, 'ȫ�浿', 99999999);
	
	insert into aa_users values(seq_user_int.nextVal, 'ȫ�浿', '�Ｚ', 24, 48000);
	insert into aa_users values(seq_user_int.nextVal, 'ȫ�浿', 'ȭ�¿�����������', 28, 15600);

	-- ��ȸ
	select * from stock
	select * from users
	select * from userMoney
	
	delete from users
	
	select * from user_view
	select * from profit_view
				
	-- �����
	create or replace view user_view
    as
		select userInt, userName, u.stock stockName, userBuyMoney , (stockMoney * stockquantity) - (userBuyMoney * stockquantity) profit,
		stockquantity, stockMoney, (userBuyMoney * stockquantity) totalSum, (stockMoney * stockquantity) appraisal, userTotal
		from aa_users u left outer join aa_stock s on u.stock = s.stock
			   		 left outer join aa_userMoney um on u.userName = um.userName
			   		 			
	create or replace view profit_view
    as
		select avg(userTotal) userTotal, sum(profit) totalProfit, sum(totalSum) totalSum,
		      (avg(userTotal) - sum(totalSum)) as remain, trunc((sum(profit)/sum(totalSum) *100),2)totalRate
		from user_view 
				
	-- �����
	drop view user_view				 		 		 			 		 			 		 			 		 	
						 		 		 			 		 			 		 			 		 				 		 		 			 		 			 		 			 		 				 		 		 			 		 			 		 			 		 	
 	-- �ݿ�
	commit
 	
 */