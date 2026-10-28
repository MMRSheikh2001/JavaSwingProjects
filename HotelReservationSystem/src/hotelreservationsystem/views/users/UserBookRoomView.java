/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystem.views.users;

import hotelreservationsystem.db.ReservationDao;
import hotelreservationsystem.db.RoomDao;
import hotelreservationsystem.models.Reservation;
import hotelreservationsystem.models.ReservationStatus;
import hotelreservationsystem.models.Room;
import hotelreservationsystem.models.RoomCategory;
import hotelreservationsystem.models.User;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrator
 */
public class UserBookRoomView extends javax.swing.JFrame {

    private User user;

    RoomDao roomDao = new RoomDao();
    ReservationDao reservationDao = new ReservationDao();

    JSpinner spnCheckIn = new JSpinner(new SpinnerDateModel());
    JSpinner spnCheckOut = new JSpinner(new SpinnerDateModel());
    JComboBox<String> cmbCategory = new JComboBox<>();
    JButton btnSearch = new JButton("Search Rooms");
    JButton btnBook = new JButton("Book Selected Room");
    JTable tblAvailableRooms = new JTable();
    JLabel lblSummary = new JLabel(" ");

    public UserBookRoomView(User user) {
        this.user = user;

        buildScreen();
        loadComboBox();
        searchRooms();
    }

    private void buildScreen() {

        setTitle("Book a Room");
        setSize(900, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("Choose your dates, search, then select a room from the table and book it", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));
        title.setBorder(new EmptyBorder(10, 0, 0, 0));

        spnCheckIn.setEditor(new JSpinner.DateEditor(spnCheckIn, "yyyy-MM-dd"));
        spnCheckOut.setEditor(new JSpinner.DateEditor(spnCheckOut, "yyyy-MM-dd"));
        spnCheckIn.setValue(toDate(LocalDate.now()));
        spnCheckOut.setValue(toDate(LocalDate.now().plusDays(1)));

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        filters.add(new JLabel("Check In:"));
        filters.add(spnCheckIn);
        filters.add(new JLabel("Check Out:"));
        filters.add(spnCheckOut);
        filters.add(new JLabel("Category:"));
        filters.add(cmbCategory);
        filters.add(btnSearch);

        JPanel north = new JPanel(new BorderLayout());
        north.add(title, BorderLayout.NORTH);
        north.add(filters, BorderLayout.CENTER);

        tblAvailableRooms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        lblSummary.setBorder(new EmptyBorder(0, 10, 0, 0));

        JPanel south = new JPanel(new BorderLayout());
        south.setBorder(new EmptyBorder(6, 6, 10, 10));
        south.add(lblSummary, BorderLayout.CENTER);
        south.add(btnBook, BorderLayout.EAST);

        getContentPane().setLayout(new BorderLayout(6, 6));
        getContentPane().add(north, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(tblAvailableRooms), BorderLayout.CENTER);
        getContentPane().add(south, BorderLayout.SOUTH);

        btnSearch.addActionListener(e -> searchRooms());
        btnBook.addActionListener(e -> bookRoom());
    }

    private void loadComboBox() {

        cmbCategory.removeAllItems();
        cmbCategory.addItem("All");
        for (RoomCategory category : RoomCategory.values()) {
            cmbCategory.addItem(category.name());
        }
    }

    private Date toDate(LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private LocalDate toLocalDate(Object value) {
        return ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // reads the two dates and checks them, returns null (after a message) if they are not valid
    private LocalDate[] readDates() {

        LocalDate checkIn = toLocalDate(spnCheckIn.getValue());
        LocalDate checkOut = toLocalDate(spnCheckOut.getValue());

        if (checkIn.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(this, "Check In date cannot be in the past");
            return null;
        }

        if (!checkOut.isAfter(checkIn)) {
            JOptionPane.showMessageDialog(this, "Check Out must be after Check In");
            return null;
        }

        return new LocalDate[]{checkIn, checkOut};
    }

    // a room is free if no BOOKED reservation of it overlaps the dates (a stay may start on the day another ends)
    private boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut, List<Reservation> reservations) {

        for (Reservation r : reservations) {
            if (r.getRoomId().equals(roomId)
                    && r.getStatus() == ReservationStatus.BOOKED
                    && r.getCheckIn().isBefore(checkOut)
                    && r.getCheckOut().isAfter(checkIn)) {
                return false;
            }
        }
        return true;
    }

    private void searchRooms() {

        String[] list = {"SL", "Room Number", "Category", "Price Per Night", "Nights", "Total Price"};
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setColumnIdentifiers(list);
        tblAvailableRooms.setModel(model);

        LocalDate[] dates = readDates();
        if (dates == null) {
            lblSummary.setText(" ");
            return;
        }

        long nights = ChronoUnit.DAYS.between(dates[0], dates[1]);
        String category = cmbCategory.getSelectedItem() == null ? "All" : cmbCategory.getSelectedItem().toString();

        List<Reservation> reservations = reservationDao.findAll();

        for (Room room : roomDao.findAll()) {

            if (!room.getActive()) {
                continue;
            }
            if (!category.equals("All") && !room.getCategory().name().equals(category)) {
                continue;
            }
            if (!isRoomAvailable(room.getId(), dates[0], dates[1], reservations)) {
                continue;
            }

            double total = Math.round(nights * room.getPricePerNight() * 100) / 100.0;
            model.addRow(new Object[]{room.getId(), room.getRoomNumber(), room.getCategory(), room.getPricePerNight(), nights, total});
        }

        lblSummary.setText(model.getRowCount() + " room(s) available for " + nights + " night(s)");
    }

    private void bookRoom() {

        int row = tblAvailableRooms.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a room from the table first");
            return;
        }

        LocalDate[] dates = readDates();
        if (dates == null) {
            return;
        }

        Long roomId = Long.valueOf(tblAvailableRooms.getModel().getValueAt(row, 0).toString());

        // read from the database again, someone may have booked it since the search
        Room room = roomDao.findById(roomId);

        if (room == null || !room.getActive()) {
            JOptionPane.showMessageDialog(this, "This room is not available any more");
            searchRooms();
            return;
        }

        if (!isRoomAvailable(roomId, dates[0], dates[1], reservationDao.findAll())) {
            JOptionPane.showMessageDialog(this, "This room was just booked for these dates. Please search again");
            searchRooms();
            return;
        }

        long nights = ChronoUnit.DAYS.between(dates[0], dates[1]);
        double total = Math.round(nights * room.getPricePerNight() * 100) / 100.0;

        int choice = JOptionPane.showConfirmDialog(this,
                "Book room " + room.getRoomNumber() + " (" + room.getCategory() + ")\n"
                + "Check In : " + dates[0] + "\n"
                + "Check Out: " + dates[1] + "  (" + nights + " nights)\n"
                + "Total    : " + total,
                "Confirm Booking", JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        Reservation reservation = new Reservation();

        reservation.setUserId(user.getId());
        reservation.setRoomId(roomId);
        reservation.setCheckIn(dates[0]);
        reservation.setCheckOut(dates[1]);
        reservation.setTotalPrice(total);
        reservation.setStatus(ReservationStatus.BOOKED);
        reservation.setCreatedAt(LocalDateTime.now());

        reservationDao.save(reservation);

        JOptionPane.showMessageDialog(this, "Room booked. Reservation number: " + reservation.getId()
                + "\nTotal: " + total + "\nOpen Make Payment from the dashboard to pay for it");

        searchRooms();
    }
}
