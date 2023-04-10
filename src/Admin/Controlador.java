/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author edin1
 */
public class Controlador {

    void Guardar(int telefono, int rol_id, int activo, String nombre, String apellido, String direccion, String correo_electronico, String contrasenia, Date fecha_nacimiento, Connection cn) {
        try {
            String Consulta = "INSERT INTO usuarios(telefono, rol_id, activo,nombre,apellido,direccion,correo_electronico,contrasenia,fecha_nacimiento) VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement pst = cn.prepareStatement(Consulta);
            pst.setInt(1, telefono);
            pst.setInt(2, rol_id);
            pst.setInt(3, activo);
            pst.setString(4, nombre);
            pst.setString(5, apellido);
            pst.setString(6, direccion);
            pst.setString(7, correo_electronico);
            pst.setString(8, contrasenia);
            SimpleDateFormat Formato = new SimpleDateFormat("yyyy/MM/dd");
            String sfNacimiento = Formato.format(fecha_nacimiento);
            pst.setString(9, sfNacimiento);
            pst.executeUpdate();
            pst.close();;
            JOptionPane.showMessageDialog(null, "Guardado");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al Guardar");
            e.printStackTrace();
        }
    }

    static DefaultTableModel tablausuario(Connection cn) {
        DefaultTableModel modelo = new DefaultTableModel();
        try {
            String consulta = "SELECT * from usuarios";
            ArrayList<Modelo> Datos = new ArrayList<>();
            Statement pr = cn.createStatement();
            ResultSet rs = pr.executeQuery(consulta);
            while (rs.next()) {
                Datos.add(new Modelo(rs.getInt("usuario_id"), rs.getInt("telefono"), rs.getInt("rol_id"), rs.getInt("activo"), rs.getString("nombre"), rs.getString("apellido"), rs.getString("direccion"), rs.getString("correo_electronico"), rs.getString("contrasenia"), rs.getDate("fecha_nacimiento")));
            }
            rs.close();
            pr.close();
            String[] Titulo = {"No", "Nombre", "Apellido", "Rol", "Correo", "telefono"};
            modelo.setColumnIdentifiers(Titulo);
            for (int x = 0; x < Datos.size(); x++) {
                String[] datos = new String[6];
                datos[0] = String.valueOf(x + 1);
                datos[1] = Datos.get(x).getNombre();
                datos[2] = Datos.get(x).getApellido();
                switch (Datos.get(x).getRol_id()) {
                    case 1:
                        datos[3] = "Administrador";
                        break;
                    case 2:
                        datos[3] = "Gerente";
                        break;
                }
                datos[4] = Datos.get(x).getCorreo_electronico();
                datos[5] = String.valueOf(Datos.get(x).getTelefono());
                modelo.addRow(datos);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return modelo;
    }

    void Actualizar(int usuario_id, int telefono, int rol_id, String nombre, String apellido, String direccion, String correo_electronico, String contrasenia, Date fecha_nacimiento, Connection cn) {
        try {
            SimpleDateFormat Formato = new SimpleDateFormat("yyyy/MM/dd");
            String sfNacimiento = Formato.format(fecha_nacimiento);
            String Consulta = "update usuarios set telefono='" + telefono + "',rol_id='" + rol_id + "',nombre='" + nombre + "',apellido='" + apellido + "',direccion='" + direccion + "',correo_electronico='" + correo_electronico + "',contrasenia='" + contrasenia + "',fecha_nacimiento='" + sfNacimiento + "' where usuario_id='" + usuario_id + "'";
            PreparedStatement pr = cn.prepareStatement(Consulta);
            pr.executeUpdate();
            pr.close();
            JOptionPane.showMessageDialog(null, "Actualizado");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No Actualizado");
        }
    }

    ArrayList obtener(Connection cn, String busqueda) {
        ArrayList<Modelo> Datos = new ArrayList();
        try {
            String consulta = "SELECT * from usuarios WHERE nombre = '" + busqueda + "'";
            Statement ps = cn.createStatement();
            ResultSet rs = ps.executeQuery(consulta);
            if (rs.next()) {
                Datos.add(new Modelo(rs.getInt("usuario_id"), rs.getInt("telefono"), rs.getInt("rol_id"), rs.getInt("activo"), rs.getString("nombre"), rs.getString("apellido"), rs.getString("direccion"), rs.getString("correo_electronico"), rs.getString("contrasenia"), rs.getDate("fecha_nacimiento")));
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Datos;
    }

    void Eliminar(int usuario_id, Connection cn) {
        try {
            String consulta = "DELETE  from usuarios WHERE usuario_id =" + usuario_id;
           PreparedStatement pst = cn.prepareStatement(consulta);
            pst.executeUpdate();
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
