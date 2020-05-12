package com.segmed;

import static com.segmed.FabricaEntityManager.createEm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class FXMLCadastroDescricaoController {
	EntityManager em= createEm();
	
	@FXML
    private ComboBox<String> comboBxDescricaoCliente;

    @FXML
    private ComboBox<String> comboBxDescricaoServico;
    
	@FXML
    private Button btnDescricaoSalvar;

    @FXML
    private Button btnDescricaoCancelar;

    @FXML
    private Button btnDescricaoCorrigir;

    @FXML
    private TextArea txtServicoDescricao;

    @FXML
    private TextField txtServicoValor;
    
    @FXML
    private Button btnDescricaoAtualizar;

    
    BDDescricao descricao;
    
    @FXML
    public void initialize() {
    	carregarClienteBox();
    	carregarServicoBox();
    	btnDescricaoSalvar.setDisable(true);
    	btnDescricaoCancelar.setDisable(true);
    	btnDescricaoCorrigir.setDisable(true);
    }

    @FXML
    void btnCorrigirDescricao(ActionEvent event) throws ParseException {
    	boolean t = false;
    	BDClientes cliente = acharCliente();
    	BDServico servico = acharServico();
    	TypedQuery<BDDescricao> query = null;
    	try {
    		query = (TypedQuery<BDDescricao>) em.createQuery("SELECT b FROM BDDescricao b WHERE data_descricao=(SELECT MAX(b.dataDescricao) FROM BDDescricao b WHERE b.clientes.codigoCliente="+cliente.getCodigoCliente()+" AND b.servico.codigoServico= "+servico.getCodigoServico()+")");
    		descricao = query.getSingleResult();
    	}
    	catch(NoResultException nre) {
    		System.out.println("criando entidade");
    		t= true;
    	}
    	if (t==true) {
    		SimpleDateFormat dt= new SimpleDateFormat("dd/MM/yyyy");
    	    Date data = dt.parse("17/01/2018");
    	    descricao = new BDDescricao(cliente,servico,data);
    	    descricao.setDescricaotexto("N/A");
    	    descricao.setValor(0);
    	    em.getTransaction().begin();
            em.persist(descricao);
            em.getTransaction().commit();
    	}
    	txtServicoDescricao.setText(descricao.getDescricaotexto());
    	txtServicoValor.setText(descricao.valorUn());
    	
    	//btnDescricaoSalvar.setDisable(true);
    	//btnDescricaoCancelar.setDisable(true);
    	//btnDescricaoCorrigir.setDisable(true);

    }
    
    @FXML
    void btnAtualizarDescricao(ActionEvent event) throws ParseException {
    	try {
	    	BDClientes cliente = acharCliente();
	    	BDServico servico = acharServico();
	    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");  
	    	LocalDateTime hoje = LocalDateTime.now();  
	    	String dataHoje= dtf.format(hoje);  
	    	
	    	//String dataString= "10/02/2018";
	    	SimpleDateFormat dt= new SimpleDateFormat("dd/MM/yyyy");
		    Date data = dt.parse(dataHoje);
		    System.out.println(dataHoje);
		    try {
		    	TypedQuery<BDDescricao> query = (TypedQuery<BDDescricao>) em.createQuery("SELECT b FROM BDDescricao b WHERE data_descricao=(SELECT MAX(b.dataDescricao) FROM BDDescricao b WHERE b.clientes.codigoCliente="+cliente.getCodigoCliente()+" AND b.servico.codigoServico= "+servico.getCodigoServico()+")"
		    																				+"AND b.clientes.codigoCliente= "+cliente.getCodigoCliente()+" AND b.servico.codigoServico= "+servico.getCodigoServico()+"");
		    	descricao = query.getSingleResult(); //comando para forçar o erro acontecer
		    	String dataDescricao = dt.format(descricao.getDataDescricao());
		    	System.out.println(dt.format(descricao.getDataDescricao()));
		    	if(dataDescricao.equals(dataHoje)) { //forma de comprar strings
		    		Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
		            alert.setTitle("Informação");
		            //alert.setHeaderText("Cabeçalho");
		            alert.setContentText("Erro!!! Descrição do serviço já foi atualizada \nna data de hoje!");
		            alert.show();
		            System.out.println("negativo "+dataDescricao);
		    	}  	
		    	else {
		    		txtServicoDescricao.setText(descricao.getDescricaotexto());
		    	    txtServicoValor.setText(String.valueOf(descricao.getValor()));
		    	    btnDescricaoSalvar.setDisable(false);
		        	btnDescricaoCancelar.setDisable(false);
		        	System.out.println(descricao.getDescricaotexto());
		        	System.out.println("atualizando cadastro existente...");
		    	}
		    	
		    	
		    	
		    }
		    catch(NoResultException nre) { //Com o erro de q nenhum objeto com a data foi encontrado é criado um objeto com a data
		    	descricao = new BDDescricao(cliente,servico,data);
			    descricao.setDescricaotexto("N/A");
			    descricao.setValor(0);
		    	em.getTransaction().begin();
		        em.persist(descricao);
		        em.getTransaction().commit();
		        txtServicoDescricao.setText(descricao.getDescricaotexto());
		    	txtServicoValor.setText(descricao.valorUn());
		    	btnDescricaoSalvar.setDisable(false);
		    	btnDescricaoCancelar.setDisable(false);
		    	btnDescricaoAtualizar.setDisable(true);
		        System.out.println("Criando descrição...");	
		    }
    	}
    	catch(NullPointerException e) {
    		Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Informação");
    		alert.setContentText("Erro!!! Selecione um cliente e um serviço!");
            alert.show();
    	}
	}
    
    @FXML
    void btnSalvarDescricao(ActionEvent event) throws ParseException {
    	String texto = txtServicoDescricao.getText();
		String k= txtServicoValor.getText().replaceAll("\\.",""); //converte a formataÃ§Ã£o de numero BR para USA
        String v= k.replaceAll(",",".");
        float valor= Float.parseFloat(v);
        
        BDDescricao novaDescricao = new BDDescricao();
        novaDescricao.setClientes(descricao.getClientes());
        novaDescricao.setServico(descricao.getServico());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");  
        SimpleDateFormat dt= new SimpleDateFormat("dd/MM/yyyy");
    	LocalDateTime hoje = LocalDateTime.now();  
    	String dataHoje= dtf.format(hoje);  
    	Date data = dt.parse(dataHoje);
        novaDescricao.setDataDescricao(data);
        novaDescricao.setDescricaotexto(texto);
        novaDescricao.setValor(valor);
        
        em.getTransaction().begin();
        em.merge(novaDescricao);
        em.getTransaction().commit();
        
        txtServicoDescricao.clear();
        txtServicoValor.clear();
        
        btnDescricaoSalvar.setDisable(true);
    	btnDescricaoCancelar.setDisable(true);
    	btnDescricaoAtualizar.setDisable(false);
    	//btnDescricaoCorrigir.setDisable(true);
    }

    @FXML
    void btnCancelarDescricao(ActionEvent event) {
        txtServicoDescricao.clear();
        txtServicoValor.clear();
        btnDescricaoSalvar.setDisable(true);
    	btnDescricaoCancelar.setDisable(true);
    	btnDescricaoAtualizar.setDisable(false);

    }

    public List<BDClientes> carregarClienteBox(){
        TypedQuery<BDClientes> query = em.createQuery("SELECT b FROM BDClientes b",BDClientes.class);
        List<BDClientes> listaClientes= query.getResultList();
        
        String listFor= query.getResultList().toString();
        listFor=listFor.replace("[","");
        listFor=listFor.replace("]","");
        List<String> minhaLista= new ArrayList<String>(Arrays.asList(listFor.split(", "))); //transf lista de BDClientecolaborador em String
        
        ObservableList<String> oblCliente= FXCollections.observableArrayList(minhaLista);
        comboBxDescricaoCliente.setItems(oblCliente); 
        return listaClientes;
    }
    
    public List<BDServico> carregarServicoBox(){
        TypedQuery<BDServico> query = em.createQuery("SELECT b FROM BDServico b",BDServico.class);
        List<BDServico> listaServicos= query.getResultList();
        
        String listFor= query.getResultList().toString();
        listFor=listFor.replace("[","");
        listFor=listFor.replace("]","");
        List<String> minhaLista= new ArrayList<String>(Arrays.asList(listFor.split(", "))); //transf lista de BDClientecolaborador em String
        
        ObservableList<String> oblServico= FXCollections.observableArrayList(minhaLista);
        comboBxDescricaoServico.setItems(oblServico);  
        return listaServicos;
    }
    
    public BDClientes acharCliente(){ //acha o objeto Colaborador pela String do seu nome
        String ClienteNome= comboBxDescricaoCliente.getSelectionModel().getSelectedItem();
        BDClientes cliente = null;
        for( int i=0;i<carregarClienteBox().size();i++){
                if(carregarClienteBox().get(i).getNome().endsWith(ClienteNome)==true){
                    cliente=carregarClienteBox().get(i);
                }
        }
        return cliente;
    }
    
    public BDServico acharServico(){ //acha o objeto Colaborador pela String do seu nome
        String servicoNome= comboBxDescricaoServico.getSelectionModel().getSelectedItem();
        BDServico servico= null;
        for( int i=0;i<carregarServicoBox().size();i++){
                if(carregarServicoBox().get(i).getNome().endsWith(servicoNome)==true){
                    servico=carregarServicoBox().get(i);
                }
        }
        return servico;
    }
    
    @FXML
    void tfValor(KeyEvent event) {
    	MaskFieldUtil.monetaryField(txtServicoValor);  
    }

}
