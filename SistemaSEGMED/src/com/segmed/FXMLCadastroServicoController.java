package com.segmed;

import static com.segmed.FabricaEntityManager.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;


public class FXMLCadastroServicoController implements Initializable {
    
    EntityManager em= createEm(); //método da classe FabricaEntityManager
    static int codigoServico;
    static Stage stage= new Stage(StageStyle.DECORATED); // Usar static p poder fechar a janela usando outro controlador
   
    @FXML
    private Button btnNovoServico;
    
    @FXML
    private Button btnSalvarServico;
    
    @FXML
    private Button btnFecharServico;
  

    @FXML
    private TextField txtServicoPesquisar;

    @FXML
    private TextField txtServicoNome;
    
    @FXML
    private TableView<BDServico> tabelaServico;
    
    @FXML
    private TableColumn<BDServico, String> tabelaServicoCodigo;
    
    @FXML
    private TableColumn<BDServico, String> tabelaServicoNome;
    
    @FXML
    private BorderPane fxmlCadastroServico;
    
    @FXML
    void btnServicoFechar(ActionEvent event) throws IOException {
        //IMPLEMENTAR ESSA FUNÇÃO
    }

    @FXML
    void btnServicoPesquisar(ActionEvent event) {
        String nome= txtServicoPesquisar.getText();
        pesquisarServicos(nome);
        if(nome==null){ //se a pesquisa for vazia retorna a lista completa
            carregarTabelaServicos();
        }
    }

    @FXML
    void btnServicoNovo(ActionEvent event) {
        if(txtServicoNome.getText().length()!=0){ //verifica se os campos estÃ£o preenchidos   
            String nome= txtServicoNome.getText();

            BDServico servico= new BDServico(nome);
            em.getTransaction().begin();
            em.persist(servico);
            em.getTransaction().commit();
            System.out.println("Codigo Servico: "+servico.getCodigoServico());
            
        }
        else{
            Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Informação");
            //alert.setHeaderText("Cabeçalho");
            alert.setContentText("Erro!!! Insira todos os campos");
            alert.show();
        }
        
        limparTexto();
        carregarTabelaServicos();
    }

    @FXML
    void btnServicoExcluir(ActionEvent event) {   //EXCLUIR
        BDServico ServicoExcluir= clicarServicoTabela();
        em.getTransaction().begin();
        em.remove(ServicoExcluir);
        em.getTransaction().commit();
        carregarTabelaServicos();
        //Atualiza a tabela de Servicos
        
    }

    @FXML
    void btnServicoAlterar(ActionEvent event) {
        //pega o objeto clicado e imprime seus dados nos campos de digitaÃ§Ã£o
        BDServico servicoAlterar= clicarServicoTabela();
        txtServicoNome.setText(servicoAlterar.getNome());
        
        btnSalvarServico.setDisable(false); //Habilita o botÃ£o de salvar
    }

    @FXML
    public void btnServicoSalvar(ActionEvent event) {
        //Novos dados inseridos para a alteraÃ§Ã£o  
        String nome= txtServicoNome.getText();
        
        BDServico servico= clicarServicoTabela();// obtem o objete a ser alterado
        //Altera os atributos do objeto
        servico.setNome(nome);
        
        em.getTransaction().begin();       
        em.merge(servico);
        em.getTransaction().commit();
        btnSalvarServico.setDisable(true); //Desabilita o botÃ£o de salvar
        limparTexto();
        carregarTabelaServicos();
        
    }
    
    @FXML
    void btnServicoDescricao(ActionEvent event) throws IOException {
        Parent root1 = FXMLLoader.load(getClass().getResource("FXMLCadastroDescricao.fxml"));
        Scene scene= new Scene(root1);
        //Stage stage= new Stage(StageStyle.DECORATED);
        stage.setTitle("Descrição");
        stage.setScene(scene);
        stage.show();
        
    }
    
    @FXML
    public BDServico clicarServicoTabela() {
        BDServico servicoSelecionado =tabelaServico.getSelectionModel().getSelectedItem(); //seleciona o objeto clicado na tabela
        codigoServico = servicoSelecionado.getCodigoServico();
        return servicoSelecionado;
    }
    
    public static int passarServico() {
		return codigoServico;
    }
    
   
    
    @Override
    public void initialize(URL location, ResourceBundle resources) { //Funciona como o mÃ©todo construtor
        carregarTabelaServicos();
        btnSalvarServico.setDisable(true);
    }
    
    public void carregarTabelaServicos(){
        tabelaServico.getColumns().get(0).setVisible(false); //resolve problema de atualizar a tabela na opÃ§Ã£o ->
        tabelaServico.getColumns().get(0).setVisible(true);//-> alterar Servico
        tabelaServicoCodigo.setCellValueFactory(new PropertyValueFactory<BDServico,String>("codigoServico"));
        tabelaServicoNome.setCellValueFactory(new PropertyValueFactory<BDServico,String>("nome"));
        
        TypedQuery<BDServico> query= em.createQuery("SELECT b FROM BDServico b",BDServico.class);
        List<BDServico> listServicos= query.getResultList();
        ObservableList<BDServico> oblServicos= FXCollections.observableArrayList(listServicos);
        tabelaServico.setItems(oblServicos);
    }
    
    public void pesquisarServicos(String nome1){
        String nome= nome1;
        TypedQuery<BDServico> createQuery = (TypedQuery<BDServico>) em.createQuery("SELECT b FROM BDServico b WHERE b.nome like '"+nome+"%'");
		TypedQuery<BDServico> query= createQuery; //pesquisa auto-completa o nome
        tabelaServicoCodigo.setCellValueFactory(new PropertyValueFactory<BDServico,String>("codigoServico"));
        tabelaServicoNome.setCellValueFactory(new PropertyValueFactory<BDServico,String>("nome"));
        
        List<BDServico> listServicos= query.getResultList();
        ObservableList<BDServico> oblServicos= FXCollections.observableArrayList(listServicos);
        tabelaServico.setItems(oblServicos);
        txtServicoPesquisar.clear();
    }
    
    public void limparTexto(){
        txtServicoNome.clear();
    }
    
    public static void stage() {
    	
    }
}
