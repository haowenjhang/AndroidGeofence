# Android_Geofence
 Geofence library with Unity

# Usage 
1. Add UnityPlayerActivity.java
2. Add Unity classes.jar to /libs and as a Library
3. Add permission & receiver in AndroidManfest

    ```
    <receiver
      android:name="com.wen.geofencelib.GeofenceBroadcastReceiver"
      android:enabled="true"
      android:exported="true"
      android:allowBackup="true">
    </receiver>
    
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
    ```
4.  Modify the latitude in MainActivity `AddPos()`  

Finally build .aar for Unity !
