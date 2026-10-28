/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystem.views.users;

import hotelreservationsystem.db.PaymentDao;
import hotelreservationsystem.db.ReservationDao;
import hotelreservationsystem.db.RoomDao;
import hotelreservationsystem.models.Payment;
import hotelreservationsystem.models.PaymentMethod;
import hotelreservationsystem.models.Reservation;
import hotelreservationsystem.models.ReservationStatus;
import hotelreservationsystem.models.Room;
import hotelreservationsystem.models.User;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FlowLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrator
 */
public class UserMakePaymentView extends javax.swing.JFrame {

    private User user;

    ReservationDao reservationDao = new ReservationDao();
    RoomDao roomDao = new RoomDao();
    PaymentDao paymentDao = new PaymentDao();

    JTable tblUnpaid = new JTable();
    JComboBox<String> cmbMethod = new JComboBox<>();
    JTextField txtCardNumber = new JTextField(16);
    JTextField txtCvv = new JTextField(4);
    JButton btnPay = new JButton("Pay Selected Reservation");
    JLabel lblSummary = new JLabel(" ");

    public UserMakePaymentView(User user) {
        this.user = user;

        buildScreen();
        loadComboBox();
        showUnpaidReservations();
    }

    private void buildScreen() {

        setTitle("Make Payment");
        setSize(900, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("Select a reservation from the table and pay for it", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));
        title.setBorder(new EmptyBorder(10, 0, 0, 0));

        tblUnpaid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        form.add(new JLabel("Method:"));
        form.add(cmbMethod);
        form.add(new JLabel("Card Number:"));
        form.add(txtCardNumber);
        form.add(new JLabel("CVV:"));
        form.add(txtCvv);
        form.add(btnPay);

        JLabel hint = new JLabel("Simulation only: nothing is charged and the card number is not saved. A card number ending in 0000 is declined.", SwingConstants.CENTER);
        hint.setFont(hint.getFont().deriveFont(Font.ITALIC, 11f));

        lblSummary.setBorder(new EmptyBorder(0, 10, 0, 0));

        JPanel south = new JPanel(new BorderLayout());
        south.setBorder(new EmptyBorder(0, 6, 8, 6));
        south.add(form, BorderLayout.NORTH);
        south.add(hint, BorderLayout.CENTER);
        south.add(lblSummary, BorderLayout.SOUTH);

        getContentPane().setLayout(new BorderLayout(6, 6));
        getContentPane().add(title, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(tblUnpaid), BorderLayout.CENTER);
        getContentPane().add(south, BorderLayout.SOUTH);

        btnPay.addActionListener(e -> makePayment());
        cmbMethod.addActionListener(e -> cardFieldsOnOff());

        // when the user comes back from another window the list is reloaded
        addWindowFocusListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                showUnpaidReservations();
            }
        });
    }

    private void loadComboBox() {

        cmbMethod.removeAllItems();
        for (PaymentMethod method : PaymentMethod.values()) {
            cmbMethod.addItem(method.name());
        }
        cardFieldsOnOff();
    }

    // the card fields are only needed when paying by card
    private void cardFieldsOnOff() {

        boolean card = cmbMethod.getSelectedItem() != null && cmbMethod.getSelectedItem().toString().equals(PaymentMethod.CARD.name());

        txtCardNumber.setEnabled(card);
        txtCvv.setEnabled(card);
    }

    private void showUnpaidReservations() {

        String[] list = {"SL", "Room Number", "Check In", "Check Out", "Total Price"};
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setColumnIdentifiers(list);
        tblUnpaid.setModel(model);

        double due = 0;

        List<Reservation> rList = reservationDao.findByUserId(user.getId());

        for (Reservation r : rList) {

            if (r.getStatus() != ReservationStatus.BOOKED) {
                continue;
            }
            if (paymentDao.findByReservationId(r.getId()) != null) {
                continue;
            }

            Room room = roomDao.findById(r.getRoomId());

            model.addRow(new Object[]{r.getId(), room.getRoomNumber(), r.getCheckIn(), r.getCheckOut(), r.getTotalPrice()});
            due += r.getTotalPrice();
        }

        due = Math.round(due * 100) / 100.0;
        lblSummary.setText(model.getRowCount() + " unpaid reservation(s), total due: " + due);
    }

    private String makeReference() {

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
        return "PAY-" + time + (100 + new Random().nextInt(900));
    }

    private void makePayment() {

        int row = tblUnpaid.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a reservation from the table first");
            return;
        }

        Long reservationId = Long.valueOf(tblUnpaid.getModel().getValueAt(row, 0).toString());

        // read from the database again before taking any money
        Reservation reservation = reservationDao.findById(reservationId);

        if (reservation == null || !reservation.getUserId().equals(user.getId()) || reservation.getStatus() != ReservationStatus.BOOKED) {
            JOptionPane.showMessageDialog(this, "This reservation cannot be paid");
            showUnpaidReservations();
            return;
        }

        if (paymentDao.findByReservationId(reservationId) != null) {
            JOptionPane.showMessageDialog(this, "This reservation is already paid");
            showUnpaidReservations();
            return;
        }

        PaymentMethod method = PaymentMethod.valueOf(cmbMethod.getSelectedItem().toString());

        if (method == PaymentMethod.CARD) {

            String cardNumber = txtCardNumber.getText().replace(" ", "").trim();
            String cvv = txtCvv.getText().trim();

            if (!cardNumber.matches("\\d{16}")) {
                JOptionPane.showMessageDialog(this, "Card Number must have 16 digits");
                return;
            }

            if (!cvv.matches("\\d{3}")) {
                JOptionPane.showMessageDialog(this, "CVV must have 3 digits");
                return;
            }

            // simulated bank answer
            if (cardNumber.endsWith("0000")) {
                JOptionPane.showMessageDialog(this, "Payment declined by the bank. Please use another card");
                return;
            }
        }

        Payment payment = new Payment();

        payment.setReservationId(reservationId);
        payment.setAmount(reservation.getTotalPrice());
        payment.setMethod(method);
        payment.setReference(makeReference());
        payment.setPaidAt(LocalDateTime.now());

        paymentDao.save(payment);

        JOptionPane.showMessageDialog(this, "Payment successful\nReference: " + payment.getReference()
                + "\nAmount: " + payment.getAmount() + "\nMethod: " + method);

        txtCardNumber.setText("");
        txtCvv.setText("");
        showUnpaidReservations();
    }
}
