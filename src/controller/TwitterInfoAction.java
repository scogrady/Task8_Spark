package controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.mybeans.form.FormBeanFactory;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.mysql.jdbc.Buffer;
import com.sun.xml.internal.bind.v2.TODO;

import formbeans.TwitterLoginForm;
import model.Model;
import databeans.TwitterBean;

public class TwitterInfoAction extends Action {
	private FormBeanFactory<TwitterLoginForm> formBeanFactory = FormBeanFactory
			.getInstance(TwitterLoginForm.class);

	public TwitterInfoAction(Model model) {
	}

	@Override
	public String getName() {
		return "twitterInfo.do";
	}

	@Override
	public String perform(HttpServletRequest request) {

		try {
			Verifier verifier = (Verifier) request.getSession().getAttribute(
					"verifier");
			OAuthService service = (OAuthService) request.getSession()
					.getAttribute("oauthService");
			Token accessToken = (Token) request.getSession().getAttribute(
					"accessToken");
			String userId = (String) request.getSession()
					.getAttribute("userId");
			String userName = (String) request.getSession()
					.getAttribute("userName");

			// String filename = "/Users/LEE45/Desktop/file.txt";
			// BufferedWriter bufferedWriter = new BufferedWriter(new
			// FileWriter(filename));

			String searchParameters = "#love_adventure2";
			String resourceURL = "https://api.twitter.com/1.1/search/tweets.json";

			OAuthRequest httpRequest = new OAuthRequest(Verb.GET, resourceURL);
			httpRequest.addQuerystringParameter("q",
					OAuth.percentEncode(searchParameters));
			httpRequest.addQuerystringParameter("count", "20");
			service.signRequest(accessToken, httpRequest);
			Response response = httpRequest.send();

			//System.out.println(response.getBody());
			// bufferedWriter.write(response.getBody());

			JSONObject jsonobject = new JSONObject(response.getBody());
			JSONArray tweetArray = jsonobject.getJSONArray("statuses");

			ArrayList<String> allTweetsHtml = new ArrayList<String>();
			ArrayList<String> userTweetsHtml = new ArrayList<String>();

			for (int i = 0; i < tweetArray.length(); i++) {
				JSONObject tweet = tweetArray.getJSONObject(i);

				String id = tweet.getString("id_str");
				resourceURL = "https://api.twitter.com/1.1/statuses/oembed.json?id="
						+ id;
				httpRequest = new OAuthRequest(Verb.GET, resourceURL);
				service.signRequest(accessToken, httpRequest);
				response = httpRequest.send();
				JSONObject embed = new JSONObject(response.getBody());
				allTweetsHtml.add(embed.getString("html"));
			}
			
			request.setAttribute("allTweetsHtml", allTweetsHtml);
			
			
			
			//#love_adventure2 from:Iris_lsy45
			searchParameters = "#love_adventure2 form:" + userName;
			resourceURL = "https://api.twitter.com/1.1/search/tweets.json";

			httpRequest = new OAuthRequest(Verb.GET, resourceURL);
			httpRequest.addQuerystringParameter("q",
					OAuth.percentEncode(searchParameters));
			httpRequest.addQuerystringParameter("count", "20");
			service.signRequest(accessToken, httpRequest);
			response = httpRequest.send();

			//System.out.println(response.getBody());
			// bufferedWriter.write(response.getBody());

			jsonobject = new JSONObject(response.getBody());
			tweetArray = jsonobject.getJSONArray("statuses");


			for (int i = 0; i < tweetArray.length(); i++) {
				JSONObject tweet = tweetArray.getJSONObject(i);

				String id = tweet.getString("id_str");
				resourceURL = "https://api.twitter.com/1.1/statuses/oembed.json?id="
						+ id;
				httpRequest = new OAuthRequest(Verb.GET, resourceURL);
				service.signRequest(accessToken, httpRequest);
				response = httpRequest.send();
				JSONObject embed = new JSONObject(response.getBody());
				userTweetsHtml.add(embed.getString("html"));

			}

			
			
			request.setAttribute("userTweetsHtml", userTweetsHtml);

			return "twitter-info.jsp";

		} catch (JSONException e) {
			System.out.println(e);

			return "customer/error.jsp";
			// 
			// } catch (IOException e) {
			// e.printStackTrace();
		}
		// return "customer/error.jsp";

	}
}
