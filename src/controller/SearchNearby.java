package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import model.Model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;


import databeans.LocationBean;
import databeans.TwitterBean;
import databeans.UserBean;

public class SearchNearby extends Action {

	public SearchNearby(Model model) {

	}

	@Override
	public String getName() {
		return "searchNearby.do";
	}

	@Override
	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);
		
		
		try {

			LocationBean[] locationList = new LocationBean[3];
			
			locationList[0] = new LocationBean();
			locationList[0].setX(37.4232);
			locationList[0].setY(-122.0853);
			locationList[0].setDescription("Me");
			
			locationList[1] = new LocationBean();
			locationList[1].setX(37.4289);
			locationList[1].setY(-122.1697);
			locationList[1].setDescription("User one");
		
			locationList[2] = new LocationBean();
			locationList[2].setX(37.6153);
			locationList[2].setY(-122.3900);
			locationList[2].setDescription("User two");
			
			request.setAttribute("locationList", locationList);
			

			return "search-nearby.jsp";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "search-nearby.jsp";
		}

	}
}
