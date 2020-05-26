package org.loocsij.javastudy;

/*
 Derby - WwdUtils.java - utilitity methods used by WwdEmbedded.java

 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.    

 */

import java.io.*;
import java.sql.*; /*
 Derby - WwdEmbedded.java

 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.    

 *//*											 
 **  This sample program is described in the Getting Started With Derby Manual
 *///## INITIALIZATION SECTION ##
//Include the java SQL classes 

public class TestDerby {
	public static void main(String[] args) {
		// ## DEFINE VARIABLES SECTION ##
		// define the driver to use
		String driver = "org.apache.derby.jdbc.EmbeddedDriver";
		// the database name
		String dbName = "jdbcDemoDB";
		// define the Derby connection URL to use
		String connectionURL = "jdbc:derby:" + dbName + ";create=true";

		Connection conn = null;
		Statement s;
		PreparedStatement psInsert;
		ResultSet myWishes;
		String printLine = "  __________________________________________________";
		String createString = "CREATE TABLE WISH_LIST  "
				+ "(WISH_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY "
				+ "   CONSTRAINT WISH_PK PRIMARY KEY, "
				+ " ENTRY_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
				+ " WISH_ITEM VARCHAR(32) NOT NULL) ";
		String answer;

		// Beginning of JDBC code sections
		// ## LOAD DRIVER SECTION ##
		try {
			/*
			 * * Load the Derby driver.* When the embedded Driver is used this
			 * action start the Derby engine.* Catch an error and suggest a
			 * CLASSPATH problem
			 */
			Class.forName(driver);
			System.out.println(driver + " loaded. ");
		} catch (java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
			System.out
					.println("\n    >>> Please check your CLASSPATH variable   <<<\n");
		}
		// Beginning of Primary DB access section
		// ## BOOT DATABASE SECTION ##
		try {
			// Create (if needed) and connect to the database
			conn = DriverManager.getConnection(connectionURL);
			System.out.println("Connected to database " + dbName);

			// ## INITIAL SQL SECTION ##
			// Create a statement to issue simple commands.
			s = conn.createStatement();
			// Call utility method to check if table exists.
			// Create the table if needed
			if (!WwdUtils.wwdChk4Table(conn)) {
				System.out.println(" . . . . creating table WISH_LIST");
				s.execute(createString);
			}
			// Prepare the insert statement to use
			psInsert = conn
					.prepareStatement("insert into WISH_LIST(WISH_ITEM) values (?)");

			// ## ADD / DISPLAY RECORD SECTION ##
			// The Add-Record Loop - continues until 'exit' is entered
			do {
				// Call utility method to ask user for input
				answer = WwdUtils.getWishItem();
				// Check if it is time to EXIT, if not insert the data
				if (!answer.equals("exit")) {
					// Insert the text entered into the WISH_ITEM table
					psInsert.setString(1, answer);
					psInsert.executeUpdate();

					// Select all records in the WISH_LIST table
					myWishes = s
							.executeQuery("select ENTRY_DATE, WISH_ITEM from WISH_LIST order by ENTRY_DATE");

					// Loop through the ResultSet and print the data
					System.out.println(printLine);
					while (myWishes.next()) {
						System.out.println("On " + myWishes.getTimestamp(1)
								+ " I wished for " + myWishes.getString(2));
					}
					System.out.println(printLine);
					// Close the resultSet
					myWishes.close();
				} // END of IF block
				// Check if it is time to EXIT, if so end the loop
			} while (!answer.equals("exit")); // End of do-while loop

			// Release the resources (clean up )
			psInsert.close();
			s.close();
			conn.close();
			System.out.println("Closed connection");

			// ## DATABASE SHUTDOWN SECTION ##
			/***
			 * In embedded mode, an application should shut down Derby. Shutdown
			 * throws the XJ015 exception to confirm success.
			 ***/
			if (driver.equals("org.apache.derby.jdbc.EmbeddedDriver")) {
				boolean gotSQLExc = false;
				try {
					DriverManager.getConnection("jdbc:derby:;shutdown=true");
				} catch (SQLException se) {
					if (se.getSQLState().equals("XJ015")) {
						gotSQLExc = true;
					}
				}
				if (!gotSQLExc) {
					System.out.println("Database did not shut down normally");
				} else {
					System.out.println("Database shut down normally");
				}
			}

			// Beginning of the primary catch block: uses errorPrint method
		} catch (Throwable e) {
			/*
			 * Catch all exceptions and pass them to* the exception reporting
			 * method
			 */
			System.out.println(" . . . exception thrown:");
			errorPrint(e);
		}
		System.out.println("Getting Started With Derby JDBC program ending.");
	}

	// ## DERBY EXCEPTION REPORTING CLASSES ##
	/***
	 * Exception reporting methods with special handling of SQLExceptions
	 ***/
	static void errorPrint(Throwable e) {
		if (e instanceof SQLException)
			SQLExceptionPrint((SQLException) e);
		else {
			System.out.println("A non SQL error occured.");
			e.printStackTrace();
		}
	} // END errorPrint

	// Iterates through a stack of SQLExceptions
	static void SQLExceptionPrint(SQLException sqle) {
		while (sqle != null) {
			System.out.println("\n---SQLException Caught---\n");
			System.out.println("SQLState:   " + (sqle).getSQLState());
			System.out.println("Severity: " + (sqle).getErrorCode());
			System.out.println("Message:  " + (sqle).getMessage());
			sqle.printStackTrace();
			sqle = sqle.getNextException();
		}
	} // END SQLExceptionPrint
}

class WwdUtils {

	/*****************
	 ** Asks user to enter a wish list item or 'exit' to exit the loop - returns
	 * the string entered - loop should exit when the string 'exit' is returned
	 ******************/
	public static String getWishItem() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String ans = "";
		try {
			while (ans.length() == 0) {
				System.out
						.println("Enter wish-list item (enter exit to end): ");
				ans = br.readLine();
				if (ans.length() == 0)
					System.out.print("Nothing entered: ");
			}
		} catch (java.io.IOException e) {
			System.out.println("Could not read response from stdin");
		}
		return ans;
	}

	/** END getWishItem ***/

	/*** Check for WISH_LIST table ****/
	public static boolean wwdChk4Table(Connection conTst) throws SQLException {
		try {
			Statement s = conTst.createStatement();
			s
					.execute("update WISH_LIST set ENTRY_DATE = CURRENT_TIMESTAMP, WISH_ITEM = 'TEST ENTRY' where 1=3");
		} catch (SQLException sqle) {
			String theError = (sqle).getSQLState();
			// System.out.println("  Utils GOT:  " + theError);
			/** If table exists will get - WARNING 02000: No row was found **/
			if (theError.equals("42X05")) // Table does not exist
			{
				return false;
			} else if (theError.equals("42X14") || theError.equals("42821")) {
				System.out
						.println("WwdChk4Table: Incorrect table definition. Drop table WISH_LIST and rerun this program");
				throw sqle;
			} else {
				System.out.println("WwdChk4Table: Unhandled SQLException");
				throw sqle;
			}
		}
		// System.out.println("Just got the warning - table exists OK ");
		return true;
	}

	/*** END wwdInitTable **/

	public static void main(String[] args) {
		// This method allows stand-alone testing of the getWishItem method
		String answer;
		do {
			answer = getWishItem();
			if (!answer.equals("exit")) {
				System.out.println("You said: " + answer);
			}
		} while (!answer.equals("exit"));
	}

}
