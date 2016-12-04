package LevelDesign;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Entity.Supportables.EntityInfo;
import Process.AllManagers;
import Process.PhysixManager;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;

public class StageManager implements Parcelable {

	private Screen mScreen;
	private Document[] mStage;
	AllManagers mManagers;

	// public static int LEVEL = 1,DESIGN = 1;
	private static final int NO_STAGES = 8;

	private static final Random mRandom = new Random();

	// TAGS
	private static final String TAG_DESIGN = "DESIGN";
	private static final String TAG_ENTITY = "ENTITY";
	private static final String TAG_ENTITY_TYPE = "TYPE";
	private static final String TAG_ENTITY_X = "X";
	private static final String TAG_ENTITY_Y = "Y";
	private static final String TAG_LEVEL = "LEVEL";

	// Attributes
	private static final String ATTR_DESIGN_NO = "no";

	public StageManager() throws ParserConfigurationException, SAXException,
			IOException {
		mScreen = new Screen(this);
		mStage = new Document[NO_STAGES];
	}

	public StageManager(AllManagers manager) throws ParserConfigurationException, SAXException,
			IOException {
		mScreen = new Screen(this);
		mStage = new Document[NO_STAGES];
		init(manager);
	}

	public StageManager(Parcel source) {
		mScreen = new Screen(this);
		mStage = new Document[NO_STAGES];

		mStageNo = source.readInt();
		randDesignNo = source.readInt();
		mScreen.setBounds((RectF) source.readParcelable(null));
	}

	public void init(AllManagers context) throws ParserConfigurationException,
			SAXException, IOException {
		mManagers = context;
		initDocument();
	}

	public void reset() {
		mStageNo = 0;
		mScreen.init();
	}

	public Screen getScreen() {
		return mScreen;
	}

	// Variables
	private int randDesignNo;
	private int mStageNo; // stageCount, mRepeat;

	public StageParser ParseFromXML(StageParser parser) {
		parser.clear();
		NodeList listLevel = null;

		// DEBUG
		// mStageNo = 7;
		try {
			listLevel = mStage[mStageNo].getElementsByTagName(TAG_LEVEL);
		} catch (Exception e) {
			mStageNo -= 3;
			listLevel = mStage[mStageNo].getElementsByTagName(TAG_LEVEL);
		}
		NodeList listDesign = ((Element) listLevel.item(0))
				.getElementsByTagName(TAG_DESIGN);

		randDesignNo = mRandom.nextInt(listDesign.getLength()) + 1;

		// DEBUG
		// randDesignNo = DESIGN;

		for (int i = 0; i < listDesign.getLength(); i++) {
			Element elementDesignNo = (Element) listDesign.item(i);

			if (elementDesignNo.getAttribute(ATTR_DESIGN_NO).equals(
					String.valueOf(randDesignNo)))
				parseDesignTag(elementDesignNo, parser);
		}

		// Increase Difficulty

		PhysixManager.BOUNCE_VELOCITY += PhysixManager.BOUNCE_VELOCITY * 0.0046875f;

		float limit = 0.0375f;
		switch (PhysixManager.DIFFICULTY) {
		case 0:
			limit *= 0.8f;
			break;
		case 2:
			limit *= 1.35f;
			break;
		default:
			break;
		}
		if (PhysixManager.BOUNCE_VELOCITY >= Screen.SCREEN_HEIGHT * limit)
			PhysixManager.BOUNCE_VELOCITY = Screen.SCREEN_HEIGHT * limit;

		PhysixManager.GRAVITY = PhysixManager
				.getGravity(PhysixManager.BOUNCE_VELOCITY);

		mStageNo++;
		return parser;
	}

	private void parseDesignTag(Element design, StageParser parser)
			throws NullPointerException {
		NodeList list = design.getElementsByTagName(TAG_ENTITY);

		for (int i = 0; i < list.getLength(); i++)
			parseEntityTag((Element) list.item(i), parser);
	}

	private void parseEntityTag(Element entity, StageParser parser) {

		EntityInfo info = new EntityInfo();
		parseTypeTag((Element) entity.getElementsByTagName(TAG_ENTITY_TYPE)
				.item(0), info);
		parseXTag((Element) entity.getElementsByTagName(TAG_ENTITY_X).item(0),
				info);
		parseYTag((Element) entity.getElementsByTagName(TAG_ENTITY_Y).item(0),
				info);

		parser.addInfo(info);
	}

	private void parseXTag(Element x, EntityInfo info) {
		try {
			float mX = Float.parseFloat(x.getTextContent());
			mX *= Screen.SCREEN_WIDTH / 100f;

			info.setX(mX);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	private void parseYTag(Element y, EntityInfo info) {
		try {
			float mY = Float.parseFloat(y.getTextContent());
			mY *= Screen.SCREEN_HEIGHT / 100f;
			info.setY(mY);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	private void parseTypeTag(Element type, EntityInfo info) {
		info.addAttributes(type);
	}

	private void initDocument() throws ParserConfigurationException,
			SAXException, IOException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbFactory.newDocumentBuilder();

		for (int i = 1; i <= NO_STAGES; i++) {
			mStage[i - 1] = db.parse(getInputStream(i));
		}
	}

	private InputStream getInputStream(int stage) throws IOException {
		return mManagers.getContext().getAssets()
				.open("Level/level" + String.valueOf(stage) + ".xml");
	}

	public static final Creator<StageManager> CREATOR = new Creator<StageManager>() {

		@Override
		public StageManager[] newArray(int size) {
			return new StageManager[size];
		}

		@Override
		public StageManager createFromParcel(Parcel source) {
			return new StageManager(source);
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mStageNo);
		dest.writeInt(randDesignNo);
		dest.writeParcelable(mScreen.getBounds(), 0);
	}
}