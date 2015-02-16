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

public class FlickrInfoAction extends Action {
	private FormBeanFactory<TwitterLoginForm> formBeanFactory = FormBeanFactory
			.getInstance(TwitterLoginForm.class);

	public FlickrInfoAction(Model model) {

	}

	public String getName() {
		return "flickrInfo.do";
	}

	public String perform(HttpServletRequest request) {
			return "flickr-info.jsp";
	}
}
