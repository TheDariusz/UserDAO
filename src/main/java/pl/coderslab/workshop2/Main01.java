package pl.coderslab.workshop2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.coderslab.workshop2.user.User;
import pl.coderslab.workshop2.user.UserDao;

public class Main01 {

  private static final Logger logger = LoggerFactory.getLogger(Main01.class);

  public static void main(String[] args) {
    logger.info("Starting main application");

    UserDao userDAO = new UserDao();

    User malarz = new User("malarz", "themalarz@wp.pl", "alamakota");
    User littleJohn = new User("littlejohn2", "little@yahoo.com", "costamtam");
    userDAO.save(malarz);
    userDAO.save(littleJohn);

    logger.info("user {}", malarz);

    User malarz2 = userDAO.findOne(malarz.getId());
    logger.info("malarz found {}", malarz2);

    try {
      littleJohn.setEmail("themalarz@wp.pl");
      userDAO.update(littleJohn);
    } catch (Exception e) {
      logger.error("Update failed", e);
    }

    logger.info("Application finished");
  }
}
