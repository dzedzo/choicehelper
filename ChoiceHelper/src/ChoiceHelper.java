import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.io.*;
import java.nio.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;

public class ChoiceHelper extends JFrame
{
	private Container cp = getContentPane();
	private int windowHeight = 500 + 39;
	private int windowWidth = 600 + 16;
	private JButton buttonNew = new JButton();
	private JButton buttonBack = new JButton();
	private JButton buttonConfirm = new JButton();
	//private JButton buttonDarkTheme = new JButton();
	private JTextField textFieldListName = new JTextField();
	private String savePath = System.getenv("APPDATA") + "\\ChoiceHelper\\";
	public ArrayList<List> megalist = new ArrayList<List>();
	public ArrayList<NewListPanel> tempList1 = new ArrayList<NewListPanel>();
	private JLabel labelList = new JLabel();
	private NewListPanel tempNewList = new NewListPanel("+");
	private int maxItemsAtOnce = 6;
	private int maxListsAtOnce = 6;
	private int scrollSpeedItem = 8;
	private int scrollSpeedList = 8;
	private JPanel newPanel = new JPanel();
	private JPanel newSubPanel1 = new JPanel(); //the top
	private JPanel newSubPanel2 = new JPanel();
	private JPanel newSubPanel3 = new JPanel(); //the bottom one
	private JPanel menuPanel = new JPanel();
	private JPanel menuSubPanel1 = new JPanel(); //The List one
	private JPanel menuSubPanel2 = new JPanel(); //The bottom one
	private JPanel listPanel = new JPanel();
	private JPanel listSubPanel1 = new JPanel(); //the list one
	private JPanel listSubPanel2 = new JPanel(); //the bottom one
	private JPanel sortPanel = new JPanel();
	private CardLayout card = new CardLayout(5,5);
	private JScrollPane scrollerList = new JScrollPane();
	private JScrollPane scrollerItem = new JScrollPane();
	private JScrollPane scrollerNew = new JScrollPane();
	private Boolean itemsSwapped = false;
	private Random random;
	
	/*private Boolean darkTheme = false;
	private Color colorSubPanelLight = new Color(238, 238, 238);
	private Color colorSubPanelDark = new Color(17, 17, 17);
	private Color colorListPanelLight = new Color(225, 225, 255);
	private Color colorListPanelDark = new Color(30, 30, 30);
	private Color colorTextFieldNewListLight = new Color(255, 255, 255);
	private Color colorTextFieldNewListDark = new Color(0, 0, 0);
	private Color colorTextAreaItemsLight = new Color(255, 255, 255);
	private Color colorTextAreaItemsDark = new Color(0, 0, 0);
	private Color colorFontLight = new Color(0, 0, 0);
	private Color colorFontDark = new Color(255, 255, 255);*/
	
	private class NewListPanel extends JPanel {
		private JPanel panel1 = new JPanel();
		private JButton button1 = new JButton();
		private JButton button2 = new JButton();
		private String chosenImage = "";
		private JTextField textField1 = new JTextField();
		private int listIndex = tempList1.size()-1;
		
		public NewListPanel(String i)
		{
			panel1.setPreferredSize(new Dimension(590, 55));
			panel1.setBorder(null);
			
			if(i == "+")
			{
				panel1.setLayout(new FlowLayout());
				
				button1.setPreferredSize(new Dimension(50, 50));
				button1.setFont(new Font("DIALOG", Font.PLAIN, 20));
				button1.setText("+");
				button1.requestFocusInWindow();
				button1.addActionListener(new ActionListener() {
			         @Override public void actionPerformed(ActionEvent evt) {
			        	 NewListPanel x = new NewListPanel("-");
			        	 tempList1.add(x);
			        	 tempList1.remove(tempNewList);
			        	 tempList1.add(tempNewList);
			        	 drawNewList();
			         }
				});
				panel1.add(button1);
			}
			else
			{
				panel1.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
				textField1.setPreferredSize(new Dimension(477, 5));
				textField1.setFont(new Font("DIALOG", Font.PLAIN, 20));
				if(tempList1.size() > maxItemsAtOnce-1)
					textField1.setPreferredSize(new Dimension(590-50*2-15, 50));//68 = 50 + 15(scroller width) + 3 da bi se video desni deo bordera kad je scroller
		        else
		        	textField1.setPreferredSize(new Dimension(590-50*2, 50));
				
				button1.setPreferredSize(new Dimension(50, 50));
				button1.setFont(new Font("DIALOG", Font.PLAIN, 14));
				button1.setText("|O|");
				button1.requestFocusInWindow();
				button1.addActionListener(new ActionListener() {
			         @Override public void actionPerformed(ActionEvent evt) {
			        	 FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
			        	 dialog.setMode(FileDialog.LOAD);
			        	 dialog.setVisible(true);
			        	 chosenImage = dialog.getDirectory() + dialog.getFile();
			         }
				});
				
				button2.setPreferredSize(new Dimension(50, 50));
				button2.setFont(new Font("DIALOG", Font.PLAIN, 20));
				button2.setText("X");
				button2.requestFocusInWindow();
				button2.addActionListener(new ActionListener() {
			         @Override public void actionPerformed(ActionEvent evt) {
			        	 tempList1.remove(listIndex);
			        	 for(NewListPanel i : tempList1)
			        	 {
			        		 if(i.getIndex() > listIndex)
			        		 {
			        			 i.decrease();
			        		 }
			        	 }
			        	 drawNewList();
			         }
				});
				panel1.add(textField1);
				panel1.add(button1);
				panel1.add(button2);
			}
		}
		public NewListPanel()
		{
		}
		public JPanel getPanel()
		{
			return panel1;
		}
		public String getName()
		{
			return textField1.getText();
		}
		public JPanel getImage()
		{
			return panel1;
		}
		public void increment()
		{
			listIndex++;
		}
		public void decrease()
		{
			listIndex--;
		}
		public int getIndex()
		{
			return listIndex;
		}
		public void fixWidth() //pains me to do this ali jebiga jednom se zivi
		{
			if(tempList1.size() > maxItemsAtOnce)
				textField1.setPreferredSize(new Dimension(590-50*2-15, 50));
			else
				textField1.setPreferredSize(new Dimension(590-50*2, 50));
		}
		public String getChosenImage()
		{
			return chosenImage;
		}
	}
	
	public ChoiceHelper() 
	{
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (Exception e) {
	    }
		
		initialize();
	}

	public void loadMegalist()
	{
		try 
		{
			File file = new File(savePath);
			if(!file.exists()) 
			{
		        file.mkdirs();
			}
			String[] fileList = file.list();
			if(fileList != null)
			{
				megalist.clear();
				if(fileList.length != 0)
				{
					for(String i : fileList)
					{
						List tempList = new List();
						
						String[] j = i.split(".txt");
						int counterLine = 0;
						String line;
						BufferedReader br = new BufferedReader(new FileReader(savePath+i));
						br.mark(1000);
						long lineNumber = br.lines().count();
						tempList.items = new Item[Integer.parseInt(Long.toString(lineNumber/2))];
						br.reset();
						while((line=br.readLine()) != null)
						{
							Item tempItem = new Item();
							tempItem.setName(line);
							tempItem.setImage(br.readLine());
							tempList.items[counterLine] = tempItem;
							counterLine++;
						}
						
						tempList.setName(j[0]);
						
						megalist.add(tempList);
						br.close();
					}
				}
			}
			
		}
		catch(Exception e) 
		{
	        e.printStackTrace();
	    }
		
	}
	
	public JPanel newListPanel(List x)
	{
		JPanel panel1 = new JPanel();
        JButton button1 = new JButton();
        JButton button2 = new JButton();
        panel1.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        
        button1.setFont(new Font("DIALOG", Font.BOLD, 15));
        button1.setText(x.getName());
        if(megalist.size() > maxListsAtOnce)
        	button1.setPreferredSize(new Dimension(590-68, 64));//68 = 50 + 15(scroller width) + 3 da bi se video desni deo bordera kad je scroller
        else
        	button1.setPreferredSize(new Dimension(590-50, 64));
        button1.addActionListener(new ActionListener() {
	         @Override public void actionPerformed(ActionEvent evt) {
	        	drawList(x);
	         }
	    });
        
        button2.setFont(new Font("DIALOG", Font.BOLD, 15));
        button2.setText("X");
        button2.setPreferredSize(new Dimension(50,64));
        button2.addActionListener(new ActionListener() {
	         @Override public void actionPerformed(ActionEvent evt) {
	        	deleteList(x);
	        	loadMegalist();
	        	drawMenu();
	         }
	    });
        
        panel1.add(button1);
        panel1.add(button2);
        
        return panel1;
	}
	
	public JPanel newItemPanel(Item x, int y)
	{
		JPanel panel1 = new JPanel();
        JTextField textField1 = new JTextField();
        
        panel1.setLayout(new BorderLayout());
        
        textField1.setEditable(false);
        textField1.setFont(new Font("DIALOG", Font.BOLD, 15));
        textField1.setText((y+1) + ": " + x.getName());
        textField1.setHorizontalAlignment(JTextField.CENTER);
        
        panel1.add(textField1, BorderLayout.CENTER);
        
        return panel1;
	} 
	
	public void drawMenu()
	{
		menuPanel.setLayout(new BorderLayout(5,0));

		menuSubPanel1.removeAll();
		menuSubPanel1.setLayout(new GridLayout(maxListsAtOnce,1,0,0));
		menuSubPanel1.setPreferredSize(new Dimension(590,menuSubPanel1.getHeight())); //ne znam zasto 360 // 360 je height subpanela brojo sam pixele nzm kako tho
		menuSubPanel1.setBorder(null);
		
		scrollerList.setViewportView(menuSubPanel1);
		scrollerList.setPreferredSize(new Dimension(590,360));
		scrollerList.getVerticalScrollBar().setUnitIncrement(scrollSpeedList);
		scrollerList.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollerList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		if(megalist.size() != 0)
		{
			for(List i : megalist)
			{
				menuSubPanel1.add(newListPanel(i));
			}
		}
		if(megalist.size() > maxListsAtOnce - 1) //maxListsAtOnce-1 ovde mozda
		{
			menuSubPanel1.setLayout(new GridLayout(megalist.size(),1,0,0));
			menuSubPanel1.setPreferredSize(new Dimension(590, 68*megalist.size()+2)); //offset je za 1px i cant do this anymore leave it
			scrollerList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		}
		
		menuSubPanel2.setPreferredSize(new Dimension(590,75));
		menuSubPanel2.setLayout(new FlowLayout());
		
		buttonNew.setPreferredSize(new Dimension(590, 70)); //zasto 587??
		buttonNew.setFont(new Font("DIALOG", Font.PLAIN, 20));
		buttonNew.setText("New List");
		buttonNew.requestFocusInWindow();
		buttonNew.addActionListener(new ActionListener() {
	         @Override public void actionPerformed(ActionEvent evt) {
	        	 drawNewList();
	         }
		});

		menuSubPanel2.add(buttonNew);
		menuPanel.add(scrollerList, BorderLayout.CENTER);
		menuPanel.add(menuSubPanel2, BorderLayout.SOUTH);
		
		card.show(cp, "Menu");
	}
	
	public void drawList(List x)
	{
		listPanel.setLayout(new BorderLayout(0,0));

		listSubPanel1.removeAll();
		listSubPanel1.setLayout(new GridLayout(maxItemsAtOnce,1,0,0));
		listSubPanel1.setPreferredSize(new Dimension(590,listSubPanel1.getHeight()));
		listSubPanel1.setBorder(null);
		
		labelList.setPreferredSize(new Dimension(590,70));
		labelList.setFont(new Font("DIALOG", Font.PLAIN, 20));
		labelList.setHorizontalAlignment(SwingConstants.CENTER);
		labelList.setText(x.getName());
		
		scrollerItem.setViewportView(listSubPanel1);
		scrollerItem.setPreferredSize(new Dimension(590,listSubPanel1.getHeight()));
		scrollerItem.getVerticalScrollBar().setUnitIncrement(scrollSpeedItem);
		scrollerItem.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollerItem.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		listSubPanel2.setPreferredSize(new Dimension(590,75));
		listSubPanel2.setLayout(new FlowLayout());
		
		if(x.items.length != 0)
		{
			int counterItem = 0;
			for(Item i : x.items)
			{
				listSubPanel1.add(newItemPanel(i, counterItem));
				counterItem++;
			}
		}
		if(x.items.length > maxItemsAtOnce-1)
		{
			listSubPanel1.setLayout(new GridLayout(x.items.length,1,0,5));
			listSubPanel1.setPreferredSize(new Dimension(590, 56*x.items.length+3));
			scrollerItem.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		}
		
		buttonBack.setPreferredSize(new Dimension(590, 70));
		buttonBack.setFont(new Font("DIALOG", Font.PLAIN, 20));
		buttonBack.setText("Back");
		buttonBack.requestFocusInWindow();
		buttonBack.addActionListener(new ActionListener() {
	         @Override public void actionPerformed(ActionEvent evt) {
	        	 //loadMegalist();
	        	 drawMenu();
	         }
		});

		listSubPanel2.add(buttonBack);
		listPanel.add(labelList, BorderLayout.NORTH);
		listPanel.add(scrollerItem, BorderLayout.CENTER);
		listPanel.add(listSubPanel2, BorderLayout.SOUTH);
		
		card.show(cp, "List");
	}	
	
	public void drawNewList()
	{
		newPanel.setLayout(new BorderLayout(0,5));

		newSubPanel1.removeAll();
		newSubPanel1.setLayout(new GridLayout(maxItemsAtOnce,1,0,0));
		newSubPanel1.setPreferredSize(new Dimension(590,listSubPanel1.getHeight()));
		newSubPanel1.setBorder(null);
		
		newSubPanel2.setPreferredSize(new Dimension(590,70));
		newSubPanel2.setLayout(new BorderLayout(0,0));
		
		textFieldListName.setFont(new Font("DIALOG", Font.PLAIN, 20));
		textFieldListName.setHorizontalAlignment(SwingConstants.CENTER);
		
		scrollerNew.setViewportView(newSubPanel1);
		scrollerNew.setPreferredSize(new Dimension(590,listSubPanel1.getHeight()));
		scrollerNew.getVerticalScrollBar().setUnitIncrement(scrollSpeedItem);
		scrollerNew.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollerNew.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		newSubPanel3.setPreferredSize(new Dimension(590,75));
		newSubPanel3.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
		
		if(tempList1.size() != 0)
		{
			for(NewListPanel i : tempList1)
			{
				i.fixWidth();
				newSubPanel1.add(i.getPanel());
			}
		}
		if(tempList1.size() > maxItemsAtOnce)
		{
			newSubPanel1.setLayout(new GridLayout(tempList1.size(),1,0,0));
			newSubPanel1.setPreferredSize(new Dimension(590, 56*tempList1.size()+3));
			scrollerNew.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		}
		
		buttonBack.setPreferredSize(new Dimension(590/2, 70));
		buttonBack.setFont(new Font("DIALOG", Font.PLAIN, 20));
		buttonBack.setText("Back");
		buttonBack.addActionListener(new ActionListener() {
	         @Override public void actionPerformed(ActionEvent evt) {
	        	 //loadMegalist();
	        	 drawMenu();
	         }
		});
		
		buttonConfirm.setPreferredSize(new Dimension(590/2, 70));
		buttonConfirm.setFont(new Font("DIALOG", Font.PLAIN, 20));
		buttonConfirm.setText("Confirm");
		buttonConfirm.requestFocusInWindow();
		buttonConfirm.addActionListener(new ActionListener() {
	         @Override public void actionPerformed(ActionEvent evt) {
	        	 if((tempList1.size() > 1) && !textFieldListName.getText().isBlank())
	        	 {
	        		 boolean check = false;
	        		 for(int i=0; i<tempList1.size()-1; ++i)
	        		 {
	        			 if(tempList1.get(i).getName().isBlank())
	        			 {
	        				 break;
	        			 }
	        			 check = true;
	        		 }
	        		 if(check)
	        		 {
		        		 List temp1 = new List();
		        		 temp1.setName(textFieldListName.getText());
		        		 temp1.items = new Item[tempList1.size()-1]; //-1 je + button
		        		 for(int i=0; i<tempList1.size()-1; ++i)
		        		 {
			        		Item temp2 = new Item();
			        		temp2.setName(tempList1.get(i).getName());
			        		if(!tempList1.get(i).getChosenImage().isBlank()) temp2.setImage(tempList1.get(i).getChosenImage());
			        		temp1.items[i] = temp2;
		        		 }
		        		 shuffleArray(temp1.items);
		        		 sortList(temp1, 0, 0);
	        		 }
	        	 }
	         }
		});

		newSubPanel2.add(textFieldListName);
		newSubPanel3.add(buttonBack);
		newSubPanel3.add(buttonConfirm);
		newPanel.add(newSubPanel2, BorderLayout.NORTH);
		newPanel.add(scrollerNew, BorderLayout.CENTER);
		newPanel.add(newSubPanel3, BorderLayout.SOUTH);
		
		card.show(cp, "New");
	}
	
	private void shuffleArray(Item[] array)
	{
		if (random == null) random = new Random();
        int count = array.length;
        for (int i = count; i > 1; i--) {
        	swapElements(array, i - 1, random.nextInt(i));
        }
	}
	
	private static void swapElements(Item[] array, int i, int j) {
        Item temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
	
	public void sortList(List x, int i, int j)
	{
		itemsSwapped = false;
		if(i<x.items.length-1)
		{
			final int iCopy = i;
			final int jCopy = j;
			final List xCopy = x;
			
			JButton buttonLeft = new JButton();
			JButton buttonRight = new JButton();
			
			sortPanel.removeAll();
			sortPanel.setLayout(new GridLayout(1,2,5,0));
			sortPanel.setPreferredSize(new Dimension(590,385));
			sortPanel.setBorder(new EmptyBorder(5,5,5,5));

			sortPanel.add(buttonLeft);
			sortPanel.add(buttonRight);
			card.show(cp, "Sort");
			
		    if(j<x.items.length-i-1)
		    {
				buttonLeft.setFont(new Font("DIALOG", Font.PLAIN, 30));
				buttonLeft.setText(x.items[j].getName());
				
		    	if(x.items[j].getImage() != null)
		    	{
		    		buttonLeft.setAlignmentX(BOTTOM_ALIGNMENT);
		    		try
		    		{
		    			buttonLeft.setIcon(resizeIcon(new ImageIcon(ImageIO.read(x.items[j].getImage())), sortPanel.getWidth()/2+15, sortPanel.getHeight()));
		    			buttonLeft.setBorder(null);
		    		}
		    		catch(Exception e)
		    		{
		    	        e.printStackTrace();
		    	    }
		    	}
		    	else
		    	{
		    		buttonLeft.setAlignmentX(CENTER_ALIGNMENT);
		    	}
				buttonLeft.addActionListener(new ActionListener() {
			         @Override public void actionPerformed(ActionEvent evt) {
			        	Item tempItem = xCopy.items[jCopy];
			        	xCopy.items[jCopy] = xCopy.items[jCopy+1];
			 			xCopy.items[jCopy+1] = tempItem;
			 			swapped();
			 			
			 			sortList(xCopy, iCopy, jCopy+1);
			         }
				});
				
				buttonRight.setFont(new Font("DIALOG", Font.PLAIN, 30));
				buttonRight.setText(x.items[j+1].getName());
				
		    	if(x.items[j+1].getImage() != null)
		    	{
		    		buttonRight.setAlignmentX(BOTTOM_ALIGNMENT);
		    		try
		    		{
		    			buttonRight.setIcon(resizeIcon(new ImageIcon(ImageIO.read(x.items[j+1].getImage())), sortPanel.getWidth()/2+15, sortPanel.getHeight()));
		    			buttonRight.setBorder(null);
		    		}
		    		catch(Exception e)
		    		{
		    	        e.printStackTrace();
		    	    }
		    	}
		    	else
		    	{
		    		buttonRight.setAlignmentX(CENTER_ALIGNMENT);
		    	}

				buttonRight.addActionListener(new ActionListener() {
			         @Override public void actionPerformed(ActionEvent evt) {
			 			sortList(xCopy, iCopy, jCopy+1);
			         }
				});
		     }
		     else
		     {
		    	 if(!itemsSwapped&&i>1)
		    	 {
		    		Collections.reverse(Arrays.asList(x.items));
		 			megalist.add(x);
		 			saveList(x);
		 			drawList(x);
		    	 }
		    	 else
		    	 {
			         sortList(x, i+1,0);
		    	 }
		     }
		}
		else
		{
			Collections.reverse(Arrays.asList(x.items));
			megalist.add(x);
			saveList(x);
			drawList(x);
		}
	}
	
	public void swapped()
	{
		itemsSwapped = true;
	}
	
	public void saveList(List x)
	{
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(savePath + x.getName() + ".txt"));
			for(Item i : x.items)
			{
				bw.write(i.getName());
				bw.write("\n");
				if(i.getImage() == null)
				{
					bw.write("null");
				}
				else
				{
					bw.write(i.getImage().getAbsolutePath());
				}
				bw.write("\n");
			}
			bw.close();
		}
		catch(Exception ex)
		{
			System.out.print("sjeblo se");
			System.out.print(ex.getMessage());

		}
	}
	
	public void deleteList(List x)
	{
		try
		{
			File file = new File(savePath + x.getName() + ".txt");
			file.delete();
		}
		catch(Exception e)
		{
			System.out.print("sjeblo se");
		}
		megalist.remove(x);
	}
	
	public Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight)
	{
		Image img = icon.getImage();
		Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(resizedImage);
	}
	
	private void initialize() 
	{
		this.setSize(windowWidth,windowHeight);
		this.setTitle("ChoiceHelper 1.0");
		try
		{
			this.setIconImage(ImageIO.read(new File("\\jre\\icon.png")));
		}
		catch(Exception e) {}
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		cp.setLayout(card);
		cp.add("Menu", menuPanel);
		cp.add("New", newPanel);
		cp.add("List", listPanel);
		cp.add("Sort", sortPanel);
		tempList1.add(tempNewList);
		
		loadMegalist();
		drawMenu();
	}
	
	public static void main(String[] args) 
	{
		SwingUtilities.invokeLater(new Runnable() {
	         @Override public void run() 
	         {
	            new ChoiceHelper();
	         }
	      });
	}
}
