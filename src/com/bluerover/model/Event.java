package com.bluerover.model;

import java.lang.reflect.Field;

public class Event {
	private String deviceId;
	private long autoIndex;
	private int statusCode;
	private long timestamp;
	private long creationTime;
	private int geozoneIndex;
	private String geozoneID;
	private String transportID;
	private int inputMask;
	private int seqNum;
	private String dataSource;
	private String charDeviceType;
	private String charDeviceResponse;
	private float heading;
	private String address;
	private int rfidCustNum;
	private int rfidTagNum;
	private int rfidAlarmFlags;
	private int rfidBatteryStatus;
	private int rfidRssi;
	private int rfidReaderId;
	private int rfidReaderType;
	private float rfidTemperature;
	private float longitude;
	private float latitude;
	private float altitude;
	private float speedKPH;
	private float odometerKM;
	private float distanceKM;
	private float zone1Avr;
	private float zone2Avr;
	private float zone3Avr;
	private float zone4Avr;
	private float zone5Avr;
	private float zone6Avr;
	private float zone7Avr;
	private float zone8Avr;
	private float zone1Low;
	private float zone2Low;
	private float zone3Low;
	private float zone4Low;
	private float zone5Low;
	private float zone6Low;
	private float zone7Low;
	private float zone8Low;
	private float zone1High;
	private float zone2High;
	private float zone3High;
	private float zone4High;
	private float zone5High;
	private float zone6High;
	private float zone7High;
	private float zone8High;
	private String rawData;

	public String getDeviceId() {
		return deviceId;
	}

	public long getAutoIndex() {
		return autoIndex;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public int getGeozoneIndex() {
		return geozoneIndex;
	}

	public String getGeozoneID() {
		return geozoneID;
	}

	public String getTransportID() {
		return transportID;
	}

	public int getInputMask() {
		return inputMask;
	}

	public int getSeqNum() {
		return seqNum;
	}

	public String getDataSource() {
		return dataSource;
	}

	public String getCharDeviceType() {
		return charDeviceType;
	}

	public String getCharDeviceResponse() {
		return charDeviceResponse;
	}

	public float getHeading() {
		return heading;
	}

	public String getAddress() {
		return address;
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

	public int getRfidBatteryStatus() {
		return rfidBatteryStatus;
	}

	public int getRfidRssi() {
		return rfidRssi;
	}

	public int getRfidReaderId() {
		return rfidReaderId;
	}

	public int getRfidReaderType() {
		return rfidReaderType;
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

	public float getAltitude() {
		return altitude;
	}

	public float getSpeedKPH() {
		return speedKPH;
	}

	public float getOdometerKM() {
		return odometerKM;
	}

	public float getDistanceKM() {
		return distanceKM;
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

	public float getZone8Avr() {
		return zone8Avr;
	}

	public float getZone1Low() {
		return zone1Low;
	}

	public float getZone2Low() {
		return zone2Low;
	}

	public float getZone3Low() {
		return zone3Low;
	}

	public float getZone4Low() {
		return zone4Low;
	}

	public float getZone5Low() {
		return zone5Low;
	}

	public float getZone6Low() {
		return zone6Low;
	}

	public float getZone7Low() {
		return zone7Low;
	}

	public float getZone8Low() {
		return zone8Low;
	}

	public float getZone1High() {
		return zone1High;
	}

	public float getZone2High() {
		return zone2High;
	}

	public float getZone3High() {
		return zone3High;
	}

	public float getZone4High() {
		return zone4High;
	}

	public float getZone5High() {
		return zone5High;
	}

	public float getZone6High() {
		return zone6High;
	}

	public float getZone7High() {
		return zone7High;
	}

	public float getZone8High() {
		return zone8High;
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
			} catch (Exception ignore) {
			}
		}
		return text;
	}
}
