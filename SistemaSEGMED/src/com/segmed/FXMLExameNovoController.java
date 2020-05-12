package com.segmed;

import static com.segmed.FabricaEntityManager.createEm;
import static com.segmed.FXMLLoginController.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import sun.security.jgss.LoginConfigImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional.TxType;

/**
 * FXML Controller class
 *
 * @author julia
 */
public class FXMLExameNovoController implements Initializable {
    EntityManager em= createEm();
    
    @FXML
    private TableView<BDExameitem> tabelaExame;
    
    @FXML
    private TableColumn<BDExameitem ,String> tabelaExameCliente;
    
    @FXML
    private TableColumn<BDExameitem, String> tabelaExameColaborador;
    
    @FXML
    private TableColumn<BDExameitem ,BDServico> tabelaExameServico;
    
    @FXML
    private ComboBox<String> comboBxExameCliente;
    
    @FXML
    private ComboBox<String> comboBxExameColaborador;
    
    @FXML
    private ComboBox<String> comboBxExameServico;
    
    @FXML
    private ComboBox<String> comboBxExameTipo;
    
    @FXML
    private Button btnExameFinalizar;
    
    @FXML
    private Button btnExameCancelarItem;
    
    @FXML
    private Button btnExameCancelar;
    
    @FXML
    private Button btnExameIncluir;
    
    BDExame exame;
    boolean finalizar= true;
    float totalExame=0;
    List<BDClientecolaborador> listaColaboradores;
    List<BDClientes> listaClientes;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarClienteBox();
        carregarServicoBox();
        carregarTipoBox();
        btnExameFinalizar.setDisable(true);  
        btnExameCancelar.setDisable(true);
        btnExameCancelarItem.setDisable(true);
        comboBxExameCliente.setOnAction(e -> carregarColaboradorBox());
    } 

    @FXML
    private BDExameitem clicarColaboradorTabela() {
        BDExameitem item = tabelaExame.getSelectionModel().getSelectedItem();
        return item;
    }
    
    @FXML
    private void btnFinalizarExame(ActionEvent event) {
        exame.setValorTotal(totalExame); //armazena o valor total da Exame
        em.getTransaction().begin();
        em.merge(exame);
        em.getTransaction().commit();
        finalizar= true;
        btnExameFinalizar.setDisable(true); 
        comboBxExameCliente.setDisable(false);
        comboBxExameColaborador.setDisable(false);
        comboBxExameTipo.setDisable(false);
        tabelaExame.getItems().clear(); //limpa os dados da tabela
        totalExame=0;
        //txtExameTotal.setText(Float.toString(totalExame));
    }
    
    @FXML
    private void btnCancelarExame(ActionEvent event) {
        List<BDExameitem> itemExcluir = tabelaExame.getSelectionModel().getTableView().getItems();
        for(int i=0;i<itemExcluir.size();i++){
            em.getTransaction().begin();
            em.remove(itemExcluir.get(i));
            em.getTransaction().commit();
        }
        em.getTransaction().begin();
        em.remove(exame);
        em.getTransaction().commit();
        
        totalExame=0;
        carregarTabelaExame();
        comboBxExameCliente.setDisable(false);//permite escolher um cliente na lista
        comboBxExameColaborador.setDisable(false);
        comboBxExameTipo.setDisable(false);
        btnExameFinalizar.setDisable(true);
        finalizar=true; // permite a criaÃ§Ã£o de um novo objeto Exame
    }

    @FXML
    private void btnCancelarExameItem(ActionEvent event) {
    	if(tabelaExame.getSelectionModel().isEmpty()==false) {
	        BDExameitem itemExcluir = em.find(BDExameitem.class, clicarColaboradorTabela().getCodigoExameitem());
	        totalExame= totalExame - itemExcluir.getDescricao().getValor();//subtrai o valor do item na Exame total
	        String valorString = "R$"+String.format("%.2f",totalExame);
	        //txtExameTotal.setText(valorString);
	        em.getTransaction().begin();
	        em.remove(itemExcluir);
	        em.getTransaction().commit();
	        carregarTabelaExame();   
    	}
    	else {
    		Alert alert= new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Informação");
            //alert.setHeaderText("Cabeçalho");
            alert.setContentText("Atenção!!! Selecione um item a ser excluido!");
            alert.show();
    	}
    }
    
    @FXML
    private void btnIncluirExameItem(ActionEvent event) throws ParseException {
    	if(comboBxExameTipo.getSelectionModel().isEmpty()==false && comboBxExameCliente.getSelectionModel().isEmpty()==false
    			&& comboBxExameColaborador.getSelectionModel().isEmpty()==false && comboBxExameServico.getSelectionModel().isEmpty()==false){ //verifica se os campos estÃ£o preenchidos   
	        String data1= "15/01/2017";
	        SimpleDateFormat dt= new SimpleDateFormat("dd/MM/yyyy");
	        Date data =dt.parse(data1);
	        /*****************************************Seleção de itens no comboBox***************************************************/
	        BDClientes cliente= null;
	        BDClientecolaborador colaborador= null;
	        BDServico servico= null;
	        String clienteNome= comboBxExameCliente.getSelectionModel().getSelectedItem(); //o do Colaborador estÃ¡ no metodo acharColaborador()
	        String colaboradorNome= comboBxExameColaborador.getSelectionModel().getSelectedItem();
	        String servicoNome= comboBxExameServico.getSelectionModel().getSelectedItem();
	          
	        for( BDClientes t: listaClientes){
	                if(t.getNome().equals(clienteNome)){
	                    cliente=t;
	                }
	        }        
	        for( BDClientecolaborador a: listaColaboradores){
	            if(a.getNome().equals(colaboradorNome)){
	                colaborador=a;
	            }
	        }
	        for( BDServico b: carregarServicoBox()){
	            if(b.getNome().equals(servicoNome)){
	                servico=b;
	            }
	        }
	        /************************************************************************************************************************/
	        if(finalizar==true){
	        	String tipoExame = comboBxExameTipo.getSelectionModel().getSelectedItem();
	            exame= new BDExame(cliente, colaborador, data,tipoExame, login.getUsuario()); //login da classe LoginController
	            em.getTransaction().begin();
	            em.persist(exame);
	            em.getTransaction().commit();
	            System.out.println(exame.getCodigoExame());
	        }
	        
	        TypedQuery<BDDescricao> query = (TypedQuery<BDDescricao>) em.createQuery(""
	        		+ "SELECT b FROM BDDescricao b WHERE "
	        		+ "data_descricao=(SELECT MAX(b.dataDescricao) FROM BDDescricao b WHERE b.clientes.codigoCliente="+cliente.getCodigoCliente()+" AND b.servico.codigoServico= "+servico.getCodigoServico()+") "
	        		+ "AND b.clientes.codigoCliente="+cliente.getCodigoCliente()+" "
	        		+ "AND b.servico.codigoServico= "+servico.getCodigoServico()+"");
	        BDDescricao descricao = query.getSingleResult();
	        BDExameitem item= new BDExameitem(exame,servico,descricao);
	        em.getTransaction().begin();
	        em.persist(item);
	        em.getTransaction().commit();
	        carregarTabelaExame();
	        totalExame=totalExame+item.getDescricao().getValor();
	        String valorString = "R$"+String.format("%.2f",totalExame);
	        //ativação e desativação dos botões
	        btnExameCancelar.setDisable(false);
	        btnExameCancelarItem.setDisable(false);
	        btnExameFinalizar.setDisable(false);
	        comboBxExameCliente.setDisable(true);
	        comboBxExameColaborador.setDisable(true);
	        comboBxExameTipo.setDisable(true);
	        finalizar=false;
    	}
    	else {
    		Alert alert= new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Informação");
            //alert.setHeaderText("Cabeçalho");
            alert.setContentText("Atenção!!! Selecione todos os campos \n(Tipo, Cliente, Colaborador, Serviço)");
            alert.show();
    	}
    }
    
    public BDClientes acharCliente(){ //acha o objeto Colaborador pela String do seu nome
        String ClienteNome= comboBxExameCliente.getSelectionModel().getSelectedItem();
        BDClientes cliente = null;
        for( int i=0;i<listaClientes.size();i++){
                if(listaClientes.get(i).getNome().endsWith(ClienteNome)==true){
                    cliente=listaClientes.get(i);
                }
        }
        System.out.println(cliente.getNome());
        return cliente;
    }
    
   
    public BDClientecolaborador acharColaborador(){ //acha o objeto Colaborador pela String do seu nome
        String colaboradorNome= comboBxExameColaborador.getSelectionModel().getSelectedItem();
        BDClientecolaborador colaborador= null;
        for( int i=0;i<listaColaboradores.size();i++){
                if(listaColaboradores.get(i).getNome().endsWith(colaboradorNome)==true){
                    colaborador=listaColaboradores.get(i);
                }
        }
        return colaborador;
    }
    
    
    public BDServico acharServico(){ //acha o objeto Colaborador pela String do seu nome
        String servicoNome= comboBxExameServico.getSelectionModel().getSelectedItem();
        BDServico servico= null;
        for( int i=0;i<carregarServicoBox().size();i++){
                if(carregarServicoBox().get(i).getNome().endsWith(servicoNome)==true){
                    servico=carregarServicoBox().get(i);
                }
        }
        return servico;
    }
    
    public void carregarClienteBox(){
        TypedQuery<BDClientes> query = em.createQuery("SELECT b FROM BDClientes b",BDClientes.class);
        listaClientes= query.getResultList();
        
        String listFor= query.getResultList().toString();
        listFor=listFor.replace("[","");
        listFor=listFor.replace("]","");
        List<String> minhaLista= new ArrayList<String>(Arrays.asList(listFor.split(", "))); //transf lista de BDClientecolaborador em String
        
        ObservableList<String> oblCliente= FXCollections.observableArrayList(minhaLista);
        comboBxExameCliente.setItems(oblCliente); 
    }
    
    public void carregarColaboradorBox(){
	    TypedQuery<BDClientecolaborador> query = (TypedQuery<BDClientecolaborador>) em.createQuery("SELECT b FROM BDClientecolaborador b where b.clientes.codigoCliente = "+acharCliente().getCodigoCliente()+"");
	    listaColaboradores= query.getResultList();
	    String listFor= query.getResultList().toString();
	    listFor=listFor.replace("[","");
	    listFor=listFor.replace("]","");
	    List<String> minhaLista= new ArrayList<String>(Arrays.asList(listFor.split(", "))); //transf lista de BDClientecolaborador em String
	        
	    ObservableList<String> oblColaborador= FXCollections.observableArrayList(minhaLista);
	    comboBxExameColaborador.setItems(oblColaborador);  
    	
    }
    
    public List<BDServico> carregarServicoBox(){
        TypedQuery<BDServico> query = em.createQuery("SELECT b FROM BDServico b",BDServico.class);
        List<BDServico> servicos= query.getResultList();
        
        String listFor= query.getResultList().toString();
        listFor=listFor.replace("[","");
        listFor=listFor.replace("]","");
        List<String> minhaLista= new ArrayList<String>(Arrays.asList(listFor.split(", "))); //transf lista de BDClientecolaborador em String
        
        ObservableList<String> oblServico= FXCollections.observableArrayList(minhaLista);
        comboBxExameServico.setItems(oblServico);  
        return servicos;
    }
    
    public void carregarTipoBox(){
        TypedQuery<BDTipoExame> query = em.createQuery("SELECT b FROM BDTipoExame b",BDTipoExame.class);
        
        String listFor= query.getResultList().toString();
        listFor=listFor.replace("[","");
        listFor=listFor.replace("]","");
        List<String> minhaLista= new ArrayList<String>(Arrays.asList(listFor.split(", "))); //transf lista de BDClientecolaborador em String
        
        ObservableList<String> oblCliente= FXCollections.observableArrayList(minhaLista);
        comboBxExameTipo.setItems(oblCliente); 
    }
    
    public void carregarTabelaExame(){
        tabelaExameColaborador.setCellValueFactory(new Callback<CellDataFeatures<BDExameitem, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<BDExameitem, String> param) { 
                return new ReadOnlyObjectWrapper<>(param.getValue().getExame().getClientecolaborador().getNome()); 
            }
        }); 
        tabelaExameCliente.setCellValueFactory(new Callback<CellDataFeatures<BDExameitem, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<BDExameitem, String> param) { 
                return new ReadOnlyObjectWrapper<>(param.getValue().getExame().getClientes().getNome()); 
            }
        }); 
        tabelaExameServico.setCellValueFactory(new PropertyValueFactory<BDExameitem,BDServico>("servico"));
        
        TypedQuery<BDExameitem> query= (TypedQuery<BDExameitem>) em.createQuery("SELECT b FROM BDExameitem b WHERE b.exame.codigoExame = "+exame.getCodigoExame()+"");
        //query.setParameter("codigo", Exame.getCodigoExame());
        List<BDExameitem> listExame =query.getResultList();
        ObservableList<BDExameitem> oblExame= FXCollections.observableArrayList(listExame);
        tabelaExame.setItems(oblExame);
    }
}
