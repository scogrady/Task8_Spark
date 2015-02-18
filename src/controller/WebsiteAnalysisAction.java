package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.x500.X500Principal;
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
import databeans.PopularTweetBean;

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
		searchParameters = "#love_adventure2";
		resourceURL = "https://api.twitter.com/1.1/search/tweets.json";

		OAuthService service = (OAuthService) request.getSession()
				.getAttribute("oauthService");
		Token accessToken = (Token) request.getSession().getAttribute(
				"accessToken");

		HashMap<String, Integer> activeUserMap = new HashMap<String, Integer>();
		HashMap<String, Integer> mostRetweet = new HashMap<String, Integer>();
		HashMap<String, Integer> hashTag = new HashMap<String, Integer>();

		ArrayList<LocationBean> mapList = new ArrayList<LocationBean>();
		ArrayList<PopularTweetBean> popularTweetList = new ArrayList<PopularTweetBean>();
		ArrayList<String> activeUser_name = new ArrayList<String>();
		ArrayList<Integer> activeUser_count = new ArrayList<Integer>();
		ArrayList<String> hashTag_name = new ArrayList<String>();
		ArrayList<Integer> hashTag_count = new ArrayList<Integer>();
		ArrayList<String> topic_name = new ArrayList<String>();
		ArrayList<Integer> topic_count = new ArrayList<Integer>();

		try {
			OAuthRequest httpRequest = new OAuthRequest(Verb.GET, resourceURL);
			httpRequest.addQuerystringParameter("q", searchParameters);
			httpRequest.addQuerystringParameter("count", "100");
			service.signRequest(accessToken, httpRequest);
			Response response = httpRequest.send();

			JSONObject jsonobject = new JSONObject(response.getBody());
			JSONArray tweetArray = jsonobject.getJSONArray("statuses");
			System.out.println("length: " + tweetArray.length());

			for (int i = 0; i < tweetArray.length(); i++) {
				JSONObject tweet = tweetArray.getJSONObject(i);
				PopularTweetBean popularTweet = new PopularTweetBean();

				String id_str = tweet.getString("id_str");

				String text = tweet.getString("text");
				popularTweet.setText(text);

				JSONObject userObject = tweet.getJSONObject("user");
				String user_id_str = userObject.getString("id_str");
				String user_screen_name = userObject.getString("screen_name");

				if (activeUserMap.containsKey(user_screen_name)) {
					Integer num = activeUserMap.get(user_screen_name);
					activeUserMap.put(user_screen_name, num + 1);

				} else {
					activeUserMap.put(user_screen_name, 1);
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

					System.out.print(placeName + ":" + mapBean.getX()
							+ mapBean.getY());
				}

				String create_at = tweet.getString("created_at");

				if (tweet.get("entities") != org.json.JSONObject.NULL) {
					JSONObject entitiesObject = tweet.getJSONObject("entities");
					JSONArray hashtagsArray = entitiesObject
							.getJSONArray("hashtags");
					for (int j = 0; j < hashtagsArray.length(); j++) {
						JSONObject hashtag = hashtagsArray.getJSONObject(j);
						String hashtagText = hashtag.getString("text");

						if (hashTag.containsKey(hashtagText)) {
							Integer num = hashTag.get(hashtagText);
							hashTag.put(hashtagText, num + 1);

						} else {
							hashTag.put(hashtagText, 1);
						}

					}
				}

				int retweet = tweet.getInt("retweet_count");
				mostRetweet.put(id_str, retweet);
				popularTweet.setRetweet_count(retweet);

				int favorite_count = tweet.getInt("favorite_count");
				popularTweet.setFavorite_count(favorite_count);

				if (tweet.get("in_reply_to_status_id_str") != org.json.JSONObject.NULL) {
					tweet.get("in_reply_to_status_id_str");
				}

				if (tweet.get("in_reply_to_user_id_str") != org.json.JSONObject.NULL) {
					tweet.get("in_reply_to_user_id_str");
				}
				popularTweetList.add(popularTweet);
			}

			// /////////////////// Active User //// /////////////////

			List<Map.Entry<String, Integer>> mappingList = new ArrayList<Map.Entry<String, Integer>>(
					activeUserMap.entrySet());
			Collections.sort(mappingList,
					new Comparator<Map.Entry<String, Integer>>() {
						public int compare(Map.Entry<String, Integer> mapping1,
								Map.Entry<String, Integer> mapping2) {
							return - mapping1.getValue().compareTo(
									mapping2.getValue());
						}
					});

			for (Map.Entry<String, Integer> mapping : mappingList) {
				activeUser_name.add(mapping.getKey());
				activeUser_count.add(mapping.getValue());
				System.out.println(mapping.getKey() + ":" + mapping.getValue());
			}

			// /////////////////// Hast Tag //// /////////////////

			List<Map.Entry<String, Integer>> hashList = new ArrayList<Map.Entry<String, Integer>>(
					hashTag.entrySet());
			Collections.sort(hashList,
					new Comparator<Map.Entry<String, Integer>>() {
						public int compare(Map.Entry<String, Integer> mapping1,
								Map.Entry<String, Integer> mapping2) {
							return - mapping1.getValue().compareTo(
									mapping2.getValue());
						}
					});

			for (Map.Entry<String, Integer> mapping : hashList) {
				hashTag_name.add(mapping.getKey());
				hashTag_count.add(mapping.getValue());
				System.out.println(mapping.getKey() + ":" + mapping.getValue());
			}

			// /////////////////// Top topic //// /////////////////

			String searchParametersUser;
			String topicString;
			resourceURL = "https://api.twitter.com/1.1/search/tweets.json";
			OAuthRequest httpRequestUser = new OAuthRequest(Verb.GET,
					resourceURL);
			httpRequestUser.addQuerystringParameter("count", "30");
			JSONObject jsonobjectUser;
			Response responseUser;

			// ---------------------------------------------------

			topicString = "Skiing";

			searchParametersUser = "#love_adventure2 #" + topicString;
			httpRequestUser.addQuerystringParameter("q", searchParametersUser);
			service.signRequest(accessToken, httpRequestUser);
			responseUser = httpRequestUser.send();
			jsonobjectUser = new JSONObject(responseUser.getBody());
			tweetArray = jsonobjectUser.getJSONArray("statuses");
			topic_name.add(topicString);
			topic_count.add(tweetArray.length());

			// ---------------------------------------------------

			topicString = "Kayaking";

			searchParametersUser = "#love_adventure2 #" + topicString;
			httpRequestUser.addQuerystringParameter("q", searchParametersUser);
			service.signRequest(accessToken, httpRequestUser);
			responseUser = httpRequestUser.send();
			jsonobjectUser = new JSONObject(responseUser.getBody());
			tweetArray = jsonobjectUser.getJSONArray("statuses");
			topic_name.add(topicString);
			topic_count.add(tweetArray.length());

			// ---------------------------------------------------

			topicString = "River rafting";

			searchParametersUser = "#love_adventure2 #" + topicString;
			httpRequestUser.addQuerystringParameter("q", searchParametersUser);
			service.signRequest(accessToken, httpRequestUser);
			responseUser = httpRequestUser.send();
			jsonobjectUser = new JSONObject(responseUser.getBody());
			tweetArray = jsonobjectUser.getJSONArray("statuses");
			topic_name.add(topicString);
			topic_count.add(tweetArray.length());

			// ---------------------------------------------------

			topicString = "Bunjee jumping";

			searchParametersUser = "#love_adventure2 #" + topicString;
			httpRequestUser.addQuerystringParameter("q", searchParametersUser);
			service.signRequest(accessToken, httpRequestUser);
			responseUser = httpRequestUser.send();
			jsonobjectUser = new JSONObject(responseUser.getBody());
			tweetArray = jsonobjectUser.getJSONArray("statuses");
			topic_name.add(topicString);
			topic_count.add(tweetArray.length());

			// ---------------------------------------------------

			topicString = "Running";

			searchParametersUser = "#love_adventure2 #" + topicString;
			httpRequestUser.addQuerystringParameter("q", searchParametersUser);
			service.signRequest(accessToken, httpRequestUser);
			responseUser = httpRequestUser.send();
			jsonobjectUser = new JSONObject(responseUser.getBody());
			tweetArray = jsonobjectUser.getJSONArray("statuses");
			topic_name.add(topicString);
			topic_count.add(tweetArray.length());
			
			// ---------------------------------------------------

			topicString = "Scuba diving";

			searchParametersUser = "#love_adventure2 #" + topicString;
			httpRequestUser.addQuerystringParameter("q", searchParametersUser);
			service.signRequest(accessToken, httpRequestUser);
			responseUser = httpRequestUser.send();
			jsonobjectUser = new JSONObject(responseUser.getBody());
			tweetArray = jsonobjectUser.getJSONArray("statuses");
			topic_name.add(topicString);
			topic_count.add(tweetArray.length());

			// ///////////////////// Set up Attribute ///////////////////////

			request.setAttribute("mapList", mapList);
			request.setAttribute("popularTweetList", popularTweetList);
			request.setAttribute("activeUser_name", activeUser_name);
			request.setAttribute("activeUser_count", activeUser_count);
			request.setAttribute("hashTag_name", hashTag_name);
			request.setAttribute("hashTag_count", hashTag_count);
			request.setAttribute("topic_name", topic_name);
			request.setAttribute("topic_count", topic_count);


			return "web-analysis.jsp";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "web-analysis.jsp";
		}

	}

}
