package databeans;

public class PopularTweetBean {
	private int retweet_count;
	private int favorite_count;
	private String text;
	public int getRetweet_count() {
		return retweet_count;
	}
	public int getFavorite_count() {
		return favorite_count;
	}
	public String getText() {
		return text;
	}
	public void setRetweet_count(int retweet_count) {
		this.retweet_count = retweet_count;
	}
	public void setFavorite_count(int favorite_count) {
		this.favorite_count = favorite_count;
	}
	public void setText(String text) {
		this.text = text;
	}
}
