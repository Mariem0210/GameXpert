

package tn.esprit.interfaces;


import tn.esprit.models.Formation;

import java.sql.SQLException;
import java.util.List;

public interface IServiceFormation<Formation> {
    void add(Formation f) throws SQLException;
    void modifierFormation(Formation f, int idf) throws SQLException;
    void supprimerFormation(tn.esprit.models.Formation formation, String nomFormation) throws SQLException;
    List<Formation> recupererFormations() throws SQLException;
    //void getFormationById(int id) throws SQLException;
}

;