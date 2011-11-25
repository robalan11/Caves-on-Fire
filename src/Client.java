import java.util.*;
import com.threed.jpct.SimpleVector;

public class Client {
	Dungeon dungeon;
	UI ui;
	Player player;
	Timer t;
	int playerID;
	
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
			ui.processMouse();
			SimpleVector dest = ui.getClicked();
			if(dest != null) {
				player.setDest(dest);
			}
			Thread.sleep(10);
		}
		
		t.cancel();
		ui.shutdown();
	}
	
	void getKeyCommands() {
		ui.getCommands();
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
			getKeyCommands();
			movePlayer();
		}
	}
}
