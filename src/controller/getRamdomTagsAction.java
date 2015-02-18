package controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import model.Model;

public class getRamdomTagsAction extends Action {
	
	public getRamdomTagsAction(Model model) {

	}

	
	@Override
	public String getName() {
		return "getRamdomTags.do";
	}

	@Override
	public String perform(HttpServletRequest request) {
		URLConnection uc = null;
		System.out.println("In Method");
		try {
			uc = new URL(
					"https://api.flickr.com/services/rest/?method=flickr.tags.getListUser&api_key=f3e75ee9d97069d826d1225ef5190730&user_id=131367443@N02")
					.openConnection();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom=null;
		try {

			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			//parse using builder to get DOM representation of the XML file
		 dom = db.parse(uc.getInputStream());


		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
		
		Element docEle = dom.getDocumentElement();
		Map<Integer, String> randomtags5= new HashMap<Integer,String>();
		
		//get a nodelist of 
		
		NodeList nl = docEle.getElementsByTagName("tag");
		String description=null;
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				Element el = (Element)nl.item(i);
				String temp = el.getTextContent();
				randomtags5.put(i, temp);
				
			}
		}
		
		Random rn = new Random();
		List<String> randomtags= new ArrayList<String>();
		for (int j=0; j<5;j++)
		{
			int answer = rn.nextInt(randomtags5.size()) + 1;
			randomtags.add(randomtags5.get(answer));
			System.out.println(randomtags5.get(answer));
		}
		
		
		
		return null;
	
		
		
	}

}
