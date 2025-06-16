package service;

import dao.EmployeeDAO;
import dao.EmployeeDAOImpl;
import model.Employee;
import java.sql.*;
import java.util.*;

public class NotificationSystem {
    private List<NotificationService> services = new ArrayList<>();
    private Connection conn;
    private EmployeeDAO empDAO;

    public NotificationSystem(Connection conn) {
        this.conn = conn;
        this.empDAO = new EmployeeDAOImpl(conn);
    }

    public void ajouterService(NotificationService s) {
        services.add(s);
    }

    public void envoyerNotifications(String emailExpediteur, String message) {
        Employee expediteur = empDAO.getParEmail(emailExpediteur);
        if (expediteur == null) {
            System.out.println("Expéditeur introuvable avec l'email : " + emailExpediteur);
            return;
        }

        // Récupérer tous les employés abonnés sauf l'expéditeur
        List<Employee> abonnes = empDAO.getEmployesAbonnesExcluant(expediteur.getId());

        if (abonnes.isEmpty()) {
            System.out.println("Aucun employé abonné trouvé.");
            return;
        }

        // Enregistrer la notification et obtenir son ID
        int notificationId = enregistrerNotification(expediteur.getId(), message);
        if (notificationId == -1) {
            System.out.println("Échec de l'enregistrement de la notification.");
            return;
        }

        // Envoyer la notification à tous les abonnés et enregistrer les destinataires
        for (Employee destinataire : abonnes) {
            for (NotificationService service : services) {
                service.notifier(destinataire, message);
            }
            enregistrerDestinataire(notificationId, destinataire.getId());
        }
    }

    private int enregistrerNotification(int expediteurId, String message) {
        String sql = "INSERT INTO Notification (message, expediteur_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, message);
            stmt.setInt(2, expediteurId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur SQL lors de l'enregistrement de la notification : " + ex.getMessage());
            ex.printStackTrace();
        }
        return -1;
    }

    private void enregistrerDestinataire(int notificationId, int employeeId) {
        String sql = "INSERT INTO NotificationRecipient (notification_id, employee_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notificationId);
            stmt.setInt(2, employeeId);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Erreur SQL lors de l'enregistrement du destinataire : " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}