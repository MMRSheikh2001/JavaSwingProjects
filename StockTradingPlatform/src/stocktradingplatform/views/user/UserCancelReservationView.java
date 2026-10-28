/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystem.views.users;

import hotelreservationsystem.db.PaymentDao;
import hotelreservationsystem.db.ReservationDao;
import hotelreservationsystem.db.RoomDao;
import hotelreservationsystem.models.Reservation;
import hotelreservationsystem.models.ReservationStatus;
import hotelreservationsystem.models.Room;
import hotelreservationsystem.models.User;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrator
 */
public class UserCancelReservationView extends javax.swing.JFrame {

    private User user;

    ReservationDao reservationDao = new ReservationDao();
    RoomDao roomDao = new RoomDao();
    PaymentDao paymentDao = new PaymentDao();

    JTable tblReservations = new JTable();
    JButton btnCancel = new JButton("Cancel Selected Reservation");
    JLabel lblSummary = new JLabel(" ");

    public UserCancelReservationView(User user) {
        this.user = user;

        buildScreen();
        showCancellableReservations();
    }

    private void buildScreen() {

        setTitle("Cancel Reservation");
        setSize(900, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("Select a reservation from the table and cancel it (only stays that have not started yet)", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));
        title.setBorder(new EmptyBorder(10, 0, 0, 0));

        tblReservations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        lblSummary.setBorder(new EmptyBorder(0, 10, 0, 0));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        buttons.add(btnCancel);

        JPanel south = new JPanel(new BorderLayout());
        south.setBorder(new EmptyBorder(0, 6, 8, 6));
        south.add(buttons, BorderLayout.CENTER);
        south.add(lblSummary, BorderLayout.SOUTH);

        getContentPane().setLayout(new BorderLayout(6, 6));
        getContentPane().add(title, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(tblReservations), BorderLayout.CENTER);
        getContentPane().add(south, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> cancelReservation());

        // when the user comes back from another window the list is reloaded
        addWindowFocusListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                showCancellableReservations();
            }
        });
    }

    // a reservation can be cancelled while it is BOOKED and the stay has not started (check in is today or later)
    private boolean canCancel(Reservation r) {
        return r.getStatus() == ReservationStatus.BOOKED && !r.getCheckIn().isBefore(LocalDate.now());
    }

    private void showCancellableReservations() {

        String[] list = {"SL", "Room Number", "Category", "Check In", "Check Out", "Total Price", "Payment"};
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setColumnIdentifiers(list);
        tblReservations.setModel(model);

        List<Reservation> rList = reservationDao.findByUserId(user.getId());

        for (Reservation r : rList) {

            if (!canCancel(r)) {
                continue;
            }

            Room room = roomDao.findById(r.getRoomId());
            String payment = paymentDao.findByReservationId(r.getId()) == null ? "Not Paid" : "Paid";

            model.addRow(new Object[]{r.getId(), room.getRoomNumber(), room.getCategory(), r.getCheckIn(), r.getCheckOut(), r.getTotalPrice(), payment});
        }

        lblSummary.setText(model.getRowCount() + " reservation(s) can be cancelled");
    }

    private void cancelReservation() {

        int row = tblReservations.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a reservation from the table first");
            return;
        }

        Long reservationId = Long.valueOf(tblReservations.getModel().getValueAt(row, 0).toString());

        // read from the database again before changing anything
        Reservation reservation = reservationDao.findById(reservationId);

        if (reservation == null || !reservation.getUserId().equals(user.getId()) || !canCancel(reservation)) {
            JOptionPane.showMessageDialog(this, "This reservation cannot be cancelled");
            showCancellableReservations();
            return;
        }

        boolean paid = paymentDao.findByReservationId(reservationId) != null;

        String message = "Cancel reservation " + reservationId + " (" + reservation.getCheckIn() + " to " + reservation.getCheckOut() + ")?";
        if (paid) {
            message += "\nThis reservation is paid. Payments are not refunded in this simulation.";
        }

        int choice = JOptionPane.showConfirmDialog(this, message, "Confirm Cancel", JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        // the row stays in the database with status CANCELLED, so the history is kept
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationDao.update(reservation);

        JOptionPane.showMessageDialog(this, "Reservation " + reservationId + " is cancelled");

        showCancellableReservations();
    }
}
