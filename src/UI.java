import com.threed.jpct.*;
import com.threed.jpct.util.*;
import java.awt.event.*;
import org.lwjgl.input.*;

public class UI {
	World world;
	FrameBuffer buffer;
	
	World colorWorld;
	FrameBuffer colorBuffer;
	
	KeyMapper keyMapper;
	MouseMapper mouseMapper;
	SimpleVector lastClicked;
	
	int playerID;
	
	UI() {
		world = new World();
		world.setAmbientLight(128,  128, 128);
		world.getCamera().setPosition(5, -5, -5);
		world.getCamera().lookAt(new SimpleVector(0.0f, 0.0f, 0.0f));
		
		buffer = new FrameBuffer(800, 600, FrameBuffer.SAMPLINGMODE_NORMAL);
		buffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
		buffer.enableRenderer(IRenderer.RENDERER_OPENGL);
		
		colorWorld = new World();
		colorWorld.setAmbientLight(255, 255, 255);
		colorWorld.getCamera().setPosition(5, -5, -5);
		colorWorld.getCamera().lookAt(new SimpleVector(0.0f, 0.0f, 0.0f));
		
		colorBuffer = new FrameBuffer(800, 600, FrameBuffer.SAMPLINGMODE_NORMAL);
		//colorBuffer.disableRenderer(IRenderer.RENDERER_SOFTWARE); //debug
		//colorBuffer.enableRenderer(IRenderer.RENDERER_OPENGL);    //debug
		
		loadTextures();
		
		keyMapper = new KeyMapper();
		mouseMapper = new MouseMapper(buffer);
		lastClicked = new SimpleVector();
	}
	
	void draw() {
		buffer.clear(java.awt.Color.BLUE);
		world.renderScene(buffer);
		world.draw(buffer);
		buffer.update();
		buffer.displayGLOnly();
		
		colorBuffer.clear(java.awt.Color.WHITE);
		colorWorld.renderScene(colorBuffer);
		colorWorld.draw(colorBuffer);
		colorBuffer.update();
		//colorBuffer.displayGLOnly(); //debug
	}
	
	void addPlayerObject(float x, float y, float z) {
		Object3D t = Primitives.getCone(0.5f);
		t.setTexture("stain");
		t.calcTextureWrapSpherical();
		t.recreateTextureCoords();
		t.build();
		t.translate(x, y, z);
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
		int c = world.addObject(t);
		
		Object3D t2 = Primitives.getCube(0.5f);
		TextureManager.getInstance().addTexture(String.valueOf(c),
				new Texture(1, 1, new java.awt.Color(c)));
		t2.setTexture(String.valueOf(c));
		t2.build();
		t2.rotateY((float)(Math.PI/4));
		t2.translate(x, y, z);
		colorWorld.addObject(t2);
		//t2.translate(x, y-1, z);
		//world.addObject(t2);
	}
	
	void updateCamera() {
		world.getCamera().setPositionToCenter(world.getObject(playerID));
		world.getCamera().moveCamera(Camera.CAMERA_MOVEOUT, 3);
		//world.getCamera().setPosition(player.x-1, player.y-2, player.z-1);
		world.getCamera().lookAt(
				world.getObject(playerID).getTransformedCenter());
		
		colorWorld.getCamera().setPositionToCenter(world.getObject(playerID));
		colorWorld.getCamera().moveCamera(Camera.CAMERA_MOVEOUT, 3);
		colorWorld.getCamera().lookAt(
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

	boolean[] commands = new boolean[4];
	boolean[] getCommands() {
		KeyState ks = null;
		while((ks = keyMapper.poll()) != KeyState.NONE) {
			if(ks.getKeyCode() == KeyEvent.VK_UP)
				commands[0] = ks.getState();
			if(ks.getKeyCode() == KeyEvent.VK_LEFT)
				commands[1] = ks.getState();
			if(ks.getKeyCode() == KeyEvent.VK_DOWN)
				commands[2] = ks.getState();
			if(ks.getKeyCode() == KeyEvent.VK_RIGHT)
				commands[3] = ks.getState();
		}
		return commands;
	}
	
	int highlighted = -1;
	Object3D obj = null;
	void highlightHovered() {
		//if(!mouseMapper.buttonDown(1)) return;
		int x = mouseMapper.getMouseX();
		int y = mouseMapper.getMouseY();
		
		int c = colorBuffer.getPixels()[x+buffer.getOutputWidth()*y];
		//int c = buffer.getPixels()[x+buffer.getOutputWidth()*y];
		c &= 0x00FFFFFF;
		//System.out.printf("%x\n", c);
		if(highlighted >= 0) {
			unhighlight(highlighted);
		}
		if(c >= 0 && c != 0xffffff) {
			highlight(c+1);
		}
	}
	
	void highlight(int c) {
		Object3D o = world.getObject(c);
		if(o != null) {
			//o.rotateZ((float)(Math.PI/2));
			//o.scale(2.0f);
			o.setTexture("highlight");
			highlighted = c;
			obj = o;
		} else {
			highlighted = -1;
			obj = null;
		}
	}
	
	void unhighlight(int c) {
		Object3D o = world.getObject(c);
		//o.rotateZ(-(float)(Math.PI/2));
		//o.scale(0.5f);
		o.setTexture("stones");
	}
	
	SimpleVector getClick() {
		if(mouseMapper.buttonDown(1)) {
			if(obj != null) {
				lastClicked = obj.getTransformedCenter();
			}
		}
		return lastClicked;
	}
	
	void shutdown() {
		buffer.disableRenderer(IRenderer.RENDERER_OPENGL);
		buffer.dispose();
		
		colorBuffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
		colorBuffer.dispose();
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
