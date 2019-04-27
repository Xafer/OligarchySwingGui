package com.immatricious.oligarchy2.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

import com.immatricious.macromanager.Application;
import com.immatricious.macromanager.environment.HashedLocation;
import com.immatricious.macromanager.environment.MapTile;
import com.immatricious.macromanager.environment.MapTileManager;
import com.immatricious.macromanager.environment.MapTileReader;
import com.immatricious.macromanager.environment.PathPoint;
import com.immatricious.macromanager.event.MMEventDataMap;
import com.immatricious.macromanager.event.MMEvent;
import com.immatricious.macromanager.event.MMEventListener;
import com.immatricious.macromanager.event.MMEventType;
import com.immatricious.macromanager.net.ThreadedConnection;
import com.immatricious.macromanager.task.TaskHandler;
import com.immatricious.macromanager.character.Character;
import com.immatricious.oligarchy2.OligarchyApplication;

@SuppressWarnings("serial")
public class MapPanel extends JPanel{
	private BufferedImage map;
	private BufferedImage path;
	
	private BufferedImage plIcon;
	private BufferedImage paIcon;
	private BufferedImage hlIcon;
	
	private int[] offset;
	private int scale;
	
	private List<Character> displayedCharacters;
	private List<HashedLocation> displayedLocations;
	private List<PathPoint> displayedPaths;
	
	public MapPanel()
	{
		String[] paths = new String[] {	"assets/ui/playericon.png",
										"assets/ui/pathicon.png",
										"assets/ui/hashloc.png"};
		int i = 0;
		for(String path : paths)
		{
			BufferedImage img = null;
			try {                
				//map = ImageIO.read(new File("assets/map/map.png"));
				img = ImageIO.read(new File(path));
				//path = new BufferedImage(map.getWidth(), map.getHeight(), BufferedImage.TYPE_INT_ARGB);
			} 	catch (IOException ex) {
				System.out.println("Could not load path " + path);
			}
			switch(i){
			case 0: plIcon = img; break;
			case 1: paIcon = img; break;
			case 2: hlIcon = img; break;
			}
			i++;
		}
		
		offset = new int[]{0,0};
		scale = 1;
		displayedCharacters = new ArrayList<Character>();
		displayedLocations = new ArrayList<HashedLocation>();
		displayedPaths = new ArrayList<PathPoint>();
		
		this.repaint();
		
		initListeners();
	}
	
	public void resize()
	{
		 
		offset[0] = this.getWidth()/2;
		offset[1] = this.getHeight()/2;
		drawMap();
	}
	
	public void drawMap()
	{
		
		map = new BufferedImage(this.getWidth(),this.getHeight(),BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) map.getGraphics();
		MapTileManager mm = OligarchyApplication.getCurrentApplication().getTaskHandler().getMapManager();
		List<MapTile> mapTiles = mm.getAvailableMapTiles();
		for(MapTile mt : mapTiles)
		{
			g.drawImage(mt.getImage(),offset[0] + mt.getx()*MapTileReader.MAPTILESIZE,offset[1] + mt.gety()*MapTileReader.MAPTILESIZE, null);
		}
	}
	
	 @Override
	 protected void paintComponent(Graphics g)
	 {
		 super.paintComponent(g);
		 
		 int x = this.getWidth();
		 int y = this.getHeight();
		 
		 BufferedImage render = new BufferedImage(x,y,BufferedImage.TYPE_INT_RGB);
		 Graphics2D gr = render.createGraphics();
		 
		 //drawMap();
		 
		 //System.out.println("Repainting this bitch");
		 
		 gr.drawImage(map, 0, 0, this);       
		 
		 List<ThreadedConnection> tc = OligarchyApplication.getCurrentApplication().getTaskHandler().getConnectionManager().getConnections();
		 
		 for(ThreadedConnection t : tc)
		 {
			 Character c = t.getCharacter();
			 
			 if(t.getCharacter() == null)
				 continue;
			 
			 double[] pos = c.getPos();
			 //g.setColor(Color.red);
			 //g.fillRect(offset[0]+(int)(pos[0]/11), offset[1]+(int)(pos[1]/11), 5, 5);
			 gr.drawImage(plIcon, offset[0]+(int)(pos[0]/11)-4, offset[1]+(int)(pos[1]/11)-4, null);
			 //g.drawImage(plIcon, x, y, this)
		 }
		 
		 for(HashedLocation hl : displayedLocations)
		 {
			 gr.drawImage(hlIcon, offset[0]+new Double(hl.getX()).intValue(), offset[1]+new Double(hl.getY()).intValue(), null);
		 }
		 
		 for(PathPoint pp : displayedPaths)
		 {
			 gr.drawImage(paIcon, offset[0]+new Double(pp.getX()).intValue()-3, offset[1]+new Double(pp.getY()).intValue()-3, null);
		 }
		 
		 BufferedImage scaledRender = new BufferedImage(x*scale,y*scale,BufferedImage.TYPE_INT_RGB);
		 AffineTransform at = new AffineTransform();
		 at.scale(scale,scale);
		 AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		 scaledRender = scaleOp.filter(render, scaledRender);
		 
		 double factor= (scale-1)/2.0;
		 int[] scaleOffset = new int[] {new Double(-factor*x).intValue(), new Double(-factor*y).intValue()};
		 
		 g.drawImage(scaledRender, scaleOffset[0], scaleOffset[1], this);   
	}
	 
	public void setOffset(int x, int y) { this.offset = new int[] {x,y}; repaint();}
	public int[] getOffset() { return new int[]{offset[0],offset[1]}; }
	public void setScale(int s) { this.scale = s; repaint();}
	public int getScale() { return this.scale; }
	
	private void initListeners()
	{
		//Java
		this.addComponentListener(new ComponentListener(){

			@Override
			public void componentHidden(ComponentEvent arg0) {}
			@Override
			public void componentMoved(ComponentEvent arg0) {}
			@Override
			public void componentShown(ComponentEvent arg0) {}

			@Override
			public void componentResized(ComponentEvent arg0) {resize();}
			});
		//Hafen Macro manager events
		TaskHandler.getCurrentDispatcher().addEventListener(MMEventType.PATH_ADDED, new MMEventListener(){

			@Override
			public void act(MMEvent event) {
				PathPoint pp = (PathPoint)((MMEventDataMap)event.getEventData()).getData("point").value;
				
				if(!displayedPaths.contains(pp))
					displayedPaths.add(pp);
				
				repaint();
			}

			@Override
			public MMEventType getEventType() { return MMEventType.PATH_ADDED; }
			
		});
		
		TaskHandler.getCurrentDispatcher().addEventListener(MMEventType.HASHLOC_ADDED, new MMEventListener(){

			@Override
			public void act(MMEvent event) {
				HashedLocation hl = (HashedLocation)((MMEventDataMap)event.getEventData()).getData("hashedLocation").value;
				
				if(!displayedLocations.contains(hl))
					displayedLocations.add(hl);
				
				repaint();
			}

			@Override
			public MMEventType getEventType() { return MMEventType.HASHLOC_ADDED; }
			
		});
		
		TaskHandler.getCurrentDispatcher().addEventListener(MMEventType.MAP_LOADED, new MMEventListener(){

			@Override
			public void act(MMEvent event) {
				drawMap();
				repaint();
			}

			@Override
			public MMEventType getEventType() { return MMEventType.MAP_LOADED; }
			
		});
		
		TaskHandler.getCurrentDispatcher().addEventListener(MMEventType.RCV_UPDATE_CHAR, new MMEventListener(){

			@Override
			public void act(MMEvent event) {
				ThreadedConnection tc = (ThreadedConnection)((MMEventDataMap)event.getEventData()).getData("connection").value;
				System.out.println(tc == null);
				if(	tc.getCharacter() != null &&
					!displayedCharacters.contains(tc.getCharacter()))
					displayedCharacters.add(tc.getCharacter());
				repaint();
				/*JList<String> hashList = (JList<String>) components.get("hashList");
				HashedLocation hl = (HashedLocation)((MMEventDataMap)event.getEventData()).getData("hashedLocation").value;
				DefaultListModel<String> hashModel = ((DefaultListModel<String>)hashList.getModel());
				hashModel.addElement(""+hl.getHash()+": " + hl.getX() + "," + hl.getY());*/
			}

			@Override
			public MMEventType getEventType() { return MMEventType.CHARACTER_UPDATED; }
			
		});
	}
}
