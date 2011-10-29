import com.threed.jpct.*;
import com.threed.jpct.util.*;
import java.awt.event.*;

public class UI {
	FrameBuffer buffer;
	KeyMapper keyMapper;
	World world;
	
	UI(World w) {
		buffer = new FrameBuffer(800, 600, FrameBuffer.SAMPLINGMODE_NORMAL);
		buffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
		buffer.enableRenderer(IRenderer.RENDERER_OPENGL);
		
		keyMapper = new KeyMapper();
		
		world = w;
	}
	
	void draw() {
		buffer.clear(java.awt.Color.BLUE);
		world.renderScene(buffer);
		world.draw(buffer);
		buffer.update();
		buffer.displayGLOnly();
	}
	
	boolean[] getCommands() {
		KeyState ks = null;
		boolean[] commands = new boolean[4];
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
	
	void shutdown() {
		buffer.disableRenderer(IRenderer.RENDERER_OPENGL);
		buffer.dispose();
	}
}
