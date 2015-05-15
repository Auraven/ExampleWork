package lcms.panels;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import lcms.accounts.Manager;
import lcms.accounts.Student;
import lcms.base.C;

public class LogP extends JPanel{
	private JScrollPane scrollpane;
	private JTable logTable;
	private DefaultTableModel model;
	private MenuP menu;
	public LogP(MenuP menu){
		setSize(menu.getWidth(), menu.getHeight());
		this.menu = menu;
		scrollpane = new JScrollPane();
		scrollpane.setSize(getWidth(), getHeight());
		logTable = new JTable();
		logTable.setSize(scrollpane.getWidth(), scrollpane.getHeight());
		model = new DefaultTableModel();
		update();
		scrollpane.add(logTable);
		scrollpane.setViewportView(logTable);
		add(scrollpane);
		//update();
	}
	
	public void update(){				
		if(menu.acc instanceof Manager){
			model.addColumn("Log");
			model.addColumn("Username");
			model.addColumn("Time");	
			ResultSet logSet = C.select("logs", "*");
			try {
				while(logSet.next()){
					model.addRow(new String[]{logSet.getString("log"), logSet.getString("u_name"), logSet.getString("time")});
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else if(menu.acc instanceof Student){
			model.addColumn("Score");
			model.addColumn("Testname");
			model.addColumn("Passed");	
			model.addColumn("Time");
			ResultSet scoreSet = C.selectWhere("scores", "*", "username", menu.acc.getUsername());
			try {
				while(scoreSet.next()){
					System.out.println("Womp");
					model.addRow(new String[]{scoreSet.getString("score"), scoreSet.getString("testname"), scoreSet.getString("passed"), scoreSet.getString("time")});
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
		logTable.setModel(model);
	}
}
