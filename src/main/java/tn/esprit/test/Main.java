/*package tn.esprit.test;

import tn.esprit.models.Formation;
import tn.esprit.models.Certificat;
import tn.esprit.interfaces.FormationService;
import tn.esprit.services.ServiceCertificat;
import tn.esprit.services.ServiceGiveaway;
import tn.esprit.models.Giveaway;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        FormationService sf = new FormationService();


        Formation f1 = new Formation("C avancée", "Formation pour les développeurs Java", "debutant", LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 15), 25, 500.0, 1);
        sf.add(f1);  // Ajout de la formation

        System.out.println("Formations dans la base de données : ");
        System.out.println(sf.getAll());



       Formation f2 = new Formation("Java Débutant", "Formation pour les débutants Java", "Débutant", LocalDate.of(2025, 4, 1), LocalDate.of(2025, 4, 15), 30, 300.0, 1);
        f2.setIdf(2);
        sf.update(f2);


        System.out.println("Formations après mise à jour : ");
        System.out.println(sf.getAll());


        int idToDelete = 4;
        Formation f3 = new Formation();
        f3.setIdf(idToDelete);
        sf.delete(f3);

    }}


     /*   ServiceCertificat sc = new ServiceCertificat();


        Certificat cert1 = new Certificat( 1, 1, "5 ans", LocalDate.of(2025, 3, 15), "Certificat de Valo");
        sc.add(cert1);


        System.out.println("Certificats dans la base de données : ");
        System.out.println(sc.getAll());


        Certificat cert2 = new Certificat( 5, 1, "Non valide", LocalDate.of(2025, 4, 15), "Certificat Java corrigé");
        cert2.setIdc(1);
        sc.update(cert2);


        System.out.println("Certificats après mise à jour : ");
        System.out.println(sc.getAll());


        int idToDelete = 1;
        Certificat cert3 = new Certificat();
        cert3.setIdc(idToDelete);
        sc.delete(cert3);*/



       /* ServiceGiveaway sg = new ServiceGiveaway();

        // Ajout d'un nouveau giveaway
        Giveaway g1 = new Giveaway(1, "PC Gamer", "Gagnez un PC Gamer !", LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 15), "Actif");
        sg.add(g1);

        // Affichage de tous les giveaways
        System.out.println("Giveaways dans la base de données : ");
        System.out.println(sg.getAll());

        // Mise à jour d'un giveaway
        Giveaway g2 = new Giveaway(1, "Clavier Gaming", "Clavier mécanique RGB", LocalDate.of(2025, 4, 1), LocalDate.of(2025, 4, 20), "Terminé");
        g2.setIdg(1); // ID supposé existant en base
        sg.update(g2);

        // Affichage après mise à jour
        System.out.println("Giveaways après mise à jour : ");
        System.out.println(sg.getAll());

        // Suppression d'un giveaway
        int idToDelete = 1;
        Giveaway g3 = new Giveaway();
        g3.setIdg(idToDelete);
        sg.delete(g3);
     }
    }*/








