import com.threed.jpct.*;

public class Main {
	public static void main(String[] args) throws Exception {
		Dungeon dungeon;
		UI ui;
		Object3D cube;
		
		dungeon = new Dungeon();
		
		TextureManager.getInstance().addTexture("box", new Texture("box.jpg"));
		
		cube = Primitives.getCube(1.0f);
		cube.setTexture("box");
		cube.setEnvmapped(Object3D.ENVMAP_ENABLED);
		cube.build();
		dungeon.world.addObject(cube);
		
		dungeon.world.getCamera().setPosition(50, -50, -5);
		dungeon.world.getCamera().lookAt(cube.getTransformedCenter());
		
		ui = new UI();
		
		while(!org.lwjgl.opengl.Display.isCloseRequested()) {
			cube.rotateY(0.01f);
			cube.translate(0.0f, 0.0f, 0.05f);
			ui.draw(dungeon);
			Thread.sleep(10);
		}
		
		ui.shutdown();
		System.exit(0);
	}
}