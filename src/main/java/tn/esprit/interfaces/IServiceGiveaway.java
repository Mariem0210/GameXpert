package tn.esprit.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface IServiceGiveaway<Giveaway> {
    void add(Giveaway g) throws SQLException;
    void modifierGiveaway(Giveaway giveaway);
    void supprimerGiveaway(Giveaway giveaway , String TitreGiveaway) throws SQLException ;
    List<Giveaway> recupereGiveaways() throws SQLException;

}
