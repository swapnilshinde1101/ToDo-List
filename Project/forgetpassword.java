
import java.awt.*;

import javax.swing.*;
import java.sql.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class forgetpassword extends JFrame{

    private JFrame frmTodo;
    public static JTextField tfpassword;
    private JTextField tfpasswordc;
    public int id;
    static forgetpassword window;
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	
                     window = new forgetpassword();
                    window.frmTodo.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public forgetpassword() {
        initialize();
    }

    private void initialize() {
        frmTodo = new JFrame();
        
        frmTodo.getContentPane().setBackground(new Color(102, 255, 153));
        frmTodo.getContentPane().setLayout(null);
        frmTodo.setBounds(100, 100, 588, 485);
        JLabel lblLogIn = new JLabel("FORGET PASSWORD");
				lblLogIn.setForeground(new Color(0, 51, 255));
				lblLogIn.setFont(new Font("Eras Bold ITC", Font.BOLD | Font.ITALIC, 18));
				lblLogIn.setHorizontalAlignment(SwingConstants.CENTER);
				lblLogIn.setBounds(98, 0, 225, 32);
				frmTodo.getContentPane().add(lblLogIn);

				JLabel lblUsername = new JLabel("NEW PASSWORD");
				lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
				lblUsername.setForeground(new Color(0, 51, 255));
				lblUsername.setFont(new Font("Lucida Handwriting", Font.BOLD, 14));
				lblUsername.setBounds(10, 72, 121, 32);
				frmTodo.getContentPane().add(lblUsername);

				JLabel lblPassword = new JLabel("CONFIRM PASSWORD");
				lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
				lblPassword.setForeground(new Color(0, 51, 255));
				lblPassword.setFont(new Font("Lucida Handwriting", Font.BOLD, 14));
				lblPassword.setBounds(10, 115, 121, 32);
				frmTodo.getContentPane().add(lblPassword);

				tfpassword = new JTextField();
				tfpassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
				tfpassword.setBounds(166, 79, 194, 20);
				frmTodo.getContentPane().add(tfpassword);
				tfpassword.setColumns(10);

				tfpasswordc = new JTextField();
				tfpasswordc.setFont(new Font("Tahoma", Font.PLAIN, 14));
				tfpasswordc.setColumns(10);
				tfpasswordc.setBounds(166, 122, 194, 20);
				frmTodo.getContentPane().add(tfpasswordc);

				JButton btnLogin = new JButton("Forget password");
				btnLogin.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent arg0) {
				        try {
				            String password1 = tfpassword.getText().trim();
				            String password2 = tfpasswordc.getText().trim();

				           
				            if (password1.isEmpty()) {
				                JOptionPane.showMessageDialog(null, "Username should not be empty.");
				                return;
				            }

				            if (password2.isEmpty()) {
				                JOptionPane.showMessageDialog(null, "Password should not be empty.");
				                return;
				            }

				            Class.forName("com.mysql.cj.jdbc.Driver");
				            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/todolist", "root", "root");
////				            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM signup where username=?");
////				            stmt.setString(1, login.tfusername.getText());
//				            
//				            ResultSet rs = stmt.executeQuery();
				            
				            if(password1.equals(password2))
				            {
				            	PreparedStatement stmt1 = conn.prepareStatement("update signup set password=? where username=?");
					            stmt1.setString(1, password1);
				            	stmt1.setString(2, login.tfusername.getText());
					            
					            int rs1 = stmt1.executeUpdate();
					            System.out.println(rs1);
					            if(rs1==1)
					            {
					            JOptionPane.showMessageDialog(null, "Password is updated");
					            frmTodo.dispose();
					            }
					            else {
					            	JOptionPane.showMessageDialog(null, "Something went wrong");
					            	return;
					            }
				            	
				            }
				            else {
				            	JOptionPane.showMessageDialog(null, "Both passwords are not same");
				            	return;
				            }

				            
				             

				           
				        } catch (Exception e) {
				            e.printStackTrace();
				            JOptionPane.showMessageDialog(null, "An unexpected error occurred. Please try again.");
				        }
				    }
				});
				btnLogin.setForeground(new Color(0, 51, 255));
				btnLogin.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 15));
				btnLogin.setBounds(98, 176, 226, 32);
				frmTodo.getContentPane().add(btnLogin);

				

				
				
				frmTodo.setBackground(Color.LIGHT_GRAY);
				frmTodo.setTitle("TO-DO");
				frmTodo.setBounds(100, 100, 450, 300);
		frmTodo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
