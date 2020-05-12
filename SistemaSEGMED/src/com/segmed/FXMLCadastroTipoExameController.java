package com.segmed;

import static com.segmed.FabricaEntityManager.createEm;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class FXMLCadastroTipoExameController {
	EntityManager em= createEm(); //método da classe FabricaEntityManager
	
	@FXML
    private TextField txtTipoPesquisar;

    @FXML
    private TableView<BDTipoExame> tabelaTipo;

    @FXML
    private TableColumn<BDTipoExame, Integer> tabelaTipoCodigo;

    @FXML
    private TableColumn<BDTipoExame, String> tabelaTipoNome;

    @FXML
    private TextField txtTipoNome;

    @FXML
    private Button btnNovoTipo;

    @FXML
    private Button btnSalvarServico;
    
    public void initialize() {
    	carregarTabelaTipo();
    }
    
    @FXML
    void btnTipoNovo(ActionEvent event) {
    	if(txtTipoNome.getText().length()!=0){ //verifica se os campos estÃ£o preenchidos   
            String nome= txtTipoNome.getText();

            BDTipoExame tipo= new BDTipoExame(nome);
            em.getTransaction().begin();
            em.persist(tipo);
            em.getTransaction().commit();
            System.out.println("Codigo Servico: "+tipo.getCodigoTipo());
            
        }
        else{
            Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Informação");
            //alert.setHeaderText("Cabeçalho");
            alert.setContentText("Erro!!! Insira um nome");
            alert.show();
        }
    }

    @FXML
    void btnServicoPesquisar(ActionEvent event) {
    	String nome= txtTipoPesquisar.getText();
        pesquisarTipo(nome);
        if(nome==null){ //se a pesquisa for vazia retorna a lista completa
            carregarTabelaTipo();
        }
    }

    @FXML
    void btnTipoAlterar(ActionEvent event) {
    	BDTipoExame servicoAlterar= clicarTipoTabela();
        txtTipoNome.setText(servicoAlterar.getTipo());
        btnSalvarServico.setDisable(false); //Habilita o botÃ£o de salvar
    }

    @FXML
    void btnTipoExcluir(ActionEvent event) {
    	BDTipoExame tipoExcluir= clicarTipoTabela();
        em.getTransaction().begin();
        em.remove(tipoExcluir);
        em.getTransaction().commit();
        carregarTabelaTipo();
    }

    @FXML
    void btnTipoSalvar(ActionEvent event) {
    	String nome= txtTipoNome.getText();
        BDTipoExame tipo= clicarTipoTabela();// obtem o objete a ser alterado
        //Altera os atributos do objeto
        tipo.setTipo(nome);
        em.getTransaction().begin();       
        em.merge(tipo);
        em.getTransaction().commit();
        btnSalvarServico.setDisable(true); //Desabilita o botÃ£o de salvar
        txtTipoNome.clear();
        carregarTabelaTipo();

    }

    @FXML
    BDTipoExame clicarTipoTabela() {
    	BDTipoExame tipoSelecionado =tabelaTipo.getSelectionModel().getSelectedItem(); //seleciona o objeto clicado na tabela
        int codigoTipo = tipoSelecionado.getCodigoTipo();
        return tipoSelecionado;
    }
    
    public void carregarTabelaTipo(){
        tabelaTipo.getColumns().get(0).setVisible(false); //resolve problema de atualizar a tabela na opÃ§Ã£o ->
        tabelaTipo.getColumns().get(0).setVisible(true);//-> alterar Servico
        tabelaTipoCodigo.setCellValueFactory(new PropertyValueFactory<BDTipoExame,Integer>("codigoTipo"));
        tabelaTipoNome.setCellValueFactory(new PropertyValueFactory<BDTipoExame,String>("tipo"));
        
        TypedQuery<BDTipoExame> query= em.createQuery("SELECT b FROM BDTipoExame b",BDTipoExame.class);
        List<BDTipoExame> listTipo= query.getResultList();
        ObservableList<BDTipoExame> oblTipo= FXCollections.observableArrayList(listTipo);
        tabelaTipo.setItems(oblTipo);
    }
    
    public void pesquisarTipo(String nome1){
        String nome= nome1;
        TypedQuery<BDTipoExame> createQuery = (TypedQuery<BDTipoExame>) em.createQuery("SELECT b FROM BDTipoExame b WHERE b.tipo like '"+nome+"%'");
		TypedQuery<BDTipoExame> query= createQuery; //pesquisa auto-completa o nome
        tabelaTipoCodigo.setCellValueFactory(new PropertyValueFactory<BDTipoExame,Integer>("codigoTipo"));
        tabelaTipoNome.setCellValueFactory(new PropertyValueFactory<BDTipoExame,String>("tipo"));
        
        List<BDTipoExame> listServicos= query.getResultList();
        ObservableList<BDTipoExame> oblServicos= FXCollections.observableArrayList(listServicos);
        tabelaTipo.setItems(oblServicos);
        txtTipoPesquisar.clear();
    }

}
