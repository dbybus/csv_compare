import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class PT extends JFrame implements ActionListener{
	
	private JFrame frame;
	private JButton open;
	private JButton openR;
	private JButton openPT;
	private JButton launch;
	private JButton dst; 
	private JLabel txtpathR;
	//private JTextArea txtpathPT;
	private JLabel txtpathPT;
	private JLabel txtpathDst;
	static String pathR;
	static String pathPT;
	static String pathdst;
	
	public PT() {
				
		frame = new JFrame("Prefered Term");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 500);
		frame.setVisible(true);
		frame.setLayout(null);

		openR = new JButton("Select Report");
		openR.setBounds(10,10,250,35);
		txtpathR = new JLabel("Monitoring Report Path is not selected");
		txtpathR.setBounds(10,50,250,35);
		
		openPT = new JButton("Select PT");
		openPT.setBounds(10,100,250,35);
		txtpathPT = new JLabel("PT Path is not selected");
		txtpathPT.setBounds(10,140,250,35);
		
		dst = new JButton("Destination");
		dst.setBounds(10,200,250,35);
		txtpathDst = new JLabel("Destination Path is not selected");
		txtpathDst.setBounds(10,240,250,35);
		
		launch = new JButton("Extract");
		launch.setBounds(10,300,250,35);
		
		frame.add(openR);
		frame.add(openPT);
		frame.add(dst);
		frame.add(launch);
		frame.add(txtpathR);
		frame.add(txtpathPT);
		frame.add(txtpathDst);
		openR.addActionListener(this);
		openPT.addActionListener(this);
		dst.addActionListener(this);
		launch.addActionListener(this);
	}
	
	public void extractNewPT() {
		 	BufferedReader br = null;
	        BufferedReader brPt = null;
	        PrintWriter writer = null;
	        String line = "";
	        String linePt = "";
	        String cvsSplitBy = ",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)";
	        ArrayList<String> ptFull = new ArrayList<String>();
	        ArrayList<String> input = new ArrayList<String>();

	        try {
	        	
	        	writer = new PrintWriter(pathdst+"\\newPT.csv", "UTF-8");
	            br = new BufferedReader(new FileReader(pathR));
	            brPt = new BufferedReader(new FileReader(pathPT));

	           while((linePt = brPt.readLine()) != null){
	        	   String[] ptList = linePt.split(cvsSplitBy);
	        	   ptFull.add(ptList[0].trim().toString());
	           }

	           while ((line = br.readLine()) != null)  {
	        	   input.add(line);
	           }
	           ArrayList<String> toRemove = new ArrayList<String>();
	           int h = -1;
	           String[] headerList = input.get(0).split(cvsSplitBy);
	           for(int i = 0; i < input.size(); i++) {
	        	   if(headerList[i].trim().equalsIgnoreCase("PT")) {
	            	   h = i;   
	               }  
	           }
	           if(h>=0) {
	           for(int i = 0; i < input.size(); i++) { 
	        	   String[] monitoringList = input.get(i).split(cvsSplitBy);
	        	   
		        	   for(int j = 0; j < ptFull.size(); j++) {
		        		   if(monitoringList[h].equalsIgnoreCase(ptFull.get(j))) { 
		        			   toRemove.add(input.get(i));
					       }
		        	   }
	        	   }
	        	   
	           }else {
	    		   JOptionPane.showMessageDialog(frame, "File do not contains PT Column");
	    	   }
	           
	           for (String pt : toRemove) {
	        	   input.remove(pt);
	           }
	           
	           for(int i = 0; i < input.size(); i++) {
	        	 writer.write(input.get(i)+"\n");	
	           }
	           
	           writer.close();
	           JOptionPane.showMessageDialog(frame, "File was succesfully exported ");
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	            JOptionPane.showMessageDialog(frame, "Encountered Error  ");
	        } catch (IOException e) {
	            e.printStackTrace();
	            JOptionPane.showMessageDialog(frame, "Encountered Error  ");
	        } finally {
	            if (br != null && brPt != null) {
	                try {
	                    br.close();
	                    brPt.close();
	                } catch (IOException e) {
	                	
	                    e.printStackTrace();
	                    JOptionPane.showMessageDialog(frame, "Encountered Error  ");
	                }
	            }
	        }
	}
	public static void main(String[] args) {
		new PT();
    	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == launch ) {
			if(pathR != null && pathPT != null && pathdst != null) {
				extractNewPT();
			}else {
				JOptionPane.showMessageDialog(frame, "Please select Monitoring report, Prefered Term List and Destination folder before excuting ");
			}
		}
		if(e.getSource() == openR ) {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Select source folder");
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			if(chooser.showOpenDialog(open)==JFileChooser.APPROVE_OPTION) {
				String tmp = chooser.getSelectedFile().getName();
				int index = tmp.indexOf(".");
				String f = tmp.substring(index, tmp.length() );
				if(f.equalsIgnoreCase(".csv")) {
				pathR = chooser.getSelectedFile().getAbsolutePath();
				txtpathR.setText(pathR);
				}else {
					JOptionPane.showMessageDialog(frame, "File should be CSV format");
				}
			}		
		}
		if(e.getSource() == openPT ) {
			JFileChooser chooser1 = new JFileChooser();
			chooser1.setDialogTitle("Select destination folder");
			chooser1.setFileSelectionMode(JFileChooser.FILES_ONLY);
			if(chooser1.showOpenDialog(open)==JFileChooser.APPROVE_OPTION) {
				String tmp = chooser1.getSelectedFile().getName();
				int index = tmp.indexOf(".");
				String f = tmp.substring(index, tmp.length() );
				if(f.equalsIgnoreCase(".csv")) {
				pathPT = chooser1.getSelectedFile().getAbsolutePath();
				txtpathPT.setText(pathPT);	
				}else {
					JOptionPane.showMessageDialog(frame, "File should be CSV format");
				}
			}		
		}
		if(e.getSource() == dst ) {
			JFileChooser chooser2 = new JFileChooser();
			chooser2.setDialogTitle("Select destination folder");
			chooser2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if(chooser2.showOpenDialog(open)==JFileChooser.APPROVE_OPTION) {
				pathdst = chooser2.getSelectedFile().getAbsolutePath();
				txtpathDst.setText(pathdst);	
			}		
		}
		
	}
	}


