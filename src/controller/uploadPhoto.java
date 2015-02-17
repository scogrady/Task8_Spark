package controller;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
// import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.people.PeopleInterface;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
// import com.flickr4java.flickr.photos.Size;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.Photosets;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
// import com.flickr4java.flickr.util.IOUtilities;
import com.flickr4java.flickr.prefs.PrefsInterface;
// import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.Uploader;
import com.flickr4java.flickr.util.AuthStore;
import com.flickr4java.flickr.util.FileAuthStore;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.xml.sax.SAXException;



// import java.io.BufferedInputStream;
import java.io.File;
import java.io.FilenameFilter;
// import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
// import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
// import java.util.Map;
import java.util.Scanner;
// import java.io.ByteArrayOutputStream;
//import java.io.File;
// import java.io.FileInputStream;
// import java.io.IOException;
// import java.io.InputStream;
import java.util.Set;

public class uploadPhoto {


    private String nsid;

    private String username;


    private Flickr flickr;

    private AuthStore authStore;

    public boolean flickrDebug = false;

    private boolean setOrigFilenameTag = true;

    private boolean replaceSpaces = false;

    private int privacy = -1;

    HashMap<String, Photoset> allSetsMap = new HashMap<String, Photoset>();

    HashMap<String, ArrayList<String>> setNameToId = new HashMap<String, ArrayList<String>>();

   
    public  uploadPhoto(String apiKey, String nsid, String sharedSecret, File authsDir, String username) throws FlickrException {
       
        System.out.println(authsDir);
        System.out.println(nsid);
        System.out.println(username);
        System.out.println(apiKey);
        System.out.println(sharedSecret);
    	flickr = new Flickr(apiKey, sharedSecret, new REST());

        this.username = username;
        this.nsid = nsid;

        if (authsDir != null) {
            this.authStore = new FileAuthStore(authsDir);
        }

        // If one of them is not filled in, find and populate it.
        if (username == null || username.equals(""))
            setUserName();
        if (nsid == null || nsid.equals(""))
            setNsid();

    }

    private void setUserName() throws FlickrException {
        if (nsid != null && !nsid.equals("")) {
            Auth auth = null;
            if (authStore != null) {
                auth = authStore.retrieve(nsid);
                if (auth != null) {
                    username = auth.getUser().getUsername();
                }
            }
            // For this to work: REST.java or PeopleInterface needs to change to pass apiKey
            // as the parameter to the call which is not authenticated.

            if (auth == null) {
                // Get nsid using flickr.people.findByUsername
                PeopleInterface peopleInterf = flickr.getPeopleInterface();
                User u = peopleInterf.getInfo(nsid);
                if (u != null) {
                    username = u.getUsername();
                }
            }
        }
    }

    /**
     * Check local saved copy first ??. If Auth by username is available, then we will not need to make the API call.
     * 
     * @throws FlickrException
     */

    private void setNsid() throws FlickrException {

        if (username != null && !username.equals("")) {
            Auth auth = null;
            if (authStore != null) {
                auth = authStore.retrieve(username); // assuming FileAuthStore is enhanced else need to
                // keep in user-level files.

                if (auth != null) {
                    nsid = auth.getUser().getId();
                }
            }
            if (auth != null)
                return;

            Auth[] allAuths = authStore.retrieveAll();
            for (int i = 0; i < allAuths.length; i++) {
                if (username.equals(allAuths[i].getUser().getUsername())) {
                    nsid = allAuths[i].getUser().getId();
                    return;
                }
            }

            // For this to work: REST.java or PeopleInterface needs to change to pass apiKey
            // as the parameter to the call which is not authenticated.

            // Get nsid using flickr.people.findByUsername
            PeopleInterface peopleInterf = flickr.getPeopleInterface();
            User u = peopleInterf.findByUsername(username);
            if (u != null) {
                nsid = u.getId();
            }
        }
    }

    private void authorize() throws IOException, SAXException, FlickrException {
        AuthInterface authInterface = flickr.getAuthInterface();
        Token accessToken = authInterface.getRequestToken();

        // Try with DELETE permission. At least need write permission for upload and add-to-set.
        String url = authInterface.getAuthorizationUrl(accessToken, Permission.WRITE);
        System.out.println("Follow this URL to authorise yourself on Flickr");
        System.out.println(url);
        System.out.println("Paste in the token it gives you:");
        System.out.print(">>");

        Scanner scanner = new Scanner(System.in);
        String tokenKey = scanner.nextLine();

        Token requestToken = authInterface.getAccessToken(accessToken, new Verifier(tokenKey));

        Auth auth = authInterface.checkToken(requestToken);
        RequestContext.getRequestContext().setAuth(auth);
        this.authStore.store(auth);
        scanner.close();
        System.out.println("Thanks.  You probably will not have to do this every time. Auth saved for user: " + auth.getUser().getUsername() + " nsid is: "
                + auth.getUser().getId());
        System.out.println(" AuthToken: " + auth.getToken() + " tokenSecret: " + auth.getTokenSecret());
    }

    /**
     * If the Authtoken was already created in a separate program but not saved to file.
     * 
     * @param authToken
     * @param tokenSecret
     * @param username
     * @return
     * @throws IOException
     */
    private Auth constructAuth(String authToken, String tokenSecret, String username) throws IOException {

        Auth auth = new Auth();
        auth.setToken(authToken);
        auth.setTokenSecret(tokenSecret);

        // Prompt to ask what permission is needed: read, update or delete.
        auth.setPermission(Permission.fromString("delete"));

        User user = new User();
        // Later change the following 3. Either ask user to pass on command line or read
        // from saved file.
        user.setId(nsid);
        user.setUsername((username));
        user.setRealName("");
        auth.setUser(user);
        this.authStore.store(auth);
        return auth;
    }

    public void setAuth(String authToken, String username, String tokenSecret) throws IOException, SAXException, FlickrException {
        RequestContext rc = RequestContext.getRequestContext();
        Auth auth = null;

        if (authToken != null && !authToken.equals("") && tokenSecret != null && !tokenSecret.equals("")) {
            auth = constructAuth(authToken, tokenSecret, username);
            rc.setAuth(auth);
        } else {
            if (this.authStore != null) {
                auth = this.authStore.retrieve(this.nsid);
                if (auth == null) {
                    this.authorize();
                } else {
                    rc.setAuth(auth);
                }
            }
        }
    }

   
    private String makeSafeFilename(String input) {
        byte[] fname = input.getBytes();
        byte[] bad = new byte[] { '\\', '/', '"', '*' };
        byte replace = '_';
        for (int i = 0; i < fname.length; i++) {
            for (byte element : bad) {
                if (fname[i] == element) {
                    fname[i] = replace;
                }
            }
            if (replaceSpaces && fname[i] == ' ')
                fname[i] = '_';
        }
        return new String(fname);
    }

    public String uploadfile(String filename, String inpTitle) throws Exception {
        String photoId;

        RequestContext rc = RequestContext.getRequestContext();

        if (this.authStore != null) {
            Auth auth = this.authStore.retrieve(this.nsid);
            if (auth == null) {
                this.authorize();
            } else {
                rc.setAuth(auth);
            }
        }

        if (privacy == -1)
           // getPrivacy();
        	privacy=1;
        UploadMetaData metaData = new UploadMetaData();

        if (privacy == 1)
            metaData.setPublicFlag(true);
        if (privacy == 2 || privacy == 4)
            metaData.setFriendFlag(true);
        if (privacy == 3 || privacy == 4)
            metaData.setFamilyFlag(true);

        if (basefilename == null || basefilename.equals(""))
            basefilename = filename; // "image.jpg";

        String title = basefilename;
        boolean setMimeType = true; // change during testing. Doesn't seem to be supported at this time in flickr.
        if (setMimeType) {
            if (basefilename.lastIndexOf('.') > 0) {
                title = basefilename.substring(0, basefilename.lastIndexOf('.'));
                String suffix = basefilename.substring(basefilename.lastIndexOf('.') + 1);
              
                if (suffix.equalsIgnoreCase("png")) {
                    metaData.setFilemimetype("image/png");
                } else if (suffix.equalsIgnoreCase("mpg") || suffix.equalsIgnoreCase("mpeg")) {
                    metaData.setFilemimetype("video/mpeg");
                } else if (suffix.equalsIgnoreCase("mov")) {
                    metaData.setFilemimetype("video/quicktime");
                }
            }
        }
        System.out.println(" File : " + filename);
        System.out.println(" basefilename : " + basefilename);

        if (inpTitle != null && !inpTitle.equals("")) {
            title = inpTitle;
            System.out.println(" title : " + inpTitle);
            metaData.setTitle(title);
        } // flickr defaults the title field from file name.

        // UploadMeta is using String not Tag class.
        if (setOrigFilenameTag) {
            List<String> tags = new ArrayList<String>();
            String tmp = basefilename;
            basefilename = makeSafeFilename(basefilename);
            tags.add("OrigFileName='" + basefilename + "'");
            metaData.setTags(tags);

            
        }

        Uploader uploader = flickr.getUploader();

        // ByteArrayOutputStream out = null;
        try {
            
            metaData.setFilename(basefilename);
            // check correct handling of escaped value

            File f = new File(filename);
            photoId = uploader.upload(f, metaData);

           System.out.println(" File : " + filename + " uploaded: photoId = " + photoId);
        } finally {

        }

        return (photoId);
    }

    public void getPhotosetsInfo() {

        PhotosetsInterface pi = flickr.getPhotosetsInterface();
        try {
            int setsPage = 1;
            while (true) {
                Photosets photosets = pi.getList(nsid, 500, setsPage, null);
                Collection<Photoset> setsColl = photosets.getPhotosets();
                Iterator<Photoset> setsIter = setsColl.iterator();
                while (setsIter.hasNext()) {
                    Photoset set = setsIter.next();
                    allSetsMap.put(set.getId(), set);

                    // 2 or more sets can in theory have the same name. !!!
                    ArrayList<String> setIdarr = setNameToId.get(set.getTitle());
                    if (setIdarr == null) {
                        setIdarr = new ArrayList<String>();
                        setIdarr.add(new String(set.getId()));
                        setNameToId.put(set.getTitle(), setIdarr);
                    } else {
                        setIdarr.add(new String(set.getId()));
                    }
                }

                if (setsColl.size() < 500) {
                    break;
                }
                setsPage++;
            }
           System.out.println(" Sets retrieved: " + allSetsMap.size());
            // all_sets_retrieved = true;
            // Print dups if any.

            Set<String> keys = setNameToId.keySet();
            Iterator<String> iter = keys.iterator();
            while (iter.hasNext()) {
                String name = iter.next();
                ArrayList<String> setIdarr = setNameToId.get(name);
                if (setIdarr != null && setIdarr.size() > 1) {
                    System.out.println("There is more than 1 set with this name : " + setNameToId.get(name));
                    for (int j = 0; j < setIdarr.size(); j++) {
                        System.out.println("           id: " + setIdarr.get(j));
                    }
                }
            }

        } catch (FlickrException e) {
            e.printStackTrace();
        }
    }

    private String setid = null;

    private String basefilename = null;

    private final PhotoList<Photo> photos = new PhotoList<Photo>();

    private final HashMap<String, Photo> filePhotos = new HashMap<String, Photo>();

    public boolean isSetorigfilenametag() {
        return setOrigFilenameTag;
    }

   
    public void setSetorigfilenametag(boolean setOrigFilenameTag) {
        this.setOrigFilenameTag = setOrigFilenameTag;
    }

    public static void main(String[] args) throws Exception {

        String apiKey ="f3e75ee9d97069d826d1225ef5190730";
        String sharedSecret ="6dad87878538613e";

        ArrayList<String> uploadfileArgs = new ArrayList<String>();
        ArrayList<String> optionArgs = new ArrayList<String>();

        

        String authsDirStr = System.getProperty("user.home") + File.separatorChar + ".flickrAuth";

        String nsid = null;
        String username = null;
        String accessToken = null; // Optional entry.
        String tokenSecret = null; // Optional entry.
        String setName = null;

        boolean settagname = true; 

        int i = 0;
       
         System.out.println("enter username");
         Scanner in = new Scanner(System.in);
         username = in.nextLine();
         
         System.out.println("enter file tag ");
         setName=in.nextLine();
         
         System.out.println("enter location");
         uploadfileArgs.add(in.nextLine());

        uploadPhoto bf= new uploadPhoto(apiKey, nsid, sharedSecret, new File(authsDirStr), username);
        for (i = 0; i < optionArgs.size(); i++) {
            bf.addOption(optionArgs.get(i));
        }
        bf.setSetorigfilenametag(settagname);
        bf.setAuth(accessToken, username, tokenSecret);
        bf.getPhotosetsInfo();

        if (setName != null && !setName.equals("")) {

            bf.getSetPhotos(setName);
        }

   

        for (i = 0; i < uploadfileArgs.size(); i++) {
            String filename = uploadfileArgs.get(i);

            File f = new File(filename);
            if (f.isDirectory()) {
                String[] filelist = f.list(new UploadFilenameFilter());
               System.out.println("Processing directory  : " + uploadfileArgs.get(i));
                for (int j = 0; j < filelist.length; j++) {
                    bf.processFileArg(uploadfileArgs.get(i) + File.separatorChar + filelist[j], setName);
                }
            } else {
                bf.processFileArg(filename, setName);
            }
        }
    }

    private static final String[] photoSuffixes = { "jpg", "jpeg", "png", "gif", "bmp", "tif", "tiff" };


    static class UploadFilenameFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            if (isValidSuffix(name))
                return true;
            else
                return false;
        }

    }

    private static boolean isValidSuffix(String basefilename) {
        if (basefilename.lastIndexOf('.') <= 0) {
            return false;
        }
        String suffix = basefilename.substring(basefilename.lastIndexOf('.') + 1).toLowerCase();
        for (int i = 0; i < photoSuffixes.length; i++) {
            if (photoSuffixes[i].equals(suffix))
                return true;
        }
        
       System.out.println(basefilename + " does not have a valid suffix, skipped.");
        return false;
    }

    private void processFileArg(String filename, String setName) throws Exception {
        String photoid;
        
        if (filename.lastIndexOf(File.separatorChar) > 0)
            basefilename = filename.substring(filename.lastIndexOf(File.separatorChar) + 1, filename.length());
        else
            basefilename = filename;

            File f = new File(filename);
            if (!f.exists() || !f.canRead()) {
                System.out.println(" File: " + filename + " cannot be processed, does not exist or is unreadable.");
                return;
            }
           System.out.println("Calling uploadfile for filename : " + filename);
           System.out.println("Upload of " + filename + " started \n");

            photoid = uploadfile(filename, null);
            // Add to Set. Create set if it does not exist.
            if (photoid != null) {
                addPhotoToSet(photoid, setName);
            }
            System.out.println("Uploaded " + filename + " finished  \n");

        } 
    

    private void addOption(String opt) {

        switch (opt) {
        case "replaceSpaces":
            replaceSpaces = true;
            break;

        case "notags":
            setSetorigfilenametag(false);
            break;

        default: // Not supported at this time.
            System.out.println("Option: " + opt + " is not supported at this time");
        }
    }

    

    private void getSetPhotos(String setName) throws FlickrException {
        // Check if this is an existing set. If it is get all the photo list to avoid reloading already
        // loaded photos.
        ArrayList<String> setIdarr;
        setIdarr = setNameToId.get(setName);
        if (setIdarr != null) {
            setid = setIdarr.get(0);
            PhotosetsInterface pi = flickr.getPhotosetsInterface();

            Set<String> extras = new HashSet<String>();
           

            extras.add("date_upload");
            extras.add("original_format");
            extras.add("media");
            // extras.add("url_o");
            extras.add("tags");

            int setPage = 1;
            while (true) {
                PhotoList<Photo> tmpSet = pi.getPhotos(setid, extras, Flickr.PRIVACY_LEVEL_NO_FILTER, 500, setPage);

                int tmpSetSize = tmpSet.size();
                photos.addAll(tmpSet);
                if (tmpSetSize < 500) {
                    break;
                }
                setPage++;
            }
            for (int i = 0; i < photos.size(); i++) {
                filePhotos.put(photos.get(i).getTitle(), photos.get(i));
            }
            if (flickrDebug) {
               System.out.println("Set title: " + setName + "  id:  " + setid + " found");
               System.out.println("   Photos in Set already loaded: " + photos.size());
            }
        }
    }

    public void addPhotoToSet(String photoid, String setName) throws Exception {

        ArrayList<String> setIdarr;

        // all_set_maps.

        PhotosetsInterface psetsInterface = flickr.getPhotosetsInterface();

        Photoset set = null;

        if (setid == null) {
            // In case it is a new photo-set.
            setIdarr = setNameToId.get(setName);
            if (setIdarr == null) {
                // setIdarr should be null since we checked it getSetPhotos.
                // Create the new set.
                // set the setid .

                String description = "";
                set = psetsInterface.create(setName, description, photoid);
                setid = set.getId();

                setIdarr = new ArrayList<String>();
                setIdarr.add(new String(setid));
                setNameToId.put(setName, setIdarr);

                allSetsMap.put(set.getId(), set);
            }
        } else {
            set = allSetsMap.get(setid);
            psetsInterface.addPhoto(setid, photoid);
        }
        // Add to photos .

        // Add Photo to existing set.
        PhotosInterface photoInt = flickr.getPhotosInterface();
        Photo p = photoInt.getPhoto(photoid);
        if (p != null) {
            photos.add(p);
            String title;
            if (basefilename.lastIndexOf('.') > 0)
                title = basefilename.substring(0, basefilename.lastIndexOf('.'));
            else
                title = p.getTitle();
            filePhotos.put(title, p);
        }
    }
}