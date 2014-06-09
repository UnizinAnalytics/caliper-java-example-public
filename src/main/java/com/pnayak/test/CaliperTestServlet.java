package com.pnayak.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imsglobal.caliper.Caliper;
import org.imsglobal.caliper.Options;

/**
 * Servlet implementation class CaliperTestServlet
 */
public class CaliperTestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static String HOST = "http://devnull.imsglobal.org";
	private static String API_KEY = "FEFNtMyXRZqwAH4svMakTw";

	private void initialize() {

		// Initialize the sensor - this needs to be done only once
		Options options = new Options();
		options.setHost(HOST);
		options.setApiKey(API_KEY);
		Caliper.initialize(options);
	}

	/**
	 * Default constructor.
	 */
	public CaliperTestServlet() {
		initialize();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO
		// Invoke the Caliper sensor, send a set of Caliper Events
		//
		response.getWriter().write(Caliper.getStatistics().toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
