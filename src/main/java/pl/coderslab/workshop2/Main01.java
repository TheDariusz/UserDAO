package pl.coderslab.workshop2;

public class Main01 {
  public static void main(String[] args) {
    UserDAO userDAO = new UserDAO();
    User user1 = new User("malarz", "themalarz@wp.pl", "alamakota");
    User user2 = new User("littlejohn2", "little@yahoo.com", "costamtam");
    userDAO.create(user1);
    userDAO.create(user2);

    user2.setEmail("themalarz@wp.pl");
    userDAO.update(user2);

  }
}
