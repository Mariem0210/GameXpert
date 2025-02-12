package tn.esprit;

import tn.esprit.models.Utilisateur;
import tn.esprit.services.ServiceUtilisateur;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        ServiceUtilisateur su = new ServiceUtilisateur();

        // ➤ Création et ajout d'un utilisateur
        Utilisateur utilisateur = new Utilisateur(2, 98765432, "Karoui", "Yahya", "karoui.yahya@example.com", "password123", "Client", LocalDate.of(1995, 8, 20), LocalDate.now());
        su.add(utilisateur);
        System.out.println("Utilisateur ajouté avec succès !");

        // ➤ Mise à jour de l'utilisateur
        utilisateur.setNomu("Ben Karoui");  // Modification du nom
        utilisateur.setMailu("ben.karoui@example.com");  // Modification de l'email
        su.update(utilisateur);
        System.out.println("Utilisateur mis à jour avec succès !");

        // ➤ Affichage des utilisateurs après mise à jour
        System.out.println("Liste des utilisateurs après mise à jour :");
        System.out.println(su.getAll());

       // ➤ Suppression de l'utilisateur
        su.delete(utilisateur);
        System.out.println("Utilisateur supprimé avec succès !");

        // ➤ Affichage des utilisateurs après suppression
        System.out.println("Liste des utilisateurs après suppression :");
        System.out.println(su.getAll());
    }
}
