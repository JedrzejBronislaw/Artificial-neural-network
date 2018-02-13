package oko;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import javafx.scene.image.WritableImage;
import siec.ANN;
import siec.ANNPreview;
import siec.ANN_matrix;
import siec.ANN_matrix_preview;
import siec.UczenieSieci;

public class Oko {

    public Oko() {
//        try{
//        go();
//        } catch (Exception e)
//        {
//            System.out.println("Blad: " + e.getMessage());
//                e.printStackTrace();
//        }

    	int n = 1;
        float[][] daneTestowe = pobierzDaneTestowe("0004.jpg","odp.png",n);

//        for(float[] przyklad : daneTestowe){
//            for(float f : przyklad)
//                System.out.print(f + ", ");
//            System.out.println();
//        }
        ANN siec = new ANN(new int[]{(int)Math.pow(n*2+1,2),10,1});
        UczenieSieci uczenieSieci = new UczenieSieci(siec);
        ANNPreview preview = new ANN_matrix_preview(siec);

        uczenieSieci.setPrzykladyTestowe(daneTestowe);
        uczenieSieci.setPrzykladyUczace(daneTestowe);

        preview.showWeights();

        System.out.println("Uczenie...");
        long time = System.nanoTime();
        uczenieSieci.ucz(10);
        uczenieSieci.setTasowanie(true);
        time = System.nanoTime()-time;
        System.out.println((time/1000000)/1000.0 + "s");
        System.out.println();

        preview.showWeights();

        wykryjZyly("0004.jpg","zyly.png",siec,n);
    }


    private void wykryjZyly(String string, String string2, ANN siec, int n) {
        File file = new File(string);
        File fOut = new File(string2);
        BufferedImage img = null;

        System.out.println(file.getAbsolutePath());

        try{
            img = ImageIO.read(file);
        } catch (IOException e)
        {
           System.out.println("Nie mo¿na zaladowac pliku");
           return;
        }

//		img.getRaster().getDataBuffer().get

//		WritableRaster wRas = null;//new WritableImage(img.getHeight(), img.getWidth());
//		img.copyData(wRas);
//		BufferedImage img2 = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
////		img.setData(wRas);
//		wRas.


//		System.out.println(img.getData().getNumBands());
//		img.getData().

        BufferedImage img2 = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);


        Graphics2D g = (Graphics2D) img2.getGraphics();
        int w = img.getWidth();
        int h = img.getHeight();
        int v;
        g.setColor(Color.RED);
        Color c = new Color(0);
        Random r = new Random();
        float[] otocznie;
        float pojedynczyWynikSieci;
        int col;
        int max = 0;

        for (int i=0; i<w; i++)
            for (int j=0; j<h; j++){

            	otocznie = new float[81];
            	pobiezOtoczenie(img, i, j, otocznie,n);

            	pojedynczyWynikSieci = siec.licz(otocznie);
            	col = (int) pojedynczyWynikSieci;
//            	col = Math.min(col, 255);
            	col = Math.max(col, 0);

            	if (max < col) max = col;
            }

        System.out.println("max = "+max);

        for (int i=0; i<w; i++)
            for (int j=0; j<h; j++){
//                v = img.getRGB(i, j);
//                c = new Color(v);
//                if (c.getBlue() < 64)
//                    g.setColor(Color.BLACK);
//                else
//                if (c.getBlue() < 128)
//                    g.setColor(Color.BLUE);
//                else
//                if (c.getBlue() < 192)
//                    g.setColor(Color.GREEN);
//                else
////				if (c.getBlue() < )
//                    g.setColor(Color.RED);

            	otocznie = new float[81];
            	pobiezOtoczenie(img, i, j, otocznie,n);

            	pojedynczyWynikSieci = siec.licz(otocznie)/(float)max*255;
            	col = Math.min((int) pojedynczyWynikSieci, 255);
            	col = Math.max(col, 0);
//            	System.out.println(col);
            	g.setColor(new Color(col,col,col));//r.nextInt()));

                g.drawRect(i, j, 1, 1);
//				System.out.println(v);
            }



//		g.drawString("HELLO!", 0, img.getHeight()/2);

        try {
			ImageIO.write(img2, "png", fOut);
		} catch (IOException e) {
			System.out.println("nie mozna zapisac wynikow sieci");
			e.printStackTrace();
		}


	}


	private float[][] pobierzDaneTestowe(String string, String string2, int n) {
        float[][] wynik;
        File fWe = new File(string);
        File fOdp = new File(string2);
        BufferedImage iWe = null;
        BufferedImage iOdp = null;

        try{
	        iWe = ImageIO.read(fWe);
	        iOdp= ImageIO.read(fOdp);
        }catch(IOException e)
        {
            System.out.println("Nie mo¿na pobrac plikow");
        }
        int w = iWe.getWidth();
        int h = iWe.getHeight();

        wynik = new float[w*h][];

//        Graphics2D gWe = (Graphics2D) iWe.getGraphics();
//        Graphics2D gOdp= (Graphics2D) iOdp.getGraphics();
//        int v;
//        g.setColor(Color.RED);
//        Color c = new Color(0);

        for (int i=0; i<w; i++)
            for (int j=0; j<h; j++){
            	wynik[i*h+j] = pobierzJednePrzyklad(iWe,iOdp,i,j,n);
//                v = iWe.getRGB(i, j);
//                c = new Color(v);
//                if (c.getBlue() < 64)
//                    g.setColor(Color.BLACK);
//                else
//                if (c.getBlue() < 128)
//                    g.setColor(Color.BLUE);
//                else
//                if (c.getBlue() < 192)
//                    g.setColor(Color.GREEN);
//                else
////				if (c.getBlue() < )
//                    g.setColor(Color.RED);
//
//                g.drawRect(i, j, 1, 1);
//				System.out.println(v);
            }


        return wynik;
    }


    private float[] pobierzJednePrzyklad(BufferedImage iWe, BufferedImage iOdp, int x, int y, int n) {
    	float[] wynik = new float[(int)Math.pow(n*2+1, 2)+1];
    	int v;
        float color;
//      g.setColor(Color.RED);

        Color c;
      pobiezOtoczenie(iWe, x,y, wynik, n);
        	  v = iOdp.getRGB(x, y);
              c = new Color(v);
              color = (c.getRed()+c.getGreen()+c.getBlue())/3f;

          wynik[wynik.length-1] = color;
          return wynik;
//	}
    }

	private void pobiezOtoczenie(BufferedImage iWe, int x, int y, float[] wynik, int n) {
    	int v;
        float color;
      Color c = new Color(0);
      int ii,jj;
      int szer = n*2+1;
      
	      for (int i=0; i<szer; i++)
	          for (int j=0; j<szer; j++){
	        	  ii = i+x-n;
	        	  jj = j+y-n;
//	          	wynik[i*h+j] = pobierzJednePrzyklad(iWe,iOdp,i,j);
	              if (ii<0 || jj<0 || ii>=iWe.getWidth() || jj>=iWe.getHeight()){
//	            	  wynik
	            	  continue;
	              }
	        	  try{
	        	  v = iWe.getRGB(ii, jj);
	        	  }catch(Exception e){
	        		  System.out.println("error get: ii="+ii+" jj="+jj);
	        		  return ;
	        	  }
	              c = new Color(v);
	              color = (c.getRed()+c.getGreen()+c.getBlue())/3f;


	              try{
	              wynik[i*szer+j] = color;
	              }catch(Exception e){
	            	  System.out.println("error tab: i="+i+" j="+j);
	            	  return ;
	              }
//	              if (c.getBlue() < 64)
//	                  g.setColor(Color.BLACK);
//	              else
//	              if (c.getBlue() < 128)
//	                  g.setColor(Color.BLUE);
//	              else
//	              if (c.getBlue() < 192)
//	                  g.setColor(Color.GREEN);
//	              else
////					if (c.getBlue() < )
//	                  g.setColor(Color.RED);
	//
//	              g.drawRect(i, j, 1, 1);
//					System.out.println(v);
	          }
	}


	private void go() throws Exception {
        File file = new File("0004.jpg");
        File fOut = new File("out.png");
        BufferedImage img = null;

        System.out.println(file.getAbsolutePath());

        try{
            img = ImageIO.read(file);
        } catch (IOException e)
        {
            throw new Exception("Nie mo¿na zaladowac pliku");
        }

//		img.getRaster().getDataBuffer().get

//		WritableRaster wRas = null;//new WritableImage(img.getHeight(), img.getWidth());
//		img.copyData(wRas);
//		BufferedImage img2 = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
////		img.setData(wRas);
//		wRas.


//		System.out.println(img.getData().getNumBands());
//		img.getData().

        BufferedImage img2 = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);


        Graphics2D g = (Graphics2D) img2.getGraphics();
        int w = img.getWidth();
        int h = img.getHeight();
        int v;
        g.setColor(Color.RED);
        Color c = new Color(0);

        for (int i=0; i<w; i++)
            for (int j=0; j<h; j++){
                v = img.getRGB(i, j);
                c = new Color(v);
                if (c.getBlue() < 64)
                    g.setColor(Color.BLACK);
                else
                if (c.getBlue() < 128)
                    g.setColor(Color.BLUE);
                else
                if (c.getBlue() < 192)
                    g.setColor(Color.GREEN);
                else
//				if (c.getBlue() < )
                    g.setColor(Color.RED);

                g.drawRect(i, j, 1, 1);
//				System.out.println(v);
            }



//		g.drawString("HELLO!", 0, img.getHeight()/2);

        ImageIO.write(img2, "png", fOut);


    }



}
