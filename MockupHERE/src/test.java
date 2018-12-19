import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class test {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
	    Main main = new Main();
//		Intersection[][] map = new Intersection[3][3];
//		
//		int index = 0;
//		for (int x = 0; x < 3; x++) {
//			for (int y = 0; y < 3; y++) {
//				map[x][y] = new Intersection(x, y, index++);
//			}
//		}
//		map[0][0].addEdge(map[0][1], 1, 1);
//		map[0][0].addEdge(map[1][0], 1, 1);
//		map[0][1].addEdge(map[0][2], 1, 1);
//		map[0][1].addEdge(map[1][1], 1, 1);
//		map[1][0].addEdge(map[1][1], 1, 1);
//		map[1][0].addEdge(map[2][0], 1, 1);
//		map[1][1].addEdge(map[1][2], 1, 1);
//		map[1][1].addEdge(map[2][1], 1, 1);
//		map[0][2].addEdge(map[1][2], 1, 1);
//		map[1][2].addEdge(map[2][2], 1, 1);
//		map[2][0].addEdge(map[2][1], 1, 1);
//		map[2][1].addEdge(map[2][2], 1, 1);
//		map[2][2].addEdge(map[0][0], 10, 10);
//		List<Intersection> intersections = new ArrayList<Intersection>();
//		for (Intersection[] I : map) for (Intersection i : I) {
//			i.fixStructure(); intersections.add(i);
//		}		
//		
//		ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("test1.test"));
//		objectOutputStream.writeObject(intersections);
//		objectOutputStream.close();
		
/*
		ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("test1.test"));
		List<Intersection> intersections2 = (List<Intersection>) objectInputStream.readObject();
		objectInputStream.close();
		
		for (Intersection i : intersections2) {
			System.out.print("[" + i.longitude + "][" + i.latitude + "] - ");
			for (Intersection i2 : i.getAdjacent()) {
				System.out.print("[" + i2.longitude + "][" + i2.latitude + "] ");
			}
			System.out.println();
		}
*/	
    }
}
