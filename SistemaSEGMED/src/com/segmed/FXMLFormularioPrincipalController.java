package com.segmed;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import static com.segmed.FabricaEntityManager.*;
import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.internal.SessionImpl;


public class FXMLFormularioPrincipalController {
    EntityManager em= createEm();
    
    @FXML
    private Menu mnuOpcoes;
    @FXML
    private Menu mnuRelatorios;
    @FXML
    private MenuItem mnuRelatorioClientes;
    @FXML
    private MenuItem mnuRelatorioProdutos;
    @FXML
    private MenuItem mnuRelatorioFornecedores;
   
    
    @FXML
    void mnuSobreActionPerformed(ActionEvent event) {
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Informação");
        //alert.setHeaderText("Cabe�alho");
        alert.setContentText("Sistem de Gerenciamento - Juliano \nTelefone:(77)99819-3781");
        alert.show();
    }

    @FXML
    void mnuSairActionPerformed(ActionEvent event) {
        sair();
    }
    
    public void sair(){
        getEmf().close();
        System.exit(0);
    }
    
    @FXML
    private void mnuCadastrarCliente(ActionEvent event) throws IOException {
        ClassePrincipal.showCadastrarClientes(); 
    }


    @FXML
    private void mnuCadastrarServico(ActionEvent event) throws IOException {
        ClassePrincipal.showCadastrarServico();
        
    }
    
    @FXML
    private void mnuCadastrarColaborador(ActionEvent event) throws IOException {
        ClassePrincipal.showCadastrarColaborador();
    }
    
    @FXML
    void mnuCadastroDescricao(ActionEvent event) throws IOException {
    	ClassePrincipal.showCadastrarDescricao();
    }

    @FXML
    private void mnuExameNovo(ActionEvent event) throws IOException {
        ClassePrincipal.showExameNovo();
    }

    @FXML
    private void mnuExamePesquisar() throws IOException  {
        ClassePrincipal.showExamePesquisar();
    }

    @FXML
    private void mnuRelatorioClientes(ActionEvent event) throws IOException {
        ClassePrincipal.showRelatorioClientes();
    }

    @FXML
    private void mnuProdutosRelatorio(ActionEvent event) throws JRException{
        em.getTransaction().begin();
        Connection connection = em.unwrap(SessionImpl.class).connection(); //necessario usar o método connection 
        Map parametros = new HashMap();
        URL url= getClass().getResource("/report/relatorioProdutos.jasper");
        JasperReport jasperReport= (JasperReport) JRLoader.loadObject(url);
        JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport,parametros,connection);
        JasperViewer jasperViwer= new JasperViewer(jasperPrint, false); //impede q o programa feche
        jasperViwer.setVisible(true); //deixa a janela aberta
        em.getTransaction().commit();
    }

    @FXML
    private void mnuFornecedoresRelatorio(ActionEvent event) throws JRException {
        em.getTransaction().begin();
        Connection connection = em.unwrap(SessionImpl.class).connection(); //necessario usar o método connection 
        Map parametros = new HashMap();
        URL url= getClass().getResource("/report/relatorioFornecedores.jasper");
        JasperReport jasperReport= (JasperReport) JRLoader.loadObject(url);
        JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport,parametros,connection);
        JasperViewer jasperViwer= new JasperViewer(jasperPrint, false); //impede q o programa feche
        jasperViwer.setVisible(true); //deixa a janela aberta
        em.getTransaction().commit();
    }
      
}
