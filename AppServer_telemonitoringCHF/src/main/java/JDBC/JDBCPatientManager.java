
package JDBC;

import java.sql.Connection;


public class JDBCPatientManager implements PatientManager{
     private Connection c;
    
    public JDBCPatientManager(Connection c) {
            this.c = c;
    }
    
   
        
    
    
    
}
