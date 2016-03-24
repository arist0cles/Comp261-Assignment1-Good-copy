import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Road {

	private int roadID;
	private int type;
	private String label;
	private String city;
	private int oneway;
	private int speed;
	private int roadClass;
	private int notforcar;
	private int notforpede;
	private int notforbicy;
	private Set<Segment> joinedSegments;
	
	public Road(int roadID, int type, String label, String city, int oneway, int speed, int roadClass, int notforcar, int notforpede, int notforbicy) {
		this.setRoadID(roadID);
		this.type = type;
		this.setLabel(label);
		this.setCity(city);
		this.oneway = oneway;
		this.speed = speed;
		this.roadClass = roadClass;
		this.setNotforcar(notforcar);
		this.notforpede = notforpede;
		this.notforbicy = notforbicy;
		joinedSegments = new HashSet<Segment>();
	}
	
	public Set<Segment> getJoinedSegments(){
		return joinedSegments;	
	}
	
	public void addSegment(Segment s){
		joinedSegments.add(s);
	}

	public int getRoadID() {
		return roadID;
	}

	public void setRoadID(int roadID) {
		this.roadID = roadID;
	}

	public int getNotforcar() {
		return notforcar;
	}

	public void setNotforcar(int notforcar) {
		this.notforcar = notforcar;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
