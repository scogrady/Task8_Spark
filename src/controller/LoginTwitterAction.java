package controller;

import javax.servlet.http.HttpServletRequest;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import model.Model;

public class LoginTwitterAction extends Action {

	public LoginTwitterAction(Model model) {

	}

	@Override
	public String getName() {
		return "loginTwitter.do";
	}

	@Override
	public String perform(HttpServletRequest request) {
		OAuthService service;
		if (request.getSession().getAttribute("oauthService") != null) 
			service = (OAuthService)request.getSession().getAttribute("oauthService"); else 
				service = new ServiceBuilder()
				.provider(TwitterApi.class)
				.apiKey("j1xlSHyHGO5aqC1JBzom9Cm0Y")
				.apiSecret("3kY5as1OCl1LizvVQBFuonrbZndUSWtnnBqDw9ZISFee9jFxcw")
				.callback("http://localhost:8080/Task8_Spark/getTokenAction.do").build();

		Token requestToken = service.getRequestToken();

		String url = service.getAuthorizationUrl(requestToken);
		request.getSession().setAttribute("oauthService", service);
		request.getSession().setAttribute("requestToken", requestToken);

		return url;

	}
}
