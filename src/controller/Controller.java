package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Model;


@SuppressWarnings("serial")
public class Controller extends HttpServlet {
	
    @Override
	public void init() throws ServletException {
        Model model = new Model(getServletConfig());

        Action.add(new LoginTwitterAction(model));
        Action.add(new GetTokenAction(model));
        Action.add(new PostTweetAction(model));
        Action.add(new TwitterInfoAction(model));
        Action.add(new WebsiteAnalysisAction(model));
        Action.add(new SearchNearby(model));
        Action.add(new SearchFlickrAction(model));
        Action.add(new LoginFlickrAction(model));
        Action.add(new getFlickrTokenAction(model));
    }

    @Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    @Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nextPage = performTheAction(request);
        sendToNextPage(nextPage,request,response);
    }
    
    /*
     * Extracts the requested action and (depending on whether the user is logged in)
     * perform it (or make the user login).
     * @param request
     * @return the next page (the view)
     */
    private String performTheAction(HttpServletRequest request) {
        HttpSession session     = request.getSession(true);
        String      servletPath = request.getServletPath();
        String      action = getActionName(servletPath);

        // System.out.println("servletPath="+servletPath+" requestURI="+request.getRequestURI()+"  user="+user);

      /* 
       * 
        if (action.equals("register.do") || action.equals("login.do")) {
        	// Allow these actions without logging in
			return Action.perform(action,request);
        }
        
       *  if (customer == null && employee == null) {
        	System.out.print("no user session!!!");
    		List<String> errors = new ArrayList<String>();
        	errors.add("Please login first!");
        	// If the user hasn't logged in, direct him to the login page
			return Action.perform("loginTwitter.do",request);
        }
        */
        
      	// Let the logged in user run his chosen action
		return Action.perform(action,request);
    }

    /*
     * If nextPage is null, send back 404
     * If nextPage ends with ".do", redirect to this page.
     * If nextPage ends with ".jsp", dispatch (forward) to the page (the view)
     * If is user's url, redirect to that page
     */
    private void sendToNextPage(String nextPage, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	if (nextPage == null) {
    		response.sendError(HttpServletResponse.SC_NOT_FOUND,request.getServletPath());
    		return;
    	}
    	if (nextPage.endsWith(".do")) {
			response.sendRedirect(nextPage);
			return;
    	}
    	
    	if (nextPage.endsWith(".jsp")) {
    		RequestDispatcher d =  request.getRequestDispatcher("WEB-INF/" + nextPage);
	   		d.forward(request,response);
	   		return;
    	} 
    	
    	if (nextPage.startsWith("https://")) {
    		response.sendRedirect(response.encodeRedirectURL(nextPage));
    		return;
    	}
    	if (nextPage.startsWith("http://")) {
    		response.sendRedirect(response.encodeRedirectURL(nextPage));
    		return;
    	}
    	response.sendRedirect(response.encodeRedirectURL("http://" + nextPage));
    	
    	//throw new ServletException(Controller.class.getName()+".sendToNextPage(\"" + nextPage + "\"): invalid extension.");
    }

	/*
	 * Returns the path component after the last slash removing any "extension"
	 * if present.
	 */
    private String getActionName(String path) {
        int slash = path.lastIndexOf('/');
        return path.substring(slash+1);
    }
}
