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
	public long addNewRoute(List<GPS> gps, List<DBMarker> markers, Long timeStart, Long timeEnd, String notes, String routeName, String location);
	public boolean editRoute(long routeID, List<GPS> gps, List<DBMarker> markers, Long timeStart,Long timeEnd, String notes, String routeName, String location);
	public boolean deleteRoute(long routeID);
	public List<DBRoute> getAllRoutes();
	public DBRoute getRoute(long routeID);

	//Markers
	public boolean addMarkers(List<DBMarker> markers, long routeID);
	public boolean addMarkerPicture(int markerID, String mediaLink);
	public boolean addMarkerText(int markerID, String text);
	public boolean addMarkerVideo(int markerID, String mediaLink);
	public boolean addMarkerAudio(int markerID, String mediaLink);
	public List<DBMarker> getMarkers(long routeID);
	public DBMarker getMarker(int markerID);
	public boolean deleteMarker(int markerID);
	public boolean deleteMarkers(long routeID);
	
}
