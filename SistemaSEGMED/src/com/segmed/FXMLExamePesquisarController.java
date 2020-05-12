/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.segmed;

import static com.segmed.FabricaEntityManager.createEm;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.internal.SessionImpl;


/**
 * FXML Controller class
 *
 * @author julia
 */
public class FXMLExamePesquisarController implements Initializable {

    EntityManager em= createEm();
    
    @FXML
    private Button btnPesquisar;
    @FXML
    private TableView<BDExame> tabelaExameConsultar;
    @FXML
    private TableColumn<BDExame, Integer> tabelaConsultarCodigo;
    @FXML
    private TableColumn<BDExame, String> tabelaConsultarCliente;
    @FXML
    private TableColumn<BDExame, String> tabelaConsultarColaborador;
    @FXML
    private TableColumn<BDExame, String> tabelaConsultarData;
    @FXML
    private TableColumn<BDExame, String> tabelaConsultarValor;
    @FXML
    private TableColumn<BDExame, String> tabelaConsultarTipo;
    @FXML
    private TableColumn<BDExame, String> tabelaConsultarUsuario;
    @FXML
    private ComboBox<String> comboBxCliente;
    
    @FXML
    private TextField txtDataIni;
    @FXML
    private TextField txtDataFim;
    @FXML
    private Button btnImprimirRelatorio;
    @FXML
    private Button btnExcluirExame;
    @FXML
    private Button btnDescricao;
    
    static Stage stage= new Stage(StageStyle.DECORATED); // Usar static p poder fechar a janela usando outro controlador
    static int codigoExame;
    
    
    public void initialize(URL url, ResourceBundle rb) {   
    	String dt1 =txtDataIni.getText();
        String dt2= txtDataFim.getText();
        String cliente = "";
        carregarClienteBox();
        carregarTabelaExame(dt1,dt2,cliente); 	
    }    

    @FXML
    private void btnPesquisarTabela(ActionEvent event) throws ParseException {
        String dt1 =txtDataIni.getText();
        String dt2= txtDataFim.getText();
        String cliente = comboBxCliente.getSelectionModel().getSelectedItem();
        try {
	        if(cliente.equals(null)==true) {
	        	cliente="";
	        }
	    }
	    catch(NullPointerException e) {
	    	cliente="";
	    }
        carregarTabelaExame(dt1,dt2,cliente); 
    }
    
    @FXML
    BDExame clicarExameTabela() {
    	BDExame exameSelecionado = tabelaExameConsultar.getSelectionModel().getSelectedItem();
    	codigoExame = exameSelecionado.getCodigoExame();
        return exameSelecionado;
    }
    
    
    @FXML
    private void btnRelatorioImprimir(ActionEvent event) throws JRException, ParseException {
    	if(txtDataIni.getText().isEmpty() | txtDataFim.getText().isEmpty() | comboBxCliente.getSelectionModel().isEmpty()) {
    		Alert alert= new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Informação");
            //alert.setHeaderText("Cabeçalho");
            alert.setContentText("Atenção!!! Selecione todos os campos \n(Cliente, Data Inicio, Data Fim)");
            alert.show();
    	}
    	else {
	    	em.getTransaction().begin();
	        String cliente = comboBxCliente.getSelectionModel().getSelectedItem();
	        String dt1 = txtDataIni.getText();
	        String dt2= txtDataFim.getText();
	        Connection connection = em.unwrap(SessionImpl.class).connection(); //necessario usar o mÃ©todo connection 
	        Map parametros = new HashMap();
	        parametros.put("cliente",cliente); //passa o código da venda do java  para jasperStudio 
	        parametros.put("data1",dt1);
	        parametros.put("data2",dt2);
	        JasperReport jasperReport= JasperCompileManager.compileReport("C:\\Users\\julia\\Documents\\Tecnologia Informacao\\Eclipse\\SistemaSEGMED\\src\\report\\relatorioClienteSEGMED.jrxml");
	        JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport,parametros,connection);
	        JasperViewer jasperViwer= new JasperViewer(jasperPrint, false); //impede q o programa feche
	        jasperViwer.setVisible(true); //deixa a janela aberta*/
	        em.getTransaction().commit();
    	}
    }
    
    @FXML
    void btnExameExcluir(ActionEvent event) {
    	if(tabelaExameConsultar.getSelectionModel().isEmpty()==false) {
	    	BDExame exameExcluir = clicarExameTabela();
	    	em.getTransaction().begin();
	        em.remove(exameExcluir);
	        em.getTransaction().commit();
	        String dt1 =txtDataIni.getText();
	        String dt2= txtDataFim.getText();
	        String cliente = comboBxCliente.getSelectionModel().getSelectedItem();
	        carregarTabelaExame(dt1,dt2,cliente); 
    	}
    	else {
    		Alert alert= new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Informação");
            //alert.setHeaderText("Cabeçalho");
            alert.setContentText("Atenção!!! Selecione um item!");
            alert.show();
    	}
    }
    
    @FXML
    void btnDescricao(ActionEvent event) throws IOException {
    	if(tabelaExameConsultar.getSelectionModel().isEmpty()==false) {
	    	Parent root1 = FXMLLoader.load(getClass().getResource("FXMLDescricaoExame.fxml"));
	        Scene scene= new Scene(root1);
	        stage.setTitle("Descrição");
	        stage.setScene(scene);
	        stage.show();
    	}
    	else {
    		Alert alert= new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Informação");
            //alert.setHeaderText("Cabeçalho");
            alert.setContentText("Atenção!!! Selecione um item!");
            alert.show();
    	}
    }
    
    public void carregarClienteBox() {
    	TypedQuery<BDClientes> query = em.createQuery("SELECT b FROM BDClientes b",BDClientes.class);
        List<BDClientes> listaClientes= query.getResultList();
        
        String listFor= query.getResultList().toString();
        listFor=listFor.replace("[","");
        listFor=listFor.replace("]","");
        List<String> minhaLista= new ArrayList<String>(Arrays.asList(listFor.split(", "))); //transf lista de BDClientecolaborador em String
        
        ObservableList<String> oblCliente= FXCollections.observableArrayList(minhaLista);
        comboBxCliente.setItems(oblCliente); 
    }
    
    public void carregarTabelaExame(String data1, String data2, String cliente){
        tabelaConsultarCodigo.setCellValueFactory(new PropertyValueFactory<BDExame, Integer>("codigoExame"));
        tabelaConsultarCliente.setCellValueFactory(new PropertyValueFactory<BDExame, String>("clientes"));
        tabelaConsultarTipo.setCellValueFactory(new PropertyValueFactory<BDExame, String>("tipo"));
        tabelaConsultarUsuario.setCellValueFactory(new PropertyValueFactory<BDExame, String>("usuario"));
        tabelaConsultarColaborador.setCellValueFactory(new PropertyValueFactory<BDExame, String>("clientecolaborador"));
        tabelaConsultarValor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BDExame, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<BDExame,String> param) {    
                return new ReadOnlyObjectWrapper<>(param.getValue().valorTot()); 
            }
        });
        tabelaConsultarData.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BDExame, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<BDExame,String> param) {    
                return new ReadOnlyObjectWrapper<>(param.getValue().dataFormat()); 
            }
        });
        if((data1.isEmpty() | data2.isEmpty()) && cliente.isEmpty()==false){ //se os campos de data forem vazios, retorna todas as vendas
        	Query query=  em.createQuery("SELECT  b FROM BDExame b WHERE b.clientes.nome= '"+cliente+"'");
        	List<BDExame> listaExames = query.getResultList();
            ObservableList<BDExame>oblExames= FXCollections.observableArrayList(listaExames);
            tabelaExameConsultar.setItems(oblExames);
            query.getFlushMode();
        }
        else if(data1.isEmpty()==false && data2.isEmpty()==false && cliente.isEmpty()){
        	Query query=  em.createQuery("SELECT  b FROM BDExame b WHERE b.dataExame between '"+data1+"' and '"+data2+"'");
        	List<BDExame> listaExames = query.getResultList();
            ObservableList<BDExame>oblExames= FXCollections.observableArrayList(listaExames);
            tabelaExameConsultar.setItems(oblExames);
        }
        else if(data1.isEmpty() && data2.isEmpty() && cliente.isEmpty()){
        	Query query=  em.createQuery("SELECT  b FROM BDExame b ",BDExame.class);
        	List<BDExame> listaExames = query.getResultList();
            ObservableList<BDExame>oblExames= FXCollections.observableArrayList(listaExames);
            tabelaExameConsultar.setItems(oblExames);
        }
        else {
        	Query query=  em.createQuery("SELECT  b FROM BDExame b WHERE b.clientes.nome= '"+cliente+"' AND b.dataExame between '"+data1+"' and '"+data2+"'");
        	List<BDExame> listaExames = query.getResultList();
            ObservableList<BDExame>oblExames= FXCollections.observableArrayList(listaExames);
            tabelaExameConsultar.setItems(oblExames);
        }
        
    }

    @FXML
    private void tfDataInicio(KeyEvent event) {
        TextFieldFormatter tff= new TextFieldFormatter(); //classe que formata a entrada/formataÃ§Ã£o de dados do usuario
        tff.setMask("##/##/####");
        tff.setCaracteresValidos("0123456789");
        tff.setTf(txtDataIni);
        tff.formatter();
    }

    @FXML
    private void tfDataFim(KeyEvent event) {
        TextFieldFormatter tff= new TextFieldFormatter(); //classe baixada do youtube, mto importante
        tff.setMask("##/##/####");
        tff.setCaracteresValidos("0123456789");
        tff.setTf(txtDataFim);
        tff.formatter(); 
    }
     
}
