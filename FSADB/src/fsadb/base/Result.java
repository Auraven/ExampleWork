package fsadb.base;

import java.io.File;
import java.util.Hashtable;

public class Result {
	private Hashtable<String, File> set;
	
	public Result(File pv){
		set = new Hashtable<String, File>();
		set.put(pv.getParentFile().getName(), pv);
		for(File kf: pv.listFiles()){
			set.put(kf.getName(), kf.listFiles()[0]);
		}
	}	
	public Hashtable<String, File> getSet(){
		return set;
	}
	public String getString(String field){
		return set.get(field).getName();
	}
}
