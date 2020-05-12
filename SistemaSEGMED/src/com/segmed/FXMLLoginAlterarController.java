package com.segmed;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import static com.segmed.FXMLLoginController.*;
import static com.segmed.FabricaEntityManager.createEm;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class FXMLLoginAlterarController {
	EntityManager em= createEm();

    @FXML
    private TextField txtLoginUsuario;

    @FXML
    private PasswordField txtLoginSenha;

    @FXML
    private PasswordField txtLoginNovaSenha;

    @FXML
    void btnAlterarCancelar(ActionEvent event) {
    	stage.close();
    }

    @FXML
    void btnAterarConfirmar(ActionEvent event) {
    	if(txtLoginNovaSenha.getText().length()!=0){
	    	try {
		    	String usuario = txtLoginUsuario.getText();
		    	String senha = txtLoginSenha.getText();
		    	String novaSenha = txtLoginNovaSenha.getText();
		    	TypedQuery<BDLoginTabela> query= (TypedQuery<BDLoginTabela>) em.createQuery("SELECT  b FROM BDLoginTabela b WHERE b.usuario= '"+usuario+"' AND b.senha= '"+senha+"'");    
		    	BDLoginTabela login = query.getSingleResult();
		    	login.setSenha(novaSenha);
		    	em.getTransaction().begin();       
		        em.merge(login);
		        em.getTransaction().commit();
		        
		        Alert alert= new Alert(Alert.AlertType.INFORMATION);
	             alert.setTitle("Informação");
	             alert.setContentText("Senha alterada com sucesso!!");
	             alert.show();
	             
		    	stage.close();
	    	}
	    	catch(NoResultException nre) {
	    		Alert alert= new Alert(Alert.AlertType.WARNING);
	            alert.setTitle("Informação");
	            //alert.setHeaderText("Cabeçalho");
	            alert.setContentText("Erro!!! Usuário e/ou senha não cadastrados");
	            alert.show();
	    	}
    	}
    	else {
    		 Alert alert= new Alert(Alert.AlertType.WARNING);
             alert.setTitle("Informação");
             alert.setContentText("Erro!!! Insira uma nova senha");
             alert.show();
    	}

    }

}
