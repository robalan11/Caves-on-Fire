import com.threed.jpct.*;

public class Dungeon {
	World world;
	byte[][][] block_type;
	OcTree doodad_list;
	
	Dungeon(World w) {
		block_type = new byte[256][256][32];
		make_floor();
		world = w;
		
		render();
	}
	
	void make_floor() {
		for(int i = 0; i < 16; i++) {
			for(int j = 0; j < 16; j++) {
				block_type[i][3][j] = 1;
			}
		}
	}
	
	void render() {
		for(int i = 0; i < 256; i++) {
			for(int j = 0; j < 256; j++) {
				for(int k = 0; k < 32; k++) {
					if(block_type[i][j][k] > 0) {
						Object3D t = Primitives.getCube(0.5f);
						t.setTexture("stones");
						//t.setEnvmapped(Object3D.ENVMAP_ENABLED);
						t.calcTextureWrapSpherical();
						t.recreateTextureCoords();
						t.build();
						t.rotateY((float)(Math.PI/4));
						t.translate(i, j, k);
						world.addObject(t);
					}
				}
			}
		}
	}
}
