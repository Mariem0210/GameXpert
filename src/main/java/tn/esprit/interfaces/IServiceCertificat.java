package tn.esprit.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface IServiceCertificat<Certificat> {
    void add(Certificat c) throws SQLException;
    void modifierCertificat(Certificat certificat);
    void supprimerCertificat(Certificat certificat , String TitreCertificat) throws SQLException ;
    List<Certificat> recupereCertificats() throws SQLException;
}
