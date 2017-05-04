package cn.softbank.purchase.widget.pagerindicator;

public abstract class PagerChangerListener {
	
	public static int changeIndictaor;
	public static int currentItem;
	
	public static void changePager(int change_indictaor,int item){
		changeIndictaor=change_indictaor;
		currentItem=item;
	}; 
	
	public void set(){};

	public abstract void onPagerChange(int changeIndictaor,int item);
}
