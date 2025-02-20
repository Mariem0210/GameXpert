package tn.esprit.interfaces;
import java.util.List;

public interface IService<T> {
    void add(T t);
    public List<T> afficher();
    void update(T t);
    void delete(T t);

    T getUser(int currentUserId);
}
