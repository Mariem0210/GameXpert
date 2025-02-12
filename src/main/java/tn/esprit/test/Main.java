package tn.esprit.test;

import tn.esprit.models.Formation;
import tn.esprit.models.Certificat;
import tn.esprit.services.ServiceFormation;
import tn.esprit.services.ServiceCertificat;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
    /*    // Création de l'objet ServiceFormation
        ServiceFormation sf = new ServiceFormation();

        // Ajouter une nouvelle formation pour tester l'ajout
        Formation f1 = new Formation("Java Avancé", "Formation pour les développeurs Java", "Avancé", LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 15), 25, 500.0, 1);
        sf.add(f1);  // Ajout de la formation

        // Tester l'affichage de toutes les formations
        System.out.println("Formations dans la base de données : ");
        System.out.println(sf.getAll());

        // Mise à jour d'une formation existante
        // (Assurez-vous d'utiliser un ID existant pour effectuer l'update)
        Formation f2 = new Formation("Java Débutant", "Formation pour les débutants Java", "Débutant", LocalDate.of(2025, 4, 1), LocalDate.of(2025, 4, 15), 30, 300.0, 1);
        f2.setIdf(9);  // Supposons que l'ID de la formation que tu veux mettre à jour est 1
        sf.update(f2);  // Mise à jour de la formation

        // Affichage après la mise à jour
        System.out.println("Formations après mise à jour : ");
        System.out.println(sf.getAll());

        // Suppression d'une formation existante
        int idToDelete = 11;  // ID de la formation à supprimer
        Formation f3 = new Formation();
        f3.setIdf(idToDelete);  // Assurez-vous que cette formation existe déjà

    }*/

        // Création de l'objet ServiceCertificat
        ServiceCertificat sc = new ServiceCertificat();

// Ajouter un nouveau certificat pour tester l'ajout
        Certificat cert1 = new Certificat( 1, 1, "Valide", LocalDate.of(2025, 3, 15), "Certificat de réussite Java");
        sc.add(cert1);  // Ajout du certificat

// Tester l'affichage de tous les certificats
        System.out.println("Certificats dans la base de données : ");
        System.out.println(sc.getAll());

// Mise à jour d'un certificat existant
// (Assurez-vous d'utiliser un ID existant pour effectuer l'update)
        Certificat cert2 = new Certificat( 1, 1, "Non valide", LocalDate.of(2025, 4, 15), "Certificat Java corrigé");
        cert2.setIdc(2);  // Supposons que l'ID de la certification que tu veux mettre à jour est 9
        sc.update(cert2);  // Mise à jour du certificat

// Affichage après la mise à jour
        System.out.println("Certificats après mise à jour : ");
        System.out.println(sc.getAll());

// Suppression d'un certificat existant
        int idToDelete = 4;  // ID du certificat à supprimer
        Certificat cert3 = new Certificat();
        cert3.setIdc(idToDelete);  // Assurez-vous que ce certificat existe déjà
        sc.delete(cert3);  // Suppression du certificat









    }
    }








