package org.hacksmu.pickmeup.postmates;

public class PostmateDelivery
{
	public String kind;
	public String id;
	public String created;
	public String updated;
	public String status;
	public boolean complete;
	public String pickup_eta;
	public String dropoff_eta;
	public String pickup_ready;
	public String pickup_deadline;
	public String dropoff_ready;
	public String dropoff_deadline;
	public String quote_id;
	public int fee;
	public String currency;
	public DeliveryManifest manifest;
	public DeliveryManifestItem[] manifest_items;
	public String dropoff_identifier;
	public Pickup pickup;
	public Dropoff dropoff;
	public Courier courier;
	public String tracking_url;

	public static class DeliveryManifest
	{
		public String reference;
		public String description;
	}

	public static class DeliveryManifestItem
	{
		public String name;
		public int quantity;
		public String size;
	}

	public static class Pickup
	{
		public String name;
		public String phone_number;
		public String address;
		public DetailedAddress detailed_address;
		public String notes;
		public Location location;
	}

	public static class DetailedAddress
	{
		public String street_address_1;
		public String street_address_2;
		public String city;
		public String state;
		public String zip_code;
		public String country;
	}

	public static class Location
	{
		public float lat;
		public float lng;
	}

	public static class Dropoff
	{
		public String name;
		public String phone_number;
		public String address;
		public DetailedAddress detailed_address;
		public String notes;
		public Location location;
	}

	public static class Courier
	{
		public String name;
		public String rating;
		public String vehicle_type;
		public String phone_number;
		public Location location;
		public String img_href;
	}
}