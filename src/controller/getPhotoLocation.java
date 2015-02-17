package controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import databeans.FlickrBean;
import databeans.FlickrLocationBean;
import formbeans.flickrsearchform;
import model.Model;

public class getPhotoLocation extends Action {

	public getPhotoLocation(Model model) {
	}

	// //////////////

	public String getLocation(String ids) throws MalformedURLException,
			IOException {
		URLConnection uc = new URL(
				"https://api.flickr.com/services/rest/?method=flickr.photos.geo.getLocation&api_key=f3e75ee9d97069d826d1225ef5190730&photo_id="
						+ ids).openConnection();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom = null;
		try {

			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			dom = db.parse(uc.getInputStream());

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("location ");
		String title = null;
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				// get the employee element
				Element el = (Element) nl.item(i);
				// get the Employee object
				String tempX = el.getAttribute("latitude");
				String tempY = el.getAttribute("longitude");
				title = tempX + ":" + tempY;
			}
		}
		return title;
	}

	@Override
	public String getName() {

		return "getPhotoLocation.do";
	}

	@Override
	public String perform(HttpServletRequest request) {
		URLConnection uc = null;
		List<String> urlList = new ArrayList<String>();

		try {
			try {
				uc = new URL(
						"https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=f3e75ee9d97069d826d1225ef5190730&user_id=131367443@N02&")
						.openConnection();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String nextline;
			String[] servers = new String[30];
			String[] ids = new String[30];
			String[] secrets = new String[30];

			XMLInputFactory factory = XMLInputFactory.newInstance();
			ArrayList<FlickrLocationBean> flickr = new ArrayList<FlickrLocationBean>();

			// FlickrBean[] flickr = new FlickrBean[30];
			XMLEventReader r = factory
					.createXMLEventReader(uc.getInputStream());
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

						String xy[] = getLocation(ids[i]).split(":");

						temp.setX(Double.parseDouble(xy[0]));
						temp.setY(Double.parseDouble(xy[1]));
						temp.setUrl(uri);

						flickr.add(temp);

					}
					request.setAttribute("flickr", flickr); // End of Printing

				}
			}
		} catch (XMLStreamException e) {

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return null;
	}

}
