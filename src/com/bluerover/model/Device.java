package com.bluerover.model;

import java.lang.reflect.Field;

public class Device {
	char isSpeedActive;
	int totalMaxConnPerMin;
	int vehicleID;
	String notes;
	char deviceAlertActive;
	int supportsDMTP;
	float staticLongitude;
	float lastValidLongitude;
	int unitLimitInterval;
	String lastAckCommand;
	String notify; //?
	int uniqueID;
	String featureSet;
	int duplexMaxConnPerMin;
	float speedThreshold;
	String smsEmail; //?
	long simPhoneNumber;
	int speedWindow;
	String duplexProfileMask;
	String pendingPingCommand;
	float staticLatitude;
	String imeiNumber;
	int maxAllowedEvents;
	String ipAddressCurrent;
	String totalProfileMask;
	String deviceType;
	char isActive;
	char isLandmarkActive;
	long lastOdometerKM;
	int listenPortCurrent;
	long odometerOffsetKM;
	String accountID;
	int timeInterval;
	char isStatic;
	char expectAck;
	String description;
	int codeVersion;
	String driverID;
	int remotePortCurrent;
	double lastUpdateTime;
	double lastConnectionTime;
	String assetTypeCode;
	String pushpinID;
	double lastPingTime;
	long totalPingCount;
	double lastDuplexConnectTime;
	String dcsConfigMask;
	String doh;
	double lastAckTime;
	String displayName;
	String supportedEncodings;
	int ipAddressValid;
	double lastGPSTimestamp;
	int totalMaxConn;
	String serialNumber;
	double creationTime;
	int equipmentType;
	String ignitionIndex;
	String deviceID;
	String deviceCode;
	int maxPingCount;
	int duplexMaxConn;
	String groupID;
	int lastInputState;
	float lastValidLatitude;
	public char getIsSpeedActive() {
		return isSpeedActive;
	}
	public int getTotalMaxConnPerMin() {
		return totalMaxConnPerMin;
	}
	public int getVehicleID() {
		return vehicleID;
	}
	public String getNotes() {
		return notes;
	}
	public char getDeviceAlertActive() {
		return deviceAlertActive;
	}
	public int getSupportsDMTP() {
		return supportsDMTP;
	}
	public float getStaticLongitude() {
		return staticLongitude;
	}
	public float getLastValidLongitude() {
		return lastValidLongitude;
	}
	public int getUnitLimitInterval() {
		return unitLimitInterval;
	}
	public String getLastAckCommand() {
		return lastAckCommand;
	}
	public String getNotify() {
		return notify;
	}
	public int getUniqueID() {
		return uniqueID;
	}
	public String getFeatureSet() {
		return featureSet;
	}
	public int getDuplexMaxConnPerMin() {
		return duplexMaxConnPerMin;
	}
	public float getSpeedThreshold() {
		return speedThreshold;
	}
	public String getSmsEmail() {
		return smsEmail;
	}
	public long getSimPhoneNumber() {
		return simPhoneNumber;
	}
	public int getSpeedWindow() {
		return speedWindow;
	}
	public String getDuplexProfileMask() {
		return duplexProfileMask;
	}
	public String getPendingPingCommand() {
		return pendingPingCommand;
	}
	public float getStaticLatitude() {
		return staticLatitude;
	}
	public String getImeiNumber() {
		return imeiNumber;
	}
	public int getMaxAllowedEvents() {
		return maxAllowedEvents;
	}
	public String getIpAddressCurrent() {
		return ipAddressCurrent;
	}
	public String getTotalProfileMask() {
		return totalProfileMask;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public char getIsActive() {
		return isActive;
	}
	public char getIsLandmarkActive() {
		return isLandmarkActive;
	}
	public long getLastOdometerKM() {
		return lastOdometerKM;
	}
	public int getListenPortCurrent() {
		return listenPortCurrent;
	}
	public long getOdometerOffsetKM() {
		return odometerOffsetKM;
	}
	public String getAccountID() {
		return accountID;
	}
	public int getTimeInterval() {
		return timeInterval;
	}
	public char getIsStatic() {
		return isStatic;
	}
	public char getExpectAck() {
		return expectAck;
	}
	public String getDescription() {
		return description;
	}
	public int getCodeVersion() {
		return codeVersion;
	}
	public String getDriverID() {
		return driverID;
	}
	public int getRemotePortCurrent() {
		return remotePortCurrent;
	}
	public double getLastUpdateTime() {
		return lastUpdateTime;
	}
	public double getLastConnectionTime() {
		return lastConnectionTime;
	}
	public String getAssetTypeCode() {
		return assetTypeCode;
	}
	public String getPushpinID() {
		return pushpinID;
	}
	public double getLastPingTime() {
		return lastPingTime;
	}
	public long getTotalPingCount() {
		return totalPingCount;
	}
	public double getLastDuplexConnectTime() {
		return lastDuplexConnectTime;
	}
	public String getDcsConfigMask() {
		return dcsConfigMask;
	}
	public String getDoh() {
		return doh;
	}
	public double getLastAckTime() {
		return lastAckTime;
	}
	public String getDisplayName() {
		return displayName;
	}
	public String getSupportedEncodings() {
		return supportedEncodings;
	}
	public int getIpAddressValid() {
		return ipAddressValid;
	}
	public double getLastGPSTimestamp() {
		return lastGPSTimestamp;
	}
	public int getTotalMaxConn() {
		return totalMaxConn;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public double getCreationTime() {
		return creationTime;
	}
	public int getEquipmentType() {
		return equipmentType;
	}
	public String getIgnitionIndex() {
		return ignitionIndex;
	}
	public String getDeviceID() {
		return deviceID;
	}
	public String getDeviceCode() {
		return deviceCode;
	}
	public int getMaxPingCount() {
		return maxPingCount;
	}
	public int getDuplexMaxConn() {
		return duplexMaxConn;
	}
	public String getGroupID() {
		return groupID;
	}
	public int getLastInputState() {
		return lastInputState;
	}
	public float getLastValidLatitude() {
		return lastValidLatitude;
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
