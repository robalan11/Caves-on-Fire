import com.threed.jpct.*;
import com.threed.jpct.util.*;
import java.awt.event.*;
import org.lwjgl.input.*;

public class UI {
	World world;
	FrameBuffer buffer;
	
	KeyMapper keyMapper;
	MouseMapper mouseMapper;
	
	int playerID;
	Object3D highlighted;
	Object3D lastClicked;
	
	UI() {
		world = new World();
		world.setAmbientLight(128,  128, 128);
		world.getCamera().setPosition(5, -5, -5);
		world.getCamera().lookAt(new SimpleVector(0.0f, 0.0f, 0.0f));
		
		buffer = new FrameBuffer(800, 600, FrameBuffer.SAMPLINGMODE_NORMAL);
		buffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
		buffer.enableRenderer(IRenderer.RENDERER_OPENGL);
		
		loadTextures();
		
		keyMapper = new KeyMapper();
		mouseMapper = new MouseMapper(buffer);
	}
	
	void draw() {
		buffer.clear(java.awt.Color.BLUE);
		world.renderScene(buffer);
		world.draw(buffer);
		buffer.update();
		buffer.displayGLOnly();
	}
	
	void addPlayerObject(float x, float y, float z) {
		Object3D t = Primitives.getCone(0.5f);
		t.setTexture("stain");
		t.calcTextureWrapSpherical();
		t.recreateTextureCoords();
		t.build();
		t.translate(x, y, z);
		t.setSelectable(Object3D.MOUSE_UNSELECTABLE);
		playerID = world.addObject(t);
	}
	
	void movePlayerObject(float dx, float dy, float dz) {
		world.getObject(playerID).translate(dx, dy, dz);
		updateCamera();
	}
	
	void movePlayerObject(SimpleVector v) {
		movePlayerObject(v.x, v.y, v.z);
	}
	
	void addBlockObject(int x, int y, int z, int type) {
		Object3D t = Primitives.getCube(0.5f);
		t.setTexture("stones");
		//t.setEnvmapped(Object3D.ENVMAP_ENABLED);
		t.calcTextureWrapSpherical();
		t.recreateTextureCoords();
		t.build();
		t.rotateY((float)(Math.PI/4));
		t.translate(x, y, z);
		world.addObject(t);
	}
	
	void updateCamera() {
		world.getCamera().setPositionToCenter(world.getObject(playerID));
		world.getCamera().moveCamera(Camera.CAMERA_MOVEOUT, 7);
		//world.getCamera().setPosition(player.x-1, player.y-2, player.z-1);
		world.getCamera().lookAt(
				world.getObject(playerID).getTransformedCenter());
	}
	
	private void loadTextures() {
		TextureManager tm = TextureManager.getInstance();
		tm.addTexture("box", new Texture("box.jpg"));
		tm.addTexture("stones", new Texture("tp/expo_stones.png"));
		tm.addTexture("steel", new Texture("tp/expo_steel.png"));
		tm.addTexture("bamboo", new Texture("tp/expo_bamboo.png"));
		tm.addTexture("stain", new Texture("tp/expo_stain.png"));
		tm.addTexture("highlight", new Texture(1, 1,
				new java.awt.Color(255, 255, 0)));
	}

	boolean leftPressed = false;
	boolean rightPressed = false;
	boolean upPressed = false;
	boolean downPressed = false;
	void getCommands() {
		KeyState ks = null;
		while((ks = keyMapper.poll()) != KeyState.NONE) {
			if(ks.getKeyCode() == KeyEvent.VK_UP)
				upPressed = ks.getState();
			if(ks.getKeyCode() == KeyEvent.VK_LEFT)
				leftPressed = ks.getState();
			if(ks.getKeyCode() == KeyEvent.VK_DOWN)
				downPressed = ks.getState();
			if(ks.getKeyCode() == KeyEvent.VK_RIGHT)
				rightPressed = ks.getState();
		}
	}
	
	void highlight() {
		if(highlighted != null) {
			highlighted.setTexture("highlight");
		}
	}
	
	void unhighlight() {
		if(highlighted != null) {
			highlighted.setTexture("stones");
		}
	}
	
	void rayCast() {
		int x = mouseMapper.getMouseX();
		int y = mouseMapper.getMouseY();
		SimpleVector dr = Interact2D.reproject2D3D(world.getCamera(), buffer, x, y);
		int id = Interact2D.getObjectID(Interact2D.pickPolygon(world.getVisibilityList(), dr, Interact2D.EXCLUDE_NOT_SELECTABLE));
		unhighlight();
		highlighted = world.getObject(id);
		if(mouseMapper.buttonDown(1)) {
			lastClicked = highlighted;
		}
		highlight();
	}
	
	void processMouse() {
		rayCast();
	}
	
	SimpleVector getClicked() {
		if(lastClicked != null) {
			return lastClicked.getTransformedCenter();
		} else {
			return null;
		}
	}
	
	void shutdown() {
		buffer.disableRenderer(IRenderer.RENDERER_OPENGL);
		buffer.dispose();
	}
	
	private static class MouseMapper {

		private boolean hidden = false;

		private int height = 0;

		public MouseMapper(FrameBuffer buffer) {
			height = buffer.getOutputHeight();
			init();
		}

		public void hide() {
			if (!hidden) {
				Mouse.setGrabbed(true);
				hidden = true;
			}
		}

		public void show() {
			if (hidden) {
				Mouse.setGrabbed(false);
				hidden = false;
			}
		}

		public boolean isVisible() {
			return !hidden;
		}

		public void destroy() {
			show();
			if (Mouse.isCreated()) {
				Mouse.destroy();
			}
		}

		public boolean buttonDown(int button) {
			return Mouse.isButtonDown(button);
		}

		public int getMouseX() {
			return Mouse.getX();
		}

		public int getMouseY() {
			return height - Mouse.getY();
		}

		public int getDeltaX() {
			if (Mouse.isGrabbed()) {
				return Mouse.getDX();
			} else {
				return 0;
			}
		}

		public int getDeltaY() {
			if (Mouse.isGrabbed()) {
				return Mouse.getDY();
			} else {
				return 0;
			}
		}

		private void init() {
			try {
				if (!Mouse.isCreated()) {
					Mouse.create();
				}

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
