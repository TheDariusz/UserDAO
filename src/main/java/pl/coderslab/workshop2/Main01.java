package pl.coderslab.workshop2;

public class Main01 {
  public static void main(String[] args) {
    UserDao userDAO = new UserDao();
    User user2 = new User("wojtek", "wojtek@yahoo.com", "costamtam");
    userDAO.create(user2);
    System.out.println(user2.toString());
    user2.setEmail("wojtas@wp.com");
    userDAO.update(user2);
    userDAO.delete(19);


  }
}
