package za.ac.cput.views;

import jdk.nashorn.internal.scripts.JO;
import za.ac.cput.entity.*;
import za.ac.cput.factory.*;
import za.ac.cput.util.Client;
import okhttp3.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class AuditClient extends JFrame implements ActionListener {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static OkHttpClient client = new OkHttpClient();

    private JFrame loginFrame;
    private JPanel pnlNorth, pnlWest, pnlEast, pnlSouth, pnlCenter;
    private JTextField txfUsername, txfTodayDate, txfEmail, txfSurname, txfCellphone,
                         txtFirstName, txtLastName, txtUsername, txtEmail, txtPhone, txtSearch;
    private JPasswordField pfPassword, txtPassword;
    private JLabel lblHeading, lblUsername, lblPassword, lblIssueDescription, lblTodayDate, lblDate;
    private JRadioButton rbtnAuditor, rbtnUser, rbtnIsValidate, rbtnIsResolved, rbtnIsClosed, rbtnAddAuditor, rbtnAddUser;
    private JComboBox cmbIssueArea;
    private JButton btnSignIn, btnCreateNewIssue, btnUpdateIssue, btnUpdateUserProfile,
            submitNewUser, searchButton, submitButton;
    private JTextArea txaReport;
    private JScrollPane scroll;
    private static JTable table;
    private static DefaultTableModel tableModel;
    private JList navList;
    private UserAccount signedInUser;
    private Client server;
    private UniversityStaff user;
    private Auditor auditor;
    private ArrayList<Issue> allOpenIssues, allMyIssues;
    private Issue selectedIssue;
    private Report selectedIssueReport;
    private JTextField logInFormEmail;
    private JPasswordField logInFormPassword;

    private AuditClient() {
        this.setPreferredSize(new Dimension(700,400));
        pnlNorth = new JPanel();
        pnlWest = new JPanel();
        pnlEast = new JPanel();
        pnlSouth = new JPanel();
        pnlCenter = new JPanel();
        server = new Client();

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
        logInFormEmail = new JTextField();
        logInFormPassword = new JPasswordField();

        pnlTop.setLayout(new GridLayout(3,2));
        pnlRadio.setLayout(new GridLayout(1,2));
        pnlBottom.setLayout(new GridLayout());

        ButtonGroup loginType = new ButtonGroup();
        loginType.add(rbtnAuditor);
        loginType.add(rbtnUser);

        pnlTop.add(lblUsername);
        pnlTop.add(logInFormEmail);
        pnlTop.add(lblPassword);
        pnlTop.add(logInFormPassword);
        pnlRadio.add(rbtnAuditor);
        pnlRadio.add(rbtnUser);
        pnlTop.add(lblLoginType);
        pnlTop.add(pnlRadio);
        pnlBottom.add(btnSignIn);

        logInFormEmail.requestFocus();

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
            loginAttempt();
        }
        if(actionEvent.getSource() == btnCreateNewIssue) {
            validateNewIssue();
        }
        if(actionEvent.getSource() == btnUpdateIssue) {
            updateOpenIssue();
        }
        if(actionEvent.getSource() == btnUpdateUserProfile) {
            if(user != null)
                updateUserProfile("user");
            else if(auditor != null)
                updateUserProfile("auditor");
        }
        if(actionEvent.getSource() == submitNewUser) {
            verifyNewUser();
        }
        if(actionEvent.getSource() == searchButton) {
            findUser();
        }
        if(actionEvent.getSource() == submitButton) {
            try{
                server.deleteUser(txtSearch.getText());
                JOptionPane.showMessageDialog(this, "User deleted!");
                txtSearch.setText("");
                txtEmail.setText("");
                txtUsername.setText("");
            }
            catch (Exception e){
                JOptionPane.showMessageDialog(this, "User NOT deleted!");
            }
        }
    }

    private void clearLoginForm(){
        logInFormEmail.setText("");
        logInFormPassword.setText("");
        logInFormEmail.requestFocus();
    }

    private void signOut(){
        pnlWest.removeAll();
        pnlEast.removeAll();
        pnlSouth.removeAll();
        pnlCenter.removeAll();
        loginFrame.setVisible(true);
        this.setEnabled(false);
        user = null;
        auditor = null;
        lblHeading.setText("sign in required");

        pnlNorth.updateUI();
    }

    private void userUI(){
        String list[] = {"Dashboard", "Raise Issue", "My Issues", "User Profile", "sign out"};
        navList = new JList(list);
        pnlWest.removeAll();
        pnlCenter.removeAll();
        pnlEast.removeAll();
        pnlSouth.removeAll();
        pnlWest.add(navList);
        userNavListSelection();
        pnlWest.updateUI();
    }

    private void auditorUI(){
        String list[] = {"Dashboard", "View Open Issues", "View Closed Issues", "All Tickets","Block User", "User Profile", "Add User", "sign out"};
        navList = new JList(list);
        pnlWest.removeAll();
        pnlCenter.removeAll();
        pnlEast.removeAll();
        pnlSouth.removeAll();
        pnlWest.add(navList);
        auditorNavListSelection();
        pnlWest.updateUI();
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
                        userProfile();
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
                        viewOpenIssuesUI();
                        break;
                    case 2:
                        viewClosedIssuesUI();
                        break;
                    case 3:
                        viewAllTicketsUI();
                        break;
                    case 4:
                        blockUser();
                        break;
                    case 5:
                        userProfile();
                        break;
                    case 6:
                        addUserUI();
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

    private void addUserUI(){
        pnlCenter.removeAll();
        pnlEast.removeAll();
        pnlSouth.removeAll();

        pnlCenter.setLayout(new GridLayout(2,1));
        JPanel pnlTop = new JPanel();
        JPanel pnlRadio = new JPanel();
        JPanel pnlBottom = new JPanel();
        rbtnAddAuditor = new JRadioButton("Auditor");
        rbtnAddUser = new JRadioButton("User");

        pnlTop.setLayout(new GridLayout(8,2));
        pnlRadio.setLayout(new GridLayout(1,2));
        pnlBottom.setLayout(new GridLayout(1,1));

        ButtonGroup loginType = new ButtonGroup();
        loginType.add(rbtnAddAuditor);
        loginType.add(rbtnAddUser);

        JLabel lblFirstName = new JLabel("First Name");
        JLabel lblLastName = new JLabel("Last Name");
        JLabel lblUsername = new JLabel("Username");
        JLabel lblPassword = new JLabel("Password");
        JLabel lblLoginType = new JLabel("Account: ");
        JLabel lblEmail = new JLabel("Email Address");
        JLabel lblPhone = new JLabel("Phone number");
        txtFirstName = new JTextField();
        txtLastName = new JTextField();
        txtUsername = new JTextField();
        txtEmail = new JTextField();
        txtPhone = new JTextField();
        txtPassword = new JPasswordField();
        submitNewUser =new JButton("Add user");
        submitNewUser.addActionListener(this);

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
        pnlRadio.add(rbtnAddAuditor);
        pnlRadio.add(rbtnAddUser);
        pnlTop.add(lblLoginType);
        pnlTop.add(pnlRadio);
        pnlTop.add(submitNewUser);

        txtUsername.requestFocus();

        pnlCenter.add(pnlTop, BorderLayout.NORTH);

        pnlCenter.add(pnlBottom, BorderLayout.SOUTH);

        pnlCenter.updateUI();
        pnlEast.updateUI();
    }

    private void clearAddUserForm(){
        txtUsername.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
    }

    private void verifyNewUser(){
        String pass = new String(txtPassword.getPassword());
        if(txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty() || pass.isEmpty() ||
                txtUsername.getText().isEmpty() || txtEmail.getText().isEmpty() || txtPhone.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "All fields are required!");
        }
        else{
            UserAccount newUserAccount =  UserAccountFactory.buildUserAccount(txtEmail.getText(), pass, 3, todayDate());
            if(rbtnAddUser.isSelected()){
                UniversityStaff newUserAdd = UniversityStaffFactory.buildUniversityStaff(newUserAccount.getUserId(), txtFirstName.getText(),
                        txtLastName.getText(), txtPhone.getText());
                server.createUserAccount(newUserAccount);
                server.createUser(newUserAdd);
                JOptionPane.showMessageDialog(this, "User Account Added");
                clearAddUserForm();
            }
            else if(rbtnAddAuditor.isSelected()){
                Auditor newAuditor = AuditorFactory.buildAuditor(newUserAccount.getUserId(),txtFirstName.getText(),txtLastName.getText(), txtPhone.getText());
                server.createUserAccount(newUserAccount);
                server.createAuditor(newAuditor);
                JOptionPane.showMessageDialog(this, "Auditor Account Added");
                clearAddUserForm();
            }
            else {
                JOptionPane.showMessageDialog(this, "Select Account type!");
            }
        }
    }

    private String todayDate(){
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        Date date = new Date();
        return df.format(date);
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
        lblDate = new JLabel("Date: XxxXXxX");
        Object[] columns = {"Status", "Description","Raised","Resolved","Area"};
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

        loadMyIssues();
        pnlCenter.updateUI();
    }

    private void loadMyIssues(){
        //LOAD Issues "Status", "Description","Raised","Resolved","Area"
        Issue issue;
        Vector row;
        allMyIssues = server.getIssuesOfUser(user.getStaffID());
        for (int i = 0; i < allMyIssues.size(); i++) {
            row = new Vector();
            issue = allMyIssues.get(i);
            String status = "Closed";
            if(issue.getIssueStatus() >= 0)
                status = "Open";
            row.add(status);
            row.add(issue.getIssueDescription());
            row.add(issue.getIssueRaisedDate());
            row.add(issue.getIssueResolvedDate());
            row.add(issue.getIssueArea());
            tableModel.addRow(row);
        }
        tableListener("loadMyIssues");
    }
    
    private void viewOpenIssuesUI(){
        pnlCenter.removeAll();
        pnlEast.removeAll();
        pnlSouth.removeAll();

        pnlCenter.setLayout(new GridLayout(1,2));
        pnlSouth.setLayout(new GridLayout(1,2));
        JPanel left = new JPanel(new GridLayout(2,1));
        JPanel right = new JPanel(new GridLayout(2,1));

        table = new JTable();
        JLabel lblTicketHeading = new JLabel("Issue");
        Object[] columns = {"Area", "Description", "Opened Date"};
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columns);
        table.setModel(tableModel);
        tableModel.setNumRows(0);
        table.setBackground(new Color(123, 123,123));
        table.setForeground(Color.BLACK);
        table.setFont(new Font("Arial", Font.BOLD, 16));
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        scroll = new JScrollPane(table);

        btnUpdateIssue = new JButton("Respond To Issue");
        btnUpdateIssue.addActionListener(this);
        left.add(lblTicketHeading);
        left.add(scroll);

        JLabel reportHeading = new JLabel("Report feedback");
        txaReport = new JTextArea();
        scroll = new JScrollPane(txaReport);

        right.add(reportHeading);
        right.add(scroll);

        pnlEast.setLayout(new GridLayout(3,1));
        rbtnIsValidate = new JRadioButton("is Validated");
        rbtnIsResolved = new JRadioButton("is Resolved");
        rbtnIsClosed = new JRadioButton("select to Closed");
        pnlEast.add(rbtnIsValidate);
        pnlEast.add(rbtnIsResolved);
        pnlEast.add(rbtnIsClosed);
        pnlCenter.add(left);
        pnlCenter.add(right);
        pnlSouth.add(btnUpdateIssue);

        btnUpdateIssue.setEnabled(false);
        loadOpenIssues();

        pnlCenter.updateUI();
        pnlSouth.updateUI();
        pnlEast.updateUI();
    }

    private void loadOpenIssues(){
        //LOAD TICKETS
        Issue issue;
        Vector row;
        allOpenIssues = server.getAllOpenIssues();
        for (int i = 0; i < allOpenIssues.size(); i++) {
            row = new Vector();
            issue = allOpenIssues.get(i);
            row.add(issue.getIssueArea());
            row.add(issue.getIssueDescription());
            row.add(issue.getIssueRaisedDate());
            tableModel.addRow(row);
        }
        tableListener("loadOpenIssues");
    }

    private void loadClosedIssues(){
        //LOAD TICKETS "Description","Area", "Opened", "Closed", "Closed By", "isResolved",
        Issue issue;
        Vector row;
        ArrayList<Issue> allClosedIssues = server.getAllClosedIssues();
        for (int i = 0; i < allClosedIssues.size(); i++) {
            row = new Vector();
            issue = allClosedIssues.get(i);
            Report issueReport = server.getReport(issue.getIssueId());
            row.add(issue.getIssueDescription());
            row.add(issue.getIssueArea());
            row.add(issue.getIssueRaisedDate());
            row.add(issue.getIssueResolvedDate());
            row.add(issueReport.getReportAuth());
            boolean resolved = issue.getIsResolved() >= 0;
            row.add(resolved);
            tableModel.addRow(row);
        }
    }

    private void loginAttempt(){
        String pass = new String(logInFormPassword.getPassword());
        if(logInFormEmail.getText().isEmpty() || pass.isEmpty()) {
        }
        else {
            signedInUser = server.login(logInFormEmail.getText(), pass); //Auditor -->test25  USER -->tt23
            if(signedInUser == null){
                JOptionPane.showMessageDialog(loginFrame, "Account Does Not exist!!");
            }
            else if(signedInUser.getLoginStatus() < 0){
                JOptionPane.showMessageDialog(this, "Account is Block");
            }
            else if(rbtnUser.isSelected()) {
                try{
                    user = server.getUser(signedInUser.getUserId());
                    if (user != null){
                        this.setEnabled(true);
                        lblHeading.setText("Welcome " + user.getStaffFirstName() + " " + user.getStaffSurname());
                        pnlNorth.updateUI();
                        loginFrame.setVisible(false);
                        userUI();
                    }
                }
                catch (Exception e){
                    clearLoginForm();
                }
            }
            else if(rbtnAuditor.isSelected()){
                try{
                    auditor = server.getAuditor(signedInUser.getUserId());
                    if(auditor != null){
                        this.setEnabled(true);
                        lblHeading.setText("Welcome " + auditor.getAuditorFirstName() + " " + auditor.getAuditorSurname());
                        pnlNorth.updateUI();
                        loginFrame.setVisible(false);
                        auditorUI();
                    }
                }
                catch (Exception e){
                    clearLoginForm();
                }
            }
        }
        clearLoginForm();
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
        pnlSouth.updateUI();
        pnlCenter.updateUI();
    }

    private void validateNewIssue() {
        if (txaReport.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Describe your Issue.");
            txaReport.requestFocus();
        }
        try {
            Issue newIssue = IssueFactory.createIssue(txaReport.getText(), cmbIssueArea.getSelectedItem().toString(), todayDate(), "NA", 1, -1, -1);
            Issue response = server.createIssue(newIssue);
            Ticket newTicket = TicketFactory.buildTicket(response.getIssueId(), response.getIssueDescription(), response.getIssueRaisedDate());
            Ticket tResponse = server.createTicket(newTicket);
            Report newReport = ReportFactory.createReport(tResponse.getTicketId(), "NA", "**empty**","NA");
            Report rResponse = server.createReport(newReport);
            UserIssue userIssue = UserIssueFactory.createUserIssue(user.getStaffID(), response.getIssueId());
            server.createUserIssue(userIssue);
            JOptionPane.showMessageDialog(this, "Issue Recorded. Auditor will reply in due.\nThank you.");
            txaReport.setText("");
            cmbIssueArea.setSelectedIndex(0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Some Unknown error occurred while creating new Issue");
        }
    }

    private void viewAllTicketsUI () {
        pnlSouth.removeAll();
        pnlCenter.removeAll();
        pnlCenter.setLayout(new GridLayout(3, 1));

        JLabel AllIssues = new JLabel("All Tickets");
        AllIssues.setHorizontalAlignment(JLabel.CENTER);

        Object[] columns = {"Ticket ID",  "Description", "Date"};
        table = new JTable();
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

        loadAllTickets();
        pnlCenter.updateUI();
        pnlEast.updateUI();

    }

    private void readSelectedIssue(){
        selectedIssue = allOpenIssues.get(table.getSelectedRow());
        selectedIssueReport = server.getReport(selectedIssue.getIssueId());
        txaReport.setText(selectedIssueReport.getReport());
    }

    private void tableListener(String method){
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){
                if(e.getSource() != null){
                    if(table.getSelectedRow() >= 0 && method.equals("loadOpenIssues")){
                        readSelectedIssue();
                        btnUpdateIssue.setEnabled(true);
                    }
                    if(table.getSelectedRow() >= 0 && method.equals("loadMyIssues")){
                        selectedIssue = allMyIssues.get(table.getSelectedRow());
                        selectedIssueReport = server.getReport(selectedIssue.getIssueId());

                        txaReport.setText(selectedIssueReport.getReport());
                        lblUsername.setText("By: " + selectedIssueReport.getReportAuth());
                        lblDate.setText("Date: " + selectedIssueReport.getReportDate());
                    }
                }
            }
        });
    }

    private void updateOpenIssue(){
        if(txaReport.getText().equals("**empty**") || txaReport.getText().isEmpty())
            JOptionPane.showMessageDialog(this, "Please write feedback");
        else {
            int closed = 1;
            if (rbtnIsClosed.isSelected())
                closed = -1;
            int resolved = -1;
            if (rbtnIsResolved.isSelected())
                resolved = 1;
            int validated = -1;
            if (rbtnIsValidate.isSelected())
                validated = 1;
            Issue update = new Issue.Builder().copy(selectedIssue)
                    .isResolved(resolved)
                    .issueStatus(closed)
                    .isValidated(validated)
                    .issueResolvedDate(todayDate())
                    .build();

            Report updateReport = new Report.Builder().copy(selectedIssueReport)
                    .setReportAuth(auditor.getAuditorFirstName())
                    .setReport(txaReport.getText())
                    .setReportDate(todayDate())
                    .build();
            try {
                server.updateIssue(update);
                server.updateReport(updateReport);
                JOptionPane.showMessageDialog(this, "Issue Updated!");
                viewOpenIssuesUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void viewClosedIssuesUI() {
        pnlCenter.removeAll();
        pnlEast.removeAll();
        pnlSouth.removeAll();
        JPanel left = new JPanel();


        pnlCenter.setLayout(new GridLayout(1,1));
        left.setLayout(new GridLayout(4,2));

        JLabel ticketHeading = new JLabel("Closed Tickets");

        table = new JTable();
        Object[] columnHeader = {"Description","Area", "Opened", "Closed", "Closed By", "isResolved",};
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columnHeader);
        table.setModel(tableModel);
        table.setBackground(new Color(123, 123,123));
        table.setForeground(Color.BLACK);
        table.setFont(new Font("Arial", Font.BOLD, 16));
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        scroll = new JScrollPane(table);

        left.add(ticketHeading);
        left.add(scroll);

        loadClosedIssues();
        pnlCenter.add(left);
        pnlCenter.updateUI();
    }

    private void loadAllTickets(){
        //LOAD TICKETS "Ticket ID",  "Description", "Date"}
        Ticket ticket;
        Vector row;
        ArrayList<Ticket> allTickets = server.getAllTickets();
        for (int i = 0; i < allTickets.size(); i++) {
            row = new Vector();
            ticket = allTickets.get(i);
            row.add(ticket.getTicketId());
            row.add(ticket.getTicketDescription());
            row.add(ticket.getTicketDate());
            tableModel.addRow(row);
        }
    }

    private void userProfile(){
        pnlCenter.removeAll();
        pnlEast.removeAll();
        pnlSouth.removeAll();

        pnlCenter.setLayout(new GridLayout(2,1));
        pnlSouth.setLayout(new GridLayout());
        JPanel pnlTop = new JPanel();
        JPanel pnlRadio = new JPanel();
        JPanel pnlBottom = new JPanel();

        pnlTop.setLayout(new GridLayout(5,2));
        pnlRadio.setLayout(new GridLayout(1,2));
        pnlBottom.setLayout(new GridLayout(1,1));

        JLabel lblEmail = new JLabel("Email Address");
        JLabel lblPassword = new JLabel("Password");
        JLabel lblFirstName = new JLabel("First Name");
        JLabel lblSurname = new JLabel("Surname");
        JLabel lblCellphone = new JLabel("Cellphone");
        txfEmail = new JTextField();
        pfPassword = new JPasswordField();
        txfUsername = new JTextField();
        txfSurname = new JTextField();
        txfCellphone = new JTextField();

        btnUpdateUserProfile = new JButton("Update");
        pnlSouth.add(btnUpdateUserProfile);

        btnUpdateUserProfile.addActionListener(this);

        Border border = BorderFactory.createLineBorder(Color.BLACK);

        txfUsername.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(0,12,0,0)));
        pfPassword.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(0,12,0,0)));
        txfEmail.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(0,12,0,0)));
        txfSurname.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(0,12,0,0)));
        txfCellphone.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(0,12,0,0)));

        txfEmail.setEnabled(false);
        txfUsername.setEnabled(false);
        txfSurname.setEnabled(false);

        pnlTop.add(lblEmail);
        pnlTop.add(txfEmail);
        pnlTop.add(lblPassword);
        pnlTop.add(pfPassword);
        pnlTop.add(lblFirstName);
        pnlTop.add(txfUsername);
        pnlTop.add(lblSurname);
        pnlTop.add(txfSurname);
        pnlTop.add(lblCellphone);
        pnlTop.add(txfCellphone);

        pnlCenter.add(pnlTop);//, BorderLayout.NORTH);
        pnlCenter.add(pnlBottom);//, BorderLayout.SOUTH);

        loadUserProfile();
        pnlCenter.updateUI();
        pnlSouth.updateUI();
        pnlEast.updateUI();

    }

    private void loadUserProfile(){
        if (user != null) {
            txfEmail.setText(signedInUser.getEmail());
            txfUsername.setText(user.getStaffFirstName());
            txfSurname.setText(user.getStaffSurname());
            txfCellphone.setText(user.getCellphone());
            pfPassword.setText(signedInUser.getPassword());
        }
        else if(auditor != null){
            txfEmail.setText(signedInUser.getEmail());
            txfUsername.setText(auditor.getAuditorFirstName());
            txfSurname.setText(auditor.getAuditorSurname());
            txfCellphone.setText(auditor.getCellphone());
            pfPassword.setText(signedInUser.getPassword());
        }
    }

    private void updateUserProfile(String userType){
        String pass = new String(pfPassword.getPassword());
        if(pass.isEmpty() || txfCellphone.getText().isEmpty())
            JOptionPane.showMessageDialog(this, "All field are required!");
        else{
            if(userType.equals("user")){
                signedInUser = new UserAccount.Builder().copy(signedInUser).setpassword(pass).build();
                user = new UniversityStaff.Builder().copy(user).cellphone(txfCellphone.getText()).Build();
                server.updateUserAccount(signedInUser);
                server.updateUser(user);

                JOptionPane.showMessageDialog(this, "Information Updated!");
            }
            else if(userType.equals("auditor")){
                signedInUser = new UserAccount.Builder().copy(signedInUser).setpassword(pass).build();
                auditor = new Auditor.Builder().copy(auditor).cellphone(txfCellphone.getText()).build();
                server.updateUserAccount(signedInUser);
                server.updateAuditor(auditor);

                JOptionPane.showMessageDialog(this, "Information Updated!");
            }
        }
    }

    private void blockUser(){
        pnlCenter.removeAll();
        pnlEast.removeAll();
        pnlSouth.removeAll();

        pnlCenter.setLayout(new GridLayout(2,1));
        JPanel pnlTop = new JPanel();
        JPanel pnlBottom = new JPanel();

        pnlTop.setLayout(new GridLayout(5,2));
        pnlBottom.setLayout(new GridLayout(5,1));

        JLabel lblUsername = new JLabel("Username:");
        JLabel lblEmail = new JLabel("Lastname:");
        JLabel lblSpace = new JLabel();
        JLabel lblSpace1 = new JLabel();
        txtUsername = new JTextField();
        txtEmail = new JTextField();
        txtSearch = new JTextField();
        searchButton =new JButton("Search");
        submitButton =new JButton("Submit");

        searchButton.addActionListener(this);


        pnlTop.add(txtSearch);
        pnlTop.add(searchButton);
        pnlTop.add(lblSpace);
        pnlTop.add(lblSpace1);
        pnlTop.add(lblUsername);
        pnlTop.add(txtUsername);
        pnlTop.add(lblEmail);
        pnlTop.add(txtEmail);
        pnlBottom.add(submitButton);
        submitButton.addActionListener(this);

        txtEmail.setEnabled(false);
        txtUsername.setEnabled(false);
        submitButton.setEnabled(false);
        pnlCenter.add(pnlTop, BorderLayout.NORTH);
        pnlCenter.add(pnlBottom);
        pnlCenter.updateUI();
        pnlEast.updateUI();
    }

    private void findUser(){
        if(txtSearch.getText().isEmpty())
            JOptionPane.showMessageDialog(this, "NO ID provided");
        else{
            try{
                UniversityStaff foundUser = server.getUser(txtSearch.getText());
                if(foundUser != null){
                    txtEmail.setText(foundUser.getStaffSurname());
                    txtUsername.setText(foundUser.getStaffFirstName());
                    submitButton.setEnabled(true);
                }
                else
                    JOptionPane.showMessageDialog(this, "No such User Account exist!");
            }
            catch (Exception e){
                JOptionPane.showMessageDialog(this, "No such User Account exist!");
            }

        }
    }


}

