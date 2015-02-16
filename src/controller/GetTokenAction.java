package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import formbeans.TwitterLoginForm;
import model.Model;
import databeans.TwitterBean;

public class GetTokenAction extends Action {
	private FormBeanFactory<TwitterLoginForm> formBeanFactory = FormBeanFactory
			.getInstance(TwitterLoginForm.class);

	public GetTokenAction(Model model) {

	}

	@Override
	public String getName() {
		return "getTokenAction.do";
	}

	@Override
	public String perform(HttpServletRequest request) {

		try {
			TwitterLoginForm form = formBeanFactory.create(request);
			Verifier verifier = new Verifier(form.getOauth_verifier());
			OAuthService service = (OAuthService) request.getSession()
					.getAttribute("oauthService");
			Token requestToken = (Token) request.getSession().getAttribute(
					"requestToken");
			request.getSession().setAttribute("verifier", verifier);

			Token accessToken = service.getAccessToken(requestToken, verifier);
			request.getSession().setAttribute("accessToken", accessToken);
			String accesString = accessToken.getToken();
			String[] acccesStrings = accesString.split("-");
			String userIdString = acccesStrings[0];

			request.getSession().setAttribute("userId", userIdString);

			String resourceURL = "https://api.twitter.com/1.1/users/lookup.json";
			OAuthRequest httpRequest = new OAuthRequest(Verb.GET, resourceURL);
			httpRequest.addQuerystringParameter("user_id",
					OAuth.percentEncode(userIdString));
			httpRequest.addQuerystringParameter("count", "1");
			service.signRequest(accessToken, httpRequest);
			Response response = httpRequest.send();
			JSONArray userArray = new JSONArray(response.getBody());
			JSONObject user = userArray.getJSONObject(0);
			String screen_name = user.getString("screen_name");
			
			request.getSession().setAttribute("userName", screen_name);

			
			return "twitterInfo.do";

		} catch (FormBeanException e) {
			return "customer/error.jsp";

		} catch (JSONException e) {
			return "customer/error.jsp";
		}

	}
}
