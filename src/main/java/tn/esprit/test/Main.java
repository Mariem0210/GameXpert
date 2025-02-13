package tn.esprit;

import tn.esprit.models.Offre_de_recrutement;

import tn.esprit.models.Transfert;
import tn.esprit.services.ServiceOffre_de_recrutement;

import tn.esprit.services.ServiceTransfert;
import tn.esprit.utils.MyDatabase;

import java.time.LocalDate;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        //ServiceTransfert st = new ServiceTransfert();
        ServiceOffre_de_recrutement so = new ServiceOffre_de_recrutement();
        //Transfert t = new Transfert(7,0, "nouvllllll", "aaa", 5555, new Date() );
       Offre_de_recrutement o=new Offre_de_recrutement(17,15,"ami","ahmed", 30, "aaaa", "aaaaaa");
        //st.add(t);
        //st.update(t);
        //st.delete(t);
        //so.add(o);
        //so.update(o);
        so.delete(o);
        //System.out.println(st.getAll());
        System.out.println(so.getAll());
    }
}