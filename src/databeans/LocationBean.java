package databeans;

public class LocationBean {
	private double x;
	private double y;
	private String description;
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public String getDescription() {
		return description;
	}
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}