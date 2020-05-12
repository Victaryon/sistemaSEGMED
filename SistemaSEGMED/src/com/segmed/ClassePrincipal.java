package com.segmed;

import static com.segmed.FabricaEntityManager.*;
import static com.segmed.FXMLExamePesquisarController.*;
import java.io.IOException;

import javax.persistence.EntityManager;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ClassePrincipal extends Application {
	static EntityManager em= createEm(); 
    public static Stage primaryStage;
    public static BorderPane mainLayout;
    public static AnchorPane loginLayout;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage= primaryStage;
        this.primaryStage.setTitle("Sistema Vendas");
        
        showLogin();
    }
    
    public static Scene showLogin() throws IOException {
    	FXMLLoader loader= new FXMLLoader();
        loader.setLocation(ClassePrincipal.class.getResource("FXMLLogin.fxml"));
        loginLayout= loader.load();
        Scene scene= new Scene(loginLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
        return scene;
    }
   
    public static Scene showFormularioPrincipal() throws IOException{
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(ClassePrincipal.class.getResource("FXMLFormularioPrincipal.fxml"));
        mainLayout= loader.load();
        Scene scene= new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
        return scene;
    }
    
    public static void showCadastrarClientes() throws IOException{
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(ClassePrincipal.class.getResource("FXMLCadastroCliente.fxml"));
        BorderPane mainItens= loader.load();
        mainLayout.setCenter(mainItens);
    }
    
    public static void showCadastrarServico() throws IOException{
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(ClassePrincipal.class.getResource("FXMLCadastroServico.fxml"));
        BorderPane mainItens= loader.load();
        mainLayout.setCenter(mainItens);
    }
    
    public static void showCadastrarColaborador() throws IOException{
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(ClassePrincipal.class.getResource("FXMLCadastroColaborador.fxml"));
        BorderPane mainItens= loader.load();
        mainLayout.setCenter(mainItens);
    }
    
    public static void showCadastrarDescricao() throws IOException{
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(ClassePrincipal.class.getResource("FXMLCadastroDescricao.fxml"));
        BorderPane mainItens= loader.load();
        mainLayout.setCenter(mainItens);
    }
    
    public static void showExameNovo() throws IOException{
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(ClassePrincipal.class.getResource("FXMLExameNovo.fxml"));
        BorderPane mainItens= loader.load();
        mainLayout.setCenter(mainItens);
    }
    
    public static void showExamePesquisar() throws IOException{
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(ClassePrincipal.class.getResource("FXMLExamePesquisar.fxml"));
        BorderPane mainItens= loader.load();
        mainLayout.setCenter(mainItens);
    }
    
    public static void showRelatorioClientes() throws IOException{
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(ClassePrincipal.class.getResource("/report/FXMLRelatorioCliente.fxml"));
        BorderPane mainItens= loader.load();
        mainLayout.setCenter(mainItens);
    }
    
    public static void main(String[] args) {
    	try {
            launch(args);
    	}
        finally{
        	if(em.getTransaction().isActive()==true) {
        		em.getTransaction().rollback();
        	}
            getEmf().close();//Encerra as conexões antes de fechar o programa
        } 
    }
    
}
