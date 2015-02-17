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

import formbeans.FlickrLoginForm;
import model.Model;
import databeans.TwitterBean;

public class getFlickrTokenAction extends Action {
	private FormBeanFactory<FlickrLoginForm> formBeanFactory = FormBeanFactory
			.getInstance(FlickrLoginForm.class);

	public getFlickrTokenAction(Model model) {

	}

	@Override
	public String getName() {
		return "getFlickrTokenAction.do";
	}

	@Override
	public String perform(HttpServletRequest request) {

		try {
			FlickrLoginForm form = formBeanFactory.create(request);
			Verifier Flickrverifier = new Verifier(form.getOauth_verifier());	
			OAuthService FlickroauthService = (OAuthService) request.getSession().getAttribute("FlickroauthService");
			Token FlickrrequestToken = (Token) request.getSession().getAttribute("FlickrrequestToken");
			request.getSession().setAttribute("Flickrverifier", Flickrverifier);
			System.out.println("verfier= "+Flickrverifier.getValue());
			Token FlickraccessToken = FlickroauthService.getAccessToken(FlickrrequestToken, Flickrverifier);
			System.out.println(FlickraccessToken);
			OAuthRequest flickrrequest = new OAuthRequest(Verb.GET, "https://api.flickr.com/services/rest/");
			flickrrequest.addQuerystringParameter("method", "flickr.test.login");
			FlickroauthService.signRequest(FlickraccessToken, flickrrequest);
		    Response response = flickrrequest.send();
		    String id=response.getBody().substring(response.getBody().indexOf("id=")+4, response.getBody().indexOf("id=")+17);
		    System.out.println("ID="+id);
		    String name=response.getBody().substring(response.getBody().indexOf("<username>")+10, response.getBody().lastIndexOf("</username>"));
		    System.out.println("Name="+name);
		    Token rt = FlickroauthService.getRequestToken();
		    System.out.println("rt:"+rt);
		    Verifier verifier2= new Verifier(rt.getSecret());
		   	System.out.println("verifier2:"+verifier2);
		   	request.getSession().setAttribute("FlickrUserName", name);
		   	request.getSession().setAttribute("FlickrNSID", id);
		   	
			
			/*
			OAuthRequest httpRequest = new OAuthRequest(Verb.GET, "https://api.flickr.com/services/rest/");
			httpRequest.addQuerystringParameter("method", "flickr.test.login");
			FlickroauthService.signRequest(FlickrrequestToken, httpRequest);
		    Response response = httpRequest.send();
		    System.out.println("Got it! Lets see what we found...");
		    System.out.println();
		    System.out.println(response.getBody());
			
		    String id=response.getBody().substring(response.getBody().indexOf("id=")+4, response.getBody().indexOf("id=")+17);
		    System.out.println("ID="+id);
		    String name=response.getBody().substring(response.getBody().indexOf("<username>")+10, response.getBody().lastIndexOf("</username>"));
		    System.out.println("Name="+name);
			
			
			
			
			
			/*OAuthRequest httpRequest = new OAuthRequest(Verb.GET,resourceURL);
			

			service.signRequest(accessToken, httpRequest);

			// request.addBodyParameter("id", value);
			Response response = httpRequest.send();
			System.out.println(response.getBody());
			JSONArray jsonarray = new JSONArray(response.getBody());
			List<TwitterBean> twitters = new ArrayList<TwitterBean>();
			for (int i = 0; i < jsonarray.length(); i++) {
				TwitterBean twitter = new TwitterBean();
			    JSONObject jsonobject = jsonarray.getJSONObject(i);
			    String createTime = jsonobject.getString("created_at");
			    String text = jsonobject.getString("text");
			    JSONObject user = (JSONObject)jsonobject.get("user");
			    twitter.setCreateTime(createTime);
			    twitter.setText(text);
			    twitter.setUser(user.getString("name"));
			    twitters.add(twitter);
			}
			request.setAttribute("tweets", twitters);
			*/
			return "uploadFlickr.do";

		} catch (FormBeanException e) {
			return "customer/error.jsp";

		}

	}
}
