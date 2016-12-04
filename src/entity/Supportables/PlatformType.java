package Entity.Supportables;


import Entity.Platforms.BreakablePlatform;
import Entity.Platforms.DecoyPlatform;
import Entity.Platforms.DoubleJumpPlatform;
import Entity.Platforms.ElectricPlatform;
import Entity.Platforms.MovablePlatform;
import Entity.Platforms.NormalPlatform;
import Entity.Platforms.Removable;
import EntityClasses.Platform;
import Process.AllManagers;


public enum PlatformType {
	Breakable, Decoy, DoubleJump, Movable, Normal, Electrical, Removable;

	public static final PlatformType translate(String value) {
		if (value.equals("NML"))
			return Normal;
		else if (value.equals("DCY"))
			return Decoy;
		else if (value.equals("BRK"))
			return Breakable;
		else if (value.equals("DBL"))
			return DoubleJump;
		else if (value.equals("ELE"))
			return Electrical;
		else if (value.equals("MOV"))
			return Movable;
		else if (value.equals("REM"))
			return Removable;
		return null;
	}

	public static final Platform getInstance(EntityInfo info, AllManagers managers) {
		PlatformType type = translate(info
				.getAttributeValue(TypeAttributeList.sub));
		switch (type) {
		case Breakable:
			return new BreakablePlatform(info, managers);
		case Decoy:
			return new DecoyPlatform(info, managers);
		case DoubleJump:
			return new DoubleJumpPlatform(info, managers);
		case Electrical:
			return new ElectricPlatform(info, managers);
		case Movable:
			return new MovablePlatform(info, managers);
		case Normal:
			return new NormalPlatform(info, managers);
		case Removable:
			return new Removable(info, managers);
		default:
			return null;
		}
	}
};