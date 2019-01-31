import java.awt.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.ArrayList;

public class ImageViewer extends JFrame{

    private static final int CANVAS_WIDTH  = 640;
    private static final int CANVAS_HEIGHT = 480;
    private DrawCanvas imageDis;
    private JPanel controls,disp;
    private JButton nextB, prevB, quitB, submitB;
    private JLabel fileLabel;
    private JTextField input;
    private int count = 0;
    private String dir = "",imageFile = "";
    private String[] fList, picList;
    private int flength = 0;
    private boolean inDir = false;
    
    public ImageViewer(){
	Container cp = new Container();
	cp.setLayout(new BorderLayout(1,1));
	this.getContentPane().add(cp);
	this.setTitle("Image Viewer");
	this.setSize(700,600);
	this.setVisible(true);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	// Instansiate action listener
	btnListener listener = new btnListener();

	// prepare control panal
	controls = new JPanel(new FlowLayout());

	// add next button
        nextB = new JButton("Next");
	nextB.addActionListener(listener);
	controls.add(nextB);

	// add quit button
	quitB = new JButton("QUIT");
	quitB.addActionListener(listener);
	controls.add(quitB);

	// add prev button
	prevB = new JButton("Prev");
	prevB.addActionListener(listener);
	controls.add(prevB);

	// add submitbutton
	submitB = new JButton("Submit");
	submitB.addActionListener(listener);
	controls.add(submitB);

	// Ready display panel
	disp = new JPanel(new BorderLayout(1,1));

	// add image canvas
	imageDis = new DrawCanvas();
	imageDis.setPreferredSize(new Dimension(CANVAS_WIDTH,CANVAS_HEIGHT));
	disp.add(imageDis,BorderLayout.CENTER);

	// add input text field
	input = new JTextField("Input directory here",50);
	disp.add(input,BorderLayout.SOUTH);

	// add label
	fileLabel = new JLabel("File Names",JLabel.CENTER);
	disp.add(fileLabel,BorderLayout.NORTH);

	// add display and controls panel to frame
	cp.add(disp,BorderLayout.CENTER);
	cp.add(controls,BorderLayout.SOUTH);

	this.pack();
    }

    private class DrawCanvas extends JPanel{
	@Override
	public void paintComponent(Graphics g){
	    super.paintComponent(g);

	    if (!imageFile.isEmpty()){
		try{
		    BufferedImage myPic = ImageIO.read(new File(imageFile));
		    g.drawImage(myPic.getScaledInstance(imageDis.getWidth(),imageDis.getHeight(),0),0,0,null);
		} catch (IOException e){
		    e.printStackTrace();
		}
	    } else {
		String defaultStr = "Pick a file";
		imageDis.setBackground(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 30));
		g.setColor(Color.MAGENTA);
		FontMetrics fm = g.getFontMetrics();
		int msgWidth = fm.stringWidth(defaultStr);
		int msgAscent = fm.getAscent();
		int msgX = imageDis.getWidth()/2 - msgWidth/2;
		int msgY = imageDis.getHeight()/2 + msgAscent/2;
		g.drawString(defaultStr,msgX,msgY);
	    }
	}
    }
    
    public static void main(String[] args){
	SwingUtilities.invokeLater(new Runnable() {
		@Override
		public void run(){
		    new ImageViewer();
		}
	    });
    }

    private void inc(){
	if(count == flength-1)
	    count=0;
	else
	    ++count;
	fileLabel.setText(picList[count]);
	imageFile = dir+picList[count];
	imageDis.repaint();
    }
    private void dec(){
	if(count == 0)
	    count = flength-1;
	else
	    --count;
	fileLabel.setText(picList[count]);
	imageFile = dir+picList[count];
	imageDis.repaint();
    }	


    private String[] getPicList(String[] FList){
	ArrayList<String> dummyList = new ArrayList<String>();
	for(String name: FList){
	    if (name.endsWith(".jpg")||name.endsWith(".JPG")){
		dummyList.add(name);
	    }
	}
	String[] returnList = new String[dummyList.size()];
	return dummyList.toArray(returnList);
    }
    
    private class btnListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent evt){
	    String btnLabel = evt.getActionCommand();
	    if (btnLabel.equals("Next")){
		if (inDir) inc();
	    } else if (btnLabel.equals("Prev")){
		if (inDir) dec();
	    } else if (btnLabel.equals("QUIT")){
		System.exit(0);
	    } else if (btnLabel.equals("Submit")){
		 dir = input.getText();
		
		if(!((dir.charAt(dir.length()-1)) == '/'))
		    dir += "/";
		File dirFile = new File(dir);
		
		if(!dirFile.isDirectory()){
		    fileLabel.setText("Need a vaild directory!");
		    inDir = false;
		} else {
		    inDir = true;
		    count = 0;
		    fList = dirFile.list();
		    picList = getPicList(fList);
		    flength = picList.length;
		    fileLabel.setText(picList[count]);
		    imageDis.repaint();
		}
		
	    }
	}
    }
}
