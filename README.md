BlueRover Java API
==================

Summary: Java wrapper for connecting to the blueRover API.

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

      * `Result<Event[] pResult` : Result object of the previous page of events

    **Returns:** `Result<Event[]>` object containing an array of events
