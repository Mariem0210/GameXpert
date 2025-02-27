package tn.esprit;

import tn.esprit.models.Offre_de_recrutement;

import tn.esprit.models.Transfert;
import tn.esprit.services.ServiceOffre_de_recrutement;
import tn.esprit.models.Equipe;

import tn.esprit.services.ServiceTransfert;
import tn.esprit.services.ServiceEquipe;
import tn.esprit.utils.MyDatabase;

import java.time.LocalDate;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        //ServiceTransfert st = new ServiceTransfert();
        ServiceOffre_de_recrutement so = new ServiceOffre_de_recrutement();
        ServiceEquipe se = new ServiceEquipe();

        //Transfert t = new Transfert(7,0, "nouvllllll", "aaa", 5555, new Date() );
       //Offre_de_recrutement o=new Offre_de_recrutement(17,15,"ami","ahmed", 30, "aaaa", "aaaaaa");
       Equipe e =new Equipe(3, "premiere equipe",new Date() , 5);
        //st.add(t);
        //st.update(t);
        //st.delete(t);
        //so.add(o);
        //so.update(o);
        //so.delete(o);
        //se.add(e);
        //se.update(e);
         se.delete(e);
        //System.out.println(st.getAll());
        //System.out.println(so.getAll());
        System.out.println(se.getAll());
    }
}