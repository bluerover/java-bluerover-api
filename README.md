BlueRover Java API
==================

Java wrapper for connecting to the blueRover API.

Dependencies (in /lib)
------------

* gson-2.2.4
* commons-codec-1.9

Public Methods
--------------

* `BlueroverApi` **api.setCredentials**(*creds*)

    Sets the credentials of the Bluerover API object.
      
    **Parameters:**
      
    `HashMap<String,String> creds` : API credentials containing the following:
      
      *   `String key` : authentication key for your user
      *   `String token` : authentication token for your user
      *   `String baseURL` : the address where you are pointing to, normally **developers.bluerover.us**
      
    **Returns:** `BlueroverApi` object

* `void` **api.clearCredentials**()

    **Parameters:** None

    Remove the key and token values from the API object.

* `Result<Event[]>` **api.getEvents**(*startTime*,*endTime*,*String page*)

    API Request to get events from a particular date range.

    **Parameters:**

      * `String startTime` : beginning of the date range
      * `String endTime` : end of the date range
      * `String page` : the particular page of results that you want (starts at page 0)

    > `startTime` and `endTime` are both in UNIX timestamp format

    **Returns:** `Result<Event[]>` object containing an array of events

* `Result<Device[]>` **api.getDevices**()

    API Request to get a list of devices for the user

    **Parameters:** None

    **Returns:** `Result<Device[]>` object containing an array of devices

* `Result<Rfid[]>` **api.getRfids**()

    API Request to get a list of rfids for the user

    **Parameters:** None

    **Returns:** `Result<Rfid[]>` object containing an array of rfids

* `Result<Event[]>` **api.next**(*pResult*)

    API Request to get the next page of events.

    **Parameters:**

      * `Result<Event[]> pResult` : Result object of the previous page of events

    **Returns:** `Result<Event[]>` object containing an array of events
    

Models
------

###Result<T>

  * `JsonObject jsonObject` - a GSON jSON object that encapsulates the API's response
  * `JsonArray jsonArray` - a GSON jSON array that encapsulates the API's response
  * `HttpRequest request` - copy of the request made to the API
  * `HttpResponse response` - copy of the response made to the API
  * `HttpRequest next` - a request object that can be used to get the next page of results (for Event[])
  * `String rawResponse` - the raw data string that came from the API's response
  * `T list` - Generic data, normally consisiting of Event[], Device[], or Rfid[]

> Note: either jsonObject or jsonArray will contain data, the other will be null. This is due to the format returned by the API.

###Event
  *  `String deviceId`
  *  `int statusCode`
  *  `long timestamp`
  *  `int rfidCustNum`
  *  `int rfidTagNum`
  *  `int rfidAlarmFlags`
  *  `float rfidTemperature`
  *  `float longitude`
  *  `float latitude`
  *  `float speedKPH`
  *  `float zone1Avr`
  *  `float zone2Avr`
  *  `float zone3Avr`
  *  `float zone4Avr`
  *  `float zone5Avr`
  *  `float zone6Avr`
  *  `float zone7Avr`
  *  `String rawData`

###Device
  *  `char isSpeedActive`
  *  `int totalMaxConnPerMin`
  *  `int vehicleID`
  *  `String notes`
  *  `char deviceAlertActive`
  *  `int supportsDMTP`
  *  `float staticLongitude`
  *  `float lastValidLongitude`
  *  `int unitLimitInterval`
  *  `String lastAckCommand`
  *  `String notify`
  *  `int uniqueID`
  *  `String featureSet`
  *  `int duplexMaxConnPerMin`
  *  `float speedThreshold`
  *  `String smsEmail`
  *  `long simPhoneNumber`
  *  `int speedWindow`
  *  `String duplexProfileMask`
  *  `String pendingPingCommand`
  *  `float staticLatitude`
  *  `String imeiNumber`
  *  `int maxAllowedEvents`
  *  `String ipAddressCurrent`
  *  `String totalProfileMask`
  *  `String deviceType`
  *  `char isActive`
  *  `char isLandmarkActive`
  *  `long lastOdometerKM`
  *  `int listenPortCurrent`
  *  `long odometerOffsetKM`
  *  `String accountID`
  *  `int timeInterval`
  *  `char isStatic`
  *  `char expectAck`
  *  `String description`
  *  `int codeVersion`
  *  `String driverID`
  *  `int remotePortCurrent`
  *  `double lastUpdateTime`
  *  `double lastConnectionTime`
  *  `String assetTypeCode`
  *  `String pushpinID`
  *  `double lastPingTime`
  *  `long totalPingCount`
  *  `double lastDuplexConnectTime`
  *  `String dcsConfigMask`
  *  `String doh`
  *  `double lastAckTime`
  *  `String displayName`
  *  `String supportedEncodings`
  *  `int ipAddressValid`
  *  `double lastGPSTimestamp`
  *  `int totalMaxConn`
  *  `String serialNumber`
  *  `double creationTime`
  *  `int equipmentType`
  *  `String ignitionIndex`
  *  `String deviceID`
  *  `String deviceCode`
  *  `int maxPingCount`
  *  `int duplexMaxConn`
  *  `String groupID`
  *  `int lastInputState`
  *  `float lastValidLatitude`

###Rfid

Coming soon.
