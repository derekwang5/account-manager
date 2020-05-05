# account-manager

## Dev Notes
* The project will be built by Maven.
* I used IntelliJ as my dev IDE.
* There is a schema.sql that will create the table when the project starts if table does not already exist.
* The DataLoader class will insert the 2 default accounts into the table when the project starts if they do not exist.

## Functional Assumptions
* I assume each account number can have multiple currencies, so I made (account number, currency) a unique key.
* I did not validate the format of the account number or currency code. We may need to do that in the real world.
* In the transfer response, I included the updated balance for both source and destination account. This may need to change in the real world.
* I did not add any index to the table. We will need to do this in the real world.
