

//import todo.login; // import login package
import java.awt.*;

import javax.swing.*;
import java.sql.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class signup extends JFrame{

    private JFrame frmTo;
    private JTextField tffirstname;
    private JTextField tflastname;
    private JTextField tfusername;
    private JTextField tfpassword;
    static signup window;
    login n = new login();  

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    window = new signup();
                    window.frmTo.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public signup() {
        initialize();
    }

    private void initialize() {
        frmTo = new JFrame();
			
				frmTo.getContentPane().setBackground(new Color(102, 235, 153));
				frmTo.setTitle("TO-DO");
				frmTo.setBounds(100, 100, 450, 298);
				frmTo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frmTo.getContentPane().setLayout(null);

				JLabel lblSignUp = new JLabel("SIGN UP");
				lblSignUp.setHorizontalAlignment(SwingConstants.CENTER);
				lblSignUp.setForeground(new Color(0, 51, 255));
				lblSignUp.setFont(new Font("Eras Bold ITC", Font.BOLD | Font.ITALIC, 18));
				lblSignUp.setBounds(144, 0, 121, 32);
				frmTo.getContentPane().add(lblSignUp);

				JLabel lblFirstName = new JLabel("FIRST NAME");
				lblFirstName.setHorizontalAlignment(SwingConstants.CENTER);
				lblFirstName.setForeground(new Color(0, 51, 255));
				lblFirstName.setFont(new Font("Lucida Handwriting", Font.BOLD, 14));
				lblFirstName.setBounds(10, 45, 121, 32);
				frmTo.getContentPane().add(lblFirstName);

				JLabel lblLastName = new JLabel("LAST NAME");
				lblLastName.setHorizontalAlignment(SwingConstants.CENTER);
				lblLastName.setForeground(new Color(0, 51, 255));
				lblLastName.setFont(new Font("Lucida Handwriting", Font.BOLD, 14));
				lblLastName.setBounds(10, 77, 121, 32);
				frmTo.getContentPane().add(lblLastName);

				JLabel lblUserName = new JLabel("USER NAME");
				lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
				lblUserName.setForeground(new Color(0, 51, 255));
				lblUserName.setFont(new Font("Lucida Handwriting", Font.BOLD, 14));
				lblUserName.setBounds(10, 110, 121, 32);
				frmTo.getContentPane().add(lblUserName);

				JLabel lblPassword = new JLabel("PASSWORD");
				lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
				lblPassword.setForeground(new Color(0, 51, 255));
				lblPassword.setFont(new Font("Lucida Handwriting", Font.BOLD, 14));
				lblPassword.setBounds(10, 140, 121, 32);
				frmTo.getContentPane().add(lblPassword);

				tffirstname = new JTextField();
				tffirstname.setFont(new Font("Tahoma", Font.PLAIN, 14));
				tffirstname.setColumns(10);
				tffirstname.setBounds(154, 52, 194, 20);
				frmTo.getContentPane().add(tffirstname);

				tflastname = new JTextField();
				tflastname.setFont(new Font("Tahoma", Font.PLAIN, 14));
				tflastname.setColumns(10);
				tflastname.setBounds(154, 84, 194, 20);
				frmTo.getContentPane().add(tflastname);

				tfusername = new JTextField();
				tfusername.setFont(new Font("Tahoma", Font.PLAIN, 14));
				tfusername.setColumns(10);
				tfusername.setBounds(154, 117, 194, 20);
				frmTo.getContentPane().add(tfusername);

				tfpassword = new JTextField();
				tfpassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
				tfpassword.setColumns(10);
				tfpassword.setBounds(154, 147, 194, 20);
				frmTo.getContentPane().add(tfpassword);

				JButton btnSignUp = new JButton("SIGN UP");
				
				btnSignUp.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent arg0) {
				    	
				        try {
				        	Class.forName("com.mysql.cj.jdbc.Driver");
				            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/todolist", "root", "root");
				           
				            String firstName = tffirstname.getText().trim();
				            if (firstName.isEmpty() && !firstName.matches("[a-zA-Z]+")) {
				                JOptionPane.showMessageDialog(null, "First name should only contain characters and should not be empty.");
				                return;
				            }

				            String lastName = tflastname.getText().trim();
				            if (lastName.isEmpty() || !lastName.matches("[a-zA-Z]+")) {
				                JOptionPane.showMessageDialog(null, "Last name should only contain characters and should not be empty.");
				                return;
				            }

				           
				            String username = tfusername.getText().trim();
				            if (username.isEmpty()) {
				            	
				                JOptionPane.showMessageDialog(null, "Username should not be empty.");
				                
				                return;
				            }

				            
				            String password = tfpassword.getText().trim();
				            if (password.isEmpty()) {
				                JOptionPane.showMessageDialog(null, "Password should not be empty.");
				                return;
				            }
                           
				            PreparedStatement ps=conn.prepareStatement("select * from signup");
				            ResultSet check=ps.executeQuery();
				            
				           System.out.println(check);
				            
				            while(check.next())
				            {
				            	if(username.equals(check.getString("username")))
				            	{
				            		JOptionPane.showMessageDialog(null, "Username should be unique");
				            		return;
				            	}
				            }
				            
				            
				           
				            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO signup(username, password, firstname, lastname) VALUES (?, ?, ?, ?)");

				            pstmt.setString(1, username);
				            pstmt.setString(2, password);
				            pstmt.setString(3, firstName);
				            pstmt.setString(4, lastName);

				            int result = pstmt.executeUpdate();

				            if (result == 1) {
				                JOptionPane.showMessageDialog(null, "Sign-up successful!");
			
				                n.main(null);
				            } else {
				                JOptionPane.showMessageDialog(null, "Sign-up failed. Please try again.");
				            }
				        } catch (SQLException e) {
				            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage() + "\nUse a different username.");
				        } catch (Exception e) {
				            e.printStackTrace();
				            JOptionPane.showMessageDialog(null, "An unexpected error occurred.");
				        }
				    }
				});
				btnSignUp.setForeground(new Color(0, 51, 255));
				btnSignUp.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 18));
				btnSignUp.setBounds(20, 205, 140, 37);
				
				JButton btnLogin = new JButton("Login");
				btnLogin.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						frmTo.dispose();
						n.main(null);
						
						
					}
					
				});
				frmTo.getContentPane().add(btnLogin);
				btnLogin.setForeground(new Color(0, 51, 255));
				btnLogin.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 18));
				btnLogin.setBounds(190, 205, 140, 37);
				
		frmTo.getContentPane().add(btnSignUp);
    }

	
}
