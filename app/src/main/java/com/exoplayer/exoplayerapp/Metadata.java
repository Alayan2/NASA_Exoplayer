package com.exoplayer.exoplayerapp;

import com.google.gson.annotations.SerializedName;

public class Metadata{

	@SerializedName("total_hits")
	private int totalHits;

	public void setTotalHits(int totalHits){
		this.totalHits = totalHits;
	}

	public int getTotalHits(){
		return totalHits;
	}
}