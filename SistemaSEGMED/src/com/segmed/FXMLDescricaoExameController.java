package com.segmed;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import static com.segmed.FXMLExamePesquisarController.*;
import static com.segmed.FabricaEntityManager.*;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class FXMLDescricaoExameController {
	EntityManager em= createEm(); 
	BDExame exame = em.find(BDExame.class, codigoExame);
	float valorTotal = exame.getValorTotal();
	
	@FXML
    private TextArea txtServicoDescricao;

    @FXML
    private TextField txtServicoValorTotal;

    @FXML
    private TableView<BDExameitem> tabelaExame;

    @FXML
    private TableColumn<BDExameitem, String> tabelaExameCliente;

    @FXML
    private TableColumn<BDExameitem, String> tabelaExameColaborador;

    @FXML
    private TableColumn<BDExameitem, BDServico> tabelaExameServico;

    @FXML
    private TableColumn<BDExameitem, String> tabelaExameValor;
    
    @FXML
    public void initialize() { //N„o Èsta funcionando
	    carregarTabelaExame();
	    txtServicoValorTotal.setText(exame.valorTot());
		stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent window){
            	em.close();
            }
        });
    }
     
    
	@FXML
	void bntDescricaoSalvar(ActionEvent event) {
		String texto = txtServicoDescricao.getText();
        BDExameitem itemSalvar = tabelaExame.getSelectionModel().getSelectedItem();
        
        itemSalvar.getDescricao().setDescricaotexto(texto);
        exame.setValorTotal(valorTotal);
        
        em.getTransaction().begin();       
        em.merge(itemSalvar);
        em.merge(exame);
        em.getTransaction().commit();
        
        txtServicoDescricao.clear();
        carregarTabelaExame();
        txtServicoValorTotal.setText(exame.valorTot());
	}
	
	@FXML
    void bntDescricaoExibir(ActionEvent event) {
		BDExameitem alterarItem = tabelaExame.getSelectionModel().getSelectedItem();
		txtServicoDescricao.setText(alterarItem.getDescricao().getDescricaotexto());
		valorTotal = valorTotal - alterarItem.getDescricao().getValor();
		System.out.println("Total: "+valorTotal);

    }

	 
	
	public void carregarTabelaExame(){
		tabelaExame.getColumns().get(0).setVisible(false); //resolve problema de atualizar a tabela na op√ß√£o -
        tabelaExame.getColumns().get(0).setVisible(true);//- alterar 
        tabelaExameServico.setCellValueFactory(new PropertyValueFactory<BDExameitem, BDServico>("servico"));
        tabelaExameCliente.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BDExameitem, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<BDExameitem,String> param) {    
                return new ReadOnlyObjectWrapper<>(param.getValue().getExame().getClientes().getNome()); 
            }
        });
        tabelaExameColaborador.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BDExameitem, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<BDExameitem,String> param) {    
                return new ReadOnlyObjectWrapper<>(param.getValue().getExame().getClientecolaborador().getNome()); 
            }
        });
        tabelaExameValor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BDExameitem, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<BDExameitem,String> param) {    
                return new ReadOnlyObjectWrapper<>(param.getValue().getDescricao().valorUn()); 
            }
        });
        TypedQuery<BDExameitem> query=  (TypedQuery<BDExameitem>) em.createQuery("SELECT  b FROM BDExameitem b WHERE b.exame.codigoExame="+codigoExame+"");
        
        List<BDExameitem> listaExames = query.getResultList();
        ObservableList<BDExameitem> oblExames= FXCollections.observableArrayList(listaExames);
        tabelaExame.setItems(oblExames);
        
      }

}
