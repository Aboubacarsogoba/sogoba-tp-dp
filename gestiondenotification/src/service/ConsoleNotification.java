package service;

import model.Employee;

public class ConsoleNotification implements NotificationService {
    @Override
    public void notifier(Employee destinataire, String message) {
        System.out.println("Console ➜ " + destinataire.getNom() + ": " + message);
    }
}