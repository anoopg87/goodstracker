package com.brighterbrain.goodstracker;

import android.os.Parcel;
import android.os.Parcelable;

public class Asset  implements Parcelable{
	
	private   String _ID="";
	private   String Name="";
	private   String Description="";
	private   String ImageUrl="";
	private   String Location="";
	private   String Cost="";

	public Asset() {

	}

	protected Asset(Parcel in) {
		_ID = in.readString();
		Name = in.readString();
		Description = in.readString();
		ImageUrl = in.readString();
		Location = in.readString();
		Cost = in.readString();
	}

	public static final Creator<Asset> CREATOR = new Creator<Asset>() {
		@Override
		public Asset createFromParcel(Parcel in) {
			return new Asset(in);
		}

		@Override
		public Asset[] newArray(int size) {
			return new Asset[size];
		}
	};

	public String get_ID() {
		return _ID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getImageUrl() {
		return ImageUrl;
	}
	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}
	public String getCost() {
		return Cost;
	}
	public void setCost(String cost) {
		Cost = cost;
	}
	public void set_ID(String _ID) {
		this._ID = _ID;
	}



	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(_ID);
		dest.writeString(Name);
		dest.writeString(Description);
		dest.writeString(ImageUrl);
		dest.writeString(Location);
		dest.writeString(Cost);
	}
}
