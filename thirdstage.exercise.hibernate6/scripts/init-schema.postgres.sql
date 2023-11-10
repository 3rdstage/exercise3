
/* https://www.postgresql.org/docs/16/sql-createuser.html */
CREATE ROLE "admin" WITH
  LOGIN
  SUPERUSER
  INHERIT
  CREATEDB
  CREATEROLE
  REPLICATION
  CONNECTION LIMIT 3
  PASSWORD 'postgres4u';

CREATE ROLE "chainz-admin" WITH
  LOGIN
  NOSUPERUSER
  NOCREATEDB
  CREATEROLE
  NOREPLICATION
  CONNECTION LIMIT 5
  PASSWORD 'chainz-admin-secret';

CREATE ROLE "st-api-user" WITH
  LOGIN
  NOSUPERUSER
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION
  CONNECTION LIMIT -1
  PASSWORD 'chainz-admin-secret';


/* https://www.postgresql.org/docs/16/sql-createdatabase.html */
CREATE DATABASE "chainz" WITH
  OWNER = "chainz-admin"
  ENCODING = 'UTF8'
  TEMPLATE = "template0"
  LOCALE = "ko_KR.UTF-8"
  CONNECTION LIMIT = -1;