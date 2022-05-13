package web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import metier.entities.Compte;
import metier.session.IBanqueLocal;

public class BanqueServlet extends HttpServlet {
	
	@EJB
	private IBanqueLocal metier;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	@Override
	   protected void doPost(HttpServletRequest request,
	           HttpServletResponse response) throws ServletException, IOException {
	       this.doGet(request, response);
	   }
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		      throws ServletException, IOException {
		String action = request.getServletPath();
		try {
			switch (action) {
			
			case "/new":
				showNewForm(request, response);
				break;
			case "/insert":
				insertAccount(request, response);
				break;
			case "/delete":
				deleteAccount(request, response);
				break;
			case "/edit":
				showEditForm(request, response);
				break;
			case "/update":
				updateAccount(request, response);
				break;
			default:
				home(request, response);
				break;
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);}
		}
	
	private void home(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<Compte> listCompte = metier.getAllComptes();
		boolean addd= true;
	    request.setAttribute("add", addd);
		request.setAttribute("listeCompte", listCompte);
		RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/home.jsp");
		dispatcher.forward(request, response);
	}
	
		
		
		private void showNewForm(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			boolean addd= true;
		    request.setAttribute("add", addd);
			RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/compte-form.jsp");
			dispatcher.forward(request, response);
		}

		private void showEditForm(HttpServletRequest request, HttpServletResponse response)
				throws SQLException, ServletException, IOException {
			boolean addd= true;
		    request.setAttribute("add", addd);
			Long code = Long.parseLong(request.getParameter("code"));
			Compte existingCompte = metier.getCompte(code);
			RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/compte-form.jsp");
			request.setAttribute("compte", existingCompte);
			dispatcher.forward(request, response);

		}

		private void insertAccount(HttpServletRequest request, HttpServletResponse response) 
				throws SQLException, IOException {
			double solde =Double.parseDouble(request.getParameter("solde")) ;
			Compte c= new Compte(solde, new Date(), true);
			metier.addCompte(c);
			boolean addd= false;
		    request.setAttribute("add", addd);
			response.sendRedirect("list");
		}

		private void updateAccount(HttpServletRequest request, HttpServletResponse response) 
				throws SQLException, IOException {
			Long code = Long.parseLong(request.getParameter("code"));
			Compte c= metier.getCompte(code);
	     	c.setSolde(Double.parseDouble(request.getParameter("solde")));
		    metier.updateCompte(c);
			response.sendRedirect("home");
		}

		private void deleteAccount(HttpServletRequest request, HttpServletResponse response) 
				throws SQLException, IOException {
			Long code = Long.parseLong(request.getParameter("code"));
			metier.deleteCompte(code);
			response.sendRedirect("home");
		
		
	}

}
