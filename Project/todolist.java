import java.awt.*;
import javax.swing.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class todolist {

    private JFrame frmTodo;
    private JTextField tftask;
    private JTable table;
    private JButton btnComplete;
    private JDateChooser dateChooser;
    private JSpinner timeSpinner;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    public String title, done, taskDatetime;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    todolist window = new todolist();
                    window.frmTodo.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public todolist() {
        initialize();
    }

    public void display() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/todolist", "root", "root");
            String un = login.tfusername.getText();
            PreparedStatement pstmt = conn.prepareStatement("select * from task where username=?");
            pstmt.setString(1, un);
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData rd = rs.getMetaData();
            int a = rd.getColumnCount();
            DefaultTableModel df = (DefaultTableModel) table.getModel();
            df.setRowCount(0);

            while (rs.next()) {
                Vector v2 = new Vector();
                title = rs.getString("title");
                done = rs.getString("done");
                taskDatetime = rs.getString("task_datetime");

                v2.add(title);
                v2.add(done);
                v2.add(taskDatetime);

                df.addRow(v2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void checkOverdueTasks() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/todolist", "root", "root");
            String un = login.tfusername.getText();
            PreparedStatement pstmt = conn.prepareStatement("select * from task where username=?");
            pstmt.setString(1, un);
            ResultSet rs = pstmt.executeQuery();

            // Current date and time
            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();

            while (rs.next()) {
                String taskTitle = rs.getString("title");
                String taskDone = rs.getString("done");
                String taskDatetime = rs.getString("task_datetime");

                if (taskDone.equals("Incomplete")) {
                    // Extract the date and time parts from task's datetime
                    String taskDateString = taskDatetime.substring(0, 10); // Extract "yyyy-MM-dd" part
                    String taskTimeString = taskDatetime.substring(20, 25); // Extract "HH:mm" part

                    LocalDate taskDate = LocalDate.parse(taskDateString); // Convert to LocalDate
                    LocalTime taskTime = LocalTime.parse(taskTimeString, DateTimeFormatter.ofPattern("HH:mm")); // Convert to LocalTime

                    // Check if the task is overdue by date
                    if (taskDate.isBefore(currentDate)) {
                        JOptionPane.showMessageDialog(frmTodo, "Task '" + taskTitle + "' is overdue by date and incomplete.");
                    } 
                    // Check tasks for today
                    else if (taskDate.isEqual(currentDate)) {
                        // Check if the task is overdue by time
                        if (taskTime.isBefore(currentTime)) {
                            JOptionPane.showMessageDialog(frmTodo, "Task '" + taskTitle + "' is overdue by time today and incomplete.");
                        } 
                        // Check if the task is upcoming within 10 minutes
                        else {
                            long minutesUntilTask = currentTime.until(taskTime, ChronoUnit.MINUTES);
                            if (minutesUntilTask > 0 && minutesUntilTask <= 10) {
                                JOptionPane.showMessageDialog(frmTodo, "Task '" + taskTitle + "' is upcoming in the next " + minutesUntilTask + " minutes.");
                            }
                        }
                    }
                }
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

        frmTodo.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                checkOverdueTasks();
            }
        });

        JLabel lblTodoList = new JLabel("TO-DO LIST");
        lblTodoList.setHorizontalAlignment(SwingConstants.CENTER);
        lblTodoList.setForeground(new Color(0, 51, 255));
        lblTodoList.setFont(new Font("Eras Bold ITC", Font.BOLD | Font.ITALIC, 18));
        lblTodoList.setBounds(211, 0, 144, 32);
        frmTodo.getContentPane().add(lblTodoList);

        tftask = new JTextField();
        tftask.setFont(new Font("Tahoma", Font.PLAIN, 14));
        tftask.setColumns(10);
        tftask.setBounds(10, 50, 465, 29);
        frmTodo.getContentPane().add(tftask);

        // Date Chooser for Task Deadline
        dateChooser = new JDateChooser();
        dateChooser.setBounds(10, 80, 200, 30);
        frmTodo.getContentPane().add(dateChooser);

        // Spinner for Time
        SpinnerDateModel timeModel = new SpinnerDateModel();
        timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setBounds(220, 80, 100, 30);
        frmTodo.getContentPane().add(timeSpinner);

        btnAdd = new JButton("Add");
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try {
                    // Get Task Name
                    String taskTitle = tftask.getText();

                    // Validate the input
                    if (taskTitle.isEmpty() || dateChooser.getDate() == null || timeSpinner.getValue() == null) {
                        JOptionPane.showMessageDialog(frmTodo, "Please enter all fields.");
                        return;
                    }

                    // Prepare date and time for storing in database
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                   
                    String taskDateTime = dateFormat.format(dateChooser.getDate()) + " " + new SimpleDateFormat("HH:mm").format(timeSpinner.getValue());

                    // Insert Task into Database
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/todolist","root","root");
                    PreparedStatement prestmt = conn.prepareStatement("insert into task (username, title, done, task_datetime) values(?, ?, ?, ?)");
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery("select * from signup where username='" + login.tfusername.getText() + "'");
                    rs.next();
                    prestmt.setString(1, login.tfusername.getText());
                    prestmt.setString(2, taskTitle);
                    prestmt.setString(3, "Incomplete");
                    prestmt.setString(4, taskDateTime);
                    prestmt.executeUpdate();
                    tftask.setText(null);
                    dateChooser.setDate(null);
                    timeSpinner.setValue(new Date());
                    display();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btnAdd.setForeground(new Color(0, 51, 255));
        btnAdd.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 14));
        btnAdd.setBounds(10, 120, 80, 29);
        frmTodo.getContentPane().add(btnAdd);

        // Edit Button
        btnEdit = new JButton("Edit");
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
                    pstmt.setString(3, login.tfusername.getText());
                    int n=pstmt.executeUpdate();
                    display();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btnEdit.setForeground(new Color(0, 51, 255));
        btnEdit.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 14));
        btnEdit.setBounds(100, 120, 80, 29);
        frmTodo.getContentPane().add(btnEdit);

        // Delete Button
        btnDelete = new JButton("Delete");
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
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btnDelete.setForeground(new Color(0, 51, 255));
        btnDelete.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 14));
        btnDelete.setBounds(190, 120, 80, 29);
        frmTodo.getContentPane().add(btnDelete);

//        JButton btnDeleteAllIncomplete = new JButton("Forget Password");
//        btnDeleteAllIncomplete.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent arg0) {
//                
//            	 forgetpassword forgetPasswordWindow = new forgetpassword();
//                 forgetPasswordWindow.setVisible(true);
//                
                	//                    // Ask the user to input a date before which tasks should be deleted
//                    String inputDate = JOptionPane.showInputDialog(
//                        frmTodo,
//                        "Enter the date (yyyy-MM-dd) before which incomplete tasks will be deleted:"
//                    );
//
//                    // Validate the input date
//                    if (inputDate == null || inputDate.trim().isEmpty()) {
//                        JOptionPane.showMessageDialog(frmTodo, "No date provided. Operation canceled.");
//                        return;
//                    }
//
//                    java.sql.Date enteredDate;
//                    try {
//                        enteredDate = java.sql.Date.valueOf(inputDate.trim());
//                    } catch (IllegalArgumentException e) {
//                        JOptionPane.showMessageDialog(frmTodo, "Invalid date format. Use yyyy-MM-dd.");
//                        return;
//                    }
//
//                    // Confirm with the user before deletion
//                    int confirmation = JOptionPane.showConfirmDialog(
//                        frmTodo,
//                        "Are you sure you want to delete all incomplete tasks before " + enteredDate + "?",
//                        "Confirm Deletion",
//                        JOptionPane.YES_NO_OPTION
//                    );
//
//                    if (confirmation == JOptionPane.YES_OPTION) {
//                        // Establish database connection
//                        Class.forName("com.mysql.cj.jdbc.Driver");
//                        Connection conn = DriverManager.getConnection(
//                            "jdbc:mysql://localhost:3306/todolist",
//                            "root",
//                            "root"
//                        );
//
//                        // Prepare the SQL statement to delete incomplete tasks before the specified date
//                        PreparedStatement pstmt = conn.prepareStatement(
//                            "DELETE FROM task WHERE done = 'Incomplete' AND task_datetime < ? AND username = ?"
//                        );
//
//                        pstmt.setDate(1, enteredDate); // Set the entered date
//                        pstmt.setString(2, login.tfusername.getText()); // Set the logged-in username
//
//                        // Execute the delete statement
//                        int rowsAffected = pstmt.executeUpdate();
//                        JOptionPane.showMessageDialog(frmTodo, "Deleted " + rowsAffected + " incomplete task(s) before " + enteredDate + ".");
//
//                        // Refresh the table to reflect the changes
//                        display();
                    
//               
//            }
//        });
//        btnDeleteAllIncomplete.setForeground(new Color(0, 51, 255));
//        btnDeleteAllIncomplete.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 14));
//        btnDeleteAllIncomplete.setBounds(430, 120, 200, 29); // Adjust the bounds as needed
//        frmTodo.getContentPane().add(btnDeleteAllIncomplete);
//


        
        // Complete Button
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
                    if(btnComplete.getText()=="Complete") {
                        pstmt.setString(1, "Complete");
                        table.setValueAt("Complete", i, 1);
                    } else {
                        pstmt.setString(1, "InComplete");
                        table.setValueAt("InComplete", i, 1);
                    }
                    pstmt.setString(2, s);
                    pstmt.setString(3, login.tfusername.getText());
                    pstmt.executeUpdate();
                    display();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btnComplete.setForeground(new Color(0, 51, 255));
        btnComplete.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 14));
        btnComplete.setBounds(280, 120, 120, 29);
        frmTodo.getContentPane().add(btnComplete);

        // Table Scroll Pane
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 150, 552, 322);
        frmTodo.getContentPane().add(scrollPane);

        table = new JTable();
        table.setFont(new Font("Sitka Subheading", Font.PLAIN, 14));
        table.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {
                "Title", "Done", "Deadline"
            }
        ));
        scrollPane.setViewportView(table);

        display();
    }
}
