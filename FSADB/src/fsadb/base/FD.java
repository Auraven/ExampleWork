package fsadb.base;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

public class FD {
	private static File dbFolder;
	private static Hashtable<String, File> tableFolders;

	public static ResultSet execute(String query){
		//ACTION<>TABLE<>KEYS<>VALUES
		query = formatQuery(query);
		String[] args = query.split("<>");
		File tf = null, config = null;
		String[] keys = null, values = null;
		switch(args[0]){
		case "SELECT":
			tf = tableFolders.get(args[1]);
			config = tf.listFiles()[0];
			keys = args[2].split(">>");
			values = null;
			if(args.length == 4){
				values = args[3].split(">>");
			}
			ResultSet set = null;
			if(keys[0].equals("*")){
				set = new ResultSet(tf.listFiles()[1], config);
			}else{
				set = new ResultSet(tf.listFiles()[1], keys, config);
			}
			if(values != null && set != null){
				for(int i = 0; i < set.getResults().size(); i++){
					Result r = set.getResults().get(i);
					for(String value: values){
						String[] assoc = value.split("->");
						if(!r.getString(assoc[0]).equals(assoc[1])){
							set.getResults().remove(i);
							i--;
							break;
						}
					}						
				}
				return set;
			}else{
				return set;
			}
		case "CREATE": //"CREATE<>tablename<>keys<>values"
			tf = new File(dbFolder.getPath() + "/" + args[1]);
			if(tf.mkdir()){
				config = new File(tf.getPath() + "/$config");
				if(config.mkdir()){
					keys = args[2].split(">>");
					values = null;
					if(args.length == 4){
						values = args[3].split(">>");
					}
					File pk = new File(config.getPath() + "/" + keys[0]);
					File vf = null;					
					if(pk.mkdir()){
						if(values != null){
							vf = new File(pk.getPath() + "/" + values[0]);
							try {
								vf.createNewFile();
							} catch (IOException e) {}
						}
						for(int i = 1; i < keys.length; i++){
							pk = new File(pk.getPath() + "/" + keys[i]);
							pk.mkdir();
							if(values != null){
								try{
									vf = new File(pk.getPath() + "/" + values[i]);
									try {
										vf.createNewFile();
									} catch (IOException e) {}
								}catch(Exception e){}								
							}
						}
						tableFolders.put(tf.getName(), tf);
					}					
				}
			}
			return null;
		case "INSERT": //"INSERT<>tablename<>values"
			tf = tableFolders.get(args[1]);
			config = tf.listFiles()[0];
			values = args[2].split(">>");
			File cpk = config.listFiles()[0];
			File pk = new File(tf.getPath() + "/" + cpk.getName());
			if(!pk.exists()){
				pk.mkdir();
			}
			File pv = new File(pk.getPath() + "/" + parseValue(pk, values[0]));
			if(pv.mkdir()){
				int i = 1;
				while(cpk.listFiles().length > 0){
					cpk = cpk.listFiles()[0];
					File kf = new File(pv.getPath() + "/" + cpk.getName());
					if(kf.mkdir()){
						File vf = new File(kf.getPath() + "/" + parseValue(kf, values[i]));
						try {
							vf.createNewFile();
							i++;
						} catch (IOException e) {}							
					}
				}
			}
			return null;
		case "DELETE": //"DELETE<>tablename<>values"
			tf = tableFolders.get(args[1]);
		    config = tf.listFiles()[0];
		    values = args[2].split(">>");
		    set = new ResultSet(tf.listFiles()[1], config);
		    for(int i = 0; i < set.getResults().size(); i++){
				Result r = set.getResults().get(i);
				boolean delete = true;
				for(String value: values){
					String[] assoc = value.split("->");
					if(!r.getString(assoc[0]).equals(assoc[1])){
						delete = false;
						break;
					}
				}						
				if(delete){
					deleteFolder(r.getFile());
				}
			}
			return null;
		case "DROP": //"DROP<>tablename"
			tf = tableFolders.get(args[1]);
			tableFolders.remove(tf);
			deleteFolder(tf);
			return null;
		}		
		return null;
	}
	private static String parseValue(File pk, String value){
		switch(value){
		case "AUTONUMBER":
			return pk.listFiles().length + 1 + ""; 
		}
		return value;
	}
	public static ResultSet executeQuery(String query){
		query = formatQuery(query);			
		
		if(query.contains("SELECT")){			
			if(query.contains("WHERE")){
				//SELECT * FROM table WHERE username=Auraven
				String[] left = query.split("FROM");
				String[] right = left[1].split("WHERE");
				String[] keys = left[0].replace("SELECT", "").split(",");
				String tableName = right[0];
				String[] values = right[1].split(",");
				File tf = tableFolders.get(tableName);
				File config = tf.listFiles()[0];
				ResultSet set = null;
				if(keys[0].equals("*")){
					set = new ResultSet(tf.listFiles()[1], config);
				}else{
					set = new ResultSet(tf.listFiles()[1], keys, config);
				}
				if(set != null){
					for(int i = 0; i < set.getResults().size(); i++){
						Result r = set.getResults().get(i);
						for(String value: values){
							String[] assoc = value.split("=");
							if(!r.getString(assoc[0]).equals(assoc[1])){
								set.getResults().remove(i);
								i--;
								break;
							}
						}						
					}
					return set;
				}
			}else{
				String[] args = query.split("FROM");
				String[] keys = args[0].replace("SELECT", "").split(",");
				String tableName = args[1];
				File tf = tableFolders.get(tableName);
				File config = tf.listFiles()[0];
				if(keys[0].equals("*")){
					//SELECT * FROM table
					return new ResultSet(tf.listFiles()[1], config);
				}else{
					//SELECT username, password FROM table
					return new ResultSet(tf.listFiles()[1], keys, config);
				}				
			}
		}
		return null;
	}
	public static boolean executeUpdate(String query){
		query = formatQuery(query);	

		if(query.contains("INSERTINTO")){
			//INSERT INTO table VALUES(id:1,username:Auraven,firstname:Dylan,lastname:Harbin)
			String[] args = query.split("VALUES");
			String tableName = args[0].replace("INSERTINTO", "");
			String[] valuekeys = args[1].replace("(", "").replace(")", "").split(",");
			File tableFolder = tableFolders.get(tableName);	
			File configFolder = tableFolder.listFiles()[0];
			String[] passoc = valuekeys[0].split(":");
			File pkey = new File(tableFolder.getPath() + "/" + passoc[0]);
			if(!pkey.exists()){
				pkey.mkdir();				
			}	
			File pvalue = null;
			if(passoc[1].equals("AUTONUMBER")){
				pvalue = new File(pkey.getPath() + "/" + (pkey.listFiles().length + 1));
				
			}else{
				pvalue = new File(pkey.getPath() + "/" + passoc[1]);				
			}			
			if(pvalue != null){
				if(!pvalue.exists()){
					pvalue.mkdir();
				}
				for(int i = 1; i < valuekeys.length; i++){
					String[] assoc = valuekeys[i].split(":");
					File key = new File(pvalue.getPath() + "/" + assoc[0]);
					key.mkdir();
					File value = new File(key.getPath() + "/" + assoc[1]);
					try {
						try{
							if(assoc[2] != null){
								String content = assoc[2];
								if(content.equals("FOLDER")){
									value.mkdir();
								}else{
									value.createNewFile();
									PrintWriter fileOut = new PrintWriter(value);
									fileOut.print(content);
									fileOut.close();
								}
							}
						}catch(ArrayIndexOutOfBoundsException aioobe){
							value.createNewFile();
						}				
					} catch (IOException e) {
						e.printStackTrace();
						return false;					
					}
				}
			}			
			return true;		
		}else if(query.contains("DELETEFROM")){
			//DELETE FROM table WHERE key=value
			if(query.contains("WHERE")){
				String[] args = query.split("WHERE");
				String tableName = args[0].replace("DELETEFROM", "");
				File tf = tableFolders.get(tableName);
				File config = tf.listFiles()[0];
				String[] values = args[1].split(",");
				ResultSet set = new ResultSet(tf.listFiles()[1], config);
				for(int i = 0; i < set.getResults().size(); i++){
					Result r = set.getResults().get(i);
					boolean delete = true;
					for(String value: values){
						String[] assoc = value.split("=");
						if(!r.getString(assoc[0]).equals(assoc[1])){
							delete = false;
							break;
						}
					}						
					if(delete){
						deleteFolder(r.getFile());
					}
				}
			}
		}else if(query.contains("CREATETABLE")){
			//CREATE TABLE table VALUES()
			String[] args = query.split("VALUES");
			args[0] = args[0].replace("CREATETABLE", "");
			File tableFolder = new File(dbFolder.getPath() + "/" + args[0]);
			if(!tableFolder.exists()){
				if(tableFolder.mkdir()){
					tableFolders.put(tableFolder.getName(), tableFolder);
					File configFolder = new File(tableFolder.getPath() + "/$config");
					if(configFolder.mkdir()){
						String[] valuekeys = args[1].replace("(", "").replace(")", "").split(",");
						String path = configFolder.getPath() + "/" + valuekeys[0];
						for(int i = 1; i < valuekeys.length; i++){
							path += "FSA" + valuekeys[i];
							
						}
						try {
							new File(path).createNewFile();
						} catch (IOException e){}					
					}
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}			
		}else if(query.contains("DROPTABLE")){
			query = query.replace("DROPTABLE", "");
			File tableFolder = tableFolders.get(query);
			tableFolders.remove(tableFolder);
			deleteFolder(tableFolder);			
		}
		return false;
	}	
	private static String formatQuery(String query){
		String temp_query = "";
		String[] targs = query.split("'");
		for(int i = 0; i < targs.length; i++){
			if(i%2 == 0){
				if(query.contains("select")){query.replace("select", "SELECT");}
				if(query.contains("where")){query.replace("where", "WHERE");}
				if(query.contains("from")){query.replace("from", "FROM");}
				if(query.contains("insert")){query.replace("insert", "INSERT");}
				if(query.contains("delete")){query.replace("delete", "DELETE");}
				if(query.contains("into")){query.replace("into", "INTO");}
				if(query.contains("values")){query.replace("values", "VALUES");}
				if(query.contains("create")){query.replace("create", "CREATE");}
				if(query.contains("table")){query.replace("table", "TABLE");}
				temp_query += targs[i].replaceAll(" ", "");
			}else{
				temp_query += targs[i];
			}
		}
		query = temp_query;
		return query;
	}
	public static void deleteFolder(File cur){
		if(cur.isDirectory()){
			for(File sub: cur.listFiles()){
				deleteFolder(sub);
			}
			cur.delete();
		}else{
			cur.delete();
		}
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
