import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Polygons {

	private String type;
	private String label;
	private String endLevel;
	private String cityIndex;
	private Set<ArrayList<Location>> polySet = new HashSet<>();
	private Color buildingColor = new Color(80, 80, 80);
	private Color carparks = new Color(120, 120, 120);
	private Color schools = new Color(140, 140, 140);
	private Color DoC = new Color(180, 180, 180);
	private Color golfClubHouse = new Color(180, 180, 180);
	private Color airportTerminalColor = Color.CYAN;
	private Color airstrip = Color.magenta;
	private Color landmark = Color.red;
	private Color forest = Color.green;
	private Color park = Color.green;
	private Color hospital = Color.darkGray;
	private Color golfCourse = new Color(2, 70, 2);
	private Color water = new Color(2, 10, 200);

	public Polygons(String type, String endLevel, String cityIndex, String label) {
		this.type = type;
		this.endLevel = endLevel;
		this.cityIndex = cityIndex;
		this.label = label;
	}

	// Used
	// https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html to
	// get the REGEX pattern used
	public void addData(String s) {
		String[] polyLatLon = s.substring(1).split("\\),\\(|\\)|,");
		ArrayList<Location> coordinates = new ArrayList<Location>();
		for (int i = 0; i < polyLatLon.length - 1; i += 2) {
			coordinates.add(
					Location.newFromLatLon(Double.parseDouble(polyLatLon[i]), Double.parseDouble(polyLatLon[i + 1])));
		}
		this.polySet.add(coordinates);
	}

	public void draw(Graphics g, Location l, Double d) {
		// converts hex type to an integer representing the feature of the polygon it represents
		Integer decodedType = Integer.decode(type); 
		switch (decodedType) {
		case 2:
		case 19:
			g.setColor(buildingColor);
			break;
		case 5:
			g.setColor(carparks);
			break;
		case 10:
			g.setColor(schools);
			break;
		case 25:
			g.setColor(DoC);
			break;
		case 64:
			g.setColor(golfClubHouse);
			break;
		case 7:
			g.setColor(airportTerminalColor);
			break;
		case 14:
			g.setColor(airstrip);
			break;
		case 8:
			g.setColor(landmark);
			break;
		case 22:
			g.setColor(forest);
			break;
		case 23:
			g.setColor(park);
			break;
		case 11:
			g.setColor(hospital);
			break;
		case 24:
			g.setColor(golfCourse);
			break;
		case 40:
			g.setColor(water);
			break;
		default:
			//g.setColor(new Color(222, 184, 135));
		}

		for (ArrayList<Location> locations : polySet) {
			int arrayLength = locations.size();
			int[] xPoints = new int[arrayLength];
			int[] yPoints = new int[arrayLength];
			int i = 0;
			for (Location j : locations) {
				Point toAdd = j.asPoint(l, d);
				xPoints[i] = (toAdd.x);
				yPoints[i] = (toAdd.y);
				i++;
			}
			g.fillPolygon(xPoints, yPoints, arrayLength);
		}
	}

}
