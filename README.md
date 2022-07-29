# Calling

A website I'm working on that will allow you to video call 
someone online.

## How

#### Dependencies: webcam-capture:0.3.2 ; slf4j-jdk14:1.7.36 ; postgresql:42.4.0

1. In terminal, type ```git clone https://github.com/elijaheaton/Calling.git ```
2. Set up a postgres user (with the password 'anunguessablepassword' when prompted):
   ```
   sudo -i -u postgres
   createuser caller -P 
   createdb calling
   psql
   GRANT ALL PRIVILEGES ON DATABASE calling TO caller;
   \q 
   exit
   ```
3. Open in IntelliJ and add dependencies
4. Run Server.java

## Why

I want to delve into server work and revisit my knowledge of 
Java as I interview for my first Software Development job.

