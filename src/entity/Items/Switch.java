package Entity.Items;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import Entity.Supportables.BitmapDrawableObject;
import Entity.Supportables.Characters;
import Entity.Supportables.Characters.CharacterAction;
import EntityClasses.Entity;
import EntityClasses.Item;
import Process.AllManagers;
import Resources.ResourceName;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Switch extends Item {

	Characters mCharacterChange;
	public Switch(AllManagers context) {
		super(context);
	}

	@Override
	protected void consume(Entity who) {
		try {
			mManagers.getResourceManager().setPlayerResource(mCharacterChange);
			mManagers.getEntityManager().getPlayer().reInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void init() {
		mCharacterChange = Characters.values()[new Random()
				.nextInt(Characters.values().length)];
	
		String fileName = Characters.getPath(CharacterAction.Idle, mCharacterChange);
		try {
			InputStream stream = mManagers.getContext().getAssets().open(fileName);
			Bitmap bmp = BitmapFactory.decodeStream(stream);
			BitmapDrawableObject obj = new BitmapDrawableObject(bmp);
			obj.setBounds(0, 0, ResourceName.getDefaultWidth(ResourceName.Item,
					mManagers.getContext()), ResourceName.getDefaultHeight(ResourceName.Item,
							mManagers.getContext()));
			addDrawable(obj);
		} catch (IOException e) {
			e.printStackTrace();
			this.die();
		}
		
		
	}

}
