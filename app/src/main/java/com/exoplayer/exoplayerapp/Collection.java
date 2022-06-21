package com.exoplayer.exoplayerapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Collection{

	@SerializedName("collection")
	private Collection collection;

	@SerializedName("metadata")
	private Metadata metadata;

	@SerializedName("href")
	private String href;

	@SerializedName("version")
	private String version;

	@SerializedName("items")
	private List<ItemsItem> items;

	public void setCollection(Collection collection){
		this.collection = collection;
	}

	public Collection getCollection(){
		return collection;
	}

	public void setMetadata(Metadata metadata){
		this.metadata = metadata;
	}

	public Metadata getMetadata(){
		return metadata;
	}

	public void setHref(String href){
		this.href = href;
	}

	public String getHref(){
		return href;
	}

	public void setVersion(String version){
		this.version = version;
	}

	public String getVersion(){
		return version;
	}

	public void setItems(List<ItemsItem> items){
		this.items = items;
	}

	public List<ItemsItem> getItems(){
		return items;
	}
}