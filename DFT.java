public class DFT {

	double re;
	double im;
	int freq;

	public DFT() {
		this.re = 0.0;
		this.im = 0.0;
		this.freq = 0;
	}
	public DFT(double re, double im, int freq) {
		this.re = re;
		this.im = im;
		this.freq = freq;
	}
}