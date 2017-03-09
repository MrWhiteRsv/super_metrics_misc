package com.wahoofitness.samples.hardwareconnectorservice.ui.capabilityfragments;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.CyclePowerMeterControl;
import com.wahoofitness.connector.capabilities.CyclePowerMeterControl.ManualZeroCalibrationResult;
import com.wahoofitness.samples.hardwareconnectorservice.service.HardwareConnectorService;
import com.wahoofitness.samples.hardwareconnectorservice.ui.util.UserRequest;

public class CapCyclePowerMeterControlFragment extends CapabilityFragment {

	private final CyclePowerMeterControl.Listener mCyclePowerMeterControlListener = new CyclePowerMeterControl.Listener() {

		@Override
		public void onGetCrankLengthResponse(boolean success, double crankLengthMm) {
			registerCallbackResult("onGetCrankLengthResponse", success, crankLengthMm);
			refreshView();

		}

		@Override
		public void onManualZeroCalibrationResult(boolean success,
				ManualZeroCalibrationResult manualZeroCalibrationResult) {
			registerCallbackResult("onGetCrankLengthResponse", success, manualZeroCalibrationResult);
			refreshView();

		}

		@Override
		public void onSetCrankLengthResponse(boolean success, double crankLengthMm) {
			registerCallbackResult("onGetCrankLengthResponse", success, crankLengthMm);
			refreshView();

		}

	};
	private TextView mTextView;

	@Override
	public void initView(final Context context, LinearLayout ll) {
		mTextView = createSimpleTextView(context);
		refreshView();
		ll.addView(mTextView);

		ll.addView(createSimpleButton(context, "sendGetCrankLength", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getCyclePowerMeterControlCap().sendGetCrankLength();
			}
		}));

		ll.addView(createSimpleButton(context, "sendSetCrankLength", new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserRequest.requestDouble(context, 0, "crank length (mm)", null, 200.0, "",
						new UserRequest.DoubleListener() {

							@Override
							public void onDouble(double number) {
								getCyclePowerMeterControlCap().sendSetCrankLength(number);
							}
						});
			}
		}));

		ll.addView(createSimpleButton(context, "sendStartManualZeroConfiguration",
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						getCyclePowerMeterControlCap().sendStartManualZeroConfiguration();
					}
				}));

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		CyclePowerMeterControl cyclePowerMeterControl = getCyclePowerMeterControlCap();
		if (cyclePowerMeterControl != null) {
			cyclePowerMeterControl.removeListener(mCyclePowerMeterControlListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getCyclePowerMeterControlCap().addListener(mCyclePowerMeterControlListener);
		refreshView();
	}

	private CyclePowerMeterControl getCyclePowerMeterControlCap() {
		return (CyclePowerMeterControl) getCapability(CapabilityType.CyclePowerMeterControl);
	}

	@Override
	protected void refreshView() {
		CyclePowerMeterControl cap = getCyclePowerMeterControlCap();
		if (cap != null) {

			mTextView.setText("");
			mTextView.append("GETTER DATA\n");
			mTextView.append(summarizeGetters(CyclePowerMeterControl.class, cap));
			mTextView.append("\n\n");
			mTextView.append("CALLBACKS\n");
			mTextView.append(getCallbackSummary());

		} else {
			mTextView.setText("Please wait... no cap...");
		}

	}
}
