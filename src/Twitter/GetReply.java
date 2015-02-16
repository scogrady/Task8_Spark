package Twitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import org.json.JSONStringer;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import controller.OAuth;
import databeans.TwitterBean;

public class GetReply {

	private static final String PROTECTED_RESOURCE_URL = "https://api.twitter.com/1.1/account/verify_credentials.json";

	public static void main(String[] args) throws IOException, SAXException,
			ParserConfigurationException, JSONException {
		// If you choose to use a callback, "oauth_verifier" will be the return
		// value by Twitter (request param)
		OAuthService service = new ServiceBuilder()
				.provider(TwitterApi.class)
				.apiKey("H3rCWJM6LUb1Kz7H3IfTvAmf9")
				.apiSecret("oqqpI3xSej2pruwghfhIGHkepatNWE1ockPoYAcnl1ZizimqCZ")
				// .callback("http://www.baidu.com")
				.build();
		Scanner in = new Scanner(System.in);

		System.out.println("=== Twitter's OAuth Workflow ===");
		System.out.println();

		// Obtain the Request Token
		System.out.println("Fetching the Request Token...");
		Token requestToken = service.getRequestToken();
		// Token requestToken = new
		// Token("3015481311-QwGXcAv4SMR0CtDbhis7ELAX28LM3Mjgf9jDzbF",
		// "hgKoWNmg2ANi7oFANFv5kK1Dh3zpEoM6AOl0n8Q6xcbYK");
		System.out.println("Got the Request Token!");
		System.out.println();
		System.out.println();

		System.out.println("Now go and authorize Scribe here:");
		System.out.println(service.getAuthorizationUrl(requestToken));
		System.out.println("And paste the verifier here");
		System.out.print(">>");
		Verifier verifier = new Verifier(in.nextLine());
		// Verifier verifier = new
		// Verifier("kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg");

		System.out.println();

		// Trade the Request Token and Verfier for the Access Token
		System.out.println("Trading the Request Token for an Access Token...");
		Token accessToken = service.getAccessToken(requestToken, verifier);
		System.out.println("Got the Access Token!");
		System.out.println("(if you're curious, it looks like this: "
				+ accessToken + " )");
		System.out.println();

		// Now let's go and ask for a protected resource!
		System.out.println("Now we're going to access a protected resource...");
		// OAuthRequest request = new OAuthRequest(Verb.GET,
		// PROTECTED_RESOURCE_URL);

		String searchParameters = "#love_adventure2";
		String resourceURL = "https://api.twitter.com/1.1/search/tweets.json";

		OAuthRequest httpRequest = new OAuthRequest(Verb.GET, resourceURL);
		httpRequest.addQuerystringParameter("q",
				OAuth.percentEncode(searchParameters));
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

			if (tweet.get("coordinates") != org.json.JSONObject.NULL) {
				JSONObject coodinObject = (JSONObject) tweet.get("coordinates");
				tweetBean.setCoordinates(coodinObject.getString("longitude"),
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
			tweetBean.setId_str(tweet.getString("id_str"));
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

			JSONObject userObject = tweet.getJSONObject("user");
			tweetBean.setUser_id_str(userObject.getString("id_str"));
			
			System.out.println(tweetBean);

		}

	}
}
