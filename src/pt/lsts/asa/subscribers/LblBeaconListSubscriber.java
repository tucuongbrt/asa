package pt.lsts.asa.subscribers;

import android.util.Log;
import pt.lsts.asa.comms.IMCSubscriber;
import pt.lsts.asa.pos.Beacon;
import pt.lsts.asa.pos.LblBeaconList;
import pt.lsts.imc.IMCMessage;

public class LblBeaconListSubscriber implements IMCSubscriber{
	
	public static final String[] SUBSCRIBED_MSGS = { "LblConfig" };
	public static final String TAG = "LblBeaconList";
	private LblBeaconList lblBeaconList;
	
	public LblBeaconListSubscriber(LblBeaconList lblBeaconList) {
		this.lblBeaconList= lblBeaconList;
	}
	
	@Override
	public void onReceive(IMCMessage msg) {
		Log.i(TAG, "List Received");
		Log.i(TAG, msg.toString());

		lblBeaconList.getList().clear();
		for (int i = 0; i < 6; i++) // FIXME For now hard-code max beacon number
		{
			IMCMessage m = msg.getMessage("beacon" + i);
			// do this because if beacon is not set it returns NULL(maybe
			// 'continue' instead of 'break'?);
			if (m == null)
				break;

			Log.i(TAG, m.toString());
			Beacon beacon = new Beacon(m.getString("beacon"), Math.toDegrees(m
					.getDouble("lat")), Math.toDegrees(m.getDouble("lon")),
					m.getDouble("depth"));

			beacon.setInterrogationChannel(m.getInteger("query_channel"));
			beacon.setReplyChannel(m.getInteger("reply_channel"));
			beacon.setTransponderDelay(m.getInteger("transponder_delay"));
			lblBeaconList.getList().add(beacon);
		}
		lblBeaconList.notifyListeners();
	}

}
