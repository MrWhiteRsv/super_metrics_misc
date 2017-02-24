package com.io.supermarket.wahoodemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.wahoofitness.connector.HardwareConnector;
import com.wahoofitness.connector.HardwareConnectorEnums;
import com.wahoofitness.connector.HardwareConnectorTypes;
import com.wahoofitness.connector.conn.connections.SensorConnection;

public class MainActivity extends AppCompatActivity {

  private HardwareConnector mHardwareConnector = null;
  private final HardwareConnector.Listener  mHardwareConnectorListener = new HardwareConnector.Listener ()  {

    @Override
    public void onHardwareConnectorStateChanged(HardwareConnectorTypes.NetworkType networkType,
                                                HardwareConnectorEnums.HardwareConnectorState hardwareConnectorState) {
    }

    @Override
    public void onFirmwareUpdateRequired(SensorConnection sensorConnection, String s, String s1) {
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });
    mHardwareConnector = new HardwareConnector(this, mHardwareConnectorListener);
  }

  @Override
  public  void onDestroy () { super . onDestroy ();
    mHardwareConnector . shutdown ();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
