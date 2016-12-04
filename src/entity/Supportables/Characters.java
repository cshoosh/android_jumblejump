package Entity.Supportables;

import java.io.File;

public enum Characters {
	Gadha, Billi, SherKhan, MotaGenda, Bhed, Bandar, Doggie, Panda, Lamboo, Cheetah, Mendak;
	
		private static final String BASE_PATH = "pngs" + File.separator;

	//Themes
		private static final String MONKEY_PATH = "monkey" + File.separator;
		private static final String CAT_PATH = "Cat" + File.separator;
		private static final String HIPPO_PATH = "Hippo" + File.separator;
		private static final String LION_PATH = "Lion" + File.separator;
		private static final String CHEETAH_PATH = "Cheetah" + File.separator;
		private static final String GIRRAFFE_PATH = "Giraffe" + File.separator;
		private static final String FROG_PATH = "Frog" + File.separator;
		private static final String PANDA_PATH = "Panda" + File.separator;
		private static final String SHEEP_PATH = "Sheep" + File.separator;
		private static final String TEDDY_PATH = "Teddybear" + File.separator;
		private static final String ZEBRA_PATH = "Zebra" + File.separator;
		
		
		//Action
		private static final String IDLE = "idle.png";
		private static final String FIRE_01 = "fire01.png";
		private static final String FIRE_02 = "fire02.png";
		
		public static final String getPath(CharacterAction action, Characters character){
			StringBuilder builder = new StringBuilder();
			
			builder.append(BASE_PATH);
			builder.append(getThemePath(character));
			builder.append(getActionPath(action));
			
			return builder.toString();
		}
		
		private static String getActionPath(CharacterAction action){
			switch (action) {
			case Idle:
				return IDLE;
			case Fire01:
				return FIRE_01;
			case Fire02:
				return FIRE_02;

			default:
				break;
			}
			
			return "";
		}
		
		private static String getThemePath(Characters theme) {
			switch (theme) {
			case Bandar:
				return MONKEY_PATH;
			case Billi:
				return CAT_PATH;
			case Cheetah:
				return CHEETAH_PATH;
			case Mendak:
				return FROG_PATH;
			case Lamboo:
				return GIRRAFFE_PATH;
			case MotaGenda:
				return HIPPO_PATH;
			case SherKhan:
				return LION_PATH;
			case Panda:
				return PANDA_PATH;
			case Bhed:
				return SHEEP_PATH;
			case Doggie:
				return TEDDY_PATH;
			case Gadha:
				return ZEBRA_PATH;
			default:
				break;
			}
			return "";
		}
		
		public enum CharacterAction{
			Idle, Fire01, Fire02
		}
}