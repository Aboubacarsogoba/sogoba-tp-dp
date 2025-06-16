package dao;

import model.Notification;
import java.util.List;

public interface NotificationDAO {
    List<Notification> listerNotifications();
    List<Notification> listerNotificationsParEmploye(int employeeId); // Nouvelle méthode
}