# UserDao
Object-oriented access layer to the MySQL database.
Project releated to the module 2 DAO workshop in CodersLab Java Developer training program (WAR_JEE_W_18 group).

# Features
According to the project requirements, `UserDao` class allows:
* create, update and delete a user record in DB based on User object or user id
* read a single user record or all records from DB
* hash user passwords 

# Dependencies
**User class**:  
`User` class has attributes: `id`, `username`, `email`, and `password`.
Constructor sets `username`, `email`, and `password` attributes. `id` is sets on default value 0 which be replaced by id retrieved from DB.
The class has default setters and getters for all attributes.

**User table**:  
`users` should be existing table in mysql db schema.
`users` table has columns: `id`, `username`, `email`, and `password`.
`id` is the primary key and `email` has unique attribute set on email column in users table.

**DbUtil**:  
`UserDao` uses one of method `DbUtil` class to get connection to DB.
`DbUtil` class is a own utility class which includes methods facilitating work with the database.

**Passwords**:  
Users passwords are kept hashed in DB. [jBCrypt](https://www.mindrot.org/projects/jBCrypt/) package was used (Blowfish password hashing code implementation).

# Usage
`UserDao` is an utility class and connector between `User` class objects and the users table (existing in a mysql db schema).
Therefore, before using it, UserDao object should be created:
`UserDao userDao = new UserDao();`

**methods description**:  
* `userDao.create(user1)` - create a single user record in DB based on User user1 object; the method also returns User object with new user id created during create new users table record
* `userDao.update(user1)` - update a single user record in DB based on changed user1 object
* `userDao.delete(id)` - delete a signle user record in DB based on given user id
* `userDao.read(id)`- read a user record from DB based on record id and return User object
* `userDao.read(email)`- read a user record from DB based on  user email and return User object
* `userDao.findAll()` - create a User objects array based on all user records in users table
