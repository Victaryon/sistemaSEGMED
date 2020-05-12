package com.segmed;

import static com.segmed.FabricaEntityManager.createEm;
import static com.segmed.FabricaEntityManager.getEmf;

import java.io.IOException;
import static com.segmed.ClassePrincipal.*;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXMLLoginController {
	EntityManager em= createEm();

    @FXML
    private TextField txtLoginUsuario;

    @FXML
    private PasswordField txtLoginSenha;
    
    static Stage stage= new Stage(StageStyle.DECORATED);
    static BDLoginTabela login;
    
    @FXML
    void btnLoginCancelar(ActionEvent event) {
    	stage.close();
    	getEmf().close();
        System.exit(0);
    }

    @FXML
    void btnLoginEntrar(ActionEvent event) throws IOException {
    	try {
	    	String usuario = txtLoginUsuario.getText();
	    	String senha = txtLoginSenha.getText();
	    	TypedQuery<BDLoginTabela> query= (TypedQuery<BDLoginTabela>) em.createQuery("SELECT  b FROM BDLoginTabela b WHERE b.usuario= '"+usuario+"' AND b.senha= '"+senha+"'");    
	    	login = query.getSingleResult();
	    	primaryStage.close();
	    	ClassePrincipal.showFormularioPrincipal();
    	}
    	catch(NoResultException nre) {
    		Alert alert= new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Informação");
            //alert.setHeaderText("Cabeçalho");
            alert.setContentText("Erro!!! Usuário e/ou senha não cadastrados");
            alert.show();
    	}
    }

    @FXML
    void btnLoginMudarSenha(ActionEvent event) throws IOException {
    	Parent root1 = FXMLLoader.load(getClass().getResource("FXMLLoginAlterar.fxml"));
        Scene scene= new Scene(root1);
        stage.setTitle("Descrição");
        stage.setScene(scene);
        stage.show();

    }

}
