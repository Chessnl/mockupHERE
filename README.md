# mockupHERE

All the newest code resides in /HereSimulation/. The directory /MockupHERE/ is an old directory. Therefore always use /HereSimulation/.

The program is run from /HereSimulation/src/main/java/COMSETsystem/Main.java.

MAPS:
To use a map, it first has to be created. This is a one-time process. Once a map is created it can be used repeatedly. 
To create a map, the program takes an OpenStreetMap map in the form of a JSON file as input. To get this map, visit https://overpass-turbo.eu/. 
On that page, mark the desired bounding box by clicking the "manually select bbox"on the left had side of the map window. Once the bbox is set, click on the "wizard" button at the top. This will show a pop-up. In the textbox at the top of the pop-up, copy and paste the text from /HereSimulation/query.txt. Then click "build and run query".
Now you should see a bunch of blue lines and red dots appear in the map on the right. Now click "export" at the top and click "download as raw OSM data." Now save the file in the folder /HereSimulation/maps/ and give it a straightforward name such as "SanFrancisco.json".

Once the OpenStreetMaps json file is in /HereSimulation/maps, go to /HereSimulation/src/main/java/COMSETsystem/Main.java. In there in the Main() method, type createMap("[name of file]"), for example createMap("SanFrancisco"). Be sure not to write createMap("SanFrancisco.json").

Running this will process the map and grid and will output these to  the maps /HereSimulation/maps/SanFrancisco_map.json and /HereSimulation/maps/SanFrancisco_grid.json respectively. The SanFrancisco_map file is a simplified version of the original OpenStreetMaps file, in which only intersections connecting multiple street and the streets themselves remain. It also clumps together intersections which are close together into a single intersection. This is done in order to minimize the running time.
The grid is bounding box around the map in which every point is mapped to the nearest intersection. This is done in order to map a given random point (resource) to an appropriate intersection. These grid files can be rather large in size. The grid file for SanFrancisco takes about 125MB and takes about 15 seconds to read into the program.

Once these map and grid files are generated, they can be used by writing Initializer("[name of original json file]") in the Main() function of the Main class. For example Initializer("SanFrancisco"). Be sure NOT to add the extension .json or to add the affix _map or _grid, only the name of the original json file. 
This will load the appropriate map and grid into the program. If the appropriate map and grid files have never been created before, then the program will crash.

Finally, if you want to verify the map that is created, run MapVisualizer(map, true) to check the map with the voronoi diagram underneath the map or MapVisualizer(map, false) is you don't want the voronoi diagram. 
Outputting the map takes quite a while because it's horribly unoptimized. Therefore it can only be used to check the map offline, not to track the progress of the taxis during a simulation.
