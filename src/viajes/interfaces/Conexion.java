/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viajes.interfaces;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ESTUDIANTE
 */
public class Conexion {

    Connection conectar = null;

    public Connection conectar() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conectar = DriverManager.getConnection("jdbc:mysql://localhost/viajes", "root", "");// base de datos , usuario, comtraseña
            // locla host:8080
//              JOptionPane.showMessageDialog(null, "Ok");
        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(null, "Conexion fallida, ponte en contacto con el administrador");
        }

        return conectar;
    }
    

}
