package za.ac.cput.views;

import okhttp3.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class AuditClient extends JFrame implements ActionListener {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static OkHttpClient client = new OkHttpClient();

    private JFrame loginFrame;
    private JPanel pnlNorth, pnlWest, pnlEast, pnlSouth, pnlCenter;
    private JTextField txfUsername, txfTodayDate;
    private JPasswordField pfPassword;
    private JLabel lblHeading, lblUsername, lblPassword, lblIssueDescription, lblTodayDate;
    private JRadioButton rbtnAuditor, rbtnUser;
    private JComboBox cmbIssueArea;
    private JButton btnSignIn, btnCreateNewIssue;
    private JTextArea txaReport;
    private JScrollPane scroll;
    private JTable table;
    private DefaultTableModel tableModel;
    private JList navList;

    private AuditClient() {
        this.setPreferredSize(new Dimension(700,400));
        pnlNorth = new JPanel();
        pnlWest = new JPanel();
        pnlEast = new JPanel();
        pnlSouth = new JPanel();
        pnlCenter = new JPanel();

        txfUsername = new JTextField();
        pfPassword = new JPasswordField();

        lblHeading = new JLabel("sign in required");
        lblUsername = new JLabel("Username: ");
        lblPassword = new JLabel("Password: ");

        rbtnAuditor = new JRadioButton("auditor");
        rbtnUser = new JRadioButton("user");

        btnSignIn = new JButton("sign in");
    }

    private void setUp(){
        pnlNorth.setLayout(new GridLayout());
        pnlWest.setLayout(new GridLayout());
        pnlEast.setLayout(new GridLayout());
        pnlSouth.setLayout(new GridLayout());
        pnlCenter.setLayout(new GridLayout());

        lblHeading.setHorizontalAlignment(JLabel.CENTER);
        pnlNorth.add(lblHeading);

        btnSignIn.addActionListener(this);

        this.add(pnlNorth, BorderLayout.NORTH);
        this.add(pnlWest, BorderLayout.WEST);
        this.add(pnlEast, BorderLayout.EAST);
        this.add(pnlCenter, BorderLayout.CENTER);
        this.add(pnlSouth, BorderLayout.SOUTH);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //userUI();
        loginForm();
    }

    private void loginForm(){
        this.setEnabled(false);
        loginFrame = new JFrame("Sign in");
        loginFrame.setPreferredSize(new Dimension(350,150));
        JPanel pnlTop = new JPanel();
        JPanel pnlRadio = new JPanel();
        JPanel pnlBottom = new JPanel();
        JLabel lblLoginType = new JLabel("Account: ");

        pnlTop.setLayout(new GridLayout(3,2));
        pnlRadio.setLayout(new GridLayout(1,2));
        pnlBottom.setLayout(new GridLayout());

        ButtonGroup loginType = new ButtonGroup();
        loginType.add(rbtnAuditor);
        loginType.add(rbtnUser);

        pnlTop.add(lblUsername);
        pnlTop.add(txfUsername);
        pnlTop.add(lblPassword);
        pnlTop.add(pfPassword);
        pnlRadio.add(rbtnAuditor);
        pnlRadio.add(rbtnUser);
        pnlTop.add(lblLoginType);
        pnlTop.add(pnlRadio);
        pnlBottom.add(btnSignIn);

        txfUsername.requestFocus();

        loginFrame.add(pnlTop, BorderLayout.NORTH);
        loginFrame.add(pnlBottom, BorderLayout.SOUTH);

        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
        loginFrame.setAlwaysOnTop(true);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new AuditClient().setUp();
        //System.out.println(new AuditClient().todayDate());
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == btnSignIn) {
            System.out.println("Signing in: " + txfUsername.getText());
            String pass = new String(pfPassword.getPassword());
            System.out.println("With Password: " + pass);
            if(txfUsername.getText().isEmpty() || pass.isEmpty()) {
            }
            else {
                if(rbtnUser.isSelected()) {
                    this.setEnabled(true);
                    lblHeading.setText("Welcome " + txfUsername.getText());
                    pnlNorth.updateUI();
                    loginFrame.setVisible(false);
                    userUI();
                }
                else if(rbtnAuditor.isSelected()){
                    this.setEnabled(true);
                    lblHeading.setText("Welcome " + txfUsername.getText());
                    pnlNorth.updateUI();
                    loginFrame.setVisible(false);
                    auditorUI();
                }
            }
            clearLoginForm();
        }
    }

    private void clearLoginForm(){
        txfUsername.setText("");
        pfPassword.setText("");
        txfUsername.requestFocus();
    }

    private void signOut(){
        pnlWest.removeAll();
        pnlEast.removeAll();
        pnlSouth.removeAll();
        pnlCenter.removeAll();
        loginFrame.setVisible(true);
        this.setEnabled(false);
        lblHeading.setText("sign in required");
        pnlNorth.updateUI();
    }

    private void userUI(){
        String list[] = {"Dashboard", "Raise Issue", "My Issues", "User Profile", "sign out"};
        navList = new JList(list);
        pnlWest.removeAll();
        pnlWest.add(navList);
        userNavListSelection();
    }

    private void auditorUI(){
        String list[] = {"Dashboard", "View Open Tickets", "View Closed Tickets", "All Issues","Block User", "User Profile", "Add User", "sign out"};
        navList = new JList(list);
        pnlWest.removeAll();
        pnlWest.add(navList);
        auditorNavListSelection();
    }

    private void userNavListSelection(){
        navList.addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()){
                switch(navList.getSelectedIndex()){
                    case 0:
                        System.out.println("Dashboard under construction!");
                        break;
                    case 1:
                        createIssueUI();
                        break;
                    case 2:
                        myIssuesUI();
                        break;
                    case 3:
                        System.out.println("User Profile under construction!");
                        break;
                    case 4:
                        signOut();
                        break;
                    default: JOptionPane.showMessageDialog(this, "Invalid Selection made.");
                }
            }
        });
    }

    private void auditorNavListSelection(){
        navList.addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting())
            try
            {
                switch(navList.getSelectedIndex()){
                    case 0:
                        System.out.println("Dashboard under construction!");
                        break;
                    case 1:
                        viewOpenTickets();
                        break;
                    case 2:
                        System.out.println("View Closed Tickets under construction!");
                        break;
                    case 3:
                        viewOpenAllIssues();
                        break;
                    case 4:
                        System.out.println("My Profile under construction!");
                        break;
                    case 5:
                        System.out.println("Add User under construction!");
                        break;
                    case 6:
                        addUser();
                        break;
                    case 7:
                        signOut();
                        break;
                    default: JOptionPane.showMessageDialog(this, "Invalid Selection made.");
                }
            }catch (Exception exception) {
                System.out.println("Null pointer exception");
            }
        });
    }
    private void addUser(){
        pnlCenter.removeAll();
        pnlEast.removeAll();
        pnlSouth.removeAll();


        pnlCenter.setLayout(new GridLayout(2,1));
        JPanel pnlTop = new JPanel();
        JPanel pnlRadio = new JPanel();
        JPanel pnlBottom = new JPanel();


        pnlTop.setLayout(new GridLayout(8,2));
        pnlRadio.setLayout(new GridLayout(1,2));
        pnlBottom.setLayout(new GridLayout(1,1));


        ButtonGroup loginType = new ButtonGroup();
        loginType.add(rbtnAuditor);
        loginType.add(rbtnUser);


        JLabel lblFirstName = new JLabel("First Name");
        JLabel lblLastName = new JLabel("Last Name");
        JLabel lblUsername = new JLabel("Username");
        JLabel lblPassword = new JLabel("Password");
        JLabel lblLoginType = new JLabel("Account: ");
        JLabel lblEmail = new JLabel("Email Address");
        JLabel lblPhone = new JLabel("Phone number");
        JTextField txtFirstName = new JTextField();
        JTextField txtLastName = new JTextField();
        JTextField txtUsername = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtPhone = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JButton submitButton =new JButton("Add user");




        // left.add(addUserHeading);
        pnlTop.add(lblFirstName);
        pnlTop.add(txtFirstName);
        pnlTop.add(lblLastName);
        pnlTop.add(txtLastName);
        pnlTop.add(lblUsername);
        pnlTop.add(txtUsername);
        pnlTop.add(lblPassword);
        pnlTop.add(txtPassword);
        pnlTop.add(lblEmail);
        pnlTop.add(txtEmail);
        pnlTop.add(lblPhone);
        pnlTop.add(txtPhone);
        pnlRadio.add(rbtnAuditor);
        pnlRadio.add(rbtnUser);
        pnlTop.add(lblLoginType);
        pnlTop.add(pnlRadio);
        pnlTop.add(submitButton);

        txtUsername.requestFocus();


        pnlCenter.add(pnlTop, BorderLayout.NORTH);

        pnlCenter.add(pnlBottom, BorderLayout.SOUTH);


        pnlCenter.updateUI();
        pnlEast.updateUI();

    }

    private String todayDate(){
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        Date date = new Date();
        return df.format(date);
    }

    private void createIssueUI(){
        pnlCenter.removeAll();
        pnlEast.removeAll();
        pnlSouth.removeAll();

        txaReport = new JTextArea();

        scroll = new JScrollPane(txaReport);
        txfTodayDate = new JTextField();
        lblIssueDescription = new JLabel("Issue Description: ");
        lblTodayDate = new JLabel("Raised Date: ");
        JLabel lblIssueArea = new JLabel("Department Area: ");
        Object areas[] = {"Administrator", "Informatics and Design", "Financial Office", "Housing", "Other"};
        cmbIssueArea = new JComboBox(areas);
        btnCreateNewIssue = new JButton("raise issue");
        pnlCenter.setLayout(new GridLayout(4,1));
        pnlSouth.setLayout(new GridLayout(1,2));

        JLabel lblConfirm = new JLabel("Submit here >> ");

        pnlCenter.add(lblIssueDescription);
        pnlCenter.add(scroll);
        pnlCenter.add(lblIssueArea);
        pnlCenter.add(cmbIssueArea);
        pnlCenter.add(lblTodayDate);
        pnlCenter.add(txfTodayDate);

        lblConfirm.setHorizontalAlignment(JLabel.RIGHT);
        pnlSouth.add(lblConfirm);
        pnlSouth.add(btnCreateNewIssue);
        pnlCenter.add(pnlSouth);

        txfTodayDate.setText(todayDate());
        txfTodayDate.setEnabled(false);

        btnCreateNewIssue.setHorizontalAlignment(JButton.CENTER);
        btnCreateNewIssue.setPreferredSize(new Dimension(25,25));

        btnCreateNewIssue.addActionListener(this);
        pnlCenter.updateUI();
    }

    private void myIssuesUI(){
        pnlCenter.removeAll();
        pnlEast.removeAll();
        pnlSouth.removeAll();
        JPanel left = new JPanel();
        JPanel right = new JPanel();

        pnlCenter.setLayout(new GridLayout(1,2));
        right.setLayout(new GridLayout(4,1));
        left.setLayout(new GridLayout(2,1));

        JLabel issueHeading = new JLabel("Issues");
        JLabel reportHeading = new JLabel("Report");
        txaReport = new JTextArea();
        table = new JTable();
        lblUsername = new JLabel("By: XxXxXx");
        JLabel lblDate = new JLabel("Date: XxxXXxX");
        Object[] columns = {"Status", "Description","Raised","Resolved","Validated"};
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columns);
        table.setModel(tableModel);
        table.setBackground(new Color(123, 123,123));
        table.setForeground(Color.BLACK);
        table.setFont(new Font("Arial", Font.BOLD, 16));
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        scroll = new JScrollPane(table);

        left.add(issueHeading);
        left.add(scroll);

        scroll = new JScrollPane(txaReport);
        right.add(reportHeading);
        right.add(scroll);
        right.add(lblUsername);
        right.add(lblDate);

        pnlCenter.add(left);
        pnlCenter.add(right);

        pnlCenter.updateUI();
    }
    
    private void viewOpenTickets(){
        pnlCenter.removeAll();
        pnlEast.removeAll();
        pnlSouth.removeAll();

        pnlCenter.setLayout(new GridLayout(1,2));
        JPanel left = new JPanel(new GridLayout(2,1));
        JPanel right = new JPanel(new GridLayout(2,1));

        table = new JTable();
        JLabel lblTicketHeading = new JLabel("Tickets");
        Object[] columns = {"Ticket Date", "Description"};
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columns);
        table.setModel(tableModel);
        table.setBackground(new Color(123, 123,123));
        table.setForeground(Color.BLACK);
        table.setFont(new Font("Arial", Font.BOLD, 16));
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        scroll = new JScrollPane(table);

        left.add(lblTicketHeading);
        left.add(scroll);

        JLabel reportHeading = new JLabel("Report feedback");
        txaReport = new JTextArea();
        scroll = new JScrollPane(txaReport);

        right.add(reportHeading);
        right.add(scroll);

        pnlEast.setLayout(new GridLayout(3,1));
        JRadioButton isValidate = new JRadioButton("is Validated");
        JRadioButton isResolved = new JRadioButton("is Resolved");
        JRadioButton isOpen = new JRadioButton("is Closed");
        pnlEast.add(isValidate);
        pnlEast.add(isResolved);
        pnlEast.add(isOpen);
        pnlCenter.add(left);
        pnlCenter.add(right);

        pnlCenter.updateUI();
        pnlEast.updateUI();
    }

    private void viewOpenAllIssues() {
        pnlCenter.removeAll();
        pnlEast.removeAll();
        pnlSouth.removeAll();

        pnlCenter.setLayout(new GridLayout(3, 1));

        JLabel AllIssues = new JLabel("All Issues");
        AllIssues.setHorizontalAlignment(JLabel.CENTER);

        Object[] columns = {"Issue ID","issueArea", "Description", "Raised", "Resolved", "Status", "Validated"};
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columns);
        table.setModel(tableModel);
        table.setBackground(new Color(123, 123, 123));
        table.setForeground(Color.BLACK);
        table.setFont(new Font("Arial", Font.BOLD, 16));
        table.setRowHeight(40);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        scroll = new JScrollPane(table);

        pnlCenter.add(AllIssues);
        pnlCenter.add(scroll);

        pnlCenter.updateUI();
        pnlEast.updateUI();

    }


}
