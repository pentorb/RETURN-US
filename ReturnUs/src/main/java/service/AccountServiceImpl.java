package service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import dao.AccountDAO;
import dao.AccountDAOImpl;
import dto.Account;
import util.NaverMailSendToChangePassword;

public class AccountServiceImpl implements AccountService {
	private AccountDAO accountDao = new AccountDAOImpl();
	 private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // SimpleDateFormat 객체 추가
	
	@Override
	public void join(HttpServletRequest request) throws Exception {
		  request.setCharacterEncoding("utf-8");

	        String accId = request.getParameter("accId");
	        String accEmail = request.getParameter("accEmail");
	        String accEmailDo = request.getParameter("accEmailDo");
	        Account sacc = accountDao.selectAccount(accId);
	        if(sacc != null) throw new Exception("아이디가 중복됩니다");
	        Account eacc = accountDao.checkedDoubleEmail(accEmail, accEmailDo);
	        if(eacc != null) throw new Exception("등록된 이메일입니다.");

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
	        String accTel = request.getParameter("accTel");;
	        String accPostCode = request.getParameter("accPostCode");
	        String accAddr = request.getParameter("accAddr");
	        String accDetailAddr = request.getParameter("accDetailAddr");
		
		Account account = new Account(accId,accPassword,accName,accBirth,accTel,accEmail,accEmailDo,accPostCode,accAddr,accDetailAddr);
		accountDao.insertAccount(account);
	}

	@Override
	public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		
		String login = request.getParameter("login");
		JSONParser parser = new JSONParser();
		JSONObject jobj = (JSONObject)parser.parse(login);
		
		String accId = (String)jobj.get("accId");
		String accPassword = (String)jobj.get("accPassword");
		boolean autologin = (boolean)jobj.get("autologin");
		System.out.println(accId);
		System.out.println(accPassword);
		System.out.println(autologin);
		// 자동로그인 쿠키 처리
		Cookie autoLoginCookie = null;
		Cookie accIdCookie = null;
		Cookie accPasswordCookie = null;
		if(autologin) {
			autoLoginCookie = new Cookie ("autologin", "true");
			autoLoginCookie.setMaxAge(365 * 24 * 60 * 60); 
			accIdCookie = new Cookie("accId", accId);
			accIdCookie.setMaxAge(365 * 24 * 60 * 60);                
			accPasswordCookie = new Cookie("accPassword", accPassword);
			accPasswordCookie.setMaxAge(365 * 24 * 60 * 60);	
		}else {
			autoLoginCookie = new Cookie("autologin", "");
            autoLoginCookie.setMaxAge(0);
            accIdCookie = new Cookie("accId", "");   
            accIdCookie.setMaxAge(0);
            accPasswordCookie = new Cookie("accPassword", "");
            accPasswordCookie.setMaxAge(0);
		}			
		response.addCookie(autoLoginCookie);
		response.addCookie(accIdCookie);
		response.addCookie(accPasswordCookie);
		/////////////////////////
		
		Account acc = accountDao.selectAccount(accId);
		if (acc == null) throw new Exception("아이디를 확인해주시기 바랍니다.");
		if(!acc.getAccPassword().equals(accPassword)) throw new Exception("비밀번호를 확인해주시기 바랍니다.");
		
		HttpSession session = request.getSession();
		session.setAttribute("acc", acc);
		
		String adminCheck = accountDao.selectAdmin(accId);
		if(adminCheck != null && adminCheck.equals("admin")) {
			session.setAttribute("adminCheck", adminCheck);
		}
		
	}

	@Override
	public boolean accountIdCheck(String accId) throws Exception {
		Account account = accountDao.selectAccount(accId);
		if(account==null) return false;
		return true;
	}

	@Override
	public Account findId(String accName, String accBirth, String email) throws Exception {
		Account sacc = accountDao.findIdAccount(accName);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String birth = null;
		if(sacc==null) {
			throw new Exception("기입하신 성명으로 가입된 <br> 회원정보가 없습니다.");
		}else if(!accBirth.equals(birth=format.format(sacc.getAccBirth()))){
			throw new Exception("생년월일을 확인해 주시기 바랍니다.");
		}else if(!email.equals(sacc.getAccEmail()+"@"+sacc.getAccEmailDo())) {
			throw new Exception("이메일을 확인해 주시기 바랍니다.");
		}else {
			System.out.println(sacc);
			return sacc;
		}
		
	}

	@Override
	public void modifyProfile(HttpServletRequest request) throws Exception {
		String id = request.getParameter("accId");
		String pw = request.getParameter("accPassword");
		String birth = request.getParameter("accBirth");
		Date accBirth = new Date(sdf.parse(birth).getTime());
				
		Account account = accountDao.selectAccount(id);
		
		account.setAccName(request.getParameter("accName"));
		account.setAccBirth(accBirth);
		account.setAccTel(request.getParameter("accTel"));
		account.setAccEmail(request.getParameter("accEmail"));
		account.setAccPostCode(request.getParameter("accPostCode"));
		account.setAccAddr(request.getParameter("accAddr"));
		account.setAccDetailAddr(request.getParameter("accDetailAddr"));
		accountDao.updateAccount(account);
		
		HttpSession sessionOld = request.getSession();
		sessionOld.invalidate();		
				
		HttpSession session = request.getSession();
		account.setAccPassword("");
		session.setAttribute("acc", account);
	}

	@Override
	public void closeAccount(HttpServletRequest request) throws Exception {
		String id = request.getParameter("accId");
		Account account = accountDao.selectAccount(id);

		HttpSession session = request.getSession();
		session.invalidate();
		accountDao.deleteAccount(id);
	}

	@Override
	public void changePassword(HttpServletRequest request) throws Exception {
		String id = request.getParameter("accId");				
		Account account = accountDao.selectAccount(id);
		
		account.setAccPassword(request.getParameter("accNewPassword"));
		accountDao.updateAccountPassword(account);
		
		HttpSession sessionOld = request.getSession();
		sessionOld.invalidate();		
	}
	

	@Override
	public void checkPassword(String accId, String email) throws Exception {
		Account sacc = accountDao.selectAccount(accId);
		if(sacc==null) {
			throw new Exception("아이디를 확인해 주시기 바랍니다.");
		}else if(!email.equals(sacc.getAccEmail()+"@"+sacc.getAccEmailDo())) {
			throw new Exception("이메일을 확인해 주시기 바랍니다.");
		}
		NaverMailSendToChangePassword naverMailSendToChangePassword = new NaverMailSendToChangePassword();
		String authPassword = naverMailSendToChangePassword.sendEmail(email);
		accountDao.updateAccountPassword(new Account(accId,authPassword));
	}
	
}
