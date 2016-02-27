import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;

public class TextEditor extends JFrame
{
	//Area to type in
	private JTextArea area = new JTextArea(20,120);
	
	//Allows the user to select files
	private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir"));
	
	//Name of the file being edited
	private String currentFile = "Untitled";
	
	//Whether the file has been changed or not
	boolean changed = false;
	
	public TextEditor()
	{
		//Sets the font, type, and size by default upon startup
		area.setFont(new Font("Monospaced", Font.PLAIN, 12));
		
		//Allow the area to be scrolled
		JScrollPane scroll = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		add(scroll, BorderLayout.CENTER);
		
		//Menu Bar
		JMenuBar jmb = new JMenuBar();
		
		setJMenuBar(jmb);
		
		//Menu Options
		JMenu file = new JMenu("File");
		JMenu edit = new JMenu("Edit");
		jmb.add(file);
		jmb.add(edit);
		
		//Add to the File Option
		file.add(New);
		file.add(Open);
		file.add(Save);
		file.add(SaveAs);
		file.add(Quit);
		file.addSeparator();
		
		//Loop through the file items and set their icons to null
		for(int i = 0; i < file.getItemCount() - 1; i++)
		{
			file.getItem(i).setIcon(null);
		}
		
		//Add to the Edit Option
		edit.add(Cut);
		edit.add(Copy);
		edit.add(Paste);
		
		edit.getItem(0).setText("Cut");
		edit.getItem(1).setText("Copy");
		edit.getItem(2).setText("Paste");
		
		
		//Toolbar Setup
		JToolBar toolbar = new JToolBar();
		add(toolbar, BorderLayout.NORTH);
		toolbar.add(New);
		toolbar.add(Open);
		toolbar.add(Save);
		toolbar.addSeparator();
		
		//Create the buttons and add them to the toolbar
		JButton cut = toolbar.add(Cut), 
				copy = toolbar.add(Copy), 
				paste = toolbar.add(Paste);
		
		cut.setText("Cut");
		copy.setText("Copy");
		paste.setText("Paste");
		
		Save.setEnabled(true);
		SaveAs.setEnabled(true);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		
		area.addKeyListener(k1);
		setTitle(currentFile);
		setVisible(true);
		
			
	}
	
	//Sets the file as changed and lets user saved if they press a key
	private KeyListener k1 = new KeyAdapter()
	{
		public void keyPressed(KeyEvent e)
		{
			changed = true;
			Save.setEnabled(true);
			SaveAs.setEnabled(true);
		}
	};
	
	//New Action
	Action New = new AbstractAction("New", new ImageIcon("images/new.png"))
	{
		public void actionPerformed(ActionEvent e)
		{
			//Save the old file and create a new oen
			saveOld();
			area.setText("");
			currentFile = "Untitled";
			setTitle(currentFile);
			changed = false;
			Save.setEnabled(false);
			SaveAs.setEnabled(false);
		}
	};
	
	//Open Action
	Action Open = new AbstractAction("Open", new ImageIcon("images/open.png"))
	{
		public void actionPerformed(ActionEvent e)
		{
			//Save the old file
			saveOld();
			
			//Select the file and open it
			if(dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
			{
				readInFile(dialog.getSelectedFile().getAbsolutePath());
			}
			SaveAs.setEnabled(true);
		}
	};
	
	//Save Action
	Action Save = new AbstractAction("Save", new ImageIcon("images/save.jpg"))
	{
		public void actionPerformed(ActionEvent e)
		{
			if(!currentFile.equals("Untitled"))
			{
				saveFile(currentFile);
			}
			else
			{
				saveFileAs();
			}
		}
	};
	
	//SaveAs Action
	Action SaveAs = new AbstractAction("Save As", new ImageIcon("images/saveAs.png"))
	{
		public void actionPerformed(ActionEvent e)
		{
			saveFileAs();
		}
	};
	
	Action Quit = new AbstractAction("Quit")
	{
		public void actionPerformed(ActionEvent e)
		{
			saveOld();
			System.exit(0);
		}
	};
	
	//Map of actions
	ActionMap m = area.getActionMap();
	Action Cut = m.get(DefaultEditorKit.cutAction);
	Action Copy = m.get(DefaultEditorKit.copyAction);
	Action Paste = m.get(DefaultEditorKit.pasteAction);
	
	//Lets the user save their file and give it a name
	private void saveFileAs()
	{
		if(dialog.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			saveFile(dialog.getSelectedFile().getAbsolutePath());
		}
	}
	
	//Saves the file after the user confirms they would like to save it
	private void saveOld()
	{
		if(changed)
		{
			//Show a confirm box that the user clicks to save the file
			if(JOptionPane.showConfirmDialog(this, "Would you like to save " + currentFile + " ?", "Save", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
			{
				saveFile(currentFile);
			}
		}
	}
	
	//Opens a file in the editor
	private void readInFile(String fileName)
	{
		try
		{
			//Tries to find the file and open it in the editor
			FileReader r = new FileReader(fileName);
			area.read(r, null);
			r.close();
			currentFile = fileName;
			setTitle(currentFile);
			changed = false;
		}
		catch(Exception e)
		{
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this,  "This editor cannot find the file called " + fileName);
		}
	}
	
	//Saves the file
	private void saveFile(String fileName)
	{
		//Tries to save the file
		try
		{
			//Create the file writer and write the file
			FileWriter w = new FileWriter(fileName);
			area.write(w);
			w.close();
			currentFile = fileName;
			setTitle(currentFile);
			changed = false;
			Save.setEnabled(false);
		}
		catch(Exception e)
		{
			
		}
	}
}