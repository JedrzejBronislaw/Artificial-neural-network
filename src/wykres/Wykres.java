package wykres;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferStrategy;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

public class Wykres extends JPanel {

	class LegendaRecord{
		public LegendaRecord(String label, Color color) {
			this.color = color;
			this.label = label;
		}
		Color color;
		String label;
	}

	private String nazwaPliku;
	private int rozdzielczosc = 100;
	private int szer = 300, wys = 300;
	private int szerOpisow = 100;
	private int dlKresekNaOsi = 5;
	private int x1,y1, x2,y2; // obszar wykresu

	private float minX, maxX, minY, maxY;
	private int n;
	private String[] kolumny;
	private float[][] wartosci;
	private List<LegendaRecord> legenda = new LinkedList<>();

	public Wykres(String nazwaPliku) {
		this.nazwaPliku = nazwaPliku;
		setSize(szer+szerOpisow, wys+szerOpisow);
		zaladujWartosci();
	}

	public void setRozdzielczosc(int rozdzielczosc) {
		this.rozdzielczosc = rozdzielczosc;
	}

	private void zaladujWartosci() {
		File file = new File(nazwaPliku);
		BufferedInputStream bis = null;
		DataInputStream dis = null;

		try{
			bis = new BufferedInputStream(new FileInputStream(file));
			dis = new DataInputStream(bis);
		}catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}

//		char c1,c2,c3,c4;
//		c1=c2=c3=c4 = 0;
		String linia;
//		int i = 0;
//		do{
//			i++;
//			try {
//				c1=c2;
//				c2=c3;
//				c3=c4;
//				c4 = dis.readChar();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				System.out.println("i="+i);
//				return;
//			}
//		}while(!(c1=='-' && c2=='-' && c3=='-' && c4=='\n'));
//
//		System.out.println("i="+i);
//

		try{
		linia = czytajDo(dis,'\n');
//		System.out.println(linia.length());
		linia = czytajDo(dis,'\n');
//		System.out.println(linia.length());
//		linia = czytajDo(dis,'\n');
//		System.out.println(linia.length());
//		linia = czytajDo(dis,'\n');
//		System.out.println(linia.length());
//		linia = czytajDo(dis,'\n');
//		System.out.println(linia.length());

//		System.out.println((int)'\n');
//		System.out.println((int)'-');

		linia = czytajDo(dis,'\n');
		}catch(IOException e){return;}
//		System.out.println(linia);
		kolumny = linia.split(";");
//		for(String kolumna : kolumny)
//			System.out.println(" -> " + kolumna);

		List<float[]> wszystkie = new ArrayList<>();

		String[] tempWart;
		float[] rekord;
		while(true){
			try {
				linia = czytajDo(dis,'\n');
			} catch (IOException e) {
				break;
			}
//			System.out.println(linia);
			tempWart = linia.split(";");
			rekord = new float[tempWart.length];

			for(int i=0;i<tempWart.length;i++)
				rekord[i] = Float.parseFloat(tempWart[i]);

			wszystkie.add(rekord);
		}


		//przenoszenie do tablicy wartosci
		wartosci = new float[rozdzielczosc][];
		float przeskok = (float)(wszystkie.size()-1)/(rozdzielczosc-1);

		minX = minY = Float.MAX_VALUE;
		maxX = maxY = Float.MIN_VALUE;

		for(int i=0; i<rozdzielczosc; i++){
			wartosci[i] = wszystkie.get((int)(przeskok*i));

//			String.join("-", wszystkie.get((int)(przeskok*i)));
//			for(int j=0;j<wartosci[i].length;j++)
//				System.out.print(wartosci[i][j]+" | ");
//			System.out.println();

			if (minX > wartosci[i][0]) minX=wartosci[i][0];
			if (maxX < wartosci[i][0]) maxX=wartosci[i][0];

			for(int j=1;j<wartosci[i].length; j++){
				if (minY > wartosci[i][j]) minY=wartosci[i][j];
				if (maxY < wartosci[i][j]) maxY=wartosci[i][j];
			}
		}

//		System.out.println("Liczba wszytkich wpisow: " + wszystkie.size());
//		System.out.println("Liczba wybranych wpisow: " + wartosci.length);
//		System.out.println("min X: " + minX);
//		System.out.println("max X: " + maxX);
//		System.out.println("min Y: " + minY);
//		System.out.println("max Y: " + maxY);
//		System.out.println("Koniec");
	}

	private String czytajDo(DataInputStream dis, char koniec) throws IOException {
		StringBuffer sb = new StringBuffer();
		byte c;

		while(true) {
//			try {
				c = dis.readByte();//Char();
//				System.out.println("|"+c+"|"+(int)c);
//			} catch (IOException e) {break;}

			if (c == koniec)
				break;
			else
				sb.append((char)c);
		};

		return sb.toString();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		rysuj((Graphics2D)g);
	}

	public void rysuj(Graphics2D g){
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rysujObszar(g);
		rysujWykres(g);
		rysujLegende(g,szerOpisow+szer+50, 50);
	}

	private void rysujObszar(Graphics2D g) {
		int wysTekstu = g.getFontMetrics().getHeight()-g.getFontMetrics().getDescent();
		x1 = szerOpisow;
		y1 = 0;
		x2 = szerOpisow+szer;
		y2 = wys;

		g.drawRect(szerOpisow, 0, szer, wys);

		g.drawLine(szerOpisow-dlKresekNaOsi, 0, szerOpisow, 0);
		g.drawLine(szerOpisow-dlKresekNaOsi, wys, szerOpisow, wys);
		g.drawLine(szerOpisow, wys+dlKresekNaOsi, szerOpisow, wys);
		g.drawLine(szerOpisow+wys, wys+dlKresekNaOsi, szerOpisow+wys, wys);


		g.drawString(maxY+""/*"100%"*/, x1-szerOpisow/2, y1 +wysTekstu);
//		g.drawString(minY+""/*"0%"*/, x1-szerOpisow/2, y2 +wysTekstu);
		g.drawString(minX+""/*"0%"*/, x1, y2+szerOpisow/2 +wysTekstu);
		g.drawString(maxX+""/*"100%"*/, x2, y2+szerOpisow/2 +wysTekstu);

		float zakres = (minY<0) ? maxY-minY : maxY;
		float poziomZero = (minY<0) ? -minY : 0;//(zakres-maxY)/zakres;

		g.drawString("0", x1-szerOpisow/2, (int)((maxY/zakres)*wys) +wysTekstu);
		g.drawLine(szerOpisow-dlKresekNaOsi, (int)((maxY/zakres)*wys), szerOpisow, (int)((maxY/zakres)*wys));

		g.drawString("1", x1-szerOpisow/2, (int)(((zakres-poziomZero-1)/zakres)*wys) +wysTekstu);
		g.drawLine(szerOpisow-dlKresekNaOsi, (int)(((zakres-poziomZero-1)/zakres)*wys), szerOpisow, (int)(((zakres-poziomZero-1)/zakres)*wys));

		g.drawString(minY+"", x1-szerOpisow/2, (int)(((zakres-poziomZero-minY)/zakres)*wys) +wysTekstu);
		g.drawLine(szerOpisow-dlKresekNaOsi, (int)(((zakres-poziomZero-minY)/zakres)*wys), szerOpisow, (int)(((zakres-poziomZero-minY)/zakres)*wys));

//		g.drawString(wysTekstu+"", 150, 150);
	}

	private void rysujWykres(Graphics2D g) {
		Color[] colors = new Color[]{Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.PINK, Color.CYAN};
		Color color;
		Random r = new Random();
		int x,y;
		int px,py;
		float zakres = (minY<0) ? maxY-minY : maxY;
		float poziomZero = (minY<0) ? -minY : 0;//(zakres-maxY)/zakres;
		
		g.setStroke(new BasicStroke(3));
		legenda = new LinkedList<>();

		for(int k=kolumny.length-1;k>=1;k--){
			if (k<=colors.length)
				color = (colors[k-1]);
			else
				color = (new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
			g.setColor(color);
			legenda.add(new LegendaRecord(kolumny[k],color));

			px=py=Integer.MAX_VALUE;

			for(int i=0;i<rozdzielczosc;i++){
				x = (int)((szer-2)*wartosci[i][0]/maxX) + szerOpisow + 1;
				y = (int)((wys-2) *(1-(wartosci[i][k]+poziomZero)/zakres/*maxY*/)) +1;

//				g.drawRect(x, y, 3, 3);
				if (px != Integer.MAX_VALUE)
					g.drawLine(px, py, x, y);
				px=x;
				py=y;
			}

		}
	}

	private void rysujLegende(Graphics2D g, int x, int y) {
//		int x,y;
//		x=y=0;
		int wysRekordu=15;
		int rozmKwadratu = 10;

		for(int i=0;i<legenda.size();i++){
			g.setColor(legenda.get(i).color);
			g.fillRect(x, y+wysRekordu*i, rozmKwadratu, rozmKwadratu);
			g.setColor(Color.BLACK);
			g.drawString(legenda.get(i).label, x+rozmKwadratu+10, y+wysRekordu*i+rozmKwadratu);

		}
	}
}
