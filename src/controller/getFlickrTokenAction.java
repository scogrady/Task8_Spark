package controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.Uploader;

import formbeans.FlickrLoginForm;
import model.Model;
import databeans.TwitterBean;

public class getFlickrTokenAction extends Action {
	private FormBeanFactory<FlickrLoginForm> formBeanFactory = FormBeanFactory
			.getInstance(FlickrLoginForm.class);

	public getFlickrTokenAction(Model model) {

	}

	public String getName() {
		return "getFlickrTokenAction.do";
	}

	public String perform(HttpServletRequest request) {

		try {
			System.out.println("getFlickrTokenAction called");
			FlickrLoginForm form = formBeanFactory.create(request);
			Verifier Flickrverifier = new Verifier(form.getOauth_verifier());	
			OAuthService FlickroauthService = (OAuthService) request.getSession().getAttribute("FlickroauthService");
			Token FlickrrequestToken = (Token) request.getSession().getAttribute("FlickrrequestToken");
			request.getSession().setAttribute("Flickrverifier", Flickrverifier);
			System.out.println("verfier= "+Flickrverifier.getValue());
			Token FlickraccessToken = FlickroauthService.getAccessToken(FlickrrequestToken, Flickrverifier);
			System.out.println(FlickraccessToken);
			/*
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
		 
			
			 
			OAuthRequest httpRequest = new OAuthRequest(Verb.POST, "https://up.flickr.com/services/upload/");
			
			////////////////
			InputStream in = new FileInputStream("C:\\Users\\abhishek\\Downloads\\upload.png");
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int i;
			byte[] buffer = new byte[1024];
			while ((i = in.read(buffer)) != -1) {
				out.write(buffer, 0, i);
			}
			in.close();
			 byte[] result = out.toByteArray();
			 
			 String imageDataString = Base64.getUrlEncoder().encodeToString(result);//encodeBase64URLSafeString(result);
			 //String imageDataString = Base64.getEncoder().encodeToString(result);
			
			
			
		   
		    //System.out.println(response1.getBody());
			
		   
			
			
			
			  	*/
			
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
			String url = "https://www.flickr.com/photos/upload/"; 

			java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));

			return "searchFlickr.do";

		} catch (FormBeanException e) {
			return "customer/error.jsp";

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "searchFlickr.do";

	}
}
