/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viajes.interfaces;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicListUI;
import javax.swing.table.DefaultTableModel;
import sun.awt.im.InputMethodJFrame;

/**
 *
 * @author ESTUDIANTE
 */
public class Autos extends javax.swing.JFrame {

    /**
     * Creates new form Autos
     */
    ArrayList listaModelo = new ArrayList();
    ArrayList listaMarca = new ArrayList();
    DefaultTableModel modeloAuto;

    public Autos() {

        initComponents();
        this.setLocationRelativeTo(null);
        cargarMarca();
        bloquear();
        cbAutMarca.setSelectedIndex(-1);
        bloquearboton();
        cargarModelo();
        CargarTablaAutos("");
        noLetrasSpinner();

    }

    public void bloquearBotonModificar() {
        btnNuevo.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnModificatr.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnSalir.setEnabled(true);

    }

    public void bloquear() {
        txtAutPla.setEnabled(false);
        cbAutMarca.setEnabled(false);
        cbAutModelo.setEnabled(false);
        cbAutColor.setEnabled(false);
        sppAutAnio.setEnabled(false);
        sppAutCapacidad.setEnabled(false);
        txtAutObservacion.setEnabled(false);
    }

    public void desbloquear() {
        txtAutPla.setEnabled(true);
        cbAutMarca.setEnabled(true);
        cbAutModelo.setEnabled(true);
        cbAutColor.setEnabled(true);
        sppAutAnio.setEnabled(true);
        sppAutCapacidad.setEnabled(true);
        txtAutObservacion.setEnabled(true);
    }

    public void bloquearboton() {
        btnNuevo.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnModificatr.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnCancelar.setEnabled(false);

    }

    public void bloquearBotonNuevo() {
        btnNuevo.setEnabled(false);
        btnGuardar.setEnabled(true);
        btnModificatr.setEnabled(false);
        btnEliminar.setEnabled(true);
        btnCancelar.setEnabled(true);

    }

    public void limPiar() {
        txtAutPla.setText("");
        cbAutMarca.setSelectedIndex(-1);
        cbAutModelo.setSelectedIndex(-1);
        cbAutColor.setSelectedIndex(-1);
        txtAutObservacion.setText("");
        sppAutAnio.setValue(0);
        sppAutCapacidad.setValue(0);
    }

    public void Modificar() {
     try {
        Conexion cc= new Conexion();
      Connection cn= cc.conectar();
      String sql="";
   sql="update autos set MOD_CODIGO='"+cbAutModelo.getSelectedItem().toString().substring(0,1).trim()+"'"
           + ",AUT_ANIO='"+sppAutAnio.getValue()+"' "
          + "where AUT_PLACA='"+txtAutPla.getText() + "'";
        
            PreparedStatement psd= cn.prepareStatement(sql);
         int n=  psd.executeUpdate();
           if(n>0){
               JOptionPane.showMessageDialog(null, "Actualizaciòn Correcta");
           }else{
                 JOptionPane.showMessageDialog(null, "No se actualizo correctamente"); 
                 bloquear();
                 bloquearboton();
                 limPiar();
           }
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null, ex);
        }
   
    }

    private String searchModelo(String placa) {
        String modelo = "sadasd";
        String sql = "";
        try {

            Conexion cc = new Conexion();
            Connection cn = cc.conectar();
            sql = "select ma.MAR_CODIGO from autos au, modelos m, marcas ma where au.MOD_CODIGO=m.MOD_CODIGO "
                    + "and ma.MAR_CODIGO=m.MAR_CODIGO and au.AUT_PLACA='" + placa + "'";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                modelo = rs.getString(1);
            }
        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(null, ex);
        }

        return modelo;
    }

    public void guardarAutoNuevo() {
        if (txtAutPla.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese placa");

        } else if (Integer.valueOf(sppAutAnio.getValue().toString()) < 1960 || Integer.valueOf(sppAutAnio.getValue().toString()) > 2020) {
            JOptionPane.showMessageDialog(null, "Ingrese año del auto entre 1960 y 2019");
        } else if (cbAutColor.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(null, "Seleccione color");
        } else if (Integer.valueOf(sppAutCapacidad.getValue().toString()) < 1 || Integer.valueOf(sppAutCapacidad.getValue().toString()) > 40) {
            JOptionPane.showMessageDialog(null, "Ingrese capacidad de auto");
        } else {
            try {
                String AUT_PLACA, MOD_CODIGO, AUT_COLOR, AUT_OBSERVACION;
                //3. asignar valores 
                Integer AUT_ANIO, AUT_CAPACIDAD;
                Conexion cc = new Conexion();
                Connection cn = cc.conectar();// guaardar en una variable cn
                //2. sentencia sql
                String sql = "";
                sql = "insert into autos(AUT_PLACA,MOD_CODIGO,AUT_ANIO,AUT_COLOR,AUT_CAPACIDAD,AUT_OBSERVACION,AUT_ESTADO)"
                        + " values (?,?,?,?,?,?,'A')";
                PreparedStatement ppt = cn.prepareStatement(sql);
                ppt.setString(1, txtAutPla.getText());
                ppt.setString(2, cbAutModelo.getSelectedItem().toString().substring(0, 1));
                ppt.setInt(3, (int) sppAutAnio.getValue());
                ppt.setString(4, cbAutColor.getSelectedItem().toString());
                ppt.setInt(5, (int) sppAutCapacidad.getValue());
                if (txtAutObservacion.getText().equals("")) {
                    ppt.setString(6, "Sin Observacion");
                } else {
                    ppt.setString(6, txtAutObservacion.getText());
                }

                ppt.executeUpdate();
                limPiar();
                JOptionPane.showMessageDialog(null, "Guardardo Correctamente");

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "No se puedo guardar el dato");
            }
        }

    }

    // cargar datos
    // conectar a la base
    public void cargarMarca() {
        try {
            Conexion cc = new Conexion();
            Connection cn = cc.conectar();
            String sql = "";
            sql = "select * from marcas";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);

            while (rs.next()) {
                String id = rs.getString("MAR_CODIGO");
                String marca = rs.getString("MAR_NOM");
               
                cbAutMarca.addItem(id + " " + marca);

            }
        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void cargarModelo() {
        try {

            Conexion cc = new Conexion();
            Connection cn = cc.conectar();
            String sql = "";
            int num = cbAutMarca.getSelectedIndex() + 1;
            sql = "select * from modelos where 	MAR_CODIGO ='" + num + "'";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                String modelo = rs.getString("MOD_NOMBRE");
                String cod = rs.getString("MOD_CODIGO");
                if (num >= 1) {
                    cbAutModelo.addItem(cod + " " + modelo);
                    listaMarca.add(modelo);

                }
            }
        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(null, ex);
        }
    }
    public void CargarTablaAutos(String dato) {
// 
        String[] titulos = {"PLACA", "MARCA", "MODELO", "AÑO", "COLOR", "CAPACIDAD", "OBSERVACIÓN"};
        String[] registros = new String[7];
        modeloAuto = new DefaultTableModel(null, titulos);
        jtbAutoDatos.setModel(modeloAuto);

        try {
            Conexion cc = new Conexion();
            Connection cn = cc.conectar();
            String sql = "";

            sql = "select autos.AUT_PLACA,"
                    + "autos.MOD_CODIGO,"
                    + "autos.AUT_ANIO,"
                    + "autos.AUT_COLOR,"

                    + "autos.AUT_CAPACIDAD,"
                    + "autos.AUT_OBSERVACION,"
                    + "modelos.MOD_NOMBRE,"
                    + "marcas.MAR_NOM "
                    + "from autos, modelos, marcas "
                    + "where autos.MOD_CODIGO = modelos.MOD_CODIGO and modelos.MAR_CODIGO = marcas.MAR_CODIGO and "
                    +"autos.AUT_ESTADO='A' and "
                    + " autos.AUT_PLACA LIKE'%" + dato + "%'"  ;

            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                // placa marca modelo
                registros[0] = rs.getString("AUT_PLACA");
                registros[1] = rs.getString("MAR_NOM");
                registros[2] = rs.getString("MOD_NOMBRE");
                registros[3] = rs.getString("AUT_ANIO");
                registros[4] = rs.getString("AUT_COLOR");
                registros[5] = rs.getString("AUT_CAPACIDAD");
                registros[6] = rs.getString("AUT_OBSERVACION");

                modeloAuto.addRow(registros);
            }

        } catch (SQLException ex) {

        }

    }

    public void cargarModificar() {
        if (jtbAutoDatos.getSelectedRow() != -1) {
        }
        bloquearBotonModificar();
        desbloquear();
        int fila = jtbAutoDatos.getSelectedRow();
        String placa = jtbAutoDatos.getValueAt(fila, 0).toString().trim();
        txtAutPla.setText(placa);//(trim  devuelve el dato sin darme los spsacios en blanco)

        cbAutMarca.setSelectedItem(searchModelo(placa) + " " + jtbAutoDatos.getValueAt(fila, 1).toString().trim());
        sppAutAnio.setValue(Integer.parseInt(jtbAutoDatos.getValueAt(fila, 3).toString().trim()));
        cbAutColor.setSelectedItem(jtbAutoDatos.getValueAt(fila, 4).toString().trim());
        sppAutCapacidad.setValue(Integer.parseInt(jtbAutoDatos.getValueAt(fila, 5).toString().trim()));
        txtAutObservacion.setText(jtbAutoDatos.getValueAt(fila, 6).toString().trim());

    }

    public void convertiraMayusculasEnJtextfield(javax.swing.JTextField jTextfieldS) {
        String cadena = (jTextfieldS.getText()).toUpperCase();
        jTextfieldS.setText(cadena);
    }
    public void EliminarAuto(){
         if(JOptionPane.showConfirmDialog(new JInternalFrame(),
                 "Estas seguro de borar el registro",
                 "Borrar Registro",
                 JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
        try {
            Conexion cc = new Conexion();
            Connection cn = cc.conectar();
            String sql = "";
             sql="UPDATE autos SET AUT_ESTADO= 'I'  where AUT_PLACA='"+txtAutPla.getText()+"'";
            //sql="delete from autos where AUT_PLACA='"+txtAutPla.getText()+"'";
            PreparedStatement psd=cn.prepareStatement(sql);
            int n  =psd.executeUpdate();
            CargarTablaAutos("");
            bloquear();
            bloquearboton();
            limPiar();
            if(n>0){
                JOptionPane.showMessageDialog(null, "Eliminado Correctamente");
            }else{
                JOptionPane.showMessageDialog(null, "No se pudo Eliminar ");
            }
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null, ex);
        }
        }

    }
    public void noLetrasSpinner(){
     ((JSpinner.DefaultEditor)sppAutAnio.getEditor()).getTextField().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
                if (!Character.isDigit(ke.getKeyChar())) {
                    ke.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                
            }
        });
     ((JSpinner.DefaultEditor)sppAutCapacidad.getEditor()).getTextField().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
                if (!Character.isDigit(ke.getKeyChar())) {
                    ke.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                
            }
        });
}
    //cambiar lo de activo e inactivo a un char de 1 :)/// :) :) :) :) :) 

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always *
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtAutPla = new javax.swing.JTextField();
        cbAutMarca = new javax.swing.JComboBox<>();
        cbAutModelo = new javax.swing.JComboBox<>();
        sppAutAnio = new javax.swing.JSpinner();
        cbAutColor = new javax.swing.JComboBox<>();
        sppAutCapacidad = new javax.swing.JSpinner();
        txtAutObservacion = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnModificatr = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtbAutoDatos = new javax.swing.JTable();
        txtBuscar = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel1.setText("PLACA");

        jLabel2.setText("MARCA");

        jLabel3.setText("MODELOS");

        jLabel4.setText("AÑO");

        jLabel5.setText("COLOR");

        jLabel6.setText("CAPACIDAD");

        jLabel7.setText("OBSERVACION");

        txtAutPla.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAutPlaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAutPlaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAutPlaKeyTyped(evt);
            }
        });

        cbAutMarca.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbAutMarcaItemStateChanged(evt);
            }
        });
        cbAutMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbAutMarcaActionPerformed(evt);
            }
        });

        cbAutModelo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbAutModeloActionPerformed(evt);
            }
        });

        sppAutAnio.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        sppAutAnio.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sppAutAnioStateChanged(evt);
            }
        });
        sppAutAnio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sppAutAnioKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sppAutAnioKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                sppAutAnioKeyTyped(evt);
            }
        });

        cbAutColor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ROJO", "AZUL", "VERDE", "MORADO", "ROSA", "AMARILLO", "GRIS", "NEGRO" }));
        cbAutColor.setSelectedIndex(-1);

        sppAutCapacidad.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        txtAutObservacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAutObservacionKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel2))
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbAutMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtAutPla)
                        .addComponent(cbAutModelo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(sppAutAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbAutColor, 0, 154, Short.MAX_VALUE)
                        .addComponent(txtAutObservacion)
                        .addComponent(sppAutCapacidad, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtAutPla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbAutMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbAutModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sppAutAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cbAutColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(sppAutCapacidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtAutObservacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnModificatr.setText("Modificar");
        btnModificatr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificatrActionPerformed(evt);
            }
        });

        btnEliminar.setText("Elininar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");

        btnSalir.setText("Salir");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnModificatr, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(28, 28, 28))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(btnNuevo)
                .addGap(18, 18, 18)
                .addComponent(btnGuardar)
                .addGap(18, 18, 18)
                .addComponent(btnModificatr)
                .addGap(18, 18, 18)
                .addComponent(btnEliminar)
                .addGap(18, 18, 18)
                .addComponent(btnCancelar)
                .addGap(18, 18, 18)
                .addComponent(btnSalir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jtbAutoDatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jtbAutoDatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jtbAutoDatosMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(jtbAutoDatos);

        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
        });

        jLabel8.setText("Buscar");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(404, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                .addGap(63, 63, 63))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        desbloquear();
        bloquearBotonNuevo();// TODO add your handling code here:
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void cbAutMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAutMarcaActionPerformed
        cbAutModelo.removeAllItems();
        cargarModelo();
    }//GEN-LAST:event_cbAutMarcaActionPerformed

    private void cbAutMarcaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbAutMarcaItemStateChanged
//        cbAutModelo.removeAll();
//        cargarModelo();        // TODO add your handling code here:
    }//GEN-LAST:event_cbAutMarcaItemStateChanged

    private void cbAutModeloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAutModeloActionPerformed
//     cbAutModelo.removeAllItems();
//     cargarModelo(); 
    }//GEN-LAST:event_cbAutModeloActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardarAutoNuevo();
        CargarTablaAutos("");
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void sppAutAnioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sppAutAnioKeyTyped

    }//GEN-LAST:event_sppAutAnioKeyTyped

    private void sppAutAnioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sppAutAnioKeyPressed

    }//GEN-LAST:event_sppAutAnioKeyPressed

    private void sppAutAnioStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sppAutAnioStateChanged

    }//GEN-LAST:event_sppAutAnioStateChanged

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        CargarTablaAutos(txtBuscar.getText());
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void jtbAutoDatosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtbAutoDatosMousePressed
        cargarModificar();

    }//GEN-LAST:event_jtbAutoDatosMousePressed

    private void sppAutAnioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sppAutAnioKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_sppAutAnioKeyReleased

    private void txtAutObservacionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAutObservacionKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAutObservacionKeyTyped

    private void txtAutPlaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAutPlaKeyTyped
        if (txtAutPla.getText().length() == 7) {

            evt.consume();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAutPlaKeyTyped

    private void txtAutPlaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAutPlaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAutPlaKeyPressed

    private void txtAutPlaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAutPlaKeyReleased
        convertiraMayusculasEnJtextfield(txtAutPla);
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAutPlaKeyReleased

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void btnModificatrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificatrActionPerformed
Modificar();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnModificatrActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
EliminarAuto();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Autos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Autos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Autos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Autos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Autos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificatr;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cbAutColor;
    private javax.swing.JComboBox<String> cbAutMarca;
    private javax.swing.JComboBox<String> cbAutModelo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtbAutoDatos;
    private javax.swing.JSpinner sppAutAnio;
    private javax.swing.JSpinner sppAutCapacidad;
    private javax.swing.JTextField txtAutObservacion;
    private javax.swing.JTextField txtAutPla;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
