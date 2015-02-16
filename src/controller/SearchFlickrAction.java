package controller;


import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
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

import model.Model;

public class SearchFlickrAction extends Action {
	
	 String urlList[]=new String[30];

	public SearchFlickrAction(Model model) {

	}

	@Override
	public String getName() {
		return "searchFlickr.do";
	}

	@Override
	public String perform(HttpServletRequest request) {
		System.out.println("searchFlickr action called");
		
		
		URLConnection uc;
		try {
			uc = new URL(
					"https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=f3e75ee9d97069d826d1225ef5190730&per_page=30&text=diving")
					.openConnection();
		
		DataInputStream dis = new DataInputStream(uc.getInputStream());
		FileWriter fw = new FileWriter(new File("Hello1.xml"));
		String nextline;
		String[] servers = new String[30];
		String[] ids = new String[30];
		String[] secrets = new String[30];
		while ((nextline = dis.readLine()) != null) {
			fw.append(nextline);
		}
		dis.close();
		fw.close();
		
		String filename = "Hello1.xml";
		XMLInputFactory factory = XMLInputFactory.newInstance();
		System.out.println("FACTORY: " + factory);
try{
		XMLEventReader r = factory.createXMLEventReader(filename,
				new FileInputStream(filename));
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
						//Printing url
						String flickrurl = "http://static.flickr.com/" + servers[i] + "/"
								+ ids[i] + "_" + secrets[i] + ".jpg";
							String uri = flickrurl;
							System.out.println(uri);
							urlList[i]=uri;
				}
						//End of Printing
						
				}
			}
		}catch (XMLStreamException e) {
			
			e.printStackTrace();
		}
		
		request.setAttribute("urlList", urlList);
		
		
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} 
		return "showFlickr.jsp";
		
	}

}
