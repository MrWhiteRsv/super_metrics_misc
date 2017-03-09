package service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.wahoofitness.connector.HardwareConnector;
import com.wahoofitness.connector.HardwareConnectorEnums;
import com.wahoofitness.connector.HardwareConnectorTypes;
import com.wahoofitness.connector.capabilities.Capability;
import com.wahoofitness.connector.conn.connections.SensorConnection;
import com.wahoofitness.connector.conn.connections.params.ConnectionParams;
import com.wahoofitness.connector.listeners.discovery.DiscoveryListener;
import com.wahoofitness.connector.HardwareConnector;
import com.wahoofitness.connector.HardwareConnectorEnums.HardwareConnectorState;
import com.wahoofitness.connector.HardwareConnectorEnums.SensorConnectionError;
import com.wahoofitness.connector.HardwareConnectorEnums.SensorConnectionState;
import com.wahoofitness.connector.HardwareConnectorTypes.NetworkType;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.conn.connections.SensorConnection;
import com.wahoofitness.connector.conn.connections.params.ConnectionParams;

public class HardwareConnectionService extends IntentService {

  /** Listener class allowing clients to be notified when API events occur */
  public interface Listener {

    void onDeviceDiscovered(ConnectionParams params);

    void onDiscoveredDeviceLost(ConnectionParams params);

    void onDiscoveredDeviceRssiChanged(ConnectionParams params);

    void onNewCapabilityDetected(SensorConnection sensorConnection,
        Capability.CapabilityType capabilityType);

    void onSensorConnectionStateChanged(SensorConnection sensorConnection,
        HardwareConnectorEnums.SensorConnectionState state);

    void onConnectorStateChanged(HardwareConnectorTypes.NetworkType networkType,
        HardwareConnectorEnums.HardwareConnectorState hardwareState);
  }



  private static final String TAG = HardwareConnectionService.class.getName();

  private HardwareConnector mHardwareConnector = null;
  private final HardwareConnector.Listener  mHardwareConnectorListener =
      new HardwareConnector.Listener ()  {
    @Override
    public void onHardwareConnectorStateChanged(HardwareConnectorTypes.NetworkType networkType,
        HardwareConnectorEnums.HardwareConnectorState hardwareConnectorState) {
    }

    @Override
    public void onFirmwareUpdateRequired(SensorConnection sensorConnection, String s, String s1) {
    }
  };

  /** Listens for discovery events from the API */
  private final DiscoveryListener mDiscoveryListener = new DiscoveryListener() {

    @Override
    public void onDeviceDiscovered(ConnectionParams params) {
      Log.i(TAG, "onDeviceDiscovered");
      Log.i(TAG, "Device name: " + params.getName());
      Log.i(TAG, "Rssi: " + params.getRssi());
      Log.i(TAG, "toString: " + params.toString());

      if (params.getName().equals("BAROMETER")) { // ACCELEROMETER
        Log.i(TAG, "BAROMETER");
        enableDiscovery(false);
        connectSensor(params);
      }
      // Notify our listeners
      /*for (Listener listener : mListeners) {
        listener.onDeviceDiscovered(params);
      }*/
    }

    @Override
    public void onDiscoveredDeviceLost(ConnectionParams params) {
      Log.i(TAG, "onDiscoveredDeviceLost");
      // Notify our listeners
      /*for (Listener listener : mListeners) {
        listener.onDiscoveredDeviceLost(params);
      }*/
    }

    @Override
    public void onDiscoveredDeviceRssiChanged(ConnectionParams params, int rssi) {
      Log.i(TAG, "onDiscoveredDeviceRssiChanged");
      // Notify our listeners
      /*for (Listener listener : mListeners) {
        listener.onDiscoveredDeviceRssiChanged(params);
      }*/
    }
  };

  /**
   * Listens for events from the API's {@link SensorConnection}. We use the same listener for all
   * {@link SensorConnection}s
   */
  private final SensorConnection.Listener mSensorConnectionListener = new SensorConnection.Listener() {

    @Override
    public void onNewCapabilityDetected(SensorConnection sensorConnection, Capability.CapabilityType capabilityType) {

      // Notify our listeners
      Log.i(TAG, "onNewCapabilityDetected");
      /*for (Listener listener : mListeners) {
        listener.onNewCapabilityDetected(sensorConnection, capabilityType);
      }*/
    }

    @Override
    public void onSensorConnectionError(SensorConnection sensorConnection, SensorConnectionError error) {
      Log.i(TAG, "onSensorConnectionError");
    }

    @Override
    public void onSensorConnectionStateChanged(SensorConnection sensorConnection, SensorConnectionState state) {
      Log.i(TAG, "onSensorConnectionError");
      /*
      // Notify our listeners
      for (Listener listener : mListeners) {
        listener.onSensorConnectionStateChanged(sensorConnection, state);
      }
      */
    }
  };

  public HardwareConnectionService() {
    super("HardwareConnectionService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Log.v(TAG, "onHandleIntent bla bla");
  }

  @Override
  public void onCreate() {
    super.onCreate();
    mHardwareConnector = new HardwareConnector(this, mHardwareConnectorListener);
    enableDiscovery(true);
  }

  @Override
  public void onDestroy () {
    enableDiscovery(false);
    mHardwareConnector.shutdown();

    super.onDestroy();
  }

  /**
   * Starts/stops device discovery. Results can be obtained via a registered {@link Listener}
   * @param enable true to enable discovery/false to disable
   * @return true if ok, false if called in invalid state
   */
  public boolean enableDiscovery(boolean enable) {
    if (mHardwareConnector != null) {
      if (enable) {
        mHardwareConnector.startDiscovery(mDiscoveryListener);
      } else {
        mHardwareConnector.stopDiscovery();
      }
      return true;
    } else {
      return false;
    }
  }
  /**
   * Requests a {@link SensorConnection} for the specified {@link ConnectionParams}. Returns null
   * if called in inappropratie state.
   * @see HardwareConnector#requestSensorConnection(ConnectionParams, SensorConnection.Listener)
   */
  public SensorConnection connectSensor(ConnectionParams params) {
    if (mHardwareConnector != null) {
      return mHardwareConnector.requestSensorConnection(params, mSensorConnectionListener);
    } else {
      return null;
    }
  }

  /**
   * Requests the disconnection of the {@link SensorConnection} with the specified
   * {@link ConnectionParams}
   * @param params the {@link ConnectionParams}
   */
  public void disconnectSensor(ConnectionParams params) {
    if (mHardwareConnector != null) {
      SensorConnection sensorConnection = mHardwareConnector.getSensorConnection(params);
      if (sensorConnection != null) {
        sensorConnection.disconnect();
      }
    }
  }

}
