package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		OAuthService service = (OAuthService) request.getSession()
				.getAttribute("oauthService");
		Token accessToken = (Token) request.getSession().getAttribute(
				"accessToken");
		searchParameters = (String) request.getSession().getAttribute("userId");
		String userName = (String) request.getSession()
				.getAttribute("userName");

		ArrayList<String> userTweetsHtml = new ArrayList<String>();
		ArrayList<String> hashUserTag_name = new ArrayList<String>();
		ArrayList<Integer> hashUserTag_count = new ArrayList<Integer>();

		HashMap<String, Integer> hashUserTag = new HashMap<String, Integer>();

		try {
			// #love_adventure2 from:Iris_lsy45
			String searchParametersUser = "#love_adventure2 from:" + userName;
			System.out.println(searchParametersUser);

			resourceURL = "https://api.twitter.com/1.1/search/tweets.json";

			OAuthRequest httpRequestUser = new OAuthRequest(Verb.GET,
					resourceURL);
			httpRequestUser.addQuerystringParameter("q", searchParametersUser);

			httpRequestUser.addQuerystringParameter("count", "10");
			service.signRequest(accessToken, httpRequestUser);
			Response responseUser = httpRequestUser.send();

			JSONObject jsonobjectUser = new JSONObject(responseUser.getBody());
			JSONArray tweetArray = jsonobjectUser.getJSONArray("statuses");

			for (int i = 0; i < tweetArray.length(); i++) {
				JSONObject tweet = tweetArray.getJSONObject(i);

				String id = tweet.getString("id_str");
				if (tweet.get("entities") != org.json.JSONObject.NULL) {
					JSONObject entitiesObject = tweet.getJSONObject("entities");
					JSONArray hashtagsArray = entitiesObject
							.getJSONArray("hashtags");
					for (int j = 0; j < hashtagsArray.length(); j++) {
						JSONObject hashtag = hashtagsArray.getJSONObject(j);
						String hashtagText = hashtag.getString("text");

						if (hashUserTag.containsKey(hashtagText)) {
							Integer num = hashUserTag.get(hashtagText);
							hashUserTag.put(hashtagText, num + 1);

						} else {
							hashUserTag.put(hashtagText, 1);
						}

					}
				}

				resourceURL = "https://api.twitter.com/1.1/statuses/oembed.json?id="
						+ id;
				httpRequestUser = new OAuthRequest(Verb.GET, resourceURL);
				service.signRequest(accessToken, httpRequestUser);
				responseUser = httpRequestUser.send();
				JSONObject embed = new JSONObject(responseUser.getBody());
				userTweetsHtml.add(embed.getString("html"));
			}
			
			

			searchParametersUser = "#love_adventure2 from:" + userName + " :)";
			httpRequestUser = new OAuthRequest(Verb.GET, resourceURL);
			httpRequestUser.addQuerystringParameter("q", searchParametersUser);
			httpRequestUser.addQuerystringParameter("count", "30");
			service.signRequest(accessToken, httpRequestUser);
			responseUser = httpRequestUser.send();
			jsonobjectUser = new JSONObject(responseUser.getBody());
			tweetArray = jsonobjectUser.getJSONArray("statuses");
			int positive = tweetArray.length();
			
			searchParametersUser = "#love_adventure2 from:" + userName + " :(";
			httpRequestUser = new OAuthRequest(Verb.GET, resourceURL);
			httpRequestUser.addQuerystringParameter("q", searchParametersUser);
			httpRequestUser.addQuerystringParameter("count", "30");
			service.signRequest(accessToken, httpRequestUser);
			responseUser = httpRequestUser.send();
			jsonobjectUser = new JSONObject(responseUser.getBody());
			tweetArray = jsonobjectUser.getJSONArray("statuses");
			int negative = tweetArray.length();
					

			resourceURL = "https://api.twitter.com/1.1/users/lookup.json";

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
			userBean.setScreen_name(user.getString("screen_name"));
			userBean.setId_str(user.getString("id_str"));
			userBean.setCreated_at(user.getString("created_at"));
			userBean.setFavourites_count(user.getInt("favourites_count"));
			userBean.setFollowers_count(user.getInt("followers_count"));
			userBean.setFriends_count(user.getInt("friends_count"));
			userBean.setStatuses_count(user.getInt("statuses_count"));

			// ///////////////////// Set up attribute ////////////////////
			List<Map.Entry<String, Integer>> mappingList = new ArrayList<Map.Entry<String, Integer>>(
					hashUserTag.entrySet());
			for (Map.Entry<String, Integer> mapping : mappingList) {
				hashUserTag_name.add(mapping.getKey());
				hashUserTag_count.add(mapping.getValue());
				System.out.println(mapping.getKey() + ":" + mapping.getValue());
			}

			request.setAttribute("userTweetsHtml", userTweetsHtml);
			request.setAttribute("userBean", userBean);
			request.setAttribute("positive", positive);
			request.setAttribute("negative", negative);
			request.setAttribute("hashUserTag_name", hashUserTag_name);
			request.setAttribute("hashUserTag_count", hashUserTag_count);


			return "index.jsp";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "index.jsp";
		}

	}
}
