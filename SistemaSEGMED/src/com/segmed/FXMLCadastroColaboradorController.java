/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.segmed;

import static com.segmed.FabricaEntityManager.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * FXML Controller class
 *
 * @author julia
 */
public class FXMLCadastroColaboradorController implements Initializable {
    
    EntityManager em= createEm();
    DateFormat formatar = new SimpleDateFormat("dd/MM/yyyy");
    
    @FXML
    private Button btnSalvarColaborador;
    
    @FXML
    private TextField txtColaboradorPesquisa;

    @FXML
    private TextField txtColaboradorCPF;

    @FXML
    private TextField txtColaboradorRG;

    @FXML
    private TextField txtColaboradorNascimento;

    @FXML
    private TextField txtColaboradorTelefone;

    @FXML
    private TextField txtColaboradorFuncao;
    
    @FXML
    private TextField txtColaboradorNome;
    
    @FXML
    private ComboBox<String> comboBxtColaboradorCliente;
    
    @FXML
    private TableView<BDClientecolaborador> tabelaColaborador;

    @FXML
    private TableColumn<BDClientecolaborador, String> tabelaColaboradorNome;

    @FXML
    private TableColumn<BDClientecolaborador, BDClientes> tabelaColaboradorCliente;

    @FXML
    private TableColumn<BDClientecolaborador, String> tabelaColaboradorCPF;

    @FXML
    private TableColumn<BDClientecolaborador, String> tabelaColaboradorTelefone;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carregarClientesBox();
        carregarTabelaColaborador();  
        btnSalvarColaborador.setDisable(true);  
    } 
    
    @FXML
    void btnColaboradorPesquisar(ActionEvent event) {
        String nome= txtColaboradorPesquisa.getText();
        pesquisarNomeColaborador(nome);
        if(nome==null){
            carregarTabelaColaborador();
        }
    }

    @FXML
    void btnColaboradorNovo(ActionEvent event) throws ParseException {
        if(txtColaboradorNome.getText().length()!=0 && txtColaboradorFuncao.getText().length()!=0 
                && txtColaboradorTelefone.getText().length()!=0 && comboBxtColaboradorCliente.getSelectionModel().isEmpty()==false){
        
            //n√£o deixar o Entity manager aberto sem transa√ß√£o
        	String nome= txtColaboradorNome.getText();
            String cpf= txtColaboradorCPF.getText();
            String rg= txtColaboradorRG.getText();
            String telefone= txtColaboradorTelefone.getText();
            String funcao= txtColaboradorFuncao.getText();
            DateFormat formatar = new SimpleDateFormat("dd/MM/yyyy");
            Date nascimento= formatar.parse(txtColaboradorNascimento.getText());
            BDClientes cliente = null;
            String clienteNome= selecionarItemComboBoxCliente();
            //Obter o objeto Fornecedor correto da lista, atraves da string do nome
            for( BDClientes t: carregarClientesBox()){
                if(t.getNome().equals(clienteNome)){
                    cliente=t;
                }

            }
            /******************************************************************************/
            //BDClientes cliente= em.find(BDClientes.class, f.getCodigoCliente());//para pesquisar o fornecedor no BD preciso do codigo
            BDClientecolaborador colaborador= new BDClientecolaborador(cliente,nome,cpf,rg,nascimento,funcao,telefone);
            em.getTransaction().begin();
            em.persist(colaborador);
            em.getTransaction().commit();   
        }
        else{
            Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("InformaÁ„o");
            //alert.setHeaderText("CabeÁalho");
            alert.setContentText("Erro!!! Insira todos os campos");
            alert.show();
        }
        limparTexto();
        carregarTabelaColaborador();
    }

    @FXML
    void btnColaboradorFechar(ActionEvent event) {
        
    }

    @FXML
    void btnColaboradorAlterar(ActionEvent event) {
        BDClientecolaborador colaboradorAlterar = clicarColaboradorTabela();
        txtColaboradorNome.setText(colaboradorAlterar.getNome());
        txtColaboradorCPF.setText(colaboradorAlterar.getCpf()); //converte formata√ß√£o USA para BR
        txtColaboradorRG.setText(colaboradorAlterar.getRg());
        txtColaboradorFuncao.setText(colaboradorAlterar.getFuncao());
        txtColaboradorTelefone.setText(colaboradorAlterar.getTelefone());
        txtColaboradorNascimento.setText(formatar.format(colaboradorAlterar.getNascimento()));
        btnSalvarColaborador.setDisable(false);
    }
    
    @FXML
    private void btnColaboradorSalvar(ActionEvent event) throws ParseException {
    	String nome= txtColaboradorNome.getText();
        String cpf= txtColaboradorCPF.getText();
        String rg= txtColaboradorRG.getText();
        String telefone= txtColaboradorTelefone.getText();
        String funcao= txtColaboradorFuncao.getText();
        Date nascimento= formatar.parse(txtColaboradorNascimento.getText());
        BDClientes cliente = null;
        String clienteNome= selecionarItemComboBoxCliente();
        //Obter o objeto Fornecedor correto da lista, atraves da string do nome
        for( BDClientes t: carregarClientesBox()){
            if(t.getNome().equals(clienteNome)){
                cliente=t;
            }
        }
        BDClientecolaborador colaborador = clicarColaboradorTabela();
        
        colaborador.setNome(nome);
        colaborador.setCpf(cpf);
        colaborador.setRg(rg);
        colaborador.setTelefone(telefone);
        colaborador.setFuncao(funcao);
        colaborador.setNascimento(nascimento);
        colaborador.setClientes(cliente);
        
        em.getTransaction().begin();       
        em.merge(colaborador);
        em.getTransaction().commit();
        
        btnSalvarColaborador.setDisable(true);
        limparTexto();
        carregarTabelaColaborador();
    }
    
    @FXML
    void btnColaboradorExcluir(ActionEvent event) {
        BDClientecolaborador fornecedorExcluir= em.find(BDClientecolaborador.class,clicarColaboradorTabela().getCodigoColaborador());
        em.getTransaction().begin();
        em.remove(fornecedorExcluir);
        em.getTransaction().commit();
        carregarTabelaColaborador();
    }

    @FXML
    public String selecionarItemComboBoxCliente() {
        String cliente = comboBxtColaboradorCliente.getSelectionModel().getSelectedItem();
        return cliente;
    }
    
    @FXML
    private BDClientecolaborador clicarColaboradorTabela() {
        BDClientecolaborador ColaboradorSelecionado= tabelaColaborador.getSelectionModel().getSelectedItem();
        return ColaboradorSelecionado;
    }
    
    public List<BDClientes> carregarClientesBox(){
        TypedQuery<BDClientes> query = em.createQuery("SELECT b FROM BDClientes b",BDClientes.class);
        List<BDClientes> clientes= query.getResultList();
        /******************************Gambiarra para n√£o dar ERRO!!!****************************************/
        String listFor= query.getResultList().toString();
        System.out.println(listFor);
        listFor=listFor.replace("[","");
        listFor=listFor.replace("]","");
        List<String> minhaLista= new ArrayList<String>(Arrays.asList(listFor.split(", "))); //transf lista de BDProdutos em String
        /***************************************************************************************************/
        ObservableList<String> oblClientes= FXCollections.observableArrayList(minhaLista);
        comboBxtColaboradorCliente.setItems(oblClientes);  
        return clientes;
    } 
    
    public void carregarTabelaColaborador(){
        tabelaColaborador.getColumns().get(0).setVisible(false); //resolve problema de atualizar a tabela na opÁ„o -
        tabelaColaborador.getColumns().get(0).setVisible(true);//- alterar 
        tabelaColaboradorNome.setCellValueFactory(new PropertyValueFactory<BDClientecolaborador,String>("nome"));
        tabelaColaboradorCliente.setCellValueFactory(new PropertyValueFactory<BDClientecolaborador,BDClientes>("clientes"));
        tabelaColaboradorCPF.setCellValueFactory(new PropertyValueFactory<BDClientecolaborador,String>("cpf"));
        tabelaColaboradorTelefone.setCellValueFactory(new PropertyValueFactory<BDClientecolaborador,String>("telefone"));
        
        TypedQuery<BDClientecolaborador> query= em.createQuery("SELECT b FROM BDClientecolaborador b",BDClientecolaborador.class);
        List<BDClientecolaborador> listColaboradors= query.getResultList();
        ObservableList<BDClientecolaborador> oblColaboradors= FXCollections.observableArrayList(listColaboradors);
        tabelaColaborador.setItems(oblColaboradors);
    }
    
    public void pesquisarNomeColaborador(String nome){
        TypedQuery<BDClientecolaborador> query= (TypedQuery<BDClientecolaborador>) em.createQuery("SELECT b FROM BDClientecolaborador b WHERE b.nome like '"+nome+"%'"); //pesquisa auto-completa o nome
        tabelaColaboradorNome.setCellValueFactory(new PropertyValueFactory<BDClientecolaborador,String>("nome"));
        tabelaColaboradorCliente.setCellValueFactory(new PropertyValueFactory<BDClientecolaborador,BDClientes>("clientes"));
        tabelaColaboradorCPF.setCellValueFactory(new PropertyValueFactory<BDClientecolaborador,String>("cpf"));
        tabelaColaboradorTelefone.setCellValueFactory(new PropertyValueFactory<BDClientecolaborador,String>("telefone"));
        
        List<BDClientecolaborador> listColaboradors= query.getResultList();
        ObservableList<BDClientecolaborador> oblColaborador= FXCollections.observableArrayList(listColaboradors);
        tabelaColaborador.setItems(oblColaborador);
        txtColaboradorPesquisa.clear();
    }
    
    @FXML
    void tfColaboradorCPF(KeyEvent event) {
    	TextFieldFormatter tff= new TextFieldFormatter(); //classe baixada do youtube, mto importante
        tff.setMask("###.###.###-##");
        tff.setCaracteresValidos("0123456789");
        tff.setTf(txtColaboradorCPF);
        tff.formatter(); 
    }

    @FXML
    void tfColaboradorNascimento(KeyEvent event) {
    	TextFieldFormatter tff= new TextFieldFormatter(); //classe baixada do youtube, mto importante
        tff.setMask("##/##/####");
        tff.setCaracteresValidos("0123456789");
        tff.setTf(txtColaboradorNascimento);
        tff.formatter(); 
    }

    @FXML
    void tfColaboradorRG(KeyEvent event) {
    	TextFieldFormatter tff= new TextFieldFormatter(); //classe baixada do youtube, mto importante
        tff.setMask("##.###.###-##");
        tff.setCaracteresValidos("0123456789");
        tff.setTf(txtColaboradorRG);
        tff.formatter(); 
    }

    @FXML
    void tfColaboradorTelefone(KeyEvent event) {
    	TextFieldFormatter tff= new TextFieldFormatter(); //classe baixada do youtube, mto importante
        tff.setMask("(##)####-####");
        tff.setCaracteresValidos("0123456789");
        tff.setTf(txtColaboradorTelefone);
        tff.formatter(); 
    }

    public void limparTexto(){
        txtColaboradorNome.clear();
        txtColaboradorCPF.clear();
        txtColaboradorRG.clear();
        txtColaboradorNascimento.clear();
        txtColaboradorFuncao.clear();
        txtColaboradorTelefone.clear();
    }

}
