package AjaxRequest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GetFlickrDetail extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String[] getComments(String ids) throws MalformedURLException,
			IOException {
		URLConnection uc = new URL(
				"https://api.flickr.com/services/rest/?method=flickr.photos.comments.getList&api_key=f3e75ee9d97069d826d1225ef5190730&photo_id="
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

		// get a nodelist of

		NodeList nl = docEle.getElementsByTagName("comment");
		String comments[] = new String[nl.getLength()];
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {

				// get the employee element
				Element el = (Element) nl.item(i);

				// get the Employee object
				String comment = el.getTextContent();
				String author = el.getAttribute("authorname");
				comments[i] = author + ":" + comment;

			}
		}
		return comments;
	}

	// /////////////////////////////////
	public String getDescription(String ids) throws MalformedURLException,
			IOException {
		URLConnection uc = new URL(
				"https://api.flickr.com/services/rest/?method=flickr.photos.getInfo&api_key=f3e75ee9d97069d826d1225ef5190730&photo_id="
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

		// get a nodelist of

		NodeList nl = docEle.getElementsByTagName("description");
		String description = null;
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {

				// get the employee element
				Element el = (Element) nl.item(i);

				// get the Employee object
				String temp = el.getTextContent();

				description = temp;
				// add it to list

			}
		}
		return description;
	}

	public String[] getDesc(String ids) throws MalformedURLException,
			IOException {
		URLConnection uc = new URL(
				"https://api.flickr.com/services/rest/?method=flickr.photos.getInfo&api_key=f3e75ee9d97069d826d1225ef5190730&photo_id="
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
		NodeList nl = docEle.getElementsByTagName("title");
		String[] result = new String[2];
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				// get the employee element
				Element el = (Element) nl.item(i);
				// get the Employee object
				String temp = el.getTextContent();
				result[0] = temp;
			}
		}
		
		nl = docEle.getElementsByTagName("description");
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				// get the employee element
				Element el = (Element) nl.item(i);
				// get the Employee object
				String temp = el.getTextContent();
				result[1] = temp;
			}
		}
		return result;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id").substring(8);
		String[] desc = getDesc(id);
		String html = "<div class='modal-dialog'>"
				+ "<div class='modal-content'>"
				+ "<div class='modal-header'>"
				+ "<button type='button' class='close' data-dismiss='modal' aria-label='Close'><span aria-hidden='true'>&times;</span></button>"
				+ "<h4 class='modal-title' id='myModalLabel'>" + desc[0] + "</h4>"
				+ "<h6 class='modal-title' id='myModalLabel'>" + desc[1] + "</h6>"
				+ "</div>"
				+ "<div class='modal-body'>";
		for (String s : getComments(id)) {
			System.out.println(s);
			html += ("<div>" + s + "</div>");
		}
		html +=	("</div>"
				+ "<div class='modal-footer'>"
				+ "<button type='button' class='btn btn-default' data-dismiss='modal'>Close</button>"
				+ "</div>" + "</div>" + "</div>");
		response.setContentType("text/plain"); // Set content type of the
												// response so that jQuery knows
												// what it can expect.
		response.setCharacterEncoding("UTF-8"); // You want world domination,
												// huh?
		response.getWriter().write(html);
	}

}
