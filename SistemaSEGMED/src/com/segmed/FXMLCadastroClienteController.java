/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.segmed;

import static com.segmed.FabricaEntityManager.createEm;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * FXML Controller class
 *
 * @author julia
 */
public class FXMLCadastroClienteController implements Initializable {
    EntityManager em= createEm(); //método da classe FabricaEntityManager
    
    @FXML
    private BorderPane fxmlCadastroCliente;
    
    @FXML
    private TableView<BDClientes> tabelaCliente;
    @FXML
    private TableColumn<BDClientes, String> tabelaClienteCNPJ;
    @FXML
    private TableColumn<BDClientes, String> tabelaClienteNome;
    @FXML
    private TableColumn<BDClientes, String> tabelaClienteEndereco;
    @FXML
    private TableColumn<BDClientes, String> tabelaClienteTelefone;
    
    @FXML
    private TextField txtClienteNome;

    @FXML
    private TextField txtClienteEndereco;

    @FXML
    private TextField txtClienteCidade;

    @FXML
    private TextField txtClienteUF;

    @FXML
    private TextField txtClienteCEP;

    @FXML
    private TextField txtClienteTelefone;

    @FXML
    private TextField txtClienteBairro;

    @FXML
    private Button btnNovoCliente;

    @FXML
    private Button btnSalvarCliente;

    @FXML
    private TextField txtClienteEmail;

    @FXML
    private TextField txtClienteCNPJ;

    @FXML
    private TextField txtClienteCelular;
    
    @FXML
    private TextField txtClientePesquisar;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTabelaClientees();
        btnSalvarCliente.setDisable(true);
    }    

    @FXML
    private void btnClientePesquisar(ActionEvent event) {
        String nome= txtClientePesquisar.getText();
        pesquisarClientees(nome);
        if(nome==null){ //se a pesquisa for vazia retorna a lista completa
            carregarTabelaClientees();
        }
    }

    @FXML
    public BDClientes clicarClienteTabela() {
        BDClientes ClienteSelecionado =tabelaCliente.getSelectionModel().getSelectedItem(); //seleciona o objeto clicado na tabela
        return ClienteSelecionado;
    }

    @FXML
    private void btnClienteNovo(ActionEvent event) {
        if(txtClienteNome.getText().length()!=0 && txtClienteCNPJ.getText().length()!=0 
                && txtClienteTelefone.getText().length()!=0 && txtClienteEndereco.getText().length()!=0){ //verifica se os campos estão preenchidos   
            
            String nome= txtClienteNome.getText();
            String endereco= txtClienteEndereco.getText();
            String cep= txtClienteCEP.getText();
            String uf= txtClienteUF.getText();
            String telefone= txtClienteTelefone.getText();
            String cidade= txtClienteCidade.getText();
            String bairro= txtClienteBairro.getText();
            String cnpj= txtClienteCNPJ.getText();
            String email= txtClienteEmail.getText();
            String celular= txtClienteCelular.getText();

            BDClientes Cliente= new BDClientes(nome,bairro,cidade,endereco,uf,cep,telefone,celular,email,cnpj);
            em.getTransaction().begin();
            em.persist(Cliente);
            em.getTransaction().commit();
        }
        else{
            Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Informa��o");
            //alert.setHeaderText("Cabeçalho");
            alert.setContentText("Erro!!! Insira todos os campos");
            alert.show();
        }
        limparTexto();
        carregarTabelaClientees();
    }

    @FXML
    private void btnClienteFechar(ActionEvent event) {
        //Implementar essa parte
        //Implementar MESMO!!!!
    }

    @FXML
    private void btnClienteExcluir(ActionEvent event) {
        BDClientes ClienteExcluir= em.find(BDClientes.class,clicarClienteTabela().getCodigoCliente());
        em.getTransaction().begin();
        em.remove(ClienteExcluir);
        em.getTransaction().commit();
        carregarTabelaClientees();
    }

    @FXML
    private void btnClienteAlterar(ActionEvent event) {
        //pega o objeto clicado e imprime seus dados nos campos de digitação
        BDClientes ClienteAlterar= clicarClienteTabela();
        txtClienteNome.setText(ClienteAlterar.getNome());
        txtClienteEndereco.setText(ClienteAlterar.getEndereco());
        txtClienteCEP.setText(ClienteAlterar.getCep());
        txtClienteUF.setText(ClienteAlterar.getUf());
        txtClienteTelefone.setText(ClienteAlterar.getTelefone());
        txtClienteCidade.setText(ClienteAlterar.getCidade());
        txtClienteBairro.setText(ClienteAlterar.getBairro());
        txtClienteEmail.setText(ClienteAlterar.getEmail());
        txtClienteCNPJ.setText(ClienteAlterar.getCnpj());
        txtClienteCelular.setText(ClienteAlterar.getCelular());
        
        btnSalvarCliente.setDisable(false); //Habilita o botão de salvar
    }

    @FXML
    private void btnClienteSalvar(ActionEvent event) {
        //Novos dados inseridos para a alteração
        
        String nome= txtClienteNome.getText();
        String endereco= txtClienteEndereco.getText();
        String cep= txtClienteCEP.getText();
        String uf= txtClienteUF.getText();
        String telefone= txtClienteTelefone.getText();
        String cidade= txtClienteCidade.getText();
        String bairro= txtClienteBairro.getText(); 
        String cnpj= txtClienteCNPJ.getText();
        String email= txtClienteEmail.getText();
        String celular= txtClienteCelular.getText();
        
        BDClientes Cliente= clicarClienteTabela();// obtem o objete a ser alterado
        //Altera os atributos do objeto
        Cliente.setNome(nome);
        Cliente.setEndereco(endereco);
        Cliente.setCep(cep);
        Cliente.setUf(uf);
        Cliente.setTelefone(telefone);
        Cliente.setCidade(cidade);
        Cliente.setBairro(bairro);
        Cliente.setEmail(email);
        Cliente.setCnpj(cnpj);
        Cliente.setCelular(celular);
        
        em.getTransaction().begin();       
        em.merge(Cliente);
        em.getTransaction().commit();
        btnSalvarCliente.setDisable(true); //Desabilita o botão de salvar
        limparTexto();
        carregarTabelaClientees();
    }
    
    public void carregarTabelaClientees(){
        tabelaCliente.getColumns().get(0).setVisible(false); //resolve problema de atualizar a tabela na opção ->
        tabelaCliente.getColumns().get(0).setVisible(true);//-> alterar Cliente
        tabelaClienteCNPJ.setCellValueFactory(new PropertyValueFactory<BDClientes,String>("cnpj"));
        tabelaClienteNome.setCellValueFactory(new PropertyValueFactory<BDClientes,String>("nome"));
        tabelaClienteEndereco.setCellValueFactory(new PropertyValueFactory<BDClientes,String>("endereco"));
        tabelaClienteTelefone.setCellValueFactory(new PropertyValueFactory<BDClientes,String>("telefone"));
        TypedQuery<BDClientes> query= em.createQuery("SELECT b FROM BDClientes b",BDClientes.class);
        List<BDClientes> listClientees= query.getResultList();
        ObservableList<BDClientes> oblClientees= FXCollections.observableArrayList(listClientees);
        tabelaCliente.setItems(oblClientees);
    }
    
    public void pesquisarClientees(String nome){
        TypedQuery<BDClientes> query= (TypedQuery<BDClientes>) em.createQuery("SELECT b FROM BDClientes b WHERE b.nome like '"+nome+"%'"); //pesquisa auto-completa o nome
        tabelaClienteCNPJ.setCellValueFactory(new PropertyValueFactory<BDClientes,String>("cnpj"));
        tabelaClienteNome.setCellValueFactory(new PropertyValueFactory<BDClientes,String>("nome"));
        tabelaClienteEndereco.setCellValueFactory(new PropertyValueFactory<BDClientes,String>("endereco"));
        tabelaClienteTelefone.setCellValueFactory(new PropertyValueFactory<BDClientes,String>("telefone"));
        
        List<BDClientes> listClientees= query.getResultList();
        ObservableList<BDClientes> oblClientees= FXCollections.observableArrayList(listClientees);
        tabelaCliente.setItems(oblClientees);
        txtClientePesquisar.clear();
    }
    
    @FXML
    void tfClienteCelular(KeyEvent event) {
    	TextFieldFormatter tff= new TextFieldFormatter(); //classe baixada do youtube, mto importante
        tff.setMask("(##)#####-####");
        tff.setCaracteresValidos("0123456789");
        tff.setTf(txtClienteCelular);
        tff.formatter(); 
    }

    @FXML
    void tfClienteTelefone(KeyEvent event) {
    	TextFieldFormatter tff= new TextFieldFormatter(); //classe baixada do youtube, mto importante
        tff.setMask("(##)####-####");
        tff.setCaracteresValidos("0123456789");
        tff.setTf(txtClienteTelefone);
        tff.formatter(); 

    }
    
    @FXML
    void tfClienteCNPJ(KeyEvent event) {
    	TextFieldFormatter tff= new TextFieldFormatter(); //classe baixada do youtube, mto importante
        tff.setMask("##.###.###/####-##");
        tff.setCaracteresValidos("0123456789");
        tff.setTf(txtClienteCNPJ);
        tff.formatter(); 
    }
    
    public void limparTexto(){
        txtClienteNome.clear();
        txtClienteEndereco.clear();
        txtClienteCEP.clear();
        txtClienteUF.clear();
        txtClienteTelefone.clear();
        txtClienteCidade.clear();
        txtClienteBairro.clear();
        txtClienteCNPJ.clear();
        txtClienteCelular.clear();
        txtClienteEmail.clear();
    }
    
}
