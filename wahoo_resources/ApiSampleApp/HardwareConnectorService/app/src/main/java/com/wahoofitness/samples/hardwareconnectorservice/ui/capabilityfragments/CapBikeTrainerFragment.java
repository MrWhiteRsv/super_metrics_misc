package com.wahoofitness.samples.hardwareconnectorservice.ui.capabilityfragments;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wahoofitness.connector.capabilities.BikeTrainer;
import com.wahoofitness.connector.capabilities.BikeTrainer.AccelerometerInfo;
import com.wahoofitness.connector.capabilities.BikeTrainer.BikeTrainerMode;
import com.wahoofitness.connector.capabilities.BikeTrainer.CalibrationInfo;
import com.wahoofitness.connector.capabilities.BikeTrainer.DeviceInfo;
import com.wahoofitness.connector.capabilities.BikeTrainer.SpinDownResult;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.samples.hardwareconnectorservice.service.HardwareConnectorService;
import com.wahoofitness.samples.hardwareconnectorservice.ui.util.UserRequest;

public class CapBikeTrainerFragment extends CapabilityFragment {

	private final BikeTrainer.Listener mBikeTrainerListener = new BikeTrainer.Listener() {

		@Override
		public void onGetAccelerometerInfoResponse(boolean success,
				AccelerometerInfo accelerometerInfo) {
			registerCallbackResult("onGetAccelerometerInfoResponse", success, accelerometerInfo);
			refreshView();
		}

		@Override
		public void onGetBikeTrainerModeResponse(boolean success, BikeTrainerMode bikeTrainerMode) {
			registerCallbackResult("onGetBikeTrainerModeResponse", success, bikeTrainerMode);
			refreshView();
		}

		@Override
		public void onGetCalibrationInfoResponse(boolean success, CalibrationInfo calibrationInfo) {
			registerCallbackResult("onGetCalibrationInfoResponse", success, calibrationInfo);
			refreshView();
		}

		@Override
		public void onGetDeviceCapabilitiesResponse(boolean success, int capabilities) {
			registerCallbackResult("onGetDeviceCapabilitiesResponse", success, capabilities);
			refreshView();
		}

		@Override
		public void onGetDeviceInfoResponse(boolean success, DeviceInfo deviceInfo) {
			registerCallbackResult("onGetDeviceInfoResponse", success, deviceInfo);
			refreshView();
		}

		@Override
		public void onGetTemperatureResponse(boolean success, int temperature) {
			registerCallbackResult("onGetTemperatureResponse", success, temperature);
			refreshView();
		}

		@Override
		public void onSetBikeTrainerModeResponse(boolean success, BikeTrainerMode bikeTrainerMode) {
			registerCallbackResult("onSetBikeTrainerModeResponse", success, bikeTrainerMode);
			refreshView();
		}

		@Override
		public void onSetSimModeGradeResponse(boolean success) {
			registerCallbackResult("onSetSimModeGradeResponse", success);
			refreshView();
		}

		@Override
		public void onSetSimModeRollingResistanceResponse(boolean success) {
			registerCallbackResult("onSetSimModeRollingResistanceResponse", success);
			refreshView();
		}

		@Override
		public void onSetSimModeWindResistanceResponse(boolean success) {
			registerCallbackResult("onSetSimModeWindResistanceResponse", success);
			refreshView();
		}

		@Override
		public void onSetSimModeWindSpeedResponse(boolean success) {
			registerCallbackResult("onSetSimModeWindSpeedResponse", success);
			refreshView();
		}

		@Override
		public void onSetWheelCircumferenceResponse(boolean success) {
			registerCallbackResult("onSetWheelCircumferenceResponse", success);
			refreshView();
		}

		@Override
		public void onSpindownComplete(SpinDownResult spinDownResult) {
			registerCallbackResult("Spindown", "complete ", spinDownResult);
			refreshView();
		}

		@Override
		public void onSpindownFailed() {
			registerCallbackResult("Spindown", "failed");
			refreshView();
		}

		@Override
		public void onSpindownStarted() {
			registerCallbackResult("Spindown", "started");
			refreshView();
		}

		@Override
		public void onTestOpticalResponse(boolean success) {
			registerCallbackResult("onTestOpticalResponse", success);
			refreshView();
		}
	};
	private TextView mTextView;

	@Override
	public void initView(final Context context, LinearLayout ll) {
		mTextView = createSimpleTextView(context);
		ll.addView(mTextView);

		ll.addView(createSimpleButton(context, "sendGetAccelerometerInfo", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getBikeTrainerCap().sendGetAccelerometerInfo();
			}
		}));

		ll.addView(createSimpleButton(context, "sendGetBikeTrainerMode", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getBikeTrainerCap().sendGetBikeTrainerMode();
			}
		}));

		ll.addView(createSimpleButton(context, "sendGetCalibrationInfo", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getBikeTrainerCap().sendGetCalibrationInfo();
			}
		}));

		ll.addView(createSimpleButton(context, "sendGetDeviceCapabilities", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getBikeTrainerCap().sendGetDeviceCapabilities();
			}
		}));

		ll.addView(createSimpleButton(context, "sendGetDeviceInfo", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getBikeTrainerCap().sendGetDeviceInfo();
			}
		}));

		ll.addView(createSimpleButton(context, "sendGetTemperature", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getBikeTrainerCap().sendGetTemperature();
			}
		}));

		ll.addView(createSimpleButton(context, "sendSetErgMode", new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserRequest.requestInt(context, 0, "Enter ERG level (watts)", null, 65, "",
						new UserRequest.IntListener() {

							@Override
							public void onInt(int integer) {
								getBikeTrainerCap().sendSetErgMode(integer);
							}
						});
			}
		}));

		ll.addView(createSimpleButton(context, "sendSetResistanceMode", new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserRequest.requestDoubleRange(context, 0, "Request resistance %", null, null, 0.0,
						1.0, new UserRequest.DoubleListener() {

							@Override
							public void onDouble(double number) {
								getBikeTrainerCap().sendSetResistanceMode((float) number);
							}
						});
			}
		}));

		ll.addView(createSimpleButton(context, "sendSetSimMode", new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserRequest.requestText(context, 0, "Enter sim mode paramters",
						"Use format weight,rollingResistanceCoefficient,windResistanceCoefficient",
						null, "55,0.0023,0.0034", new UserRequest.TextListener() {

							@Override
							public void onText(String text) {
								String[] els = text.split(",");
								if (els.length == 3) {
									try {
										Float weight = Float.valueOf(els[0]);
										Float rollingResistanceCoefficient = Float.valueOf(els[1]);
										Float windResistanceCoefficient = Float.valueOf(els[2]);
										getBikeTrainerCap().sendSetSimMode(weight,
												rollingResistanceCoefficient,
												windResistanceCoefficient);
									} catch (Exception e) {
										Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT)
												.show();
									}
								} else {
									Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT)
											.show();
								}
							}
						});
			}
		}));

		ll.addView(createSimpleButton(context, "sendSetSimModeGrade", new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserRequest.requestDoubleRange(context, 0, "Enter grade", null, null, -45.0, +45.0,
						new UserRequest.DoubleListener() {

							@Override
							public void onDouble(double number) {
								getBikeTrainerCap().sendSetSimModeGrade((float) number);
							}
						});
			}
		}));

		ll.addView(createSimpleButton(context, "sendSetSimModeRollingResistance",
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						UserRequest.requestDouble(context, 0,
								"Enter Rolling Resistance Coefficient", null, 0.0054, "",
								new UserRequest.DoubleListener() {

									@Override
									public void onDouble(double number) {
										getBikeTrainerCap().sendSetSimModeRollingResistance(
												(float) number);
									}
								});
					}
				}));

		ll.addView(createSimpleButton(context, "sendSetSimModeWindResistance",
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						UserRequest.requestDouble(context, 0, "Enter wind resistance coefficient",
								null, 0.0023, "", new UserRequest.DoubleListener() {

									@Override
									public void onDouble(double number) {
										getBikeTrainerCap().sendSetSimModeWindResistance(
												(float) number);
									}
								});
					}
				}));

		ll.addView(createSimpleButton(context, "sendSetSimModeWindSpeed", new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserRequest.requestDoubleRange(context, 0, "Enter wind speed", null, null, -30.0,
						+30.0, new UserRequest.DoubleListener() {

							@Override
							public void onDouble(double number) {
								getBikeTrainerCap().sendSetSimModeWindSpeed((float) number);
							}
						});
			}
		}));

		ll.addView(createSimpleButton(context, "sendSetStandardMode", new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserRequest.requestDoubleRange(context, 0, "Enter standard mode level", null, null,
						0.0, 9.0, new UserRequest.DoubleListener() {

							@Override
							public void onDouble(double number) {
								getBikeTrainerCap().sendSetStandardMode((int) Math.round(number));
							}
						});
			}
		}));

		ll.addView(createSimpleButton(context, "sendSetWheelCircumference", new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserRequest.requestDouble(context, 0, "Enter wheel circumference in millimeters",
						null, 2000.34, "", new UserRequest.DoubleListener() {

							@Override
							public void onDouble(double number) {
								getBikeTrainerCap().sendSetWheelCircumference((float) number);
							}
						});
			}
		}));

		refreshView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		BikeTrainer bikeTrainer = getBikeTrainerCap();
		if (bikeTrainer != null) {
			bikeTrainer.removeListener(mBikeTrainerListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {
		getBikeTrainerCap().addListener(mBikeTrainerListener);
		refreshView();
	}

	private BikeTrainer getBikeTrainerCap() {
		return (BikeTrainer) getCapability(CapabilityType.BikeTrainer);
	}

	@Override
	protected void refreshView() {
		BikeTrainer cap = getBikeTrainerCap();
		if (cap != null) {

			mTextView.setText("");
			mTextView.append("GETTER DATA\n");
			mTextView.append(summarizeGetters(BikeTrainer.class, cap));
			mTextView.append("\n\n");
			mTextView.append("CALLBACKS\n");
			mTextView.append(getCallbackSummary());

		} else {
			mTextView.setText("Please wait... no cap...");
		}
	}
}
