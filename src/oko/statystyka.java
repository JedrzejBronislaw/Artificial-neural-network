package oko;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class statystyka {

	public static void klastry_srJasnosc(String plikWe, String plikWy, int n){
		File fIn = new File(plikWe);
		File fOut= new File(plikWy);
		BufferedImage iIn;
		BufferedImage iOut;
		int w;
		int h;



		try{
			iIn = ImageIO.read(fIn);
		}catch(IOException e){
			e.printStackTrace();
			return;
		}
		w = iIn.getWidth();
		h = iIn.getHeight();

		int kw = w/n;
		int kh = h/n;



		iOut = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		long kolor = 0;

		for(int i=0; i<n; i++)
			for(int j=0; j<n; j++){
				kolor = 0;
				for(int xi=0; xi<kw; xi++)
					for(int xj=0; xj<kh; xj++)
						iIn.getRGB(i*kw+xi, j*kh+xj);
				//TODO !!! zasoni obraz prostok¹tami o œredniej jasnoœci
			}


	}
}
