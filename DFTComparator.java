import java.util.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class DFTComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			DFT p1 = (DFT) o1;
			DFT p2 = (DFT) o2;

			double p1Mag = Math.sqrt(Math.pow(p1.re, 2) + Math.pow(p1.im, 2));
			double p2Mag = Math.sqrt(Math.pow(p2.re, 2) + Math.pow(p2.im, 2));

			if(p1Mag < p2Mag)
				return 1;
			else if(p1Mag == p2Mag)
				return 0;
			else
				return -1;
		}
}