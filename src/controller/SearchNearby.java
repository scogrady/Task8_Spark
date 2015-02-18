package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import model.Model;
import databeans.LocationBean;

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
		
		
		String resourceURL;
		String searchParameters;
		searchParameters = "#love_adventure2";
		resourceURL = "https://api.twitter.com/1.1/search/tweets.json";
		
		OAuthService service = (OAuthService) request.getSession()
				.getAttribute("oauthService");
		Token accessToken = (Token) request.getSession().getAttribute(
				"accessToken");
		
		HashMap<String, Integer> activeUser = new HashMap<String, Integer>();
		HashMap<String, Integer> mostRetweet = new HashMap<String, Integer>();
		ArrayList<LocationBean> mapList = new ArrayList<LocationBean>();


		try {
			OAuthRequest httpRequest = new OAuthRequest(Verb.GET, resourceURL);
			httpRequest.addQuerystringParameter("q", searchParameters);
			httpRequest.addQuerystringParameter("count", "100");
			service.signRequest(accessToken, httpRequest);
			Response response = httpRequest.send();
			System.out.println(response.getBody());

			JSONObject jsonobject = new JSONObject(response.getBody());
			JSONArray tweetArray = jsonobject.getJSONArray("statuses");
			System.out.println("length: "+tweetArray.length());
			
			for (int i = 0; i < tweetArray.length(); i++) {
				JSONObject tweet = tweetArray.getJSONObject(i);				

				String id_str = tweet.getString("id_str");

				JSONObject userObject = tweet.getJSONObject("user");
				String user_id_str = userObject.getString("id_str");
				String user_screen_name = userObject.getString("screen_name");

				if (activeUser.containsKey(user_id_str)) {
					Integer num = activeUser.get(user_id_str);
					activeUser.put(user_id_str, num+1);
				} else {
					activeUser.put(user_id_str, 1);
				}
				
				
				if (tweet.get("place") != org.json.JSONObject.NULL) {

					JSONObject place = (JSONObject) tweet.get("place");
					String placeName = place.getString("name");
					JSONObject bounding_box = (JSONObject) place
							.get("bounding_box");
					JSONArray coordinateArray = (JSONArray) bounding_box
							.get("coordinates");
					JSONArray coordArray = (JSONArray) coordinateArray.get(0);

					double x = 0.0, y = 0.0;
					JSONArray coordinates;
					for (int j = 0; j < 4; j++) {
						coordinates = (JSONArray) coordArray.get(j);
						x = x + coordinates.getDouble(0);
						y = y + coordinates.getDouble(1);
					}
					LocationBean mapBean = new LocationBean();
					mapBean.setX(x / 4);
					mapBean.setY(y / 4);
					mapBean.setDescription(user_screen_name + " at "
							+ placeName);
					mapList.add(mapBean);

				}
				request.setAttribute("locationList", mapList);
				

			}
		
		
			return "search-nearby.jsp";
		} catch (Exception e) {

			return "search-nearby.jsp";
		}

	}
}
