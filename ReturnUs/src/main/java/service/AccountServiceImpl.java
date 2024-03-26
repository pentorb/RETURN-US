package service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import dao.AccountDAO;
import dao.AccountDAOImpl;
import dto.Account;

public class AccountServiceImpl implements AccountService {
	private AccountDAO accountDao = new AccountDAOImpl();
	 private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // SimpleDateFormat 객체 추가
	
	@Override
	public void join(HttpServletRequest request) throws Exception {
		  request.setCharacterEncoding("utf-8");

	        String accId = request.getParameter("accId");
	        Account sacc = accountDao.selectAccount(accId);
	        if(sacc != null) throw new Exception("아이디가 중복됩니다");

	        String accPassword = request.getParameter("accPassword");
	        String accName = request.getParameter("accName");
	        Date accBirth = null;
	        try {
	            String accBirthString = request.getParameter("accBirth");
	            if (accBirthString != null && !accBirthString.isEmpty()) {
	                accBirth = new Date(sdf.parse(accBirthString).getTime()); // SimpleDateFormat 객체 사용하여 날짜 파싱
	            }
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	        String accTel = request.getParameter("accTel");
	        String accEmail = request.getParameter("accEmail");
	        String accEmailDo = request.getParameter("accEmailDo");
	        String accPostCode = request.getParameter("accPostCode");
	        String accAddr = request.getParameter("accAddr");
	        String accDetailAddr = request.getParameter("accDetailAddr");
		
		Account account = new Account(accId,accPassword,accName,accBirth,accTel,accEmail,accEmailDo,accPostCode,accAddr,accDetailAddr);
		accountDao.insertAccount(account);
	}

	@Override
	public void login(HttpServletRequest request) throws Exception {
		request.setCharacterEncoding("utf-8");
		
		String id = request.getParameter("accId");
		String pw = request.getParameter("accPassword");
		Account acc = accountDao.selectAccount(id);
		
		if (acc == null) throw new Exception("아이디 오류");
		if(!acc.getAccPassword().equals(pw)) throw new Exception("비밀번호 오류");
		
		HttpSession session = request.getSession();
		acc.setAccPassword("");
		session.setAttribute("acc", acc);
	}

	@Override
	public boolean accountIdCheck(String accId) throws Exception {
		Account account = accountDao.selectAccount(accId);
		if(account==null) return false;
		return true;
	}
	
	

}