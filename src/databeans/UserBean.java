package databeans;

public class UserBean {
	private String created_at;
	private int favourites_count;
	private int followers_count;
	private int friends_count;
	private String id_str;
	private String name;
	private String screen_name;
	private int statuses_count;
	private String withheld_in_countries;

	public String getCreated_at() {
		return created_at;
	}

	public int getFavorites_count() {
		return favourites_count;
	}

	public int getFollowers_count() {
		return followers_count;
	}

	public int getFriends_count() {
		return friends_count;
	}

	public String getId_str() {
		return id_str;
	}

	public String getName() {
		return name;
	}

	public String getScreen_name() {
		return screen_name;
	}

	public int getStatuses_count() {
		return statuses_count;
	}

	public String getWithheld_in_countries() {
		return withheld_in_countries;
	}

	public void setCreated_at(String s) {
		created_at = s;
	}

	public void setFavorites_count(int s) {
		favourites_count = s;
	}

	public void setFollowers_count(int s) {
		followers_count = s;
	}

	public void setFriends_count(int s) {
		friends_count = s;
	}

	public void setId_str(String s) {
		id_str = s;
	}

	public void setName(String s) {
		name = s;
	}

	public void setScreen_name(String s) {
		screen_name = s;
	}

	public void setStatuses_count(int s) {
		statuses_count = s;
	}

	public void setWithheld_in_countries(String s) {
		withheld_in_countries = s;
	}

}