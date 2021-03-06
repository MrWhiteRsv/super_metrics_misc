package com.wahoofitness.samples.hardwareconnectorservice.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.wahoofitness.common.log.Logger;
import com.wahoofitness.connector.HardwareConnectorEnums.SensorConnectionState;
import com.wahoofitness.connector.capabilities.Capability;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.Heartrate;
import com.wahoofitness.connector.capabilities.Heartrate.Data;
import com.wahoofitness.connector.conn.connections.SensorConnection;
import com.wahoofitness.connector.conn.connections.params.ConnectionParams;
import com.wahoofitness.connector.conn.connections.params.ConnectionParams.RssiMeasurement;
import com.wahoofitness.connector.util.threading.Poller;
import com.wahoofitness.samples.hardwareconnectorservice.R;
import com.wahoofitness.samples.hardwareconnectorservice.service.HardwareConnectorService;

public class DiscoverFragment extends HardwareConnectorFragment {

	private final Poller mPoller = new Poller(1000) {

		@Override
		protected void onPoll() {
			refreshView();
		}
	};

	private class DiscoveryListAdapter extends ArrayAdapter<ConnectionParams> {

		/** All known devices (connected, saved and discovered) reported in discovery */
		protected List<ConnectionParams> mDiscoveryConnectionParams = new ArrayList<ConnectionParams>();

		public DiscoveryListAdapter(Context context) {
			super(context, 0);
		}

		@Override
		public int getCount() {
			return mDiscoveryConnectionParams.size();
		}

		@Override
		public ConnectionParams getItem(int position) {
			return mDiscoveryConnectionParams.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ConnectionParams params = getItem(position);

			View view = convertView;
			if (view == null) {
				view = LayoutInflater.from(getContext()).inflate(R.layout.discover_item,
						null);
			}

			View background = view.findViewById(R.id.ddi_background);
			TextView name = (TextView) view.findViewById(R.id.ddi_name);
			TextView state = (TextView) view.findViewById(R.id.ddi_connectedstate);
			Switch connect = (Switch) view.findViewById(R.id.ddi_connect);
			Switch save = (Switch) view.findViewById(R.id.ddi_save);
			View rssi_layout = view.findViewById(R.id.ddi_rssi_layout);
			ProgressBar rssi_bar = (ProgressBar) view.findViewById(R.id.ddi_rssi_bar);
			TextView rssi_txt = (TextView) view.findViewById(R.id.ddi_rssi_txt);
			ImageButton about = (ImageButton) view.findViewById(R.id.ddi_about);
			TextView summary = (TextView) view.findViewById(R.id.ddi_summary);

			connect.setOnCheckedChangeListener(null);

			background.setBackgroundColor(Color.LTGRAY);

			name.setText(params.getName());

			final SensorConnection sensorConnection = getSensorConnection(params);

			if (sensorConnection != null) {

				switch (sensorConnection.getConnectionState()) {
				case CONNECTED:
					state.setText("Connected");
					connect.setChecked(true);
					background.setBackgroundColor(Color.GREEN);
					break;
				case CONNECTING:
					state.setText("Connecting");
					connect.setChecked(true);
					background.setBackgroundColor(Color.YELLOW);
					break;
				case DISCONNECTED:
					state.setText("Disconnected");
					connect.setChecked(false);
					break;
				case DISCONNECTING:
					state.setText("Disconnecting");
					connect.setChecked(false);
					background.setBackgroundColor(Color.YELLOW);
					break;
				default:
					state.setText("ERROR");
					break;

				}
			} else {

				if (getDiscoveredConnectionParams().contains(params)) {
					state.setText("Discovered");
					connect.setChecked(false);
				} else {
					state.setText("Saved");
					connect.setChecked(false);
				}
			}

			connect.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						connectSensor(params);
					} else {
						disconnectSensor(params);
					}
				}
			});

			if (sensorConnection != null) {
				about.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						DeviceDetailsActivity.launchActivity(getActivity(), sensorConnection);

					}
				});
				about.setEnabled(true);
			} else {
				about.setOnClickListener(null);
				about.setEnabled(false);
			}

			save.setOnCheckedChangeListener(null);
			save.setChecked(mSavedConnectionParams.contains(params));
			save.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						mSavedConnectionParams.add(params);
					} else {
						mSavedConnectionParams.remove(params);
					}
					saveConnectionParams();
					refreshView();
				}
			});

			RssiMeasurement rssiMeasurement = params.getRssiMeasurement();
			if (rssiMeasurement.getRssi() == ConnectionParams.RSSI_UNKNOWN) {
				// No RSSI
				rssi_layout.setVisibility(View.GONE);
			} else {
				// Show RSSI as a "progress bar" percentage
				int rawRssiValue = rssiMeasurement.getRssi();
				int rssiPercentage = getRssiPercent(rawRssiValue); // RSSI is negative, 0 is max

				rssi_layout.setVisibility(View.VISIBLE);
				rssi_bar.setProgress(rssiPercentage);
				rssi_txt.setText(rawRssiValue + "dBm");
			}

			StringBuilder sum = new StringBuilder();
			if (sensorConnection != null && sensorConnection.isConnected()) {
				for (CapabilityType ct : CapabilityType.values()) {
					Capability cap = sensorConnection.getCurrentCapability(ct);
					if (cap != null) {
						switch (ct) {
						case Heartrate:
							Data hrData = ((Heartrate) cap).getHeartrateData();
							if (hrData != null)
								sum.append("HR:")
										.append((int) hrData.getHeartrate().asEventsPerMinute())
										.append(" ");
							break;
						default:
							break;
						}
					}
				}
			}
			summary.setText(sum.toString().trim());

			return view;
		}

		private static final int RSSI_CLOSEST = -30;
		private static final int RSSI_FURTHEST = -100;
		private static final int RSSI_RANGE = RSSI_CLOSEST - RSSI_FURTHEST;

		/**
		 * // Convert -100dBm to -30dBm into 0 to 100%
		 */
		private int getRssiPercent(int rssi) {
			if (rssi <= RSSI_FURTHEST) {
				return 0; // 0%
			} else if (rssi >= RSSI_CLOSEST) {
				return 100; // 100%
			} else {
				return (100 * (rssi - RSSI_FURTHEST) / RSSI_RANGE); // percent
			}
		}

		void refresh() {

			mDiscoveryConnectionParams.clear();

			// CONNECTED DEVICES
			List<ConnectionParams> connectedConnectionParams = new ArrayList<ConnectionParams>();

			for (SensorConnection connectedDevices : getSensorConnections()) {
				ConnectionParams connectedParams = connectedDevices.getConnectionParams();
				if (!mDiscoveryConnectionParams.contains(connectedParams)) {
					connectedConnectionParams.add(connectedParams);
				}
			}

			mDiscoveryConnectionParams.addAll(connectedConnectionParams);

			// SAVED DEVICES
			List<ConnectionParams> savedConnectionParams = new ArrayList<ConnectionParams>();

			for (ConnectionParams connectedParams : mSavedConnectionParams) {
				if (!mDiscoveryConnectionParams.contains(connectedParams)) {
					savedConnectionParams.add(connectedParams);
				}
			}

			mDiscoveryConnectionParams.addAll(savedConnectionParams);

			// DISCOVERED DEVICES
			List<ConnectionParams> discoveredConnectionParams = new ArrayList<ConnectionParams>();

			for (ConnectionParams discoveredParams : getDiscoveredConnectionParams()) {
				if (!mDiscoveryConnectionParams.contains(discoveredParams)) {
					discoveredConnectionParams.add(discoveredParams);
				}
			}

			mDiscoveryConnectionParams.addAll(discoveredConnectionParams);

			this.notifyDataSetChanged();
		}

	}

	private static final Logger L = new Logger("DiscoverFragment");

	private ProgressBar mDiscoveringProgress;
	private Switch mDiscoverSwitch;
	private DiscoveryListAdapter mDiscoveryListAdapter;
	private final Set<ConnectionParams> mSavedConnectionParams = new HashSet<ConnectionParams>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mSavedConnectionParams.clear();
		SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
				"PersistentConnectionParams", 0);
		for (Object entry : sharedPreferences.getAll().values()) {
			ConnectionParams params = ConnectionParams.fromString((String) entry);
			mSavedConnectionParams.add(params);
		}
		L.i("onCreate", mSavedConnectionParams.size(), "saved ConnectionParams loaded");
	}

	@Override
	public View
			onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.discover_fragment, null);

		mDiscoverSwitch = (Switch) view.findViewById(R.id.df_discover);
		mDiscoveringProgress = (ProgressBar) view.findViewById(R.id.df_discovering);

		mDiscoverSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				enableDiscovery(isChecked);

				refreshView();
			}
		});

		mDiscoveryListAdapter = new DiscoveryListAdapter(getActivity());
		ListView list = (ListView) view.findViewById(R.id.df_list);
		list.setAdapter(mDiscoveryListAdapter);

		return view;
	}

	@Override
	public void onDeviceDiscovered(ConnectionParams params) {
		refreshView();
	}

	@Override
	public void onDiscoveredDeviceRssiChanged(ConnectionParams params) {
	}

	@Override
	public void onDiscoveredDeviceLost(ConnectionParams params) {
		refreshView();
	}

	@Override
	public void onHardwareConnectorServiceConnected(
			HardwareConnectorService service) {
		refreshView();

	}

	@Override
	public void onSensorConnectionStateChanged(SensorConnection sensorConnection,
			SensorConnectionState state) {
		refreshView();
	}

	public void saveConnectionParams() {

		SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
				"PersistentConnectionParams", 0);
		Editor editor = sharedPreferences.edit();
		editor.clear();
		int count = 0;
		for (ConnectionParams params : mSavedConnectionParams) {
			editor.putString("" + count++, params.serialize());
		}
		editor.commit();
		L.i("saveConnectionParams", mSavedConnectionParams.size(), "ConnectionParams saved");
	}

	private void refreshView() {
		boolean discovering = isDiscovering();
		mDiscoverSwitch.setChecked(discovering);
		mDiscoveringProgress.setVisibility(discovering ? View.VISIBLE : View.INVISIBLE);

		mDiscoveryListAdapter.refresh();

	}

	@Override
	public void onPause() {
		super.onPause();
		mPoller.stop();
	}

	@Override
	public void onResume() {
		super.onResume();
		mPoller.start();
	}

}
