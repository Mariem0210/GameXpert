

package tn.esprit.interfaces;


import tn.esprit.models.Formation;

import java.sql.SQLException;
import java.util.List;

public interface IServiceFormation<Formation> {
    void add(Formation f) throws SQLException;
    void modifier(Formation f, int idf) throws SQLException;
    void supprimerFormation( Formation formation , int idf) throws SQLException;
    List<Formation> recupererFormations() throws SQLException;
}

;