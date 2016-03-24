
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Node {

	private int ID;
	private Location l;
	private Set<Segment> inSegments;
	private Set<Segment> outSegments;
	private int onClickSize;
	private Color c;
	private boolean isSelected;
	
	public Node(int ID, Location l) {
		this.ID = ID;
		this.l = l;
		inSegments = new HashSet<Segment>();
		outSegments = new HashSet<Segment>();
		c = Color.red;
	}
	
	public void onClickHighlight(){
		onClickSize=5;
		c = Color.blue;
		isSelected = true;
	}
	
	public void onClickUnhighlight(){
		onClickSize=0;
		c = Color.red;
		isSelected = false;
	}
	
	public void addInSegment(Segment s){
		inSegments.add(s);
	}
	
	public void addOutSegment(Segment s){
		outSegments.add(s);
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
	public Location getLocation() {
		return l;
	}
	
	public Set<Segment> getinSegments() {
		return inSegments;
	}
	
	public Set<Segment> getoutSegments() {
		return outSegments;
	}

	public void draw(Graphics g, Location origin, int scale) {		
		g.setColor(c);
		Point p = l.asPoint(origin, scale);
		g.fillOval(p.x-((scale/15)+onClickSize)/2, p.y-((scale/15)+onClickSize)/2, (scale/15)+onClickSize, (scale/15)+onClickSize);
	}
}
