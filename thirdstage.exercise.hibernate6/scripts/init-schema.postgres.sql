/* 
  * Preconditions
      * Users of 'chainz-admin' and 'st-api-user' exist
      * Catalog 'chainz' exists
      * User 'chainz-admin' is owner of catalog 'chainz'
      * Logined as 'chainz-admin' on catalog 'chainz'
*/

SET ROLE "chainz-admin";

/* https://www.postgresql.org/docs/current/sql-createschema.html */
CREATE SCHEMA IF NOT EXISTS "st-api"
  AUTHORIZATION "chainz-admin";

/* By chainz-admin */
GRANT CONNECT 
  ON DATABASE "chainz" 
  TO "st-api-user";

GRANT USAGE 
  ON SCHEMA "st-api" 
  TO "st-api-user";

/* https://www.postgresql.org/docs/16/sql-alterdefaultprivileges.html */
ALTER DEFAULT PRIVILEGES IN SCHEMA "st-api" 
  GRANT SELECT, INSERT, UPDATE, DELETE 
    ON TABLES TO "st-api-user";

/* Both 'alter default privileges' and 'grant' will be reflectec */
SELECT *
  FROM information_schema.role_table_grants
  WHERE grantee = 'st-api-user';


