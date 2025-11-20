package LibraryGUI;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class RegisterFrame extends JDialog {
    private static final long serialVersionUID = 1L;
	private JTextField txtFirst = new JTextField(12);
    private JTextField txtLast  = new JTextField(12);
    private JTextField txtBirth = new JTextField(10); // YYYY-MM-DD
    private JTextField txtPhone = new JTextField(12);
    private JTextField txtEmail = new JTextField(16);
    private JPasswordField txtPass = new JPasswordField(12);

    public RegisterFrame(Window parent) {
        super(parent, "Create Account", ModalityType.APPLICATION_MODAL);
        setSize(420, 300);
        setLocationRelativeTo(parent);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.LINE_END;

        gbc.gridx=0; gbc.gridy=0; form.add(new JLabel("First name:"), gbc);
        gbc.gridy++; form.add(new JLabel("Last name:"), gbc);
        gbc.gridy++; form.add(new JLabel("Birthday (YYYY-MM-DD):"), gbc);
        gbc.gridy++; form.add(new JLabel("Phone:"), gbc);
        gbc.gridy++; form.add(new JLabel("Email (username):"), gbc);
        gbc.gridy++; form.add(new JLabel("Password:"), gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx=1; gbc.gridy=0; form.add(txtFirst, gbc);
        gbc.gridy++; form.add(txtLast, gbc);
        gbc.gridy++; form.add(txtBirth, gbc);
        gbc.gridy++; form.add(txtPhone, gbc);
        gbc.gridy++; form.add(txtEmail, gbc);
        gbc.gridy++; form.add(txtPass, gbc);

        JButton btnCreate = new JButton("Create Account");
        JButton btnCancel = new JButton("Cancel");
        JPanel btns = new JPanel();
        btns.add(btnCancel); btns.add(btnCreate);

        add(form, BorderLayout.CENTER);
        add(btns, BorderLayout.SOUTH);

        btnCreate.addActionListener(e -> create());
        btnCancel.addActionListener(e -> dispose());
    }

    private void create() {
        try {
            String first = txtFirst.getText().trim();
            String last  = txtLast.getText().trim();
            LocalDate dob = LocalDate.parse(txtBirth.getText().trim());
            String phone = txtPhone.getText().trim();
            String email = txtEmail.getText().trim();
            String pass  = new String(txtPass.getPassword());

            if (first.isEmpty() || last.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields.");
                return;
            }
            if (LibraryData.findUser(email, pass) != null) {
                JOptionPane.showMessageDialog(this, "User already exists.");
                return;
            }
            int id = LibraryData.nextMemberId();
            Member m = new Member(id, first, last, dob, phone, email);
            LibraryData.MEMBERS.add(m);
            User u = new User(email, pass, User.Role.USER);
            u.setMemberId(id);
            LibraryData.USERS.add(u);
            JOptionPane.showMessageDialog(this, "Account created. You can now log in.");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid data: " + ex.getMessage());
        }
    }
}
