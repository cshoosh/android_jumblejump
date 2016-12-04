package Process;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import Menu.CharacterSlot;
import Menu.TouchableDrawable.TouchableTypes;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.zerotwoone.highjump.MainMenuActivity;

public class InAppStore implements ServiceConnection {

	private static final String KEY_APP = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvmQdo9lmFfdEUWwHKoN6yL8+SvLxo41C6f1xWgiBzhrqZcHpyx7T4Faud/OEw1PXgeu0N8R8nSmGunWB7cpjojw6anMrYbbeOwx/A/PK47BCVW1oN2UzUfF3a6lmewcz4MiKD0ObNRZ5qQLbcEJofGgEtKV91KAULYmzQwfAZ1Ggz51i9Sg9efStc9h9zDx9QPEq2RjMSRsqZDYUoobxt18C/rr/Hqek6AzBqB3oEdHw412ianT0fi5hH9mN3IeL0XFWdcbXCBfG26LgK7kLdp0jIMENVhFFdMT2jqytjBnTtA8jXezVofZ0cGzGF0m210Px3Jp/AgjdqGG/q3+4BwIDAQAB";

	public static final String UPGRADE = "upgrade";

	public static final String PACKAGE1 = "package1";
	public static final String PACKAGE2 = "package2";
	public static final String PACKAGE3 = "package3";
	public static final String PACKAGE4 = "package4";
	public static final String PACKAGE5 = "package5";

	public static final String TYPE_INAPP = "inapp";

	private IInAppBillingService mBillingService;
	private Context mContext;
	private boolean isUpgraded;

	private static final int REQUEST_PURCHASE = 5;

	public InAppStore(Context context) {
		init(context);
	}

	public InAppStore() {
	}

	public void init(Context context) {
		mContext = context;
		context.bindService(new Intent(
				"com.android.vending.billing.InAppBillingService.BIND"), this,
				Context.BIND_AUTO_CREATE);
	}

	public boolean isUpgraded() {
		return isUpgraded;
	}

	public void setUpgraded(boolean isUpgraded) {
		if (isUpgraded) {
			try {
				MainMenuActivity.getManager().getMainMenu().getViewHandler()
						.getTouchable(TouchableTypes.RemoveAds)
						.setEnabled(false);

				for (CharacterSlot slot : MainMenuActivity.getManager()
						.getMainMenu().getCharacterSelection().getArray())
					slot.setEnabled(true);

			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		PreferenceManager.getDefaultSharedPreferences(mContext).edit()
				.putBoolean(UPGRADE, isUpgraded).commit();
		this.isUpgraded = isUpgraded;
	}

	public boolean isBillingSupported() {
		if (mBillingService != null)
			try {
				if (mBillingService.isBillingSupported(3,
						mContext.getPackageName(), TYPE_INAPP) == 0)
					return true;
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		return false;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_PURCHASE) {
			// int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
			// String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");

			if (resultCode == Activity.RESULT_OK) {
				String signatureData = data
						.getStringExtra("INAPP_DATA_SIGNATURE");
				String inappData = data.getStringExtra("INAPP_PURCHASE_DATA");

				try {
					JSONObject json = new JSONObject(inappData);
					boolean isPackaage = json.getString("productId").equals(
							UPGRADE);
					if (signatureData.equals(KEY_APP) && isPackaage)
						setUpgraded(true);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void makePurchase(String id, String type) {
		Bundle buyIntentBundle = null;

		if (isBillingSupported()) {
			try {
				if (mBillingService != null) {
					buyIntentBundle = mBillingService.getBuyIntent(3,
							mContext.getPackageName(), id, type,
							Secure.ANDROID_ID);
				} else
					Toast.makeText(
							mContext,
							"Service not available, try installing Google "
									+ "Play services", Toast.LENGTH_SHORT)
							.show();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			if (buyIntentBundle != null) {
				PendingIntent pendingIntent = buyIntentBundle
						.getParcelable("BUY_INTENT");
				try {
					((Activity) mContext).startIntentSenderForResult(
							pendingIntent.getIntentSender(), REQUEST_PURCHASE,
							new Intent(), 0, 0, 0);
				} catch (SendIntentException e) {
					e.printStackTrace();
				}
			}
		} else {
			Toast.makeText(
					mContext,
					"Billing not supported on this device, "
							+ "\nUpgrade Google Play Store", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void destroy() {
		if (mContext != null)
			mContext.unbindService(this);
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		mBillingService = IInAppBillingService.Stub.asInterface(service);

		try {

			Bundle purchases = mBillingService.getPurchases(3,
					mContext.getPackageName(), "inapp", null);
			int response = purchases.getInt("RESPONSE_CODE");
			if (response == 0) {
				ArrayList<String> listPurchases = purchases
						.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");

				for (String thisResponse : listPurchases) {
					if (thisResponse.equals(UPGRADE))
						setUpgraded(true);
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		mBillingService = null;
	}
}
