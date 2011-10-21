import com.threed.jpct.*;

public class Dungeon {
	World world;
	byte[][][] block_type;
	OcTree doodad_list;
	
	Dungeon() {
		block_type = new byte[32][256][256];
		world = new World();
		world.setAmbientLight(0,  255, 0);
	}
}
