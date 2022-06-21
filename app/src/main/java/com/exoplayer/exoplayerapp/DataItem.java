package com.exoplayer.exoplayerapp;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("keywords")
	private List<String> keywords;

	@SerializedName("media_type")
	private String mediaType;

	@SerializedName("date_created")
	private String dateCreated;

	@SerializedName("center")
	private String center;

	@SerializedName("description")
	private String description;

	@SerializedName("title")
	private String title;

	@SerializedName("nasa_id")
	private String nasaId;

	@SerializedName("album")
	private List<String> album;

	public void setKeywords(List<String> keywords){
		this.keywords = keywords;
	}

	public List<String> getKeywords(){
		return keywords;
	}

	public void setMediaType(String mediaType){
		this.mediaType = mediaType;
	}

	public String getMediaType(){
		return mediaType;
	}

	public void setDateCreated(String dateCreated){
		this.dateCreated = dateCreated;
	}

	public String getDateCreated(){
		return dateCreated;
	}

	public void setCenter(String center){
		this.center = center;
	}

	public String getCenter(){
		return center;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setNasaId(String nasaId){
		this.nasaId = nasaId;
	}

	public String getNasaId(){
		return nasaId;
	}

	public void setAlbum(List<String> album){
		this.album = album;
	}

	public List<String> getAlbum(){
		return album;
	}
}