import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Segment {

	private int roadID;
	private double length;
	private Node nodeID1;
	private Node nodeID2;
	private double[] d;
	private Color c = Color.black;

	public Segment(int roadID, double length, Node nodeID1, Node nodeID2, double[] d) {
		if (nodeID1 == null)
			System.out.println("danger");
		if (nodeID1 == null)
			System.out.println("danger2");
		setRoadID(roadID);
		setD(d);
		setLength(length);
		setNodeID1(nodeID1);
		setNodeID2(nodeID2);
	}
	
	public Color getColor(){
		return c;
	}
	
	public int getRoadID() {
		return roadID;
	}

	public void setRoadID(int roadID) {
		this.roadID = roadID;
	}

	public Node getnodeID1() {
		return nodeID1;
	}

	public void setNodeID1(Node nodeID1) {
		this.nodeID1 = nodeID1;
	}

	public Node getnodeID2() {
		return nodeID2;
	}

	public void setNodeID2(Node nodeID2) {
		this.nodeID2 = nodeID2;
	}

	public double[] getD() {
		return d;
	}

	public void setD(double[] d) {
		this.d = d;
	}

	public void add(double[] newD){
		double[] both = new double[d.length+newD.length];
		//System.out.print(newD);
		int count = 0;
		for (int i = 0; i<both.length; i++){
		if(i<=d.length-1){
			both[i] = d[i];
		} else {
        	both[i] = newD[count];	
        	count++;
		}
		}
		d = both;
	}

	public void draw(Graphics g, Location origin, int scale) {
		g.setColor(c);
		Point start = Location.newFromLatLon(d[0], d[1]).asPoint(origin, scale);
		Point mid = null;
		Point end = Location.newFromLatLon(d[d.length-2], d[d.length-1]).asPoint(origin, scale);
		if (d.length > 1) {
			segmentDrawingLoop: for (int i = 2; i < d.length - 1; i += 2) {
				if (d[i] == 0.0)
					continue segmentDrawingLoop;
				mid = Location.newFromLatLon(d[i], d[i + 1]).asPoint(origin, scale);
				g.drawLine(start.x, start.y, mid.x, mid.y);
				start = mid;
			}
			g.drawLine(mid.x, mid.y, end.x, end.y);
		} else {
			g.drawLine(start.x, start.y, end.x, end.y);
		}
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}
	
	public void unhighlight(){
		c = Color.black;
	}
	
	public void highlight(){
		c = new Color(255, 255, 255);
	}
	
	

}
