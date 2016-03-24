import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.io.File;

public class ProgramMain extends GUI {
	private Map<Integer, Node> nodes = new ConcurrentHashMap<Integer, Node>();
	private Map<Integer, Road> roads = new ConcurrentHashMap<Integer, Road>();
	private Map<Integer, Segment> segments = new ConcurrentHashMap<Integer, Segment>();
	private Set<Polygons> Polygons = new HashSet<Polygons>();
	private Set<Segment> highlightedSegments = new HashSet<Segment>();
	private static int Scale = 50;
	private final String nodeFilePath;
	private final String roadFilePath;
	private final String segFilePath;
	private final String nodeFilePathLarge;
	private final String roadFilePathLarge;
	private final String segFilePathLarge;
	private final String polyFilePathLarge;
	private File nodeInfo;
	private File roadInfo;
	private File segInfo;
	private File polyInfo;
	public static Location mapOrigin = (new Location(-8, 2));
	private JFrame frame2;
	private Node selectedNode = new Node(-1, mapOrigin);
	private Road selectedRoad = null;
	private trie roadNameTrie = new trie();
	private boolean firstClick = false;

	public ProgramMain() {
		nodeFilePath = "C:\\Users\\Patrick\\Documents\\UNI\\COMP261\\Assignment 1\\data\\small\\nodeID-lat-lon.tab";
		roadFilePath = "C:\\Users\\Patrick\\Documents\\UNI\\COMP261\\Assignment 1\\data\\small\\roadID-roadInfo.tab";
		segFilePath = "C:\\Users\\Patrick\\Documents\\UNI\\COMP261\\Assignment 1\\data\\small\\roadSeg-roadID-length-nodeID-nodeID-coords.tab";
		nodeFilePathLarge = "C:\\Users\\Patrick\\Documents\\UNI\\COMP261\\Assignment 1\\data\\large\\nodeID-lat-lon.tab";
		roadFilePathLarge = "C:\\Users\\Patrick\\Documents\\UNI\\COMP261\\Assignment 1\\data\\large\\roadID-roadInfo.tab";
		segFilePathLarge = "C:\\Users\\Patrick\\Documents\\UNI\\COMP261\\Assignment 1\\data\\large\\roadSeg-roadID-length-nodeID-nodeID-coords.tab";
		polyFilePathLarge = "C:\\Users\\Patrick\\Documents\\UNI\\COMP261\\Assignment 1\\data\\large\\polygon-shapes.mp";
		int n = popup();
		if (n == 1) {
			nodeInfo = new File(nodeFilePath);
			roadInfo = new File(roadFilePath);
			segInfo = new File(segFilePath);
			polyInfo = null;
		} else if (n == 0) {
			nodeInfo = new File(nodeFilePathLarge);
			roadInfo = new File(roadFilePathLarge);
			segInfo = new File(segFilePathLarge);
			polyInfo = new File(polyFilePathLarge);
		}
		onLoad(nodeInfo, roadInfo, segInfo, polyInfo);
		redraw();
	}

	/**
	 * Pops up a dialog box to allow the user to choose quickly between either
	 * the large or small version of the map
	 */
	public int popup() {
		Object[] options = { "Large", "Small" };
		return JOptionPane.showOptionDialog(frame2, "Would you like to load the large " + "or small map?", "Attention",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
	}

	public void loadNodeID(File data) {
		String line;
		try {
			BufferedReader br = null;
			br = new BufferedReader(new FileReader(data));
			while ((line = br.readLine()) != null) {
				String[] tabValues = line.split("\t");
				Location l = Location.newFromLatLon(Double.parseDouble(tabValues[1]), Double.parseDouble(tabValues[2]));
				Node n = new Node(Integer.parseInt(tabValues[0]), l);
				nodes.put(n.getID(), n);
			}
			br.close();
		} catch (IOException e) {
		}
	}

	public void loadRoadID(File data) {
		String line;
		try {
			BufferedReader br = null;
			br = new BufferedReader(new FileReader(data));
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] tabValues = line.split("\\t");
				Road r = new Road(Integer.parseInt(tabValues[0]), Integer.parseInt(tabValues[1]), tabValues[2],
						tabValues[3], Integer.parseInt(tabValues[4]), Integer.parseInt(tabValues[5]),
						Integer.parseInt(tabValues[6]), Integer.parseInt(tabValues[7]), Integer.parseInt(tabValues[8]),
						Integer.parseInt(tabValues[9]));
				roads.put(r.getRoadID(), r);
				roadNameTrie.add(r);
			}
			br.close();

			/**
			 * TESTER FOR TRIE String test = "n"; List<Road> l = new ArrayList
			 * <Road>(); l.addAll(roadNameTrie.contains(test.split(""),
			 * roadNameTrie.getRoot()));
			 * 
			 * for (Road r:l){ System.out.println(r.getLabel()); }
			 */

		} catch (IOException e) {

		}
	}

	public void loadRoadSeg(File data) {
		String line;
		try {
			BufferedReader br = null;
			br = new BufferedReader(new FileReader(data));
			br.readLine();
			segWhile: while ((line = br.readLine()) != null) {
				String[] tabValues = line.split("\t");
				double[] d = new double[tabValues.length - 4];
				if (tabValues.length > 4) {
					for (int i = 4; i < tabValues.length; i++) {
						d[i - 4] = Double.parseDouble(tabValues[i]);
					}
				}
				if (segments.containsKey(Integer.parseInt(tabValues[0]))) {
					segments.get(Integer.parseInt(tabValues[0])).add(d);
					continue segWhile;
				}
				Segment s = new Segment(Integer.parseInt(tabValues[0]), Double.parseDouble(tabValues[1]),
						nodes.get(Integer.parseInt(tabValues[2])), nodes.get(Integer.parseInt(tabValues[3])), d);
				segments.put(s.getRoadID(), s);
				roads.get(s.getRoadID()).addSegment(s);
				// System.out.println(s.getRoadID());
				s.getnodeID1().addOutSegment(s);
				s.getnodeID2().addInSegment(s);
			}
			br.close();
		} catch (IOException e) {

		}
	}

	public void loadPoly(File data) {
		String line = "";
		try {
			BufferedReader br = null;
			br = new BufferedReader(new FileReader(data));
			String type = "";
			String label = "";
			String endLevel = "";
			String cityIndex = "";
			Polygons p = null;
			while (line != null) {
				if (line.equals("[POLYGON]")) {
					line = br.readLine();
					if (line.startsWith("Type")) {
						type = line.split("=")[1];
						line = br.readLine();
					}
					if (line.startsWith("Label")) {
						label = line.split("=")[1];
						line = br.readLine();
					}
					if (line.startsWith("EndLevel")) {
						endLevel = line.split("=")[1];
						line = br.readLine();
					}
					if (line.startsWith("CityIdx")) {
						cityIndex = line.split("=")[1];
						line = br.readLine();
					}

					p = new Polygons(type, label, endLevel, cityIndex);

					while (!line.equals("[END]")) {
						if (line.startsWith("Data")) {
							p.addData(line.split("=")[1]);
							line = br.readLine();
						}
					}
					line = br.readLine();
					Polygons.add(p);
				}
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {

		}
	}

	@Override
	// Swing timer for double clicking implemented from https://docs.oracle.com/javase/7/docs/api/javax/swing/Timer.html
	protected void onClick(MouseEvent e) {

		if (firstClick) {
			Scale = Scale + 8;
			firstClick = false;
		} 
		else {
			firstClick = true;
			Timer t = new Timer("doubleclickTimer", false);
			
			t.schedule(new TimerTask() {
				@Override
				public void run() {
					firstClick = false;
				}  }, 500);
		}

		selectedNode.onClickUnhighlight();
		Point p = new Point(e.getX(), e.getY());
		Location j = Location.newFromPoint(p, mapOrigin, Scale);
		Node closestNode = new Node(-1, new Location(Double.MAX_VALUE, Double.MAX_VALUE));
		for (int i : nodes.keySet()) {
			Node testNode = nodes.get(i);
			if (isCloser(closestNode, testNode, j)) {
				closestNode = nodes.get(i);
			}
		}
		redraw();
		getTextOutputArea().setText("");
		selectedNode = closestNode;
		selectedNode.onClickHighlight();

		getTextOutputArea().append("" + closestNode.getID() + "\n");
		for (Segment s : closestNode.getinSegments()) {
			getTextOutputArea().append("" + roads.get(s.getRoadID()).getLabel() + "\n");
		}
		for (Segment s : closestNode.getoutSegments()) {
			getTextOutputArea().append("" + roads.get(s.getRoadID()).getLabel() + "\n");
		}
	}

	public boolean isCloser(Node closestNode, Node testNode, Location click) {
		return closestNode.getLocation().distance(click) > testNode.getLocation().distance(click);
	}

	// CORE SEARCH
	/*
	 * @Override protected void onSearch() { String search =
	 * getSearchBox().getText(); if (selectedRoad != null) { for (Road t :
	 * roads.values()) { for (Segment s : t.getJoinedSegments()) {
	 * s.unhighlight(); } } }
	 * 
	 * for (Road r : roads.values()) { if
	 * (search.equalsIgnoreCase(r.getLabel())) { selectedRoad = r; for (Segment
	 * s : r.getJoinedSegments()) { s.highlight(); } } } }
	 */
	@Override
	protected void onSearch() {
		getTextOutputArea().setText("");
		String search = getSearchBox().getText();
		for (Segment g : highlightedSegments) {
			g.unhighlight();
		}
		String s[] = search.split("");
		boolean found = false;
		for (Road l : roads.values()) {
			if (search.equalsIgnoreCase(l.getLabel())) {
				if (found = false)
					getTextOutputArea().append(l.getLabel() + ", " + l.getCity() + "\n");
				found = true;

				for (Segment y : l.getJoinedSegments()) {
					y.highlight();
				}
			}
		}
		if (found) {
			getTextOutputArea().append(search + "\n");
			return;
		}

		for (Road r : roadNameTrie.contains(s, roadNameTrie.getRoot())) {
			getTextOutputArea().append(r.getLabel() + ", " + r.getCity() + "\n");

			for (Segment j : r.getJoinedSegments()) {
				j.highlight();
				highlightedSegments.add(j);
			}
		}
	}

	@Override
	protected void onMove(Move m) {
		if (m == GUI.Move.ZOOM_OUT) {
			Scale = Scale - 4;

			if (Scale < 0)
				Scale = 3;
		}
		if (m == GUI.Move.ZOOM_IN) {
			this.mapOrigin = this.mapOrigin.moveBy(0.1, -0.1);
			Scale = Scale + 4;
		}
		if (m == GUI.Move.NORTH)
			this.mapOrigin = this.mapOrigin.moveBy(0.0, 1.0);
		if (m == GUI.Move.SOUTH)
			this.mapOrigin = this.mapOrigin.moveBy(0.0, -1.0);
		if (m == GUI.Move.WEST)
			this.mapOrigin = this.mapOrigin.moveBy(-1.0, 0.0);
		if (m == GUI.Move.EAST)
			this.mapOrigin = this.mapOrigin.moveBy(1.0, 0.0);
	}

	@Override
	protected void onLoad(File nodes, File roads, File segments, File polygons) {
		loadNodeID(nodes);
		loadRoadID(roads);
		loadRoadSeg(segments);
		if(polygons!=null)
		loadPoly(polygons);
	}

	@Override
	protected void redraw(Graphics g) {
		if(Polygons.size()>1){
		g.setColor(new Color(10, 80, 20));
		g.fillRect(0, 0, 10000, 10000);
		}
		for (Polygons p : Polygons) {
			// Node n = nodes.get(i);
			Double ggg = (double) Scale;
			p.draw(g, mapOrigin, ggg);
		}
		for (Segment s : segments.values()) {
			// Node n = nodes.get(i);
			s.draw(g, mapOrigin, Scale);
		}
		for (Node n : nodes.values()) {
			// Node n = nodes.get(i);
			n.draw(g, mapOrigin, Scale);
		}

	}

	public static void main(String[] args) {
		new ProgramMain();
	}

	public static int getScale() {
		return Scale;
	}

	public void setScale(int scale) {
		Scale = scale;
	}
}
