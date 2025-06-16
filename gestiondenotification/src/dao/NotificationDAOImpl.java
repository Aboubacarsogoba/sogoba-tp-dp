package dao;

import model.Notification;
import java.sql.*;
import java.util.*;

public class NotificationDAOImpl implements NotificationDAO {
    private Connection conn;

    public NotificationDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Notification> listerNotifications() {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT n.id, n.message, n.date_envoi, n.expediteur_id, e.nom AS expediteur_nom, e.email AS expediteur_email " +
                "FROM Notification n " +
                "JOIN Employee e ON n.expediteur_id = e.id";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Notification notification = new Notification();
                notification.setId(rs.getInt("id"));
                notification.setMessage(rs.getString("message"));
                notification.setDateEnvoi(rs.getTimestamp("date_envoi"));
                notification.setExpediteurId(rs.getInt("expediteur_id"));
                notification.setExpediteurNom(rs.getString("expediteur_nom"));
                notification.setExpediteurEmail(rs.getString("expediteur_email"));
                notifications.add(notification);
            }
            if (notifications.isEmpty()) {
                System.out.println("Aucune notification trouvée.");
            }
        } catch (SQLException ex) {
            System.err.println("Erreur SQL lors de la récupération des notifications : " + ex.getMessage());
            ex.printStackTrace();
        }
        return notifications;
    }

    @Override
    public List<Notification> listerNotificationsParEmploye(int employeeId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT n.id, n.message, n.date_envoi, n.expediteur_id, e.nom AS expediteur_nom, e.email AS expediteur_email " +
                "FROM Notification n " +
                "JOIN Employee e ON n.expediteur_id = e.id " +
                "JOIN NotificationRecipient nr ON n.id = nr.notification_id " +
                "WHERE nr.employee_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Notification notification = new Notification();
                    notification.setId(rs.getInt("id"));
                    notification.setMessage(rs.getString("message"));
                    notification.setDateEnvoi(rs.getTimestamp("date_envoi"));
                    notification.setExpediteurId(rs.getInt("expediteur_id"));
                    notification.setExpediteurNom(rs.getString("expediteur_nom"));
                    notification.setExpediteurEmail(rs.getString("expediteur_email"));
                    notifications.add(notification);
                }
                if (notifications.isEmpty()) {
                    System.out.println("Aucune notification trouvée pour cet employé.");
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur SQL lors de la récupération des notifications par employé : " + ex.getMessage());
            ex.printStackTrace();
        }
        return notifications;
    }
}