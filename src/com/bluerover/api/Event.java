package com.bluerover.api;

import java.lang.reflect.Field;

public class Event {
	private String deviceId;
	private int statusCode;
	private long timestamp;
	private int rfidCustNum;
	private int rfidTagNum;
	private int rfidAlarmFlags;
	private float rfidTemperature;
	private float longitude;
	private float latitude;
	private float speedKPH;
	private float zone1Avr;
	private float zone2Avr;
	private float zone3Avr;
	private float zone4Avr;
	private float zone5Avr;
	private float zone6Avr;
	private float zone7Avr;
	private String rawData;

	public String getDeviceId() {
		return deviceId;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public int getRfidCustNum() {
		return rfidCustNum;
	}

	public int getRfidTagNum() {
		return rfidTagNum;
	}

	public int getRfidAlarmFlags() {
		return rfidAlarmFlags;
	}

	public float getRfidTemperature() {
		return rfidTemperature;
	}

	public float getLongitude() {
		return longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public float getSpeedKPH() {
		return speedKPH;
	}

	public float getZone1Avr() {
		return zone1Avr;
	}

	public float getZone2Avr() {
		return zone2Avr;
	}

	public float getZone3Avr() {
		return zone3Avr;
	}

	public float getZone4Avr() {
		return zone4Avr;
	}

	public float getZone5Avr() {
		return zone5Avr;
	}

	public float getZone6Avr() {
		return zone6Avr;
	}

	public float getZone7Avr() {
		return zone7Avr;
	}

	public String getRawData() {
		return rawData;
	}
	
	@Override
	public String toString() {
		String text = "";
		for (Field f : getClass().getDeclaredFields()) {
		    try {
				text += f.getName() + "=" + f.get(this) + ",";
			} catch (Exception ignore) {}
		}
		return text;
	}
}
