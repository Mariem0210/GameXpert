package tn.esprit.services;

public class UserDataManager {
    private static UserDataManager instance;
    private int idu; // Remplacement de userId par udu

    private UserDataManager() {

    }

    public static UserDataManager getInstance() {
        if (instance == null) {
            instance = new UserDataManager();
        }
        return instance;
    }

    public int getIdu() { // Mise à jour du getter
        return idu;
    }

    public void setIdu(int idu) { // Mise à jour du setter
        this.idu = idu;
    }
    public void setUserId(int idu) {
        this.idu = idu;
    }
    public int getUserId() {
        return idu;
    }

    public void logout() {
        idu = 0; // Mise à jour dans la méthode logout
    }
}
