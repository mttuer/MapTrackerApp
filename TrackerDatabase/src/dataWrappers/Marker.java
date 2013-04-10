package dataWrappers;

public class Marker {
	public long markerID;
	public long routeID;
	public String pictureLink;
	public String videoLink;
	public String audioLink;
	public String text;
	public GPS gps;
	public long timeStamp;
	
	public boolean hasPic(){
		return pictureLink!=null;
	}
	public boolean hasVid(){
		return videoLink!=null;
	}
	public boolean hasAudio(){
		return audioLink!=null;
	}
	public boolean hasText(){
		return text != null;
	}
	
	
}
