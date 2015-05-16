package fsadb.base;

import java.io.File;
import java.util.ArrayList;

public class ResultSet {
	private ArrayList<Result> results;
	
	public ResultSet(File pk){
		results = new ArrayList<Result>();
		for(File pv: pk.listFiles()){
			results.add(new Result(pv));
		}
	}	
	public ArrayList<Result> getResults(){
		return results;
	}
}
