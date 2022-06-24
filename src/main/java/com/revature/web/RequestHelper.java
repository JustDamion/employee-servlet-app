package com.revature.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dao.EmployeeDao;
import com.revature.models.Employee;
import com.revature.service.EmployeeService;

public class RequestHelper {
	
	// employeeservice
	private static EmployeeService eserv = new EmployeeService(new EmployeeDao());
	// object mapper (for frontend)
	private static ObjectMapper om = new ObjectMapper();
	
	/**
	 * This method will call the EmployeeService's findAll method()
	 * -- use an object mapper to transform that list to a JSON String
	 * -- then use the print writer to print out that JSON string to the screen
	 */
	public static void processEmployees(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// will return an entire list of Employees
		
		// 1. set the content type to be application/json
		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		// 2. Call the getAll() method from the employee service
		List<Employee> emps = eserv.getAll();
		
		// 3. transform the list to a string
		String jsonString = om.writeValueAsString(emps);
		
		// get print writer, then write it out
		PrintWriter out = response.getWriter();
		out.println(jsonString); // write the string to the response body
	}
	
	public static void processRegistration(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 1. extract all values from the parameters
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		// 2. construct a new employee object
		Employee e = new Employee(firstname, lastname, username, password);
		
		// 3. call the register() method from the service layer
		int pk = eserv.register(e);
		
		// 4. check it's ID... if it's > 0 it's successful
		if (pk > 0) {
			
			e.setId(pk);
			// add the user to the session
			HttpSession session = request.getSession();
			session.setAttribute("the-user", e);
			
			request.getRequestDispatcher("welcome.html").forward(request, response);;
			// using the request dispatcher, forward the request and response to a new resource...
			// send the user to a new page -- welcome.html
		} else {
			
			// if it's -1, that means the register method failed (and there's probably a duplicate user)
			// use the PrintWriter to print out
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			
			out.println("<h1>Registration failed. Username already exists</h1>");
			out.println("<a href=\"index.html\">Back</a>");
		}
				
	}
	
	/**
	 * What does this method do?
	 * 
	 * It extracts the parameters from a request (username and password) from the UI
	 * It will call the confirmLogin() method from the EmployeeService and
	 * see if a user with that username and password exists
	 * 
	 * Who will provide the method with the HttpRequest? The UI
	 * We need to build an html doc with a form that will send these parameters to the method
	 */
	public static void processLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		// 1. Extract the parameters from the request (username & password)
		String username = request.getParameter("username");
		String password = request.getParameter("password"); // use fn + arrow key < or > to get to the beginning or end of a line of code
		
		// 2. call the confirm login(0 method from the employeeService and see what it returns
		Employee e = eserv.confirmLogin(username, password);
		
		// 3. If the user exists, lets print their info to the screen
		if (e.getId() > 0) {
			
			// grab the session
			HttpSession session = request.getSession();
			
			// add the user to the session
			session.setAttribute("the-user", e);
			
			// alternatively you can redirect to another resource instead of printing out dynamically
			
			// print out the user's data with the print writer
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			
			out.println("<h1>Welcome " + e.getFirstName() + "!</h1>");
			out.println("<h3>You have successfully logged in!</h3>");
			
			// you COULD print the object out as a JSON string
			String jsonString = om.writeValueAsString(e);
			out.println(jsonString);
		} else {
			
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			out.println("No user found, sorry");
			
//			response.setStatus(204); // 204 means successful connection to the server, but no content found
		}
	}
}
