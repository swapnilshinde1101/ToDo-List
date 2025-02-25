

import java.awt.*;
import javax.swing.*;
import java.sql.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

public class todolist2 {

    private JFrame frmTodo;
    private JTextField tftask;
    private JTable table;
    private JButton btnComplete;
    

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    todolist2 window = new todolist2();
                    window.frmTodo.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public todolist2() {
        initialize();
    }

    public void display() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/todolist","root","root");
            String un = login.tfusername.getText(); // accessing login class static field
            PreparedStatement pstmt = conn.prepareStatement("select * from task where username=?");
            pstmt.setString(1, un);
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData rd = rs.getMetaData();
            int a = rd.getColumnCount();
            DefaultTableModel df = (DefaultTableModel) table.getModel();
            df.setRowCount(0);
            while (rs.next()) {
                Vector v2 = new Vector();
                
                for (int j = 1; j <= a; j++) {
                    v2.add(rs.getString("title"));
                    v2.add(rs.getString("done"));
                }
                df.addRow(v2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        frmTodo = new JFrame();
				
				frmTodo.getContentPane().setBackground(new Color(102, 255, 153));
				frmTodo.setTitle("TO-DO");
				frmTodo.setBounds(100, 100, 588, 485);
				frmTodo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frmTodo.getContentPane().setLayout(null);

				JLabel lblTodoList = new JLabel("TO-DO LIST");
				lblTodoList.setHorizontalAlignment(SwingConstants.CENTER);
				lblTodoList.setForeground(new Color(0, 51, 255));
				lblTodoList.setFont(new Font("Eras Bold ITC", Font.BOLD | Font.ITALIC, 18));
				lblTodoList.setBounds(211, 0, 144, 32);
				frmTodo.getContentPane().add(lblTodoList);
				
				

				JLabel lblEnterTaskTo = new JLabel("Enter Task To Add");
				lblEnterTaskTo.setHorizontalAlignment(SwingConstants.CENTER);
				lblEnterTaskTo.setForeground(new Color(0, 51, 255));
				lblEnterTaskTo.setFont(new Font("Lucida Handwriting", Font.BOLD, 14));
				lblEnterTaskTo.setBounds(0, 24, 185, 32);
				frmTodo.getContentPane().add(lblEnterTaskTo);

				tftask = new JTextField();
				tftask.setFont(new Font("Tahoma", Font.PLAIN, 14));
				tftask.setColumns(10);
				tftask.setBounds(10, 50, 465, 29);
				frmTodo.getContentPane().add(tftask);

				JButton btnAdd = new JButton("Add");
				btnAdd.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
							Class.forName("com.mysql.cj.jdbc.Driver");
							Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/todolist","root","root");

							PreparedStatement prestmt = conn.prepareStatement("insert into task values(?,?,?,?)");
							
//							
							
							Statement st=conn.createStatement();
							ResultSet rs=st.executeQuery("select * from signup where username='"+login.tfusername.getText()+"'");
							
							
							rs.next();
							prestmt.setInt(1, rs.getInt("Id"));
							prestmt.setString(2, tftask.getText());
							prestmt.setString(3, "Incomplete");
							prestmt.setString(4,login.tfusername.getText());
							prestmt.executeUpdate();
							tftask.setText(null);
							display();
						}catch(Exception e) {
							e.printStackTrace();
						}
					}
				});
				btnAdd.setForeground(new Color(0, 51, 255));
				btnAdd.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 14));
				btnAdd.setBounds(10, 78, 77, 29);
				frmTodo.getContentPane().add(btnAdd);

				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setBounds(10, 113, 552, 322);
				frmTodo.getContentPane().add(scrollPane);

				table = new JTable();
				table.setFont(new Font("Sitka Subheading", Font.PLAIN, 14));
				table.setModel(new DefaultTableModel(
					new Object[][] {
					},
					new String[] {
						"Title", "Done"
					}
				));
				scrollPane.setViewportView(table);

				JButton btnEdit = new JButton("Edit");
				btnEdit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {

					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/todolist","root","root");
					DefaultTableModel dl =(DefaultTableModel) table.getModel();
					int i=table.getSelectedRow();
					String s= dl.getValueAt(i, 0).toString();
					PreparedStatement pstmt = conn.prepareStatement("update task set title=? where title=? and username=?");
					pstmt.setString(1, tftask.getText());
					pstmt.setString(2, s);
					pstmt.setString(3,login.tfusername.getText());
					int n=pstmt.executeUpdate();
					System.out.println(n);
					display();
						}catch(Exception e) {
							e.printStackTrace();
						}
					}
				});
				btnEdit.setForeground(new Color(0, 51, 255));
				btnEdit.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 14));
				btnEdit.setBounds(97, 78, 77, 29);
				frmTodo.getContentPane().add(btnEdit);

				JButton btnDelete = new JButton("Delete");
				btnDelete.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/todolist","root","root");
						DefaultTableModel dl =(DefaultTableModel) table.getModel();
						int i=table.getSelectedRow();
						String s= dl.getValueAt(i, 0).toString();
						PreparedStatement pstmt = conn.prepareStatement("delete from task where title=? and username=?");
						pstmt.setString(1, s);
						pstmt.setString(2, login.tfusername.getText());
						pstmt.executeUpdate();
						display();
						}catch(Exception e)
						{
							e.printStackTrace();
						}
					}

				});
				btnDelete.setForeground(new Color(0, 51, 255));
				btnDelete.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 14));
				btnDelete.setBounds(184, 78, 103, 29);
				frmTodo.getContentPane().add(btnDelete);

				btnComplete = new JButton("Complete");
				btnComplete.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/todolist","root","root");
						DefaultTableModel dl =(DefaultTableModel) table.getModel();
						int i=table.getSelectedRow();
						String s= dl.getValueAt(i, 0).toString();

						PreparedStatement pstmt = conn.prepareStatement("update task set done=? where title=? and username=?");
						if(btnComplete.getText()=="Complete")
						{
							pstmt.setString(1, "Complete");
							table.setValueAt("Complete", i, 1);
						}else {
							pstmt.setString(1, "InComplete");
							table.setValueAt("InComplete", i, 1);
						}
						pstmt.setString(2, s);
						pstmt.setString(3, login.tfusername.getText());
						int x=pstmt.executeUpdate();
						System.out.println(x);
						}catch(Exception e) {
							e.printStackTrace();
						}
					}
				});
				
				btnComplete.setForeground(new Color(0, 51, 255));
				btnComplete.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 14));
				btnComplete.setBounds(297, 78, 178, 29);
				frmTodo.getContentPane().add(btnComplete);
				display();
				
				
				JLabel lblSignup = new JLabel("Login");
				lblSignup.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						login s=new login();
						s.main(null);
												
					}
				});
				
				JLabel lblforget = new JLabel("Forget Password");
				lblforget.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						forgetpassword s=new forgetpassword();
						
						s.main(null);
												
					}
				});
				lblforget.setForeground(new Color(0, 51, 204));
				lblforget.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 15));
				lblforget.setBounds(200, 24, 185, 32);
				frmTodo.getContentPane().add(lblforget);
				ListSelectionModel cellSelectionModel = table.getSelectionModel();
				cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
				      public void valueChanged(ListSelectionEvent e) {
				    	  if(table.hasFocus()) {
				    	 
				    		  String str=table.getValueAt(table.getSelectedRow(),1).toString();

				    	if(str.startsWith("I"))
				    		btnComplete.setText("Complete");
				    	else
				    		btnComplete.setText("InComplete");
				    	  }
				      }

		    });
    }
}
//20, 25    private void checkOverdueTasks() {
//    try {
//        Class.forName("com.mysql.cj.jdbc.Driver");
//        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/todolist", "root", "root");
//        String un = login.tfusername.getText();
//        PreparedStatement pstmt = conn.prepareStatement("select * from task where username=?");
//        pstmt.setString(1, un);
//        ResultSet rs = pstmt.executeQuery();
//
//        // Current time
//        LocalTime currentTime = LocalTime.now();
//
//        while (rs.next()) {
//            String taskTitle = rs.getString("title");
//            String taskDone = rs.getString("done");
//            String taskDatetime = rs.getString("task_datetime");
//
//            if (taskDone.equals("Incomplete")) {
//                // Extract the time part from task's datetime (e.g., "20:05")
//                String taskTimeString = taskDatetime.substring(20, 25); // Extract "HH:mm" part
//                LocalTime taskTime = LocalTime.parse(taskTimeString, DateTimeFormatter.ofPattern("HH:mm"));
//
//                // Checking if task time is overdue (before current time)
//                if (taskTime.isBefore(currentTime)) {
//                    JOptionPane.showMessageDialog(frmTodo, "Task '" + taskTitle + "' is overdue by time and incomplete.");
//                } 
//                // Checking if task is upcoming within 10 minutes
//                else {
//                    long minutesUntilTask = currentTime.until(taskTime, java.time.temporal.ChronoUnit.MINUTES);
//                    if (minutesUntilTask > 0 && minutesUntilTask <= 10) {
//                        JOptionPane.showMessageDialog(frmTodo, "Task '" + taskTitle + "' is upcoming in the next " + minutesUntilTask + " minutes.");
//                    }
//                }
//            }
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//}