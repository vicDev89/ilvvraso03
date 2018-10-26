package de.berlin.htw.usws.mock_logic_ll;

/**
 * Mock-logic concerning the database.
 *
 * @since 25.10.2018
 * @author Lucas Larisch
 */
public class MockDbLogic {

    /**
     * MOCK!
     * TODO: Check if ID is stored yet by calling the DB.
     *
     * Checks whether a specific ID is already stored in the database
     * The database has to be used since the last ID could have been deleted.
     *
     * @since 24.10.2018
     * @author Lucas Larisch
     * @param idNumber Recipe ID to be checked.
     * @return true if the ID is stored yet, false if not.
     */
    public static boolean dataBaseContainsId(String idNumber) {
        // Always returning false leads to parsing all recipe IDs/Recipes!

        // Existing ID.
        return idNumber.equals("3586111538944438");
        // return false;
    }
}
