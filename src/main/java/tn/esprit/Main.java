package tn.esprit;

import tn.esprit.models.Match;
import tn.esprit.models.Tournoi;
import tn.esprit.services.ServiceMatch;
import tn.esprit.services.ServiceTournoi;
import tn.esprit.utils.MyDatabase;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        //ServiceTournoi st = new ServiceTournoi();
        ServiceMatch sm = new ServiceMatch();
       // Tournoi t = new Tournoi(24, "winner", "Tournoi annuel", LocalDate.of(2025, 3, 10), LocalDate.of(2025, 3, 20), 16, 5000.0f, "En cours");
        Match m=new Match(4,10,"amineamineamine","ahmedahmedahmed", LocalDate.of(2025, 3, 10),"ahaha","3:0");
        //   st.add(new Tournoi(1, "Championnat", "Tournoi annuel", LocalDate.of(2025, 3, 10), LocalDate.of(2025, 3, 20), 16, 5000.0f, "En cours"));
        //st.update(new Tournoi(6, "Championnat1", "cv cv", LocalDate.of(2025, 2, 10), LocalDate.of(2025, 4, 20), 16, 5000.0f, "En cours"));
       // st.add(t);
        //st.update(t);
        //st.delete(t);
       // sm.add(m);
        //sm.update(m);
        sm.delete(m);
       // System.out.println(st.getAll());
        System.out.println(sm.getAll());
    }
}
