// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License.
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the GNU General Public License
// * as published by the Free Software Foundation; either version 2
// * of the License, or (at your option) any later version. *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
// * along with this program; if not, write to the Free Software
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
// *
// * <OSCAR TEAM>
// * This software was written for the
// * Department of Family Medicine
// * McMaster Unviersity
// * Hamilton
// * Ontario, Canada
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarDB;

import java.sql.*;
import java.util.*;

/** A class for preallocating, recycling, and managing
 *  JDBC connections.
 */

public class DBConnectionPool implements Runnable {
   private String driver, url, username, password;
   private int maxConnections;
   private boolean waitIfBusy;
   private Vector availableConnections, busyConnections;
   private boolean connectionPending = false;
   
   public DBConnectionPool(String driver, String url,String username, String password,int initialConnections,int maxConnections,boolean waitIfBusy) throws SQLException {
      this.driver = driver;
      this.url = url;
      this.username = username;
      this.password = password;
      this.maxConnections = maxConnections;
      this.waitIfBusy = waitIfBusy;
      if (initialConnections > maxConnections) {
         initialConnections = maxConnections;
      }
      availableConnections = new Vector(initialConnections);
      busyConnections = new Vector();
      for(int i=0; i<initialConnections; i++) {
         availableConnections.addElement(makeNewConnection());
      }
   }
   /** Provides a connestion from the connection pool to the caller.
    */
   
   
   public boolean testConn(Connection conn){
      boolean test = true;
      String testQuery = "select 1";
      try{         
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(testQuery);				                  
      }catch(Exception e){
         test = false;
      }
      return test;
   }
   
   public synchronized Connection getConnection() throws SQLException {
      if (!availableConnections.isEmpty()) {
         Connection existingConnection = (Connection) availableConnections.lastElement();
         int lastIndex = availableConnections.size() - 1;
         availableConnections.removeElementAt(lastIndex);
         // If connection on available list is closed (e.g.,
         // it timed out), then remove it from available list
         // and repeat the process of obtaining a connection.
         // Also wake up threads that were waiting for a
         // connection because maxConnection limit was reached.
         if (existingConnection.isClosed()) {
            notifyAll(); // Freed up a spot for anybody waiting
            return(getConnection());
         } else {
            if(!testConn(existingConnection)){
               return(getConnection()); 
            }                        
            busyConnections.addElement(existingConnection);
            return(existingConnection);
         }
      } else {
         
         // Three possible cases:
         // 1) You haven't reached maxConnections limit. So
         //    establish one in the background if there isn't
         //    already one pending, then wait for
         //    the next available connection (whether or not
         //    it was the newly established one).
         // 2) You reached maxConnections limit and waitIfBusy
         //    flag is false. Throw SQLException in such a case.
         // 3) You reached maxConnections limit and waitIfBusy
         //    flag is true. Then do the same thing as in second
         //    part of step 1: wait for next available connection.
         
         if ((totalConnections() < maxConnections) && !connectionPending) {
            makeBackgroundConnection();
         } else if (!waitIfBusy) {
            throw new SQLException("Connection limit reached");
         }
         // Wait for either a new connection to be established
         // (if you called makeBackgroundConnection) or for
         // an existing connection to be freed up.
         try {
            wait();
         } catch(InterruptedException ie) {}
         // Someone freed up a connection, so try again.
         return(getConnection());
      }
   }
   
   
   private void makeBackgroundConnection() {
      connectionPending = true;
      try {
         Thread connectThread = new Thread(this);
         connectThread.start();
      } catch(OutOfMemoryError oome) {
         // Give up on new connection
      }
   }
   
   public void run() {
      try {
         Connection connection = makeNewConnection();
         synchronized(this) {
            availableConnections.addElement(connection);
            connectionPending = false;
            notifyAll();
         }
      } catch(Exception e) { // SQLException or OutOfMemory
         // Give up on new connection and wait for existing one
         // to free up.
      }
   }
   
   // This explicitly makes a new connection. Called in
   // the foreground when initializing the DBConnectionPool,
   // and called in the background when running.
   
   private Connection makeNewConnection()
   throws SQLException {
      
      try {
         // Load database driver if not already loaded
         Class.forName(driver);
         // Establish network connection to database
         Connection connection =
         DriverManager.getConnection(url, username, password);
         return(connection);
      } catch(ClassNotFoundException cnfe) {
         // Simplify try/catch blocks of people using this by
         // throwing only one exception type.
         throw new SQLException("Can't find class for driver: " +
         driver);
      }
   }
   
   public synchronized void free(Connection connection) {
      
      busyConnections.removeElement(connection);
      if (!availableConnections.contains(connection)){
         availableConnections.addElement(connection);
      }
      // Wake up threads that are waiting for a connection
      notifyAll();
   }
   
   public synchronized int totalConnections() {
      return(availableConnections.size() +
      busyConnections.size());
   }
   
   /** Close all the connections. Use with caution:
    *  be sure no connections are in use before
    *  calling. Note that you are not <I>required</I> to
    *  call this when done with a DBConnectionPool, since
    *  connections are guaranteed to be closed when
    *  garbage collected. But this method gives more control
    *  regarding when the connections are closed.
    */
   
   public synchronized void closeAllConnections() {
      closeConnections(availableConnections);
      availableConnections = new Vector();
      closeConnections(busyConnections);
      busyConnections = new Vector();
   }
   
   private void closeConnections(Vector connections) {
      try {
         for(int i=0; i<connections.size(); i++) {
            Connection connection =
            (Connection)connections.elementAt(i);
            if (!connection.isClosed()) {
               connection.close();
            }
         }
      } catch(SQLException sqle) {
         // Ignore errors; garbage collect anyhow
      }
   }
   
   public synchronized String toString() {
      String info =
      "DBConnectionPool(" + url + "," + username + ")" +
      ", available=" + availableConnections.size() +
      ", busy=" + busyConnections.size() +
      ", max=" + maxConnections;
      return(info);
   }
}

