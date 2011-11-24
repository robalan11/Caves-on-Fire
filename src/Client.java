import java.util.*;
import com.threed.jpct.SimpleVector;

public class Client {
	Dungeon dungeon;
	UI ui;
	Player player;
	Timer t;
	int playerID;
	float vx, vy, vz;
	
	Client() {
		ui = new UI();
		addPlayer(0.0f, 2.0f, 0.0f);
		buildDungeon();
	}
	
	void run() throws Exception {
		t = new Timer();
		t.schedule(new AnimationTask(), 10, 10);
		while(!org.lwjgl.opengl.Display.isCloseRequested()) {
			ui.draw();
			ui.highlightHovered();
			SimpleVector click = ui.getClick();
			player.setDest(click);
			Thread.sleep(10);
		}
		
		t.cancel();
		ui.shutdown();
	}
	
	void getArrows() {
		boolean[] commands;
		vx = vy = vz = 0.0f;
		commands = ui.getCommands();
		if(commands[0] == true)
			vx = 1.0f;
		if(commands[1] == true)
			vz = 1.0f;
		if(commands[2] == true)
			vx = -1.0f;
		if(commands[3] == true)
			vz = -1.0f;
	}
	
	void addPlayer(float x, float y, float z) {
		player = new Player(x, y, z);
		
		ui.addPlayerObject(x, y, z);
	}
	
	void movePlayer() {
		SimpleVector v;
		v = player.move();
		ui.movePlayerObject(v);
	}
	
	void buildDungeon() {
		dungeon = new Dungeon();
		
		for(int x = 0; x < dungeon.xSize; x++) {
			for(int y = 0;  y < dungeon.ySize; y++) {
				for(int z = 0; z < dungeon.zSize; z++) {
					int type = dungeon.block_type[x][y][z];
					if(type > 0) {
						ui.addBlockObject(x, y, z, type);
					}
				}
			}
		}
	}
	
	private class AnimationTask extends TimerTask {
		public void run() {
			//getArrows();
			movePlayer();
		}
	}
}
