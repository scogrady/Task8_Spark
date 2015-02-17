package model;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.genericdao.ConnectionPool;

public class Model {

	public Model(ServletConfig config) throws ServletException {
		String jdbcDriver = config.getInitParameter("jdbcDriverName");
		String jdbcURL    = config.getInitParameter("jdbcURL");
		String user = "team2";
		String password = "123zhxxhz321";
		ConnectionPool pool = new ConnectionPool(jdbcDriver, jdbcURL, user, password);
	}


}
