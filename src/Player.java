import com.threed.jpct.SimpleVector;

public class Player {
	SimpleVector loc;
	SimpleVector dest;
	int id;
	
	Player(float startX, float startY, float startZ) {
		loc = new SimpleVector(startX, startY, startZ);
		dest = new SimpleVector(loc);
	}
	
	void setDest(SimpleVector v) {
		dest.set(v.x, dest.y, v.z);
	}
	
	void move(float dx, float dy, float dz) {
		loc.x += dx;
		loc.y += dy;
		loc.z += dz;
	}
	
	SimpleVector move() {
		SimpleVector dis = new SimpleVector(dest);
		dis.sub(loc);
		if(dis.length() > 0.05) {
			dis.scalarMul(1/dis.length());
			dis.scalarMul(0.05f);
			move(dis.x, dis.y, dis.z);
		} else {
			dis.set(0, 0, 0);
		}
		return dis;
	}
}
