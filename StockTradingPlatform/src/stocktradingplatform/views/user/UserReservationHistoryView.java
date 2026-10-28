/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystem.views.users;

import hotelreservationsystem.db.PaymentDao;
import hotelreservationsystem.db.ReservationDao;
import hotelreservationsystem.db.RoomDao;
import hotelreservationsystem.models.Payment;
import hotelreservationsystem.models.Reservation;
import hotelreservationsystem.models.Room;
import hotelreservationsystem.models.User;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrator
 */
public class UserReservationHistoryView extends javax.swing.JFrame {

    private User user;

    ReservationDao reservationDao = new ReservationDao();
    RoomDao roomDao = new RoomDao();
    PaymentDao paymentDao = new PaymentDao();

    JTable tblHistory = new JTable();
    JTextArea txtDetails = new JTextArea();

    public UserReservationHistoryView(User user) {
        this.user = user;

        buildScreen();
        showHistory();
    }

    private void buildScreen() {

        setTitle("My Reservations");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("All your reservations. Select one to see its full details", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));
        title.setBorder(new EmptyBorder(10, 0, 0, 0));

        tblHistory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        txtDetails.setEditable(false);
        txtDetails.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        txtDetails.setBorder(new EmptyBorder(8, 10, 8, 10));
        txtDetails.setText("Select a reservation from the table.");

        JScrollPane detailsScroll = new JScrollPane(txtDetails);
        detailsScroll.setPreferredSize(new Dimension(100, 220));

        getContentPane().setLayout(new BorderLayout(6, 6));
        getContentPane().add(title, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(tblHistory), BorderLayout.CENTER);
        getContentPane().add(detailsScroll, BorderLayout.SOUTH);

        tblHistory.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showDetails();
            }
        });

        // when the user comes back from another window the list is reloaded
        addWindowFocusListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                showHistory();
            }
        });
    }

    private void showHistory() {

        String[] list = {"SL", "Room Number", "Category", "Check In", "Check Out", "Nights", "Total Price", "Status", "Payment"};
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setColumnIdentifiers(list);
        tblHistory.setModel(model);

        // newest first
        List<Reservation> rList = reservationDao.findByUserId(user.getId());

        for (Reservation r : rList) {

            Room room = roomDao.findById(r.getRoomId());
            long nights = ChronoUnit.DAYS.between(r.getCheckIn(), r.getCheckOut());
            String payment = paymentDao.findByReservationId(r.getId()) == null ? "Not Paid" : "Paid";

            model.addRow(new Object[]{r.getId(), room.getRoomNumber(), room.getCategory(), r.getCheckIn(), r.getCheckOut(),
                nights, r.getTotalPrice(), r.getStatus(), payment});
        }

        txtDetails.setText(rList.isEmpty() ? "You have no reservations yet." : "Select a reservation from the table.");
    }

    // the booking details view: everything about the selected reservation
    private void showDetails() {

        int row = tblHistory.getSelectedRow();
        if (row < 0) {
            return;
        }

        Long reservationId = Long.valueOf(tblHistory.getModel().getValueAt(row, 0).toString());

        Reservation r = reservationDao.findById(reservationId);
        if (r == null) {
            return;
        }

        Room room = roomDao.findById(r.getRoomId());
        Payment payment = paymentDao.findByReservationId(reservationId);
        long nights = ChronoUnit.DAYS.between(r.getCheckIn(), r.getCheckOut());
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%-18s: %s%n", "Reservation No", r.getId()));
        sb.append(String.format("%-18s: %s (%s)%n", "Guest", user.getFullName(), user.getUserName()));
        sb.append(String.format("%-18s: %s (%s), %s per night%n", "Room", room.getRoomNumber(), room.getCategory(), room.getPricePerNight()));
        sb.append(String.format("%-18s: %s%n", "Check In", r.getCheckIn()));
        sb.append(String.format("%-18s: %s (%d nights)%n", "Check Out", r.getCheckOut(), nights));
        sb.append(String.format("%-18s: %s%n", "Total Price", r.getTotalPrice()));
        sb.append(String.format("%-18s: %s%n", "Status", r.getStatus()));
        sb.append(String.format("%-18s: %s%n", "Booked On", r.getCreatedAt().format(format)));
        sb.append(String.format("%n"));

        if (payment == null) {
            sb.append(String.format("%-18s: %s%n", "Payment", "Not Paid"));
        } else {
            sb.append(String.format("%-18s: %s by %s%n", "Payment", payment.getAmount(), payment.getMethod()));
            sb.append(String.format("%-18s: %s%n", "Reference", payment.getReference()));
            sb.append(String.format("%-18s: %s%n", "Paid On", payment.getPaidAt().format(format)));
        }

        txtDetails.setText(sb.toString());
        txtDetails.setCaretPosition(0);
    }
}
