package databeans;

import Twitter.Coordinates;
import Twitter.Entities;

public class TwitterBean {
	private Coordinates coordinates;
	private String createTime;
	private Entities entities;
	private Integer favorite_count;
	private String id_str;
	private String in_reply_to_status_id_str;
	private String in_reply_to_user_id_str;
	private int retweet_count;
	private String text;
	private String user_id_str;

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public String getCreateTime() {
		return createTime;
	}

	public String getText() {
		return text;
	}

	public String getUser_id_str() {
		return user_id_str;
	}

	public Entities getEntities() {
		return entities;
	}

	public Integer getFavorite_count() {
		return favorite_count;
	}

	public String getId_str() {
		return id_str;
	}

	public String getIn_reply_to_status_id_str() {
		return in_reply_to_status_id_str;
	}

	public String getIn_reply_to_user_id_str() {
		return in_reply_to_user_id_str;
	}

	public int getRetweet_count() {
		return retweet_count;
	}

	public void setCoordinates(Double longitude, Double latitude) {
		coordinates = new Coordinates();
		coordinates.setLongitude(longitude);
		coordinates.setLatitude(latitude);
	}

	public void setCreateTime(String s) {
		createTime = s;
	}

	public void setText(String s) {
		text = s;
	}

	public void setUser_id_str(String s) {
		user_id_str = s;
	}

	public void setEntities(String[] s) {
		entities = new Entities();
		entities.setHashTag(s);
	}

	public void setFavorite_count(Integer s) {
		favorite_count = s;
	}

	public void setId_str(String s) {
		id_str = s;
	}

	public void setIn_reply_to_status_id_str(String s) {
		in_reply_to_status_id_str = s;
	}

	public void setIn_reply_to_user_id_str(String s) {
		in_reply_to_user_id_str = s;
	}

	public void setRetweet_count(int s) {
		retweet_count = s;
	}

}
