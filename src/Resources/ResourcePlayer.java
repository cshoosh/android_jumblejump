package Resources;

import java.io.IOException;
import java.io.InputStream;

import Entity.Supportables.AnimatedDrawable;
import Entity.Supportables.BitmapDrawableObject;
import Entity.Supportables.Characters;
import Entity.Supportables.DrawableObject;
import Entity.Supportables.Characters.CharacterAction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.zerotwoone.highjump.R;

public enum ResourcePlayer {
	PlayerIdle, PlayerFire, PlayerShield, PlayerBalloon, Balloon;

	public static final DrawableObject getResource(ResourcePlayer res,
			Context context, Characters theme) throws IOException {

		DrawableObject ret = null;
		switch (res) {
		case PlayerIdle:
			String pathIdle = Characters.getPath(CharacterAction.Idle, theme);
			InputStream idle = context.getResources().getAssets()
					.open(pathIdle);

			ret = new BitmapDrawableObject(BitmapFactory.decodeStream(idle));
			break;
		case PlayerFire:
			String pathFire1 = Characters
					.getPath(CharacterAction.Fire01, theme),
			pathFire2 = Characters.getPath(CharacterAction.Fire02, theme);
			InputStream inFire01 = context.getResources().getAssets()
					.open(pathFire1),
			inFire02 = context.getResources().getAssets().open(pathFire2);

			Bitmap[] array = { BitmapFactory.decodeStream(inFire01),
					BitmapFactory.decodeStream(inFire02) };

			ret = new AnimatedDrawable(array, 5);
			break;
		case PlayerShield:
			ret = new BitmapDrawableObject(context, R.drawable.shield);
			break;
		case Balloon:
			ret = new BitmapDrawableObject(context, R.drawable.balloon_01);
			break;
		case PlayerBalloon:
			Bitmap handsup = BitmapFactory.decodeStream(context.getResources()
					.getAssets().open(Characters.getPath(CharacterAction.Fire02, theme)));
			ret = new BitmapDrawableObject(handsup);
			break;
		default:
			break;
		}

		float defWidth = ResourceName.getDefaultWidth(ResourceName.Player,
				context);
		float defHeight = ResourceName.getDefaultHeight(ResourceName.Player,
				context);

		if (res == PlayerShield)
			ret.setBounds(0, 0, defWidth * 0.75f, defHeight * 0.75f);
		else
			ret.setBounds(0, 0, defWidth, defHeight);

		return ret;
	}
}