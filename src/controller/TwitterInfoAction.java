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

import formbeans.TwitterLoginForm;
import model.Model;

public class TwitterInfoAction extends Action {
	private FormBeanFactory<TwitterLoginForm> formBeanFactory = FormBeanFactory
			.getInstance(TwitterLoginForm.class);

	public TwitterInfoAction(Model model) {
	}

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
			String userName = (String) request.getSession().getAttribute(
					"userName");

			ArrayList<String> allTweetsHtml = new ArrayList<String>();
			ArrayList<String> userTweetsHtml = new ArrayList<String>();

			// String filename = "/Users/LEE45/Desktop/file.txt";
			// BufferedWriter bufferedWriter = new BufferedWriter(new
			// FileWriter(filename));
			String searchParameters = "";
			if (request.getParameter("searchKey") != null) {
				searchParameters = "#love_adventure2 "
						+ request.getParameter("searchKey");

			} else {
				searchParameters = "#love_adventure2";
			}

			String resourceURL = "https://api.twitter.com/1.1/search/tweets.json";

			OAuthRequest httpRequest = new OAuthRequest(Verb.GET, resourceURL);
			httpRequest.addQuerystringParameter("q", searchParameters);
			httpRequest.addQuerystringParameter("count", "10");
			service.signRequest(accessToken, httpRequest);
			Response response = httpRequest.send();

			// System.out.println(response.getBody());
			// bufferedWriter.write(response.getBody());

			JSONObject jsonobject = new JSONObject(response.getBody());
			JSONArray tweetArray = jsonobject.getJSONArray("statuses");

			for (int i = 0; i < tweetArray.length(); i++) {
				JSONObject tweet = tweetArray.getJSONObject(i);

				String id = tweet.getString("id_str");
				resourceURL = "https://api.twitter.com/1.1/statuses/oembed.json?id="
						+ id;
				httpRequest = new OAuthRequest(Verb.GET, resourceURL);
				service.signRequest(accessToken, httpRequest);
				response = httpRequest.send();
				// System.out.println(response.getBody());
				JSONObject embed = new JSONObject(response.getBody());
				allTweetsHtml.add(embed.getString("html"));
			}

			request.setAttribute("allTweetsHtml", allTweetsHtml);

			System.out.println(userTweetsHtml);

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
