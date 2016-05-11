package com.face;


//Main.java

import java.lang.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Main extends JFrame implements ActionListener
{
	JFrame frmMain=new JFrame("Face Recognition Using LaplacianFaces");
	JLabel lblTestPath=new JLabel("Input Test Image:");
	JTextField txtTestPath=new JTextField("images\\test\\face16d.pgm");
	JButton btRecognize=new JButton("Recognize");
	JLabel lblResult=new JLabel("Result:");
	JTextArea txtResult=new JTextArea("");
	JScrollPane spResult=new JScrollPane(txtResult);
	JFrame frmImage1=new JFrame("Input Image");
	JFrame frmImage2=new JFrame("Matched Image");

	//system declarations
	int MaxFaceIndex=17;
	double DifferenceThreshold=15000.0;
	int NumFaces;
	int MaxFaces=100;
	int FaceTemplate[][];
	int Faces[][][];
	int LaplacianFaces[][][];
	String FaceFileNames[];
	String tResult="";
	
	//constructor
	public Main()
	{
		frmMain.setDefaultLookAndFeelDecorated(true);
		frmMain.setResizable(false);
		frmMain.setBounds(100,100,315,250);
		frmMain.getContentPane().setLayout(null);
		
		lblTestPath.setBounds(17,15,100,20);
		frmMain.getContentPane().add(lblTestPath);
		txtTestPath.setBounds(15,35,170,20);
		frmMain.getContentPane().add(txtTestPath);
		
		lblResult.setBounds(17,65,100,20);
		frmMain.getContentPane().add(lblResult);
		spResult.setBounds(15,85,280,120);
		frmMain.getContentPane().add(spResult);
		txtResult.setEditable(false);
		
		btRecognize.setBounds(193,35,100,20);
		btRecognize.addActionListener(this);
		frmMain.getContentPane().add(btRecognize);
		
		frmImage1.setDefaultLookAndFeelDecorated(true);
		frmImage1.setResizable(false);
		frmImage1.setBounds(450,100,200,150);
		
		frmImage2.setDefaultLookAndFeelDecorated(true);
		frmImage2.setResizable(false);
		frmImage2.setBounds(670,100,200,150);
		
		frmImage1.setVisible(true);
		frmImage2.setVisible(true);
		frmMain.setVisible(true);
	}
	
	//events
	public void actionPerformed(ActionEvent evt)
	{
		if(evt.getSource()==btRecognize)
		{
			if(new File(txtTestPath.getText()).exists()==false)
			{
				JOptionPane.showMessageDialog(null,"Test Image, not found.");
				return;
			}
			
			drawImage(frmImage1.getGraphics(),txtTestPath.getText());
			
			tResult="";
			txtResult.setText(tResult);
			train();
			test();
		}
	}
	
	//internal methods
	void drawImage(Graphics g,String tPath)
	{
		PGM tpgm=new PGM();
		tpgm.setFilePath(tPath);
		tpgm.readImage();
		
		g.clearRect(0,0,200,150);
		for(int r=0;r<tpgm.getRows();r++)
		{
			for(int c=0;c<tpgm.getCols();c++)
			{
				int intensity=tpgm.getPixel(r,c);
				Color color=new Color(intensity,intensity,intensity);
				g.setColor(color);
				g.fillRect(c,r+30,1,1);
			}
		}
	}
	
	
	//methods
	public void addResultText(String tStr)
	{
		tResult=tResult+tStr;
		txtResult.setText(tResult);
	}
	
	public void train()
	{
		int xBase,yBase,xSub,ySub;
		int xLow,xHigh,yLow,yHigh;
		int GrayLevel;
		int CellSum,CellAvg;
		int i,j;
		int xDiv,yDiv;
		int BlockWidth,BlockHeight;
		int StartX,StartY;
		int SizeX,SizeY;
		
		//set system parameters
		SizeX=80;
		SizeY=80;
		xDiv=20;
		yDiv=20;
		BlockWidth=SizeX/xDiv;
		BlockHeight=SizeY/yDiv;
		
		FaceTemplate=new int[xDiv][yDiv];
		Faces=new int[xDiv][yDiv][MaxFaces];
		LaplacianFaces=new int[xDiv][yDiv][MaxFaces];
		FaceFileNames=new String[MaxFaces];
		NumFaces=0;
		
		addResultText("Training...");
		PGM pgm1=new PGM();
		for(i=0;i<=MaxFaceIndex;i++)
		{
			for(j=97;j<=98;j++)//'a' to 'b'
			{
				NumFaces=NumFaces+1;
				
				FaceFileNames[NumFaces]="images\\train\\face"+i+(char)j+".pgm";
				PGM_ImageFilter imgFilter=new PGM_ImageFilter();
				imgFilter.set_inFilePath(FaceFileNames[NumFaces]);
				imgFilter.set_outFilePath("temp.pgm");
				imgFilter.resize(SizeX,SizeY);
				pgm1.setFilePath("temp.pgm");
				pgm1.readImage();
				
				for(xBase=0;xBase<=xDiv-1;xBase++)
				{
					for(yBase=0;yBase<=yDiv-1;yBase++)
					{
						StartX=xBase*BlockWidth;
						StartY=yBase*BlockHeight;
						xLow=StartX;
						xHigh=StartX+BlockWidth-1;
						yLow=StartY;
						yHigh=StartY+BlockHeight-1;
						
						CellSum=0;
						for(xSub=xLow;xSub<=xHigh;xSub++)
						{
							for(ySub=yLow;ySub<=yHigh;ySub++)
							{
								GrayLevel=pgm1.getPixel(xSub,ySub);
								CellSum=CellSum+GrayLevel;
							}
						}
						CellAvg=CellSum/(BlockWidth*BlockHeight);
						Faces[xBase][yBase][NumFaces]=CellAvg;
					}
				}
			}
		}
		
		for(xBase=0;xBase<=xDiv-1;xBase++)
		{
			for(yBase=0;yBase<=yDiv-1;yBase++)
			{
				CellSum=0;
				for(i=1;i<=NumFaces;i++)
				{
					CellSum=CellSum+Faces[xBase][yBase][i];
				}
				CellAvg=CellSum/NumFaces;
				FaceTemplate[xBase][yBase]=CellAvg;
			}
		}

		for(xBase=0;xBase<=xDiv-1;xBase++)
		{
			for(yBase=0;yBase<=yDiv-1;yBase++)
			{
				for(i=1;i<=NumFaces;i++)
				{
					LaplacianFaces[xBase][yBase][i]=Faces[xBase][yBase][i]-FaceTemplate[xBase][yBase];
				}
			}
		}

		PGM pgm2=new PGM();
		pgm2.setFilePath("template.pgm");
		pgm2.setType("P5");
		pgm2.setComment("");
		pgm2.setDimension(SizeX,SizeY);
		pgm2.setMaxGray(255);
		
		for(xBase=0;xBase<=xDiv-1;xBase++)
		{
			for(yBase=0;yBase<=yDiv-1;yBase++)
			{
				StartX=xBase*BlockWidth;
				StartY=yBase*BlockHeight;
				xLow=StartX;
				xHigh=StartX+BlockWidth-1;
				yLow=StartY;
				yHigh=StartY+BlockHeight-1;
				
				for(xSub=xLow;xSub<=xHigh;xSub++)
				{
					for(ySub=yLow;ySub<=yHigh;ySub++)
					{
						GrayLevel=FaceTemplate[xBase][yBase];
						pgm2.setPixel(xSub,ySub,GrayLevel);
					}
				}
			}
		}
		
		pgm2.writeImage();
		addResultText("done.");
	}
	
	public void test()
	{
		int xBase,yBase,xSub,ySub;
		int xLow,xHigh,yLow,yHigh;
		int GrayLevel;
		int CellSum,CellAvg;
		int i,j;
		int xDiv,yDiv;
		int BlockWidth,BlockHeight;
		int StartX,StartY;
		int SizeX,SizeY;
		
		//set system parameters
		SizeX=80;
		SizeY=80;
		xDiv=20;
		yDiv=20;
		BlockWidth=SizeX/xDiv;
		BlockHeight=SizeY/yDiv;
		
		int TestFace[][]=new int[xDiv][yDiv];
		int TestLaplacianFace[][]=new int[xDiv][yDiv];
		int LaplacianDiff;
		int MinLaplacianIndex;
		double TotalLaplacianDiff,MinLaplacianDiff;
		
		addResultText("\nTesting...");
		PGM pgm1=new PGM();
		pgm1.setFilePath(txtTestPath.getText());
		pgm1.readImage();
		
		for(xBase=0;xBase<=xDiv-1;xBase++)
		{
			for(yBase=0;yBase<=yDiv-1;yBase++)
			{
				StartX=xBase*BlockWidth;
				StartY=yBase*BlockHeight;
				xLow=StartX;
				xHigh=StartX+BlockWidth-1;
				yLow=StartY;
				yHigh=StartY+BlockHeight-1;
				
				CellSum=0;
				for(xSub=xLow;xSub<=xHigh;xSub++)
				{
					for(ySub=yLow;ySub<=yHigh;ySub++)
					{
						GrayLevel=pgm1.getPixel(xSub,ySub);
						CellSum=CellSum+GrayLevel;
					}
				}
				CellAvg=CellSum/(BlockWidth*BlockHeight);
				TestFace[xBase][yBase]=CellAvg;
			}
		}
		
		PGM pgm2=new PGM();
		pgm2.setFilePath("diff.pgm");
		pgm2.setType("P5");
		pgm2.setComment("");
		pgm2.setDimension(SizeX,SizeY);
		pgm2.setMaxGray(255);
		
		for(xBase=0;xBase<=xDiv-1;xBase++)
		{
			for(yBase=0;yBase<=yDiv-1;yBase++)
			{
				StartX=xBase*BlockWidth;
				StartY=yBase*BlockHeight;
				xLow=StartX;
				xHigh=StartX+BlockWidth-1;
				yLow=StartY;
				yHigh=StartY+BlockHeight-1;
				
				for(xSub=xLow;xSub<=xHigh;xSub++)
				{
					for(ySub=yLow;ySub<=yHigh;ySub++)
					{
						GrayLevel=TestFace[xBase][yBase];
						pgm2.setPixel(xSub,ySub,GrayLevel);
					}
				}
			}
		}
		
		for(xBase=0;xBase<=xDiv-1;xBase++)
		{
			for(yBase=0;yBase<=yDiv-1;yBase++)
			{
				TestLaplacianFace[xBase][yBase]=TestFace[xBase][yBase]-FaceTemplate[xBase][yBase];
			}
		}
		             
		MinLaplacianDiff=2147483647; //2^32
		MinLaplacianIndex=-1;
		for(i=1;i<=NumFaces;i++)
		{
			TotalLaplacianDiff=0;
			for(xBase=0;xBase<=xDiv-1;xBase++)
			{
				for(yBase=0;yBase<=yDiv-1;yBase++)
				{
					TotalLaplacianDiff=TotalLaplacianDiff+java.lang.Math.abs(TestLaplacianFace[xBase][yBase]-LaplacianFaces[xBase][yBase][i]);
				}
			}
			if(MinLaplacianDiff>TotalLaplacianDiff)
			{
				MinLaplacianDiff=TotalLaplacianDiff;
				MinLaplacianIndex=i;
			}
		}
		
		pgm2.writeImage();
		
		if(MinLaplacianDiff>DifferenceThreshold)
		{
			frmImage2.getGraphics().clearRect(0,0,200,150);
			addResultText("done.");
			addResultText("\n\nNot Matched.");
			JOptionPane.showMessageDialog(null,"Not Matched.");
		}
		else
		{
			PGM pgmMatched=new PGM();
			pgmMatched.setFilePath(FaceFileNames[MinLaplacianIndex]);
			pgmMatched.readImage();
			pgmMatched.setFilePath("matched.pgm");
			pgmMatched.writeImage();
			drawImage(frmImage2.getGraphics(),"matched.pgm");
			addResultText("done.");
			addResultText("\n\nMatched: "+FaceFileNames[MinLaplacianIndex]);
		}
	}
	
	public static void main(String args[])
	{
		new Main();
	}
}
//----------------------------------------------------------------------------------------------------------

//PGM.java


////pgmimage
class PGM
{
	private String tFilePath;

	//pgm imageheader
	private String type;
	private String comment;
	private int cols,rows,maxgray;

	//pgm imagedata
	private int[][] pixel;
	
	//constructor
	public PGM()
	{
		tFilePath="";
		type="";
		comment="";
		cols=0;
		rows=0;
		maxgray=0;
		pixel=null;
	}

	//get functions
	public String getFilePath()
	{
		return(tFilePath);
	}

	public String getType()
	{
		return(type);
	}

	public String getComment()
	{
		return(comment);
	}

	public int getCols()
	{
		return(cols);
	}

	public int getRows()
	{
		return(rows);
	}

	public int getMaxGray()
	{
		return(maxgray);
	}

	public int getPixel(int tr,int tc)
	{
		return(tr<0||tr>rows-1||tc<0||tc>cols-1?0:pixel[tr][tc]);
	}

	

	//set functions
	public void setFilePath(String ttFilePath)
	{
		tFilePath=ttFilePath;
	}

	public void setType(String ttype)
	{
		type=ttype;
	}

	public void setComment(String tcomment)
	{
		comment=tcomment;
	}

	public void setDimension(int tcols,int trows)
	{
		rows=trows;
		cols=tcols;
		pixel=new int[rows][cols];
	}

	public void setMaxGray(int tmaxgray)
	{
		maxgray=tmaxgray;
	}

	public void setPixel(int tr,int tc,int tpval)
	{
		if(tr<0||tr>rows-1||tc<0||tc>cols-1) return;
		else pixel[tr][tc]=tpval;
	}

	//methods
	public void readImage()
	{
		FileInputStream fin;
		
		try
		{
			fin=new FileInputStream(tFilePath);
	
		    int tr,tc,c;
		    String tstr;
		    
		    //read first line of ImageHeader
		    tstr="";
		    c=fin.read();
		    tstr+=(char)c;
		    c=fin.read();
		    tstr+=(char)c;
		    type=tstr;

		    //read second line of ImageHeader
		    c=fin.read(); //read Lf (linefeed)
		    c=fin.read(); //read '#'
			tstr="";
		    boolean iscomment=false;
		    while((char)c=='#') //read comment
		    {
				iscomment=true;
			    tstr+=(char)c;
		        while(c!=10&&c!=13)
		        {
		            c=fin.read();
				    tstr+=(char)c;
		     	}
		        c=fin.read(); //read next '#'
		 	}
		    comment=tstr;
		    
		    //read third line of ImageHeader
		    //read cols
			if(iscomment==true) c=fin.read();
		    tstr+=(char)c;
		    while(c!=32&&c!=10&&c!=13)
		    {
		        c=fin.read();
		        tstr+=(char)c;
		 	}
		    tstr=tstr.substring(0,tstr.length()-1);
		    cols=Integer.parseInt(tstr);
		    
		    //read rows
			c=fin.read();
			tstr="";
		    tstr+=(char)c;
		    while(c!=32&&c!=10&&c!=13)
		    {
		        c=fin.read();
		        tstr+=(char)c;
		 	}
		    tstr=tstr.substring(0,tstr.length()-1);
		    rows=Integer.parseInt(tstr);
		    
		    //read maxgray
			c=fin.read();
			tstr="";
		    tstr+=(char)c;
		    while(c!=32&&c!=10&&c!=13)
		    {
		        c=fin.read();
		        tstr+=(char)c;
		 	}
		    tstr=tstr.substring(0,tstr.length()-1);
		    maxgray=Integer.parseInt(tstr);
		    
		    //read pixels from ImageData
			pixel=new int[rows][cols];
		    for(tr=0;tr<rows;tr++)
		    {
		    	for(tc=0;tc<cols;tc++)
		    	{
		    		c=(int)fin.read();
		    		setPixel(tr,tc,c);
		    	}
		    }
		    
			fin.close();
		}
		catch(Exception e)
		{
			System.out.println("Error: "+e.getMessage());
		}
	}

	public void writeImage()
	{
		FileOutputStream fout;
		
		try
		{
			fout=new FileOutputStream(tFilePath);
			
			//write image header
			//write PGM magic value 'P5'
			String tstr;
			tstr="P5"+"\n";
			fout.write(tstr.getBytes());
			
			//write comment
			comment=comment+"\n";
			//fout.write(comment.getBytes());

			//write cols
			tstr=Integer.toString(cols);
			fout.write(tstr.getBytes());
			fout.write(32); //write blank space
			
			//write rows
			tstr=Integer.toString(rows);
			fout.write(tstr.getBytes());
			fout.write(32); //write blank space
			
			//write maxgray
			tstr=Integer.toString(maxgray);
			tstr=tstr+"\n";
			fout.write(tstr.getBytes());
			
			for(int r=0;r<rows;r++)
			{
				for(int c=0;c<cols;c++)
				{
					fout.write(getPixel(r,c));
				}
			}

			fout.close();
		}
		catch(Exception e)
		{
			System.out.println("Error: "+e.getMessage());
		}
	}

	public void copyImage(String tstrOuttFilePath)
	{
		PGM imgout=new PGM();

		//create new image
		imgout.setFilePath(tstrOuttFilePath);
		imgout.setType(getType());
		imgout.setComment(getComment());
		imgout.setDimension(getCols(),getRows());
		imgout.setMaxGray(getMaxGray());
		for(int r=0;r<getRows();r++)
		{
			for(int c=0;c<getCols();c++)
			{
				imgout.setPixel(r,c,getPixel(r,c));
			}
		}
		imgout.writeImage();
	}
}
//-------------------------------------------------------------------------------------------------------------

//PGM_ImageFilter.java


class PGM_ImageFilter
{
	String inFilePath,outFilePath;
	boolean printStatus=false;

	//constructor
	public PGM_ImageFilter()
	{
		inFilePath="";
		outFilePath="";
	}

	//get functions
	public String get_inFilePath()
	{
		return(inFilePath);
	}
	
	public String get_outFilePath()
	{
		return(outFilePath);
	}
	
	//set functions
	public void set_inFilePath(String tFilePath)
	{
		inFilePath=tFilePath;
	}
	
	public void set_outFilePath(String tFilePath)
	{
		outFilePath=tFilePath;
	}

	//methods
	public void resize(int wout,int hout)
	{
		PGM imgin=new PGM();
		PGM imgout=new PGM();
	
		if(printStatus==true)
		{
			System.out.print("\nResizing...");
		}
		int r,c,inval,outval;
	
		//read input image
		imgin.setFilePath(inFilePath);
		imgin.readImage();
	
		//set output-image header
		imgout.setFilePath(outFilePath);
		imgout.setType("P5");
		imgout.setComment("#resized image");
		imgout.setDimension(wout,hout);
		imgout.setMaxGray(imgin.getMaxGray());
	
		//resize algorithm (linear)
		double win,hin;
		int xi,ci,yi,ri;
	
		win=imgin.getCols();
		hin=imgin.getRows();
	
		for(r=0;r<imgout.getRows();r++)
		{
			for(c=0;c<imgout.getCols();c++)
			{
				xi=c;
				yi=r;
	
				ci=(int)(xi*((double)win/(double)wout));
				ri=(int)(yi*((double)hin/(double)hout));
				
				inval=imgin.getPixel(ri,ci);
				outval=inval;
	
				imgout.setPixel(yi,xi,outval);
			}
		}
	
		if(printStatus==true)
		{
			System.out.println("done.");
		}
	
		//write output image
		imgout.writeImage();
	}
	
}
