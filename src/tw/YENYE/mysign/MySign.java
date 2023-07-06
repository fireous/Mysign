package tw.YENYE.mysign;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import tw.YENYE.tools.MyClock;
import tw.YENYE.tools.MyDrawe;

public class MySign extends JFrame{
	private MyDrawe myDrawer;
	private JButton clear, undo, redo, color;
	private JButton save, load, saveJPG;
	private MyClock myClock;// 後加時鐘
	
	public  MySign() {
		super("簽名");
		
		myDrawer = new MyDrawe();
		setLayout(new BorderLayout());
		add(myDrawer,BorderLayout.CENTER);
		
		clear = new JButton("Clear");
		undo = new JButton("Undo");
		redo = new JButton("Rredo");
		color = new JButton("Color");
		save = new JButton("Save");
		load = new JButton("Load");
		saveJPG = new JButton("SaveJPG");
				
		myClock = new MyClock();
		
		JPanel top = new JPanel(new FlowLayout());
		top.add(clear);top.add(undo);top.add(redo);top.add(color);
		top.add(save);top.add(load);top.add(saveJPG);
		
		top.add(myClock);
		
		add(top, BorderLayout.NORTH);
		
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myDrawer.clear();
			}
		});

		undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myDrawer.undo();
			}
		});
		
		redo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myDrawer.redo();
			}
		});
		
		color.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeColor();
			}
		});

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveObject();
			}
		});
		
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadObject();
			}
		});
		saveJPG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveJPG();
			}
		});
		
		setSize(800,640);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void saveJPG() {
		JFileChooser jfc = new JFileChooser(new File("dir1"));
		if(jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			myDrawer.saveImage(file.getAbsolutePath(), "jpeg");
		}	
	}
	
	
	private void saveObject() {
		JFileChooser jfc = new JFileChooser(new File("dir1"));
		if(jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			myDrawer.saveLines(file.getAbsolutePath());
		}	
	}
	
	private void loadObject() {
		JFileChooser jfc = new JFileChooser(new File("dir1"));
		if(jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			myDrawer.loadLines(file.getAbsolutePath());
		}		
	}
	
	private void changeColor() {
		Color newColor = JColorChooser.showDialog(
				null, "Change Color", myDrawer.getColor());
		if(newColor != null) {
			myDrawer.setColor(newColor);
		}
	}
	
	public static void main(String[] args) {
		new MySign();
	}
}