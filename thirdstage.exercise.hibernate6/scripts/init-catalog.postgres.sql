/*
  * Preconditions
      * Logined as a superuser(usually `postgres`) into 'postgres' catalog
*/

/* https://www.postgresql.org/docs/16/sql-createuser.html */
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

/* https://www.postgresql.org/docs/16/sql-grant.html#SQL-GRANT-DESCRIPTION-ROLES */
GRANT "st-api-user" TO "chainz-admin" 
  WITH ADMIN FALSE, INHERIT FALSE, SET TRUE;
/*
GRANT "st-api-user" TO "chainz-admin" WITH INHERIT FALSE;
GRANT "st-api-user" TO "chainz-admin" WITH ADMIN OPTION;
GRANT "st-api-user" TO "chainz-admin" WITH SET OPTION; 
*/

/* https://www.postgresql.org/docs/16/sql-createdatabase.html */
CREATE DATABASE "chainz" WITH
  OWNER = 'chainz-admin'
  ENCODING = 'UTF8'
  TEMPLATE = 'template0'
  LOCALE = 'ko_KR.UTF-8'
  CONNECTION LIMIT = -1;

/* https://www.postgresql.org/docs/16/sql-alteruser.html */
ALTER USER "st-api-user" 
  IN DATABASE "chainz" 
  SET search_path TO "$user","st-api";

