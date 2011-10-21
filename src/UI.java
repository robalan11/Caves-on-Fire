import com.threed.jpct.*;

public class UI {
	FrameBuffer buffer;
	
	UI() {
		buffer = new FrameBuffer(800, 600, FrameBuffer.SAMPLINGMODE_NORMAL);
		buffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
		buffer.enableRenderer(IRenderer.RENDERER_OPENGL);
	}
	
	void draw(Dungeon dungeon) {
		buffer.clear(java.awt.Color.BLUE);
		dungeon.world.renderScene(buffer);
		dungeon.world.draw(buffer);
		buffer.update();
		buffer.displayGLOnly();
	}
	
	void shutdown() {
		buffer.disableRenderer(IRenderer.RENDERER_OPENGL);
		buffer.dispose();
	}
}
