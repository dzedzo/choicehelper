import javax.swing.*;
import java.io.*;
import java.nio.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class List {
	private String name;
	public Item[] items;
	private int itemNum;
	
	public void setName(String name) {this.name = name;}
	public String getName() {return name;}
	
	public void setItemNum(int itemNum) {this.itemNum = itemNum;}
	public int getItemNum() {return itemNum;}
}
