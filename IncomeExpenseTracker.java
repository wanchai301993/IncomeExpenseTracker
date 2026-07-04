import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IncomeExpenseTracker {
      
 private static JLabel lblTotalIncome, lblTotalExpense, lblBalance;
    private static DefaultTableModel model;

    public static void main(String[] args) {
        // 1. เปิดใช้งานธีม Nimbus
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) { }

        // 2. กำหนดฟอนต์ภาษาไทยมาตรฐาน
        Font thaiFont = new Font("Tahoma", Font.PLAIN, 15);
        Font boldFont = new Font("Tahoma", Font.BOLD, 15);

        JFrame frame = new JFrame("โปรแกรมบันทึกรายรับรายจ่าย");
        frame.setSize(520, 680);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Header ด้านบน
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setPreferredSize(new Dimension(500, 55));
        JLabel headerLabel = new JLabel("บันทึกรายรับรายจ่าย");
        headerLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        frame.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        centerPanel.setBackground(Color.WHITE);

        // 3. Form Panel (🔥 สั่งล็อกฟอนต์ทีละตัวตรงนี้ ไม่ให้สี่เหลี่ยมมากวนใจอีกแล้ว!)
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 12));
        formPanel.setBackground(Color.WHITE);

        JLabel lblDate = new JLabel("วันที่:");
        lblDate.setFont(boldFont);
        String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        JTextField txtDate = new JTextField(currentDate);
        txtDate.setFont(thaiFont);

        JLabel lblType = new JLabel("ประเภท:");
        lblType.setFont(boldFont);
        JComboBox<String> comboType = new JComboBox<>(new String[]{"รายรับ", "รายจ่าย"});
        comboType.setFont(thaiFont);
        comboType.setBackground(Color.WHITE);

        JLabel lblDetail = new JLabel("รายละเอียด:");
        lblDetail.setFont(boldFont);
        JTextField txtDetail = new JTextField();
        txtDetail.setFont(thaiFont);

        JLabel lblAmount = new JLabel("จำนวนเงิน (บาท):");
        lblAmount.setFont(boldFont);
        JTextField txtAmount = new JTextField();
        txtAmount.setFont(thaiFont);

        formPanel.add(lblDate);   formPanel.add(txtDate);
        formPanel.add(lblType);   formPanel.add(comboType);
        formPanel.add(lblDetail); formPanel.add(txtDetail);
        formPanel.add(lblAmount); formPanel.add(txtAmount);

        centerPanel.add(formPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // ปุ่มเพิ่มรายการ
        JButton btnAdd = new JButton("เพิ่มรายการ");
        btnAdd.setFont(boldFont);
        btnAdd.setBackground(new Color(52, 152, 219));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(btnAdd);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // ตารางแสดงข้อมูล
        String[] columns = {"วันที่", "ประเภท", "รายละเอียด", "จำนวนเงิน"};
        model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setFont(thaiFont);
        table.setRowHeight(26);
        
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(boldFont);
        tableHeader.setBackground(new Color(236, 240, 241));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(460, 200));
        centerPanel.add(scrollPane);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // กล่องสรุปยอดเงินด้านล่าง
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        summaryPanel.setBackground(Color.WHITE);

        lblTotalIncome = createSummaryBox("รายรับรวม", new Color(46, 204, 113));
        lblTotalExpense = createSummaryBox("รายจ่ายรวม", new Color(231, 76, 60));
        lblBalance = createSummaryBox("คงเหลือ", new Color(52, 152, 219));

        summaryPanel.add(lblTotalIncome);
        summaryPanel.add(lblTotalExpense);
        summaryPanel.add(lblBalance);
        centerPanel.add(summaryPanel);

        frame.add(centerPanel, BorderLayout.CENTER);

        // 4. การทำงานของปุ่มบันทึก
        btnAdd.addActionListener(e -> {
            try {
                String date = txtDate.getText().trim();
                String type = (String) comboType.getSelectedItem();
                String detail = txtDetail.getText().trim();
                String amountStr = txtAmount.getText().trim();

                if (detail.isEmpty() || amountStr.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "กรุณากรอกข้อมูลให้ครบถ้วนครับ", "แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double amount = Double.parseDouble(amountStr);

                model.addRow(new Object[]{date, type, detail, String.format("%.2f", amount)});
                updateSummary();
                
                txtDetail.setText("");
                txtAmount.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "กรุณากรอกจำนวนเงินเป็นตัวเลขเท่านั้นครับ", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    private static JLabel createSummaryBox(String title, Color bgColor) {
        JLabel label = new JLabel("<html><div style='text-align: center; font-size: 13px;'>" + title + "<br><b style='font-size: 16px;'>0.00</b></div></html>", SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(bgColor);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Tahoma", Font.PLAIN, 14));
        label.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));
        return label;
    }

    private static void updateSummary() {
        double income = 0, expense = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            String type = model.getValueAt(i, 1).toString();
            double amt = Double.parseDouble(model.getValueAt(i, 3).toString());
            if (type.equals("รายรับ")) {
                income += amt;
            } else {
                expense += amt;
            }
        }
        lblTotalIncome.setText("<html><div style='text-align: center; font-size: 13px;'>รายรับรวม<br><b style='font-size: 16px;'>" + String.format("%.2f", income) + "</b></div></html>");
        lblTotalExpense.setText("<html><div style='text-align: center; font-size: 13px;'>รายจ่ายรวม<br><b style='font-size: 16px;'>" + String.format("%.2f", expense) + "</b></div></html>");
        lblBalance.setText("<html><div style='text-align: center; font-size: 13px;'>คงเหลือ<br><b style='font-size: 16px;'>" + String.format("%.2f", (income - expense)) + "</b></div></html>");
    }
}
