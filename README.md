# UserDAO
Object-oriented access layer to the MySQL database


## Running infrastructure (sftp servers)
```
./start-infra
```
When container is up and running migrate schema:
```
./db-migrate
```

DB properties:

#### jdbc-url:```jdbc:mysql://localhost:3307/workshop2?useSSL=false&characterEncoding=UTF-8```
#### schema: workshop2
#### user: root
#### db-pass: coderslab

