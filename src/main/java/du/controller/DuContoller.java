package du.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import du.db.BoardDto;
import du.db.MemberDao;
import du.db.MemberDto;
import du.service.BoardService;

/**
 * Servlet implementation class DuContoller
 */
@WebServlet("/")
public class DuContoller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DuContoller() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String view = null;

		String uri = request.getRequestURI();
		String conPath = request.getContextPath();
		String com = uri.substring(conPath.length());

		if (com.equals("/")) {
			view = "main.jsp";
			
		} else if (com.equals("/logout")) {
			HttpSession session = request.getSession();
			session.invalidate();
			view = "main.jsp";
		}
		
		else if (com.equals("/loginForm")) {
			view = "/sign-in/loginForm.jsp";
			
		} else if (com.equals("/login")) {
			request.setCharacterEncoding("utf-8");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			System.out.println(email + ", " + password);
			
			MemberDto memberDto = new MemberDto(0, "", email, password, "");
			MemberDao memberDao = new MemberDao();
			int login = memberDao.isMember(memberDto);
			
			if (login == 1) {
				System.out.println("로그인 성공");
				memberDto = memberDao.findMemeber(memberDto);
				System.out.println(memberDto);
				HttpSession session = request.getSession();
				session.setAttribute("customInfo", memberDto);
				request.setAttribute("userLoggedIn", true);
			} else {
				System.out.println("로그인 실패");
			}
			view = "main.jsp";
			
		} else if (com.equals("/signUpForm")) {
			view = "/sign-up/signUpForm.jsp";
			
		} else if (com.equals("/signUp")) {
			request.setCharacterEncoding("utf-8");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			
			MemberDto memberDto = new MemberDto(0, name, email, password, "");
			MemberDao memberDao = new MemberDao();
			memberDao.insertMember(memberDto);
			
			view = "main.jsp";
			
		} else if (com.equals("/list")) {
			String tmp = request.getParameter("page");
			int pageNo = (tmp != null && tmp.length() > 0)
					? Integer.parseInt(tmp) : 1;
			
			request.setAttribute("msgList", 
					new BoardService().getMsgList(pageNo));
			request.setAttribute("pgnList", 
					new BoardService().getPagination(pageNo));
			System.out.println(new BoardService().getPagination(pageNo));
			view = "list.jsp";
			
		} else if (com.equals("/view")) {
			int num = Integer.parseInt(request.getParameter("num"));
			request.setAttribute("msg", new BoardService().getMsg(num));
			view = "view.jsp";
			
		} else if (com.equals("/write")) {
			String tmp = request.getParameter("num");
			int num = (tmp != null && tmp.length() > 0)
					? Integer.parseInt(tmp) : 0;
			/*
			HttpSession session = request.getSession();
			
			MemberDao memberDao = new MemberDao();
			MemberDto getDto = (MemberDto)session.getAttribute("customInfo");
			MemberDto memberDto = memberDao.findMemeber(getDto);
			
			session.setAttribute("customInfo", memberDto);	
		*/
			BoardDto dto = new BoardDto();
			String action = "insert";
			
			if (num > 0) {
				dto = new BoardService().getMsgForWrite(num);
				action = "update?num=" + num;
			}
			
			request.setAttribute("msg", dto);
			request.setAttribute("action", action);
			view = "write.jsp";
		} 
		else if (com.equals("/insert")) {
			request.setCharacterEncoding("utf-8");
			String writer = request.getParameter("writer");
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			try {
				new BoardService().writeMsg(writer, title, content);
				view = "redirect:list";
			} catch(Exception e) {
				request.setAttribute("errorMessage", e.getMessage());
				view = "errorback.jsp";
			}
		} else if (com.equals("/update")) {
			request.setCharacterEncoding("utf-8");
			int num = Integer.parseInt(request.getParameter("num"));
			String writer = request.getParameter("writer");
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			try {
				new BoardService().updateMsg(writer, title, content, num);
				view = "redirect:list";
			} catch(Exception e) {
				request.setAttribute("errorMessage", e.getMessage());
				view = "errorBack.jsp";
			}
		} else if (com.equals("/delete")) {
			int num = Integer.parseInt(request.getParameter("num"));
			
			new BoardService().deleteMsg(num);
			view = "redirect:list";
		}

		if (view.startsWith("redirect:")) {
			response.sendRedirect(view.substring(9));
		} else {
			request.getRequestDispatcher(view).forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
