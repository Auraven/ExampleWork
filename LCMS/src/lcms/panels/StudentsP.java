package lcms.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import lcms.base.C;

public class StudentsP extends JPanel implements ActionListener {
	private JScrollPane scrollpane;
	private JTable studentTable;
	public DefaultTableModel model;
	private JButton logB, scoresB;
	private MenuP menu;

	public StudentsP(MenuP menu) {
		this.menu = menu;
		setSize(menu.getWidth(), menu.getHeight());
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		// ScrollPane/Table
		scrollpane = new JScrollPane();
		scrollpane.setSize(getWidth(), getHeight());
		studentTable = new JTable();
		studentTable.setSize(scrollpane.getWidth(), scrollpane.getHeight());
		update();
		scrollpane.add(studentTable);
		scrollpane.setViewportView(studentTable);

		// Bottom
		logB = new JButton("View Selected Logs");
		logB.addActionListener(this);
		scoresB = new JButton("View Selected Scores");
		scoresB.addActionListener(this);
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(scrollpane, gbc);
		gbc.gridy++;
		add(logB, gbc);
		gbc.gridy++;
		add(scoresB, gbc);
	}

	public void update() {
		model = new DefaultTableModel();
		model.addColumn("Type");
		model.addColumn("Username");
		model.addColumn("FirstName");
		model.addColumn("LastName");
		model.addColumn("Age");
		model.addColumn("Active");
		studentTable.setModel(model);

		ResultSet userSet = C.selectWhere("users", "*", "accounttype", "student");
		try {
			while (userSet.next()) {
				JCheckBox cb = new JCheckBox();
				cb.setName(userSet.getString("username"));

				model.addRow(new Object[] {"student",
						userSet.getString("username"),
						userSet.getString("firstname"),
						userSet.getString("lastname"),
						userSet.getString("age"),
						userSet.getString("isactive") });
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void actionPerformed(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		int row = studentTable.getSelectedRow();
		if(row != -1 && cmd.equals("View Selected Logs")){
			JFrame popup = new JFrame();
			popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			popup.setAlwaysOnTop(true);
			popup.setTitle((String)model.getValueAt(row, 1) + "'s Log File");
			popup.setSize(600, 400);
			JScrollPane subscrollpane = new JScrollPane();
			subscrollpane.setSize(popup.getWidth(), popup.getHeight());
			JTable logTable = new JTable();
			DefaultTableModel lmodel = new DefaultTableModel();
			lmodel.addColumn("Log");
			lmodel.addColumn("Time");
			ResultSet logSet = C.selectWhere("logs", "*", "u_name", (String)model.getValueAt(row, 1));
			try {
				while(logSet.next()){
					lmodel.addRow(new String[]{logSet.getString("log"), logSet.getString("time")});
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}			
			logTable.setModel(lmodel);
			subscrollpane.setViewportView(logTable);
			popup.add(subscrollpane);
			popup.setVisible(true);
		}else if(row != -1 && cmd.equals("View Selected Scores")){
			JFrame popup = new JFrame();
			popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			popup.setAlwaysOnTop(true);
			popup.setTitle((String)model.getValueAt(row, 1) + "'s Score File");
			popup.setSize(600, 400);
			JScrollPane subscrollpane = new JScrollPane();
			subscrollpane.setSize(popup.getWidth(), popup.getHeight());
			JTable scoreTable = new JTable();
			DefaultTableModel lmodel = new DefaultTableModel();
			lmodel.addColumn("Scores");
			lmodel.addColumn("Test");
			lmodel.addColumn("Passed");
			lmodel.addColumn("Time");
			ResultSet scoreSet = C.selectWhere("scores", "*", "username", (String)model.getValueAt(row, 1));
			try {
				while(scoreSet.next()){
					lmodel.addRow(new String[]{scoreSet.getString("score"), scoreSet.getString("testname"), scoreSet.getString("passed"), scoreSet.getString("time")});
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}				
			scoreTable.setModel(lmodel);
			subscrollpane.setViewportView(scoreTable);
			popup.add(subscrollpane);
			popup.setVisible(true);
		}else{
			JOptionPane.showMessageDialog(this, "Please select a student.");
		}
	}

}
