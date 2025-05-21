# Setting Up the Interest List Table

This document outlines the steps to create SQL migration files for the `interest_list` table using Flyway.

## Steps

1. **Create a new migration file**

   Create a new SQL file in the `src/main/resources/db/migration` directory with a name following Flyway's naming convention:
   
   ```
   V<version>__<description>.sql
   ```
   
   For example: `V1__create_interest_list_table.sql`

2. **Define the table structure**

   In the migration file, add SQL to create the `interest_list` table with appropriate fields:
   
   ```sql
   CREATE TABLE interest_list (
       id SERIAL PRIMARY KEY,
       name VARCHAR(100) NOT NULL,
       email VARCHAR(100) NOT NULL,
       phone VARCHAR(20),
       interest_area VARCHAR(50),
       comments TEXT,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
   ```

   Adjust the fields as needed based on your interest list form requirements.

3. **Run the migration**

   The migration will be automatically applied when the application starts, thanks to the Flyway configuration in `application.properties`.

   To run it manually, use the Maven command:
   ```
   ./mvnw flyway:migrate
   ```

4. **Verify the table creation**

   Connect to your PostgreSQL database and verify the table was created:
   ```sql
   \dt interest_list
   SELECT * FROM interest_list;
   ```

## Additional Considerations

- Consider adding indexes for fields that will be frequently searched
- Add constraints as needed (e.g., unique email addresses)
- Consider adding audit fields (created_by, updated_at, etc.)
