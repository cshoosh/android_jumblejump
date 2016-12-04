package Process;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import Resources.ResourceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.zerotwoone.highjump.MainMenuActivity;
import com.zerotwoone.highjump.R;

public class PlayServiceManager implements OnConnectionFailedListener,
		ConnectionCallbacks {

	// public static final String KEY_SCORE = "scoreKey";
	private static final String OFFLINE_SCORE_PATH = "offlinescore.xml";

	private Activity mActivity;
	private boolean isServiceAvailable;
	private boolean isResoluting;
	// private String[] mHighScoreDisconnected = new String[10];

	private GoogleApiClient mClient;

	public PlayServiceManager(Activity activity) {
		mActivity = activity;

		mClient = new GoogleApiClient.Builder(mActivity, this, this)
				.addApi(Games.API).addApi(Plus.API).addScope(Games.SCOPE_GAMES)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		// loadscoredisconnected(mHighScoreDisconnected);
	}

	/*
	 * private void loadscoredisconnected(String[] transfer) { for (int i = 0; i
	 * < transfer.length; i++) { transfer[i] =
	 * PreferenceManager.getDefaultSharedPreferences(
	 * mActivity).getString(KEY_SCORE + String.valueOf(i), ""); } }
	 */

	public void ConnectToService() {
		if (!mClient.isConnected())
			mClient.connect();
	}

	public void SignIn() {
		isResoluting = true;
		ConnectToService();
	}

	public void SignOut() {
		if (mClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mClient);
			mClient.disconnect();
			mActivity.findViewById(R.id.sign_in_button).setVisibility(
					View.VISIBLE);
		}
	}

	public void startAchievement() {
		if (isConnected()) {
			Games.Achievements.load(mClient, false);
			mActivity.startActivityForResult(
					Games.Achievements.getAchievementsIntent(mClient),
					MainMenuActivity.REQUEST_ACHIEVEMENT);

		} else
			showWarning();
	}

	public void startLeaderBoard() {
		if (isConnected())
			mActivity.startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getClient())
						,MainMenuActivity.REQUEST_LEADERBOARD);
		else
			showWarning();

	}

	private void showWarning() {
		String message = "";
		String title = "";
		if (hasServiceAvailable()) {
			message = "Google Service not connected,\n" + "try signing in.";
			title = "Warning !!";
		} else {
			message = "Google Service not available,\n"
					+ "try installing or updating Google Play.";
			title = "Error !!";
		}
		showWarning(message, title);
	}

	private void showWarning(String message, String title) {
		new AlertDialog.Builder(mActivity).setMessage(message).setTitle(title)
				.setPositiveButton("OK", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create().show();
	}

	public void pause() {
		mClient.disconnect();
	}

	public void resume() {
		mClient.connect();
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		mActivity.findViewById(R.id.sign_in_button).setVisibility(View.GONE);
		isResoluting = false;
	}

	@Override
	public void onConnectionSuspended(int cause) {
		getClient().reconnect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {

		int resultCode = result.getErrorCode();
		if (resultCode == ConnectionResult.SERVICE_DISABLED
				|| resultCode == ConnectionResult.SERVICE_INVALID
				|| resultCode == ConnectionResult.SERVICE_MISSING)
			isServiceAvailable = false;
		else
			isServiceAvailable = true;

		if (!isServiceAvailable) {
			{
				showWarning("Service not available, try installing Google"
						+ " Play Services.", "Service Unavailable");

				mActivity.findViewById(R.id.sign_in_button).setVisibility(
						View.GONE);
			}
		}

		if (isResoluting && hasServiceAvailable())
			if (result.hasResolution())
				try {
					isResoluting = false;
					result.startResolutionForResult(mActivity,
							MainMenuActivity.REQUEST_RESOLUTION);
				} catch (SendIntentException e) {
					e.printStackTrace();
				}
	}

	public void submitAchievements(Bundle achievements,int difficulty) {

		if (isConnected()) {
			submitBundleOnline(achievements, difficulty);
			try {
				Document doc = getDocumentAchievement();
				NodeList list = doc.getElementsByTagName("DATA");

				if (list.getLength() > 0) {
					for (int i = 0; i < list.getLength(); i++) {
						if (!getClient().isConnected())
							break;
						Element data = (Element) list.item(i);
						int diff = Integer.valueOf(data.getElementsByTagName("DIFFICULTY").item(0)
								.getTextContent());
						submitBundleOnline(getBundle(data),diff);
						doc.getElementsByTagName("ACHIEVEMENT").item(0)
								.removeChild(list.item(i));
					}

					writeDocument(doc);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			try {
				Document achieveDoc = getDocumentAchievement();

				if (achieveDoc.getElementsByTagName("DATA").getLength() < 100)
					writeBundle(achievements, achieveDoc, difficulty);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void submitBundleOnline(Bundle bundle, int difficulty) {
		int score = bundle.getInt(ResourceManager.translateID(
				R.string.achievement_scorer, mActivity));
		int coins = bundle.getInt(ResourceManager.translateID(
				R.string.achievement_saver, mActivity));
		int spider = bundle.getInt(ResourceManager.translateID(
				R.string.achievement_creepy_spiders, mActivity));
		int snakes = bundle.getInt(ResourceManager.translateID(
				R.string.achievement_snakes_in_the_jungle, mActivity));

		boolean ram = bundle.getBoolean(ResourceManager.translateID(
				R.string.achievement_rammer, mActivity));
		boolean toasted = bundle.getBoolean(ResourceManager.translateID(
				R.string.achievement_toasted, mActivity));
		boolean bigboss = bundle.getBoolean(ResourceManager.translateID(
				R.string.achievement_big_boss, mActivity));
		boolean cliff = bundle.getBoolean(ResourceManager.translateID(
				R.string.achievement_cliffhanger, mActivity));

		switch (difficulty) {
		case 0:
			Games.Leaderboards.submitScore(mClient, ResourceManager.translateID(
					R.string.leaderboard_high_score__easy, mActivity), score);

			if (score >= 10000)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_scorer__easy, mActivity));
			if (score >= 20000)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_player__easy, mActivity));
			if (score >= 35000)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_victor__easy, mActivity));
			if (score >= 50000)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_champion__easy, mActivity));

			if (ram)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_rammer__easy, mActivity));
			/*if (bigboss)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_big_boss, mActivity));*/
			if (cliff)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_cliffhanger__easy, mActivity));
			if (toasted)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_toasted__easy, mActivity));

			submitCoins(coins, difficulty);

			if (snakes > 0) {
				Games.Achievements.increment(getClient(), ResourceManager
						.translateID(R.string.achievement_snake_hunter__easy, mActivity),
						snakes);
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_snakes_in_the_jungle__easy, mActivity));
			}

			if (spider > 0) {
				Games.Achievements.increment(getClient(),
						ResourceManager.translateID(
								R.string.achievement_spider_hunter__easy, mActivity),
						spider);
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_creepy_spiders__easy, mActivity));
			}	
			break;
		case 1:
			Games.Leaderboards.submitScore(mClient, ResourceManager.translateID(
					R.string.leaderboard_high_score__medium, mActivity), score);

			if (score >= 10000)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_scorer, mActivity));
			if (score >= 20000)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_player, mActivity));
			if (score >= 35000)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_victor, mActivity));
			if (score >= 50000)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_champion, mActivity));

			if (ram)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_rammer, mActivity));
			if (bigboss)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_big_boss, mActivity));
			if (cliff)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_cliffhanger, mActivity));
			if (toasted)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_toasted, mActivity));

			submitCoins(coins,difficulty);

			if (snakes > 0) {
				Games.Achievements.increment(getClient(), ResourceManager
						.translateID(R.string.achievement_snake_hunter, mActivity),
						snakes);
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_snakes_in_the_jungle, mActivity));
			}

			if (spider > 0) {
				Games.Achievements.increment(getClient(),
						ResourceManager.translateID(
								R.string.achievement_spider_hunter, mActivity),
						spider);
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_creepy_spiders, mActivity));
			}
			break;
		case 2:
			Games.Leaderboards.submitScore(mClient, ResourceManager.translateID(
					R.string.leaderboard_high_score__hard, mActivity), score);

			if (score >= 10000)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_scorer__hard, mActivity));
			if (score >= 20000)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_player__hard, mActivity));
			if (score >= 35000)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_victor__hard, mActivity));
			if (score >= 50000)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_champion__hard, mActivity));

			if (ram)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_rammer__hard, mActivity));
			/*if (bigboss)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_big_boss, mActivity));*/
			if (cliff)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_cliffhanger__hard, mActivity));
			if (toasted)
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_toasted__hard, mActivity));

			submitCoins(coins, difficulty);

			if (snakes > 0) {
				Games.Achievements.increment(getClient(), ResourceManager
						.translateID(R.string.achievement_snake_hunter__hard, mActivity),
						snakes);
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_snakes_in_the_jungle__hard, mActivity));
			}

			if (spider > 0) {
				Games.Achievements.increment(getClient(),
						ResourceManager.translateID(
								R.string.achievement_spider_hunter__hard, mActivity),
						spider);
				Games.Achievements.unlock(getClient(), ResourceManager.translateID(
						R.string.achievement_creepy_spiders__hard, mActivity));
			}
			break;
		default:
			break;
		}
	}

	private Bundle getBundle(Element data) {
		int score = Integer.valueOf(data.getElementsByTagName("SCORE").item(0)
				.getTextContent());
		int coins = Integer.valueOf(data.getElementsByTagName("COINS").item(0)
				.getTextContent());
		int spiders = Integer.valueOf(data.getElementsByTagName("SPIDERS")
				.item(0).getTextContent());
		int snakes = Integer.valueOf(data.getElementsByTagName("SNAKES")
				.item(0).getTextContent());

		boolean ram = Boolean.valueOf(data.getElementsByTagName("RAM").item(0)
				.getTextContent());
		boolean toasted = Boolean.valueOf(data.getElementsByTagName("TOASTED")
				.item(0).getTextContent());
		boolean bigboss = Boolean.valueOf(data.getElementsByTagName("BIGBOSS")
				.item(0).getTextContent());
		boolean cliff = Boolean.valueOf(data.getElementsByTagName("CLIFF")
				.item(0).getTextContent());

		Bundle bundle = new Bundle();

		bundle.putInt(ResourceManager.translateID(R.string.achievement_scorer,
				mActivity), score);
		bundle.putInt(ResourceManager.translateID(R.string.achievement_saver,
				mActivity), coins);
		bundle.putInt(ResourceManager.translateID(
				R.string.achievement_creepy_spiders, mActivity), spiders);
		bundle.putInt(ResourceManager.translateID(
				R.string.achievement_snakes_in_the_jungle, mActivity), snakes);
		bundle.putBoolean(ResourceManager.translateID(
				R.string.achievement_rammer, mActivity), ram);
		bundle.putBoolean(ResourceManager.translateID(
				R.string.achievement_toasted, mActivity), toasted);
		bundle.putBoolean(ResourceManager.translateID(
				R.string.achievement_big_boss, mActivity), bigboss);
		bundle.putBoolean(ResourceManager.translateID(
				R.string.achievement_cliffhanger, mActivity), cliff);

		return bundle;
	}

	private void writeDocument(Document doc)
			throws TransformerConfigurationException, TransformerException,
			TransformerFactoryConfigurationError, FileNotFoundException {
		DOMSource source = new DOMSource(doc);
		FileOutputStream resultStream = mActivity.openFileOutput(
				OFFLINE_SCORE_PATH, Context.MODE_PRIVATE);

		StreamResult result = new StreamResult(resultStream);
		TransformerFactory.newInstance().newTransformer()
				.transform(source, result);
	}

	private void writeBundle(Bundle bundle, Document main, int difficulty)
			throws TransformerConfigurationException, FileNotFoundException,
			TransformerException, TransformerFactoryConfigurationError {

		Element data = main.createElement("DATA");
		int score = bundle.getInt(ResourceManager.translateID(
				R.string.achievement_scorer, mActivity));
		int coins = bundle.getInt(ResourceManager.translateID(
				R.string.achievement_saver, mActivity));
		int spiders = bundle.getInt(ResourceManager.translateID(
				R.string.achievement_creepy_spiders, mActivity));
		int snakes = bundle.getInt(ResourceManager.translateID(
				R.string.achievement_snakes_in_the_jungle, mActivity));

		boolean ram = bundle.getBoolean(ResourceManager.translateID(
				R.string.achievement_rammer, mActivity));
		boolean toasted = bundle.getBoolean(ResourceManager.translateID(
				R.string.achievement_toasted, mActivity));
		boolean bigboss = bundle.getBoolean(ResourceManager.translateID(
				R.string.achievement_big_boss, mActivity));
		boolean cliff = bundle.getBoolean(ResourceManager.translateID(
				R.string.achievement_cliffhanger, mActivity));

		Element scoreEle = main.createElement("SCORE");
		Element coinsEle = main.createElement("COINS");
		Element spidersEle = main.createElement("SPIDERS");
		Element snakesEle = main.createElement("SNAKES");
		Element ramEle = main.createElement("RAM");
		Element toastedEle = main.createElement("TOASTED");
		Element bigbossEle = main.createElement("BIGBOSS");
		Element cliffEle = main.createElement("CLIFF");
		Element difficultyEle = main.createElement("DIFFICULTY");

		scoreEle.setTextContent(String.valueOf(score));
		coinsEle.setTextContent(String.valueOf(coins));
		spidersEle.setTextContent(String.valueOf(spiders));
		snakesEle.setTextContent(String.valueOf(snakes));
		ramEle.setTextContent(String.valueOf(ram));
		toastedEle.setTextContent(String.valueOf(toasted));
		bigbossEle.setTextContent(String.valueOf(bigboss));
		cliffEle.setTextContent(String.valueOf(cliff));
		difficultyEle.setTextContent(String.valueOf(difficulty));

		data.appendChild(scoreEle);
		data.appendChild(coinsEle);
		data.appendChild(spidersEle);
		data.appendChild(snakesEle);
		data.appendChild(ramEle);
		data.appendChild(toastedEle);
		data.appendChild(bigbossEle);
		data.appendChild(cliffEle);
		data.appendChild(difficultyEle);

		main.getElementsByTagName("ACHIEVEMENT").item(0).appendChild(data);
		writeDocument(main);
	}

	private Document getDocumentAchievement()
			throws ParserConfigurationException {
		try {
			FileInputStream input = mActivity.openFileInput(OFFLINE_SCORE_PATH);

			return DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(input);
		} catch (Exception e) {
			e.printStackTrace();
			return newDocumentAchievement();
		}
	}

	private Document newDocumentAchievement()
			throws ParserConfigurationException {
		Document newInstance = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().newDocument();

		Element root = newInstance.createElement("ROOT");
		Element achievement = newInstance.createElement("ACHIEVEMENT");

		root.appendChild(achievement);
		newInstance.appendChild(root);

		return newInstance;
	}

	private void submitCoins(int coins, int difficulty) {
		if (isConnected() && coins > 0) {

			switch (difficulty) {
			case 0:
				Games.Achievements.increment(getClient(), ResourceManager
						.translateID(R.string.achievement_saver__easy, mActivity), coins);
				Games.Achievements.increment(getClient(), ResourceManager
						.translateID(R.string.achievement_investor__easy, mActivity),
						coins);
				Games.Achievements.increment(getClient(), ResourceManager
						.translateID(R.string.achievement_gatherer__easy, mActivity),
						coins);
				Games.Achievements.increment(getClient(), ResourceManager
						.translateID(R.string.achievement_collector__easy, mActivity),
						coins);
				break;
			case 1:
				Games.Achievements.increment(getClient(), ResourceManager
						.translateID(R.string.achievement_saver, mActivity), coins);
				Games.Achievements.increment(getClient(), ResourceManager
						.translateID(R.string.achievement_investor, mActivity),
						coins);
				Games.Achievements.increment(getClient(), ResourceManager
						.translateID(R.string.achievement_gatherer, mActivity),
						coins);
				Games.Achievements.increment(getClient(), ResourceManager
						.translateID(R.string.achievement_collector, mActivity),
						coins);
				break;
			case 2:
				Games.Achievements.increment(getClient(), ResourceManager
						.translateID(R.string.achievement_saver__hard, mActivity), coins);
				Games.Achievements.increment(getClient(), ResourceManager
						.translateID(R.string.achievement_investor__hard, mActivity),
						coins);
				Games.Achievements.increment(getClient(), ResourceManager
						.translateID(R.string.achievement_gatherer__hard, mActivity),
						coins);
				Games.Achievements.increment(getClient(), ResourceManager
						.translateID(R.string.achievement_collector__hard, mActivity),
						coins);
				break;
			default:
				break;
			}
			
		}
	}

	public boolean isConnected() {
		return mClient.isConnected();
	}

	public GoogleApiClient getClient() {
		return mClient;
	}

	public boolean hasServiceAvailable() {
		return isServiceAvailable;
	}

}