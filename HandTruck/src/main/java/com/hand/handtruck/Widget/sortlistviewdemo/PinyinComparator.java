package  com.hand.handtruck.Widget.sortlistviewdemo;

import com.hand.handtruck.bean.CompanyTruckGroupBean;

import java.util.Comparator;

/**
 *
 * @author
 *
 */
public class PinyinComparator implements Comparator<CompanyTruckGroupBean> {

	public int compare(CompanyTruckGroupBean o1, CompanyTruckGroupBean o2) {
		if (o1.getLetters().equals("@")
				|| o2.getLetters().equals("#")) {
			return -1;
		} else if (o1.getLetters().equals("#")
				|| o2.getLetters().equals("@")) {
			return 1;
		} else {
			return o1.getLetters().compareTo(o2.getLetters());
		}
	}

}
