
package formbeans;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class FlickrLoginForm extends FormBean{
    private String oauth_token;
    private String oauth_verifier;
	
    public String getOauth_token()  { return oauth_token; }
    public String getOauth_verifier()  { return oauth_verifier; }
	
    public void setOauth_token(String s)  { oauth_token = s.trim(); }
    public void setOauth_verifier(String s)  { oauth_verifier = s.trim(); }

    @Override
	public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<String>();

 
        if (errors.size() > 0) return errors;
		
        return errors;
    }
}
