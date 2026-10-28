/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hotelreservationsystem;

import hotelreservationsystem.db.DataBase;
import hotelreservationsystem.db.PaymentDao;
import hotelreservationsystem.db.ReservationDao;
import hotelreservationsystem.db.RoomDao;
import hotelreservationsystem.db.UserDao;
import hotelreservationsystem.models.User;
import hotelreservationsystem.views.LoginView;

/**
 *
 * @author Administrator
 */
public class HotelReservationSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
//        DataBase db = new DataBase();
//        db.getCon();
//
//        PaymentDao pd = new PaymentDao();
//        pd.createTable();
//        ReservationDao rd = new ReservationDao();
//        rd.createTable();
//
//        RoomDao roomDao = new RoomDao();
//        roomDao.createTable();
//
        UserDao ud = new UserDao();
//        ud.createTable();
//
//        User admin = new User();
//        admin.setFullName("Admin");
//        admin.setUserName("admin");
//        admin.setPassword("1234");
//        admin.setIsAdmin(Boolean.TRUE);
//        ud.save(admin);

//        User adminFound=ud.findById(1L);
//        System.out.println("Name  :"+ adminFound.getFullName() +" Password : "+adminFound.getPassword());
        LoginView lv = new LoginView();
        lv.setVisible(true);
        
    }
    
}
