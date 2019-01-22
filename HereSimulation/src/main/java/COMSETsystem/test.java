package COMSETsystem;

import java.io.IOException;

public class test {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
            try {
                Main main = new Main();
                main.create();
            } catch (Exception e) {
                e.printStackTrace();
            }
//		COMSETsystem.Intersection[][] map = new COMSETsystem.Intersection[3][3];
//		
//		int index = 0;
//		for (int x = 0; x < 3; x++) {
//			for (int y = 0; y < 3; y++) {
//				map[x][y] = new COMSETsystem.Intersection(x, y, index++);
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
//		List<COMSETsystem.Intersection> intersections = new ArrayList<COMSETsystem.Intersection>();
//		for (COMSETsystem.Intersection[] I : map) for (COMSETsystem.Intersection i : I) {
//			i.fixStructure(); intersections.add(i);
//		}		
//		
//		ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("test1.COMSETsystem.test"));
//		objectOutputStream.writeObject(intersections);
//		objectOutputStream.close();
		
/*
		ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("test1.COMSETsystem.test"));
		List<COMSETsystem.Intersection> intersections2 = (List<COMSETsystem.Intersection>) objectInputStream.readObject();
		objectInputStream.close();
		
		for (COMSETsystem.Intersection i : intersections2) {
			System.out.print("[" + i.longitude + "][" + i.latitude + "] - ");
			for (COMSETsystem.Intersection i2 : i.getAdjacent()) {
				System.out.print("[" + i2.longitude + "][" + i2.latitude + "] ");
			}
			System.out.println();
		}
*/	
    }
}
