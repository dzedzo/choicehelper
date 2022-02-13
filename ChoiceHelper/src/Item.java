import javax.swing.*;
import java.io.*;
import java.nio.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Item {
	private String name = "";
	private File image = null;
	
	public Item(String name)
	{
		this.name = name;
	}
	public Item() {};
	
	public void setName(String name) {this.name = name;}
	public String getName() {return name;}
	public File getImage() {return image;}
	public void setImage(String x) {image = new File(x);}
}
