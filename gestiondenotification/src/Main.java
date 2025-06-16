import dao.EmployeeDAO;
import dao.EmployeeDAOImpl;
import dao.NotificationDAO;
import dao.NotificationDAOImpl;
import model.Employee;
import model.Notification;
import service.ConsoleNotification;
import service.EmailNotification;
import service.NotificationSystem;
import util.BdConnection;

import java.sql.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = BdConnection.getConnection()) {
            Scanner sc = new Scanner(System.in);
            EmployeeDAO empDAO = new EmployeeDAOImpl(conn);
            NotificationDAO notifDAO = new NotificationDAOImpl(conn);
            NotificationSystem notifier = new NotificationSystem(conn);

            notifier.ajouterService(new ConsoleNotification());
            notifier.ajouterService(new EmailNotification());

            while (true) {
                System.out.println("\n--- MENU ---");
                System.out.println("1. Ajouter un employé");
                System.out.println("2. Lister tous les employés");
                System.out.println("3. Désabonner un employé (par email)");
                System.out.println("4. Envoyer une notification");
                System.out.println("5. Lister les employés abonnés");
                System.out.println("6. Lister les notifications d'un employé (par email)");
                System.out.println("7. Lister toutes les notifications");
                System.out.println("8. Quitter");

                System.out.print("Choix : ");
                try {
                    int choix = sc.nextInt();
                    sc.nextLine(); // Consomme la nouvelle ligne

                    switch (choix) {
                        case 1:
                            System.out.print("Nom : ");
                            String nom = sc.nextLine();
                            System.out.print("Email : ");
                            String email = sc.nextLine();
                            System.out.print("Abonné aux notifications (true/false) : ");
                            String abonneInput = sc.nextLine();
                            boolean abonne;
                            try {
                                abonne = Boolean.parseBoolean(abonneInput);
                            } catch (Exception e) {
                                System.out.println("Veuillez entrer 'true' ou 'false'.");
                                break;
                            }
                            if (nom == null || nom.trim().isEmpty() || email == null || email.trim().isEmpty()) {
                                System.out.println("Le nom et l'email ne peuvent pas être vides.");
                                break;
                            }
                            empDAO.ajouter(new Employee(0, nom, email, abonne));
                            break;

                        case 2:
                            List<Employee> employes = empDAO.lister();
                            if (employes.isEmpty()) {
                                System.out.println("Aucun employé trouvé.");
                            } else {
                                employes.forEach(e -> System.out.println(e.getId() + " - " + e.getNom() + " - " + e.getEmail() + " - Abonné: " + e.isEstAbonne()));
                            }
                            break;

                        case 3:
                            System.out.print("Email de l'employé à désabonner : ");
                            String emailToUnsubscribe = sc.nextLine();
                            if (emailToUnsubscribe == null || emailToUnsubscribe.trim().isEmpty()) {
                                System.out.println("L'email ne peut pas être vide.");
                                break;
                            }
                            empDAO.desabonnerParEmail(emailToUnsubscribe);
                            break;

                        case 4:
                            System.out.print("Email de l'expéditeur : ");
                            String expEmail = sc.nextLine();
                            if (expEmail == null || expEmail.trim().isEmpty()) {
                                System.out.println("L'email de l'expéditeur ne peut pas être vide.");
                                break;
                            }
                            System.out.print("Message : ");
                            String msg = sc.nextLine();
                            if (msg == null || msg.trim().isEmpty()) {
                                System.out.println("Le message ne peut pas être vide.");
                                break;
                            }
                            notifier.envoyerNotifications(expEmail, msg);
                            break;

                        case 5:
                            List<Employee> abonnes = empDAO.listerAbonnes();
                            if (abonnes.isEmpty()) {
                                System.out.println("Aucun employé abonné trouvé.");
                            } else {
                                System.out.println("Liste des employés abonnés :");
                                abonnes.forEach(e -> System.out.println(e.getId() + " - " + e.getNom() + " - " + e.getEmail() + " - Abonné: " + e.isEstAbonne()));
                            }
                            break;

                        case 6:
                            System.out.print("Email de l'employé : ");
                            String empEmail = sc.nextLine();
                            if (empEmail == null || empEmail.trim().isEmpty()) {
                                System.out.println("L'email ne peut pas être vide.");
                                break;
                            }
                            Employee emp = empDAO.getParEmail(empEmail);
                            if (emp == null) {
                                System.out.println("Employé introuvable avec l'email : " + empEmail);
                                break;
                            }
                            List<Notification> notifications = notifDAO.listerNotificationsParEmploye(emp.getId());
                            if (notifications.isEmpty()) {
                                System.out.println("Aucune notification trouvée pour " + emp.getNom() + ".");
                            } else {
                                System.out.println("Notifications pour " + emp.getNom() + " :");
                                notifications.forEach(n -> System.out.println("ID: " + n.getId() + ", De: " + n.getExpediteurNom() + " (" + n.getExpediteurEmail() + "), Message: " + n.getMessage() + ", Date: " + n.getDateEnvoi()));
                            }
                            break;

                        case 7:
                            List<Notification> allNotifications = notifDAO.listerNotifications();
                            if (allNotifications.isEmpty()) {
                                System.out.println("Aucune notification trouvée.");
                            } else {
                                System.out.println("Liste des notifications :");
                                allNotifications.forEach(n -> System.out.println("ID: " + n.getId() + ", De: " + n.getExpediteurNom() + " (" + n.getExpediteurEmail() + "), Message: " + n.getMessage() + ", Date: " + n.getDateEnvoi()));
                            }
                            break;

                        case 8:
                            System.out.println("Au revoir !");
                            sc.close();
                            return;

                        default:
                            System.out.println("Choix invalide.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Veuillez entrer un numéro valide.");
                    sc.nextLine();
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
            e.printStackTrace();
        }
    }
}