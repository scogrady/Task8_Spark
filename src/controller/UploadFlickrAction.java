package controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import model.Model;

import com.flickr4java.flickr.FlickrException;

public class UploadFlickrAction extends Action {

	@Override
	public String getName() {
		return "uploadFlickr.do";
	}
	public UploadFlickrAction(Model model) {
	}
	@Override
	public String perform(HttpServletRequest request) {
		
		 String apiKey ="f3e75ee9d97069d826d1225ef5190730";
	     String secret ="6dad87878538613e";
	     String authsDir="C:\\Users\\abhishek\\Downloads\\upload.png";
	        
		try {
			uploadPhoto ub=new uploadPhoto(apiKey,  request.getSession().getAttribute("FlickrNSID").toString(), secret, new File(authsDir),  request.getSession().getAttribute("FlickrUserName").toString());
			System.out.println(ub);
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "SearchFlickr.do";
	}

}
