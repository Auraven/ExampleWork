package fsadb.base;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ResultSet {
	private ArrayList<Result> results;
	private File config;
	public ResultSet(File pk, File config){		
		this.config = config;
		results = new ArrayList<Result>();
		for(File pv: pk.listFiles()){
			if(!pv.getName().startsWith("$")){
				results.add(new Result(pv));
			}
		}
	}	
	public ResultSet(File pk, String[] keys, File config){
		this.config = config;
		results = new ArrayList<Result>();
		for(File pv: pk.listFiles()){
			if(!pv.getName().startsWith("$")){
				Result r = new Result(pv, keys);
				if(!r.getSet().isEmpty()){
					results.add(r);
				}				
			}
		}
	}		
	public ArrayList<Result> getResults(){
		return results;
	}
	public void showTable(){
		JFrame frame = new JFrame();	
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JScrollPane pane = new JScrollPane();		
		JTable table = new JTable();
		DefaultTableModel model = new DefaultTableModel();
		boolean modelSet = false;
		for(Result r: results){
			if(!modelSet){
				ArrayList<String> inOrder = getInOrder(r.getSet());
				for(String key: inOrder){					
					model.addColumn(key);
				}
				modelSet = true;
			}			
			String[] rowData = new String[r.getSet().keySet().size()];
			for(int i = 0; i < r.getSet().keySet().size(); i++){
				rowData[i] = r.getString(model.getColumnName(i));
			}
			model.addRow(rowData);
		}
		table.setModel(model);
		pane.setViewportView(table);
		frame.add(pane);
		frame.setSize(100*model.getColumnCount(), 300);
		frame.setVisible(true);
	}
	private ArrayList<String> getInOrder(Hashtable<String, File> set){
		ArrayList<String> keys = new ArrayList<String>();
		File cpk = config.listFiles()[0];
		while(cpk.listFiles().length > 0){
			keys.add(cpk.getName());
			cpk = cpk.listFiles()[0];
		}		
		keys.add(cpk.getName());
		return keys;
	}
	public JTable getTable(){
		JTable table = new JTable();
		DefaultTableModel model = new DefaultTableModel();
		boolean modelSet = false;
		for(Result r: results){
			if(!modelSet){
				for(int i = r.getSet().keySet().size() - 1; i >= 0; i--){
					model.addColumn(r.getSet().keySet().toArray()[i]);
				}				
			}
			modelSet = true;
			String[] rowData = new String[r.getSet().keySet().size()];
			for(int i = 0; i < r.getSet().keySet().size(); i++){
				rowData[i] = r.getString(model.getColumnName(i));
			}
			model.addRow(rowData);
		}
		table.setModel(model);
		return table;
	}
}
