package interfaces;
import java.util.List;
import dataWrappers.*;
/**
 * TrackerDB
 *
 */
public interface TrackerDB {
	//GPS
	public boolean addGPSData(List<GPS> gps, long routeID);
	public List<GPS> getGPSData(long routeID);
	public boolean deleteGPS(int gpsID);
	public boolean deleteRouteGPS(long routeID);

	//Route
	public long addNewRoute(List<GPS> gps, List<Marker> markers, Long timeStart, Long timeEnd, String notes, String routeName, String location);
	public boolean editRoute(long routeID, List<GPS> gps, List<Marker> markers, Long timeStart,Long timeEnd, String notes, String routeName, String location);
	public boolean deleteRoute(long routeID);
	public List<Route> getAllRoutes();
	public Route getRoute(long routeID);

	//Markers
	public boolean addMarkers(List<Marker> markers, long routeID);
	public boolean addMarkerPicture(int markerID, String mediaLink);
	public boolean addMarkerText(int markerID, String text);
	public boolean addMarkerVideo(int markerID, String mediaLink);
	public boolean addMarkerAudio(int markerID, String mediaLink);
	public List<Marker> getMarkers(long routeID);
	public Marker getMarker(int markerID);
	public boolean deleteMarker(int markerID);
	public boolean deleteMarkers(long routeID);
	
}
