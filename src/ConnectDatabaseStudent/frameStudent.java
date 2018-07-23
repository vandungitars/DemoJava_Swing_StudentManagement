package ConnectDatabaseStudent;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.JobAttributes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class frameStudent extends JFrame implements ActionListener
{
	MyConnect myConnect = new MyConnect();
	private JTable table;
	private JTextField textID;
	private JTextField textName;
	private JTextField textPoint;
	private JButton btnOK;
	private JButton btnCancel;
	private boolean isUpdate = false;
	
	public frameStudent()
	{
		myConnect.connect();
		this.add(createMainPanel());
		setDisplayInput(false, false);
		this.setTitle("Hi Hi Hi");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	private JPanel createMainPanel()
	{
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(createTitlePanel(), BorderLayout.PAGE_START);
		panel.add(createTablePanel(), BorderLayout.CENTER);
		panel.add(createBottomPanel(), BorderLayout.PAGE_END);
		return panel;
	}
	
	private JPanel createTitlePanel()
	{
		JPanel panel = new JPanel();
		JLabel label = new JLabel("List Student");
		panel.add(label);
		return panel;
	}
	
	private JTable createTable()
	{
		JTable jtable = new JTable();
		return jtable;
	}
	private JPanel createTablePanel()
	{
		JPanel panel = new JPanel();
		JScrollPane scroll = new JScrollPane(table = createTable());
		panel.add(scroll);
		panel.setBorder(new EmptyBorder(5, 60, 10, 60));
		return panel;
	}
	
	private JPanel createInputPanel()
	{
		JPanel panel = new JPanel(new BorderLayout(10,10));
		
		JPanel panelleft = new JPanel(new GridLayout(3, 1, 5, 5));
		panelleft.add(new JLabel("Id"));
		panelleft.add(new JLabel("Name"));
		panelleft.add(new JLabel("Point"));
		
		JPanel panelright = new JPanel(new GridLayout(3, 1, 5, 5));
		panelright.add(textID = new JTextField());
		panelright.add(textName = new JTextField());
		panelright.add(textPoint = new JTextField());
		
		JPanel panelbutton = new JPanel(new GridLayout(1, 2, 5, 5));
		panelbutton.setBorder(new EmptyBorder(0, 50, 0, 50));
		panelbutton.add(btnOK = createButton("OK"));
		panelbutton.add(btnCancel = createButton("Cancel"));
		
		panel.add(panelleft, BorderLayout.WEST);
		panel.add(panelright, BorderLayout.CENTER);
		panel.add(panelbutton, BorderLayout.PAGE_END);
		
		return panel;
	}
	private JButton createButton(String text) 
	{
		JButton btn = new JButton(text);
		btn.addActionListener((ActionListener)this);
		return btn;
	}
	private JPanel createButtonPanel()
	{
		JPanel panel = new JPanel(new GridLayout(1, 3, 5, 5));
		panel.add(createButton("ADD"));
		panel.add(createButton("UPDATE"));
		panel.add(createButton("DELETE"));
		return panel;
	}
	private JPanel createBottomPanel()
	{
		JPanel panel = new JPanel(new BorderLayout(10,10));
		panel.setBorder(new EmptyBorder(20, 60, 10, 60));
		panel.add(createInputPanel(), BorderLayout.CENTER);
		panel.add(createButtonPanel(), BorderLayout.PAGE_END);
		return panel;
	}
	
	private boolean setDisplayInput(boolean display, boolean update)
	{
		if(update && table.getSelectedRow() < 0)
		{
			return false;
		}
		else if(update)
		{
			int row = table.getSelectedRow();
			textID.setText((String)table.getValueAt(row, 0));
			textName.setText((String)table.getValueAt(row, 1));
			textPoint.setText((String)table.getValueAt(row, 2));
		}
		textID.setEnabled(display);
		textName.setEnabled(display);
		textPoint.setEnabled(display);
		btnOK.setEnabled(display);
		btnCancel.setEnabled(display);
		
		return true;
	}
	
	private void loadData()
	{
		DefaultTableModel tablemodel = new DefaultTableModel();
		ResultSet rs = myConnect.getData();
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columncount = rsmd.getColumnCount();
			String[] titlecolumn = new String[columncount];
			for(int i=0; i<columncount; i++)
			{
				titlecolumn[i] = rsmd.getColumnName(i+1);
			}
			tablemodel.setColumnIdentifiers(titlecolumn);
			while(rs.next())
			{
				for(int i=0; i<columncount; i++)
				{
					titlecolumn[i] = rs.getString(i+1);
				}
				tablemodel.addRow(titlecolumn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		table.setModel(tablemodel);
	}
	
	private Student getStudent()
	{
		int id = Integer.parseInt(textID.getText());
		String name = textName.getText().trim();
		double point = Double.parseDouble(textPoint.getText());
		Student st = new Student(id, name, point);
		return st;
	}
	
	private void add()
	{
		setDisplayInput(true, false);
	}
	private void update()
	{
		setDisplayInput(true, true);
		isUpdate = true;
	}
	private void delete()
	{
		int row = table.getSelectedRow();
		if(row < 0)
		{
			JOptionPane.showMessageDialog(null, "you must select a student", "Error Delete", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int select = JOptionPane.showOptionDialog(null, "Are you want delete?", "Delete", 0, JOptionPane.YES_NO_OPTION, null, null, 1);
		if(select == 0)
		{
			myConnect.deleteDataID((int)table.getValueAt(row, 1));
			loadData();
		}
	}
	private void clear() 
	{
		textID.setText("");
		textName.setText("");
		textPoint.setText("");
	}
	private void addOrUpdate()
	{
		Student st = getStudent();
		if(st != null)
		{
			if(isUpdate)
			{
				myConnect.updateData(st.getId(), st);
				loadData();
				isUpdate = false;
			}
			else
			{
				myConnect.insertData(st);
				loadData();
			}
			clear();
			setDisplayInput(false, false);
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Infomation is error", "Error info", JOptionPane.ERROR_MESSAGE);
		}
	}
	private void cancel() 
	{
		clear();
		setDisplayInput(false, false);
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand() == "ADD")
		{
			add();
			return;
		}
		if(e.getActionCommand() == "UPDATE")
		{
			update();
			return;
		}
		if(e.getActionCommand() == "DELETE")
		{
			delete();
			return;
		}
		if (e.getSource() == btnOK) {
			addOrUpdate();
		}

		if (e.getSource() == btnCancel) {
			cancel();
		}
	}
	
	public static void main(String[] args) {
		frameStudent fs = new frameStudent();
		fs.loadData();
	}
	
	
	
	
	
	
	
	
	
}
