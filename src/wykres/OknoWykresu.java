package wykres;

import javax.swing.JFrame;

public class OknoWykresu extends JFrame {
	private static final long serialVersionUID = 94907498385851658L;
	Wykres wykres;
	private String nazwaPliku;
	
	public OknoWykresu(String nazwaPliku) {

		super("Wykres");
		this.nazwaPliku = nazwaPliku;
		
		setSize(550, 430);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		addComponents();
		
		setVisible(true);
	}

	private void addComponents() {
		wykres = new Wykres(nazwaPliku);//("daneDoWykresu.txt");
		add(wykres);
	}
}
