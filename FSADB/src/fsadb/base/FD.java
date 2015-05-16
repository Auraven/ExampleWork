package fsadb.base;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

public class FD {
	private static File dbFolder;
	private static Hashtable<String, File> tableFolders;
	
	public static void main(String[] args){
		connect("testdb");
		for(Result r: executeQuery("SELECT * FROM users").getResults()){
			System.out.println("id: " + r.getString("id"));
			System.out.println("username: " + r.getString("username"));
			System.out.println("firstname: " + r.getString("firstname"));
			System.out.println("lastname: " + r.getString("lastname"));			
		}
	}
	
	public static ResultSet executeQuery(String query){
		query = query.replaceAll(" ", "");
		if(query.contains("select")){query.replace("select", "SELECT");}
		if(query.contains("where")){query.replace("where", "WHERE");}
		if(query.contains("from")){query.replace("from", "FROM");}
		if(query.contains("SELECT")){
			//SELECT * FROM table
			//SELECT username, password FROM table
			//SELECT * FROM table WHERE username = Auraven
			if(query.contains("WHERE")){
				
			}else{
				String[] args = query.split("FROM");
				String[] keys = args[0].replace("SELECT", "").split(",");
				String tableName = args[1];
				File tf = tableFolders.get(tableName);
				if(keys[0].equals("*")){
					return new ResultSet(tf.listFiles()[0]);
				}else{
					for(File pf: tf.listFiles()){
						
					}
				}				
			}
			return null;
		}
		return null;
	}
	public static boolean executeUpdate(String query){
		query = query.replaceAll(" ", "");
		if(query.contains("insert")){query.replace("insert", "INSERT");}
		if(query.contains("into")){query.replace("into", "INTO");}
		if(query.contains("values")){query.replace("values", "VALUES");}
		if(query.contains("create")){query.replace("create", "CREATE");}
		if(query.contains("table")){query.replace("table", "TABLE");}

		if(query.contains("INSERTINTO")){
			//INSERT INTO table VALUES(id:1,username:Auraven,firstname:Dylan,lastname:Harbin)
			String[] args = query.split("VALUES");
			String tableName = args[0].replace("INSERTINTO", "");
			String[] valuekeys = args[1].replace("(", "").replace(")", "").split(",");
			File tableFolder = tableFolders.get(tableName);			
			String[] passoc = valuekeys[0].split(":");
			File pkey = new File(tableFolder.getPath() + "/" + passoc[0]);
			if(!pkey.exists()){
				pkey.mkdir();				
			}			
			File pvalue = new File(pkey.getPath() + "/" + passoc[1]);
			if(!pvalue.exists()){
				pvalue.mkdir();
			}
			for(int i = 1; i < valuekeys.length; i++){
				String[] assoc = valuekeys[i].split(":");
				File key = new File(pvalue.getPath() + "/" + assoc[0]);
				key.mkdir();
				File value = new File(key.getPath() + "/" + assoc[1]);
				try {
					value.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					return false;					
				}
			}
			return true;		
		}else if(query.contains("CREATETABLE")){
			File tableFolder = new File(dbFolder.getPath() + "/" + query.replace("CREATETABLE", ""));
			if(!tableFolder.exists()){
				if(tableFolder.mkdir()){
					tableFolders.put(tableFolder.getName(), tableFolder);
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}			
		}
		return false;
	}
	public static int createDatabase(String dbFolderPath){
		dbFolder = new File(dbFolderPath);
		if(dbFolder.exists()){
			return 2;
		}else if(dbFolder.mkdir()){
			return 1;		
		}else{
			return 0;
		}
	}	
	public static boolean connect(String dbFolderPath){
		dbFolder = new File(dbFolderPath);
		tableFolders = new Hashtable<String, File>();
		if(dbFolder.exists() && dbFolder.isDirectory()){
			for(File tf: dbFolder.listFiles()){
				if(tf.isDirectory() && !tf.getName().contains("?")){
					tableFolders.put(tf.getName(), tf);
				}
			}
			return true;
		}
		return false;
	}	
	public static File getDBFolder(){
		return dbFolder;
	}
}
