package com.ctrlbot.registration;

import java.io.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String uemail = request.getParameter("username");
		String upwd = request.getParameter("password");
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;
		
		 // Basic validation to check if username and password are provided
        if (uemail == null || uemail.trim().isEmpty() || upwd == null || upwd.trim().isEmpty()) {
            request.setAttribute("status", "failed");
            dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
            return;
        }
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ctrlbot?useSSL=false", "root", "100705");
			PreparedStatement pst = con.prepareStatement("select * from users where uemail = ? and upwd = ?");
			pst.setString(1, uemail);
			pst.setString(2, upwd);
			
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				session.setAttribute("name", rs.getString("uname"));
				dispatcher = request.getRequestDispatcher("index.jsp");
			}
			else {
				request.setAttribute("status", "failed");
				dispatcher = request.getRequestDispatcher("login.jsp");
			}
			dispatcher.forward(request, response);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
		}
	}

}
