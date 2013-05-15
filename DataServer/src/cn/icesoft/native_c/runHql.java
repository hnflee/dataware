package cn.icesoft.native_c;

public class runHql {
	
	static
	{
		System.loadLibrary("runHqllib");
	}
	
	public native static void run(String hql,String uuid);
	
	

}
