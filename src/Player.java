
public class Player {
	float x, y, z;
	int id;
	
	Player(float startX, float startY, float startZ) {
		x = startX;
		y = startY;
		z = startZ;
	}
	
	void move(float dx, float dy, float dz) {
		x += dx;
		y += dy;
		z += dz;
	}
}
