import com.threed.jpct.*;

public class Client {
	Dungeon dungeon;
	UI ui;
	Player player;
	World world;
	int playerID;
	
	Client() {
		world = new World();
		world.setAmbientLight(128,  128, 128);
		world.getCamera().setPosition(5, -5, -5);
		world.getCamera().lookAt(new SimpleVector(0.0f, 0.0f, 0.0f));
		
		loadTextures();
		
		addPlayer();
		dungeon = new Dungeon(world);
		ui = new UI(world);
	}
	
	void run() throws Exception {
		boolean[] commands;
		float dx, dy, dz;
		while(!org.lwjgl.opengl.Display.isCloseRequested()) {
			dx = dy = dz = 0.0f;
			commands = ui.getCommands();
			if(commands[0] == true)
				dx = 1f;
			if(commands[1] == true)
				dz = 1f;
			if(commands[2] == true)
				dx = -1f;
			if(commands[3] == true)
				dz = -1f;
			movePlayer(dx, dy, dz);
			ui.draw();
			Thread.sleep(10);
		}
		
		ui.shutdown();
	}
	
	void loadTextures() {
		TextureManager tm = TextureManager.getInstance();
		tm.addTexture("box", new Texture("box.jpg"));
		tm.addTexture("stones", new Texture("tp/expo_stones.png"));
		tm.addTexture("steel", new Texture("tp/expo_steel.png"));
		tm.addTexture("bamboo", new Texture("tp/expo_bamboo.png"));
		tm.addTexture("stain", new Texture("tp/expo_stain.png"));
	}
	
	void updateCamera() {
		world.getCamera().setPosition(player.x-1, player.y-2, player.z-1);
		world.getCamera().lookAt(
				world.getObject(playerID).getTransformedCenter());
	}
	
	void addPlayer() {
		player = new Player(0.0f, 2.0f, 0.0f);
		
		Object3D t = Primitives.getCone(0.5f);
		t.setTexture("stain");
		t.calcTextureWrapSpherical();
		t.recreateTextureCoords();
		t.build();
		t.translate(0.0f, 2.0f, 0.0f);
		playerID = world.addObject(t);
	}
	
	void movePlayer(float dx, float dy, float dz) {
		player.move(dx, dy, dz);
		world.getObject(playerID).translate(dx, dy, dz);
		updateCamera();
	}
}
