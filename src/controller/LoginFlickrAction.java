package controller;

import javax.servlet.http.HttpServletRequest;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

public class LoginFlickrAction extends Action {

	@Override
	public String getName() {
		return "loginFlickr.do";
	}

	@Override
	public String perform(HttpServletRequest request) {
		OAuthService service;
		 String apiKey = "f3e75ee9d97069d826d1225ef5190730";
		    String apiSecret = "6dad87878538613e";
		if (request.getSession().getAttribute("oauthService") != null) 
			service = (OAuthService)request.getSession().getAttribute("oauthService"); else 
				service = new ServiceBuilder()
				.provider(FlickrApi.class)
				.apiKey(apiKey)
				.apiSecret(apiSecret)
				.callback("http://localhost:8080/Task8/getFlickrTokenAction.do").build();

		Token FlickrrequestToken = service.getRequestToken();

		String url = service.getAuthorizationUrl(FlickrrequestToken);
		request.getSession().setAttribute("FlickroauthService", service);
		request.getSession().setAttribute("FlickrrequestToken", FlickrrequestToken);

		return url;
	}

}
