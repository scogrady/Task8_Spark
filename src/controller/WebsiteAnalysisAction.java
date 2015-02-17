package controller;

import java.util.ArrayList;
import java.util.HashMap;

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

public class WebsiteAnalysisAction extends Action {

	public WebsiteAnalysisAction(Model model) {

	}

	@Override
	public String getName() {
		return "websiteAnalysis.do";
	}

	@Override
	public String perform(HttpServletRequest request) {

		String resourceURL;
		String searchParameters;
		ArrayList<TwitterBean> result = new ArrayList<TwitterBean>();
		HashMap<String, Integer> activeUser = new HashMap<String, Integer>();
		HashMap<String, Integer> mostRetweet = new HashMap<String, Integer>();

		OAuthService service = (OAuthService) request.getSession()
				.getAttribute("oauthService");
		Token accessToken = (Token) request.getSession().getAttribute(
				"accessToken");

		searchParameters = "#love_adventure2";
		resourceURL = "https://api.twitter.com/1.1/search/tweets.json";

		try {

			OAuthRequest httpRequest = new OAuthRequest(Verb.GET, resourceURL);
			httpRequest.addQuerystringParameter("q", searchParameters);
			httpRequest.addQuerystringParameter("count", "100");
			service.signRequest(accessToken, httpRequest);
			Response response = httpRequest.send();

			System.out.println(response.getBody());
			System.out.println();

			JSONObject jsonobject = new JSONObject(response.getBody());
			JSONArray tweetArray = jsonobject.getJSONArray("statuses");

			for (int i = 0; i < tweetArray.length(); i++) {
				JSONObject tweet = tweetArray.getJSONObject(i);
				TwitterBean tweetBean = new TwitterBean();

				tweetBean.setId_str(tweet.getString("id_str"));
				String id_str = tweet.getString("id_str");

				if (tweet.get("coordinates") != org.json.JSONObject.NULL) {
					JSONObject coodinObject = (JSONObject) tweet
							.get("coordinates");
					tweetBean.setCoordinates(
							coodinObject.getString("longitude"),
							coodinObject.getString("latitude"));
				}

				tweetBean.setCreateTime(tweet.getString("created_at"));

				if (tweet.get("entities") != org.json.JSONObject.NULL) {
					JSONObject entitiesObject = tweet.getJSONObject("entities");
					JSONArray hashtagsArray = entitiesObject
							.getJSONArray("hashtags");
					String[] hashtags = new String[hashtagsArray.length()];
					for (int j = 0; j < hashtagsArray.length(); j++) {
						JSONObject hashtag = hashtagsArray.getJSONObject(j);
						hashtags[j] = hashtag.getString("text");
					}
					tweetBean.setEntities(hashtags);
				}

				tweetBean.setFavorite_count(tweet.getInt("favorite_count"));

				if (tweet.get("in_reply_to_status_id_str") != org.json.JSONObject.NULL) {
					tweetBean.setIn_reply_to_status_id_str((String) tweet
							.get("in_reply_to_status_id_str"));
				}

				if (tweet.get("in_reply_to_user_id_str") != org.json.JSONObject.NULL) {
					tweetBean.setIn_reply_to_user_id_str((String) tweet
							.get("in_reply_to_user_id_str"));
				}

				tweetBean.setText(tweet.getString("text"));

				tweetBean.setRetweet_count(tweet.getInt("retweet_count"));
				int retweet = tweet.getInt("retweet_count");
				mostRetweet.put(id_str, retweet);

				JSONObject userObject = tweet.getJSONObject("user");
				tweetBean.setUser_id_str(userObject.getString("id_str"));
				String user_id_str = userObject.getString("id_str");
				if (activeUser.containsKey(user_id_str)) {
					Integer num = activeUser.get(user_id_str);
					activeUser.replace(user_id_str, num, num + 1);
				} else {
					activeUser.put(user_id_str, 1);
				}
				result.add(tweetBean);
			}

			return "index.jsp";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "index.jsp";
		}

	}
}
