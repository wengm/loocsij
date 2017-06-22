/*
package org.loocsij._3party.dbcp;

*/
/*
 * $Source: 
 /home/cvspublic/jakarta-commons/dbcp/doc/BasicDataSourceExample.java,v $
 * $Revision: 1.1 $
 * $Date: 2009-07-14 09:37:03 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowledgement:
 *       "This product includes software developed by the
 *        Apache Software Foundation - http://www.apache.org/"
 *    Alternately, this acknowledgement may appear in the software itself,
 *    if and wherever such third-party acknowledgements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * http://www.apache.org/
 *
 *//*


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

//
// Here are the dbcp-specific classes.
// Note that they are only used in the setupDataSource
// method. In normal use, your classes interact
// only with the standard JDBC API
//
import org.apache.commons.dbcp.BasicDataSource;

//
// Here's a simple example of how to use the BasicDataSource.
// In this example, we'll construct the BasicDataSource manually,
// but you could also configure it using an external conifguration file.
//

//
// Note that this example is very similiar to the PoolingDriver
// example.

//
// To compile this example, you'll want:
// * commons-pool.jar
// * commons-dbcp.jar
// * j2ee.jar (for the javax.sql classes)
// in your classpath.
//
// To run this example, you'll want:
// * commons-collections.jar
// * commons-pool.jar
// * commons-dbcp.jar
// * j2ee.jar (for the javax.sql classes)
// * the classes for your (underlying) JDBC driver
// in your classpath.
//
// Invoke the class using two arguments:
// * the connect string for your underlying JDBC driver
// * the query you'd like to execute
// You'll also want to ensure your underlying JDBC driver
// is registered. You can use the "jdbc.drivers"
// property to do this.
//
// For example:
// java -Djdbc.drivers=oracle.jdbc.driver.OracleDriver \
// -classpath
// commons-collections.jar:commons-pool.jar:commons-dbcp.jar:j2ee.jar:oracle-jdbc.jar:.
// \
// BasicDataSourceExample
// "jdbc:oracle:thin:scott/tiger@myhost:1521:mysid"
// "SELECT * FROM DUAL"
//
public class BasicDataSourceExample {

	public static void main(String[] args) {
		// First we set up the BasicDataSource.
		// Normally this would be handled auto-magically by
		// an external configuration, but in this example we'll
		// do it manually.
		//
		System.out.println("Setting up data source.");
		DataSource dataSource = setupDataSource(args[0]);
		System.out.println("Done.");

		//
		// Now, we can use JDBC DataSource as we normally would.
		//
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;

		try {
			System.out.println("Creating connection.");
			conn = dataSource.getConnection();
			System.out.println("Creating statement.");
			stmt = conn.createStatement();
			System.out.println("Executing statement.");
			rset = stmt.executeQuery(args[1]);
			System.out.println("Results:");
			int numcols = rset.getMetaData().getColumnCount();
			while (rset.next()) {
				for (int i = 1; i <= numcols; i++) {
					System.out.print("\t" + rset.getString(i));
				}
				System.out.println("");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
			} catch (Exception e) {
			}
			try {
				stmt.close();
			} catch (Exception e) {
			}
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}

	public static DataSource setupDataSource(String connectURI) {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUsername("root");
		ds.setPassword("root");
		ds.setUrl(connectURI);
		return ds;
	}

	public static void printDataSourceStats(DataSource ds) throws SQLException {
		BasicDataSource bds = (BasicDataSource) ds;
		System.out.println("NumActive: " + bds.getNumActive());
		System.out.println("NumIdle: " + bds.getNumIdle());
	}

	public static void shutdownDataSource(DataSource ds) throws SQLException {
		BasicDataSource bds = (BasicDataSource) ds;
		bds.close();
	}
}
*/
