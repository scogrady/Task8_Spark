package controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import model.Model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import databeans.TwitterBean;
import databeans.UserBean;

public class UserAnalysisAction extends Action {

	public UserAnalysisAction(Model model) {

	}

	@Override
	public String getName() {
		return "userAnalysis.do";
	}

	@Override
	public String perform(HttpServletRequest request) {

		String resourceURL;
		String searchParameters;
		ArrayList<TwitterBean> result = new ArrayList<TwitterBean>();

		OAuthService service = (OAuthService) request.getSession()
				.getAttribute("oauthService");
		Token accessToken = (Token) request.getSession().getAttribute(
				"accessToken");
		searchParameters = (String) request.getSession().getAttribute("userId");

		// searchParameters = "#love_adventure2";
		resourceURL = "https://api.twitter.com/1.1/users/lookup.json";

		try {

			OAuthRequest httpRequest = new OAuthRequest(Verb.GET, resourceURL);
			httpRequest.addQuerystringParameter("user_id", searchParameters);
			httpRequest.addQuerystringParameter("count", "1");
			service.signRequest(accessToken, httpRequest);
			Response response = httpRequest.send();

			System.out.println(response.getBody());
			System.out.println();

			JSONArray userArray = new JSONArray(response.getBody());
			JSONObject user = userArray.getJSONObject(0);

			UserBean userBean = new UserBean();

			userBean.setName(user.getString("name"));
			userBean.setId_str(user.getString("id_str"));
			userBean.setCreated_at(user.getString("created_at"));
			userBean.setFavourites_count(user.getInt("favourites_count"));
			userBean.setFollowers_count(user.getInt("followers_count"));
			userBean.setFriends_count(user.getInt("friends_count"));
			userBean.setScreen_name(user.getString("screen_name"));
			userBean.setStatuses_count(user.getInt("statuses_count"));

			return "index.jsp";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "index.jsp";
		}

	}
}
