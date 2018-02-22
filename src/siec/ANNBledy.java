package siec;

public interface ANNBledy {
	public double bladSredni(float[][] zbiorTestowy);
	public double blad(float[] przykladTestowy);
	public double pochBladu(float[] przykladTestowy);
	double bladWzgledny(float[] przykladTestowy);
	double sredniBladWzgledny(float[][] zbiorTestowy);
}
