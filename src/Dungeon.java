import com.threed.jpct.*;

public class Dungeon {
	byte[][][] block_type;
	OcTree doodad_list;
	int xSize = 256;
	int ySize = 256;
	int zSize = 32;
	
	Dungeon() {
		block_type = new byte[xSize][ySize][zSize];
		make_floor();
	}
	
	void make_floor() {
		for(int i = 0; i < 16; i++) {
			for(int j = 0; j < 16; j++) {
				block_type[i][3][j] = 1;
			}
		}
	}
}
