package service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.wahoofitness.connector.HardwareConnector;
import com.wahoofitness.connector.HardwareConnectorEnums;
import com.wahoofitness.connector.HardwareConnectorTypes;
import com.wahoofitness.connector.conn.connections.SensorConnection;

public class HardwareConnectionService extends IntentService {
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

  public HardwareConnectionService() {
    super("HardwareConnectionService");
    Log.v(TAG, "constructor bla bla");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Log.v(TAG, "onHandleIntent bla bla");
  }

  @Override
  public void onCreate() {
    super.onCreate();
    Log.v(TAG, "onCreate bla bla");
    mHardwareConnector = new HardwareConnector(this, mHardwareConnectorListener);
  }

  @Override
  public void onDestroy () {
    super.onDestroy ();
    mHardwareConnector.shutdown ();
  }
}
