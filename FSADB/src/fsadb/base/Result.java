package fsadb.base;

import java.io.File;
import java.util.Hashtable;

public class Result {
	private Hashtable<String, File> set;
	private File file;
	
	public Result(File pv){
		this.file = pv;
		set = new Hashtable<String, File>();
		set.put(pv.getParentFile().getName(), pv);
		for(File kf: pv.listFiles()){
			if(!kf.getName().startsWith("$")){
				set.put(kf.getName(), kf.listFiles()[0]);
			}
		}	
	}	
	public Result(File pv, String[] keys){
		set = new Hashtable<String, File>();
		for(String key: keys){
			if(key.equals(pv.getParentFile().getName())){
				set.put(pv.getParentFile().getName(), pv);
				break;
			}
		}		
		for(File kf: pv.listFiles()){
			if(!kf.getName().startsWith("$")){
				for(String key: keys){
					if(key.equals(kf.getName())){
						set.put(kf.getName(), kf.listFiles()[0]);
					}
				}
			}
		}
	}	
	public Hashtable<String, File> getSet(){
		return set;
	}
	public String getString(String field){
		return set.get(field).getName();
	}
	public File getFile(String field){
		return set.get(field);
	}
	public File getFile(){
		return file;
	}
}
