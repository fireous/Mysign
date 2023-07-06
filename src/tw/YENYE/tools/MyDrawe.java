package tw.YENYE.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MyDrawe extends JPanel{
	private LinkedList<LineV2> lines, recycleBin;
	private Color nowColor;
	
	public MyDrawe() {
		setBackground(Color.WHITE);
		
		nowColor = Color.BLUE;
		lines = new LinkedList<>();
		recycleBin = new LinkedList<>();
		
		MyListener myListener = new MyListener();
		addMouseListener(myListener);
		addMouseMotionListener(myListener);
	}
	
	public Color getColor() {
		return nowColor;
	}
	
	public void setColor(Color color) {
		nowColor = color;
	}
	
	public void clear() {
		recycleBin.clear();
		while(lines.size()>0) {
			recycleBin.add(lines.removeFirst());
		}
		lines.clear();
		repaint();
	}

	public void undo() {
		if(lines.size() > 0) {
			recycleBin.add(lines.removeLast());		
		}else {
			for(LineV2 line : recycleBin) {
				lines.add(line);
			}			
		}
		repaint();	
	}
	
	public void redo() {
		if(recycleBin.size()>0) {
			lines.add(recycleBin.removeLast());
			repaint();
		}	
	}

	public boolean saveLines(String fname){// throws Exception 
		try (
			FileOutputStream fin = new FileOutputStream(fname);
			ObjectOutputStream oout = new ObjectOutputStream(fin);
		){
			oout.writeObject(lines);
			oout.flush();
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	public boolean loadLines(String fname){// throws Exception 
		try (ObjectInputStream oin = new ObjectInputStream(new FileInputStream(fname));){
			lines = (LinkedList<LineV2>)oin.readObject();
			repaint();
			recycleBin.clear();
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
		
		for(LineV2 line : lines) {
			float tempR = 0f;
			float width = 3f;
			g2d.setColor(line.getColor());
			for(int j=1; j<line.size(); j++) {
				Point p0 = line.getPoint(j-1);
				Point p1 = line.getPoint(j);
				double length = Math.sqrt( Math.pow((p1.x - p0.x), 2) + Math.pow((p1.y - p0.y), 2));
				
				float rate = (float)length/(p1.time - p0.time);

				if(j == line.size()*9/10) {
					tempR = width/(line.size()/10);
				}
				
				if(j >= line.size()*9/10) {
					width -= tempR;
				}else{
					width = rate < tempR? width*1.2f : width/1.2f;
					tempR = rate*1.1f;
				}
				
				width = width>0 ? (width>8? 8 : width) : 0.0f;
								
				g2d.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));

				g2d.drawLine(p0.x, p0.y, p1.x, p1.y);
			}			
		}
	}
	
	private class MyListener extends MouseAdapter{
		LineV2 currLine = new LineV2(nowColor);
		public void mousePressed(MouseEvent e) {
			recycleBin.clear();
			LineV2 line = new LineV2(nowColor);
			currLine = line;
			lines.add(line);
			
			Point point = new Point();
			point.x = e.getX();
			point.y = e.getY();
			point.time = System.currentTimeMillis();
			line.addPoint(point);
			repaint();
		}
		
		public void mouseDragged(MouseEvent e) {
			Point point = new Point();
			point.x = e.getX();
			point.y = e.getY();
			point.time = System.currentTimeMillis();
			currLine.addPoint(point);
			repaint();
		}
	}
	
	public void saveImage(String fname,String type) {
		BufferedImage image = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
		paint(g2);
		if(fname.indexOf(".") == -1) {
			fname = fname+"."+type;
		}
		try{
			ImageIO.write(image, type, new File(fname));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class LineV2 implements Serializable{
		private Color color;
		private LinkedList<Point> points;
		
		LineV2(Color color) {
			this.color = color;
			points = new LinkedList<>();
		}
	
	
		public Color getColor() {
			return color;
		}
	
		public void setColor(Color color) {
			this.color = color;
		}
	
		public LinkedList<Point> getPoints() {
			return points;
		}
	
		public void setPoints(LinkedList<Point> points) {
			this.points = points;
		}
		
		public void addPoint(Point point) {
			points.add(point);
		}
		
		public Point getPoint(int index) {
			return points.get(index);
		}
			
		public int size() {
			return points.size();
		}
	}
	
	class Point implements Serializable{
		public int x, y;
		public long time;
	}
}


