
import java.awt.*;

import javax.swing.*;
import java.sql.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class login {

    private JFrame frmTodo;
    public static JTextField tfusername;
    private JTextField tfpassword;
    public int id;
   static login window;
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	
                     window = new login();
                    window.frmTodo.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public login() {
        initialize();
    }

    private void initialize() {
        frmTodo = new JFrame();
        
        frmTodo.getContentPane().setBackground(new Color(102, 255, 153));
        frmTodo.getContentPane().setLayout(null);

        JLabel lblLogIn = new JLabel("LOG IN");
				lblLogIn.setForeground(new Color(0, 51, 255));
				lblLogIn.setFont(new Font("Eras Bold ITC", Font.BOLD | Font.ITALIC, 18));
				lblLogIn.setHorizontalAlignment(SwingConstants.CENTER);
				lblLogIn.setBounds(143, 0, 121, 32);
				frmTodo.getContentPane().add(lblLogIn);

				JLabel lblUsername = new JLabel("USERNAME");
				lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
				lblUsername.setForeground(new Color(0, 51, 255));
				lblUsername.setFont(new Font("Lucida Handwriting", Font.BOLD, 14));
				lblUsername.setBounds(10, 72, 121, 32);
				frmTodo.getContentPane().add(lblUsername);

				JLabel lblPassword = new JLabel("PASSWORD");
				lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
				lblPassword.setForeground(new Color(0, 51, 255));
				lblPassword.setFont(new Font("Lucida Handwriting", Font.BOLD, 14));
				lblPassword.setBounds(10, 115, 121, 32);
				frmTodo.getContentPane().add(lblPassword);

				tfusername = new JTextField();
				tfusername.setFont(new Font("Tahoma", Font.PLAIN, 14));
				tfusername.setBounds(166, 79, 194, 20);
				frmTodo.getContentPane().add(tfusername);
				tfusername.setColumns(10);

				tfpassword = new JPasswordField();
				tfpassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
				tfpassword.setColumns(10);
				tfpassword.setBounds(166, 122, 194, 20);
				frmTodo.getContentPane().add(tfpassword);

				JButton btnLogin = new JButton("LOGIN");
				btnLogin.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent arg0) {
				        try {
				            String username = tfusername.getText().trim();
				            String password = tfpassword.getText().trim();

				           
				            if (username.isEmpty()) {
				                JOptionPane.showMessageDialog(null, "Username should not be empty.");
				                return;
				            }

				            if (password.isEmpty()) {
				                JOptionPane.showMessageDialog(null, "Password should not be empty.");
				                return;
				            }

				            Class.forName("com.mysql.cj.jdbc.Driver");
				            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/todolist", "root", "root");
				            Statement stmt = conn.createStatement();
				            ResultSet rs = stmt.executeQuery("SELECT * FROM signup");

				            boolean isValidUser = false;
				            while (rs.next()) {
				                if (rs.getString("username").equals(username) && rs.getString("password").equals(password)) {
				                    isValidUser = true;
				                    id = rs.getInt("Id");
				                    frmTodo.dispose();
				                    todolist.main(null);
				                    
				                    System.out.println("Successfully logged in");
				                    break;
				                }
				            }

				            if (!isValidUser) {
				                JOptionPane.showMessageDialog(null, "Invalid Username or Password");
				            }
				        } catch (Exception e) {
				            e.printStackTrace();
				            JOptionPane.showMessageDialog(null, "An unexpected error occurred. Please try again.");
				        }
				    }
				});
				btnLogin.setForeground(new Color(0, 51, 255));
				btnLogin.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 15));
				btnLogin.setBounds(166, 176, 112, 37);
				frmTodo.getContentPane().add(btnLogin);

				JLabel forget = new JLabel("Forget Password");
				forget.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						forgetpassword f=new forgetpassword();
						frmTodo.dispose();
						f.main(null);
						
					}
				});

				JLabel lblSignup = new JLabel("Signup");
				lblSignup.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						signup s=new signup();
						frmTodo.dispose();
						s.main(null);
						
						
					}
				});
				lblSignup.setForeground(new Color(0, 51, 204));
				lblSignup.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 15));
				lblSignup.setBounds(263, 224, 78, 26);
				frmTodo.getContentPane().add(lblSignup);
				frmTodo.setBackground(Color.LIGHT_GRAY);
				frmTodo.setTitle("TO-DO");
				frmTodo.setBounds(100, 100, 450, 300);
		frmTodo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
