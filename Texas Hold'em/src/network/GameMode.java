package network;

public enum GameMode {

	NOLIMIT("NOLIMIT"), POTLIMIT("POTLIMIT"), FIXEDLIMIT("FIXEDLIMIT");
	
	private String mode;
	private GameMode(String mode){
		this.mode = mode;
	}
	
	public static GameMode setMode(String mode){
		for (GameMode m : values()){
			if(m.mode.equals(mode)){
				System.out.println("GameMode accepted.");
				return m;
			}
		}
		System.out.println("GameMode denied.");
		return setDefault();
	}
	
	public static GameMode setDefault(){
		return NOLIMIT;
	}
	
}
