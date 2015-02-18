package controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.json.JSONArray;
import org.json.JSONObject;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import model.Model;
import databeans.FlickrLocationBean;
import databeans.LocationBean;

public class SearchNearby extends Action {

	public SearchNearby(Model model) {

	}

	public String getLocation(String ids) throws MalformedURLException,
			IOException {
		String title = null;
		URLConnection uc = new URL(
				"https://api.flickr.com/services/rest/?method=flickr.photos.geo.getLocation&api_key=f3e75ee9d97069d826d1225ef5190730&photo_id="
						+ ids).openConnection();
		//System.out.println(uc.getURL());
		XMLInputFactory factory = XMLInputFactory.newInstance();

		try {
			String TempX = null;
			String TempY = null;

			XMLEventReader r = factory
					.createXMLEventReader(uc.getInputStream());
			int i = -1;
			while (r.hasNext()) {

				XMLEvent event = r.nextEvent();
				if (event.isStartElement()) {
					StartElement element = (StartElement) event;
					String elementName = element.getName().toString();
					if (elementName.equals("location")) {
						i++;
						Iterator iterator = element.getAttributes();

						while (iterator.hasNext()) {

							Attribute attribute = (Attribute) iterator.next();
							QName name = attribute.getName();
							String value = attribute.getValue();
							if ((name.toString()).equals("latitude")) {
								TempX = value;

							}
							if ((name.toString()).equals("longitude")) {
								TempY = value;
							}

						}
						title = TempX + ":" + TempY;

					}
					// End of Printing

				}
			}
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			//System.out.println("do something");
		}

		return title;
	}

	@Override
	public String getName() {
		return "searchNearby.do";
	}

	@Override
	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		String resourceURL;
		String searchParameters;
		searchParameters = "#love_adventure2";
		resourceURL = "https://api.twitter.com/1.1/search/tweets.json";

		OAuthService service = (OAuthService) request.getSession()
				.getAttribute("oauthService");
		Token accessToken = (Token) request.getSession().getAttribute(
				"accessToken");

		HashMap<String, Integer> activeUser = new HashMap<String, Integer>();
		ArrayList<LocationBean> mapList = new ArrayList<LocationBean>();

		try {
			OAuthRequest httpRequest = new OAuthRequest(Verb.GET, resourceURL);
			httpRequest.addQuerystringParameter("q", searchParameters);
			httpRequest.addQuerystringParameter("count", "100");
			service.signRequest(accessToken, httpRequest);
			Response response = httpRequest.send();
			System.out.println(response.getBody());

			JSONObject jsonobject = new JSONObject(response.getBody());
			JSONArray tweetArray = jsonobject.getJSONArray("statuses");
			System.out.println("length: " + tweetArray.length());

			for (int i = 0; i < tweetArray.length(); i++) {
				JSONObject tweet = tweetArray.getJSONObject(i);

				JSONObject userObject = tweet.getJSONObject("user");
				String user_id_str = userObject.getString("id_str");
				String user_screen_name = userObject.getString("screen_name");

				if (activeUser.containsKey(user_id_str)) {
					Integer num = activeUser.get(user_id_str);
					activeUser.put(user_id_str, num + 1);
				} else {
					activeUser.put(user_id_str, 1);
				}

				if (tweet.get("place") != org.json.JSONObject.NULL) {

					JSONObject place = (JSONObject) tweet.get("place");
					String placeName = place.getString("name");
					JSONObject bounding_box = (JSONObject) place
							.get("bounding_box");
					JSONArray coordinateArray = (JSONArray) bounding_box
							.get("coordinates");
					JSONArray coordArray = (JSONArray) coordinateArray.get(0);

					double x = 0.0, y = 0.0;
					JSONArray coordinates;
					for (int j = 0; j < 4; j++) {
						coordinates = (JSONArray) coordArray.get(j);
						x = x + coordinates.getDouble(0);
						y = y + coordinates.getDouble(1);
					}
					LocationBean mapBean = new LocationBean();
					mapBean.setX(x / 4);
					mapBean.setY(y / 4);
					mapBean.setDescription(user_screen_name + " at "
							+ placeName);
					mapList.add(mapBean);
				}
				request.setAttribute("locationList", mapList);	
			}
			//////////////////////Flickr///////////////////////////
			URLConnection uc = null;
		//	List<String> urlList = new ArrayList<String>();
			uc = new URL(
					"https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=f3e75ee9d97069d826d1225ef5190730&per_page=20&user_id=131367443@N02")
					.openConnection();
			//String nextline;
			String[] servers = new String[100];
			String[] ids = new String[100];
			String[] secrets = new String[100];

			XMLInputFactory factory = XMLInputFactory.newInstance();
			ArrayList<FlickrLocationBean> flickr = new ArrayList<FlickrLocationBean>();

			// FlickrBean[] flickr = new FlickrBean[30];
			XMLEventReader r = factory
					.createXMLEventReader(uc.getInputStream());
			//System.out.println(uc.getURL());
			int i = -1;
			while (r.hasNext()) {

				XMLEvent event = r.nextEvent();
				if (event.isStartElement()) {
					StartElement element = (StartElement) event;
					String elementName = element.getName().toString();
					if (elementName.equals("photo")) {
						i++;
						Iterator iterator = element.getAttributes();

						while (iterator.hasNext()) {

							Attribute attribute = (Attribute) iterator.next();
							QName name = attribute.getName();
							String value = attribute.getValue();
							if ((name.toString()).equals("server")) {
								servers[i] = value;

							}
							if ((name.toString()).equals("id")) {
								ids[i] = value;
							}
							if ((name.toString()).equals("secret")) {
								secrets[i] = value;
							}
						}
						// Printing url
						String flickrurl = "http://static.flickr.com/"
								+ servers[i] + "/" + ids[i] + "_" + secrets[i]
								+ ".jpg";
						String uri = flickrurl;
						FlickrLocationBean temp = new FlickrLocationBean();
						//System.out.println(ids[i]);
						String xy[] = getLocation(ids[i]).split(":");
						
						temp.setX(Double.parseDouble(xy[0]));
						temp.setY(Double.parseDouble(xy[1]));
						temp.setUrl(uri);

						flickr.add(temp);
						//System.out.println("---------------------------------------------------------------");
						//System.out.println("X="+temp.getX());
						//System.out.println("Y="+temp.getY());
						//System.out.println("URL="+temp.getUrl());

					}
					request.setAttribute("flickrLocationList", flickr); // End of Printing
				}
			}

			return "search-nearby.jsp";
		} catch (Exception e) {

			return "search-nearby.jsp";
		}

	}
}
