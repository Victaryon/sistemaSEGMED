package com.segmed; 

import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.scene.control.Alert;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.jboss.logging.Logger;

public  class FabricaEntityManager {
    //Sempre irÃ¡ utilizar a mesma instancia(ojeto) emf -> PadrÃ£o Singleton
    private static EntityManagerFactory emf;
    
	public static EntityManagerFactory getEmf() {
		if (emf == null){
			synchronized (FabricaEntityManager.class) {
				if (emf == null)
					try {
						emf = Persistence.createEntityManagerFactory("SistemaSEGMED");
					} catch (RuntimeException ex) {
						StringWriter stackTraceWriter = new StringWriter();
						ex.printStackTrace(new PrintWriter(stackTraceWriter));
                        Alert alert= new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERRO!");
                        //alert.setHeaderText("Cabeçalho");
                        alert.setContentText("Não foi possivel connectar ao Banco de Dados \n"
                             +"Falha ao carregar unidade de Persistencia \n\n"+ex.toString());
                        alert.showAndWait();
                         
                        Logger.getLogger(FabricaEntityManager.class).fatal("não foi possivel carregar a unidade de persistencia", ex);
						throw ex;
					}
			}
		}
		return emf;
	}
        
	public static EntityManager createEm() { //método que usa uma transação pra cada parte do programa
		try {
			return getEmf().createEntityManager();
		} 
                catch (RuntimeException ex) {
                    StringWriter stackTraceWriter = new StringWriter(); 
                    ex.printStackTrace(new PrintWriter(stackTraceWriter));
                    Alert alert= new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERRO!");
                    alert.setContentText("Não foi possivel connectar ao Banco de Dados \n"
                            + "Falha ao criar o EntityManager\n\n"+ex.toString());
                    //alert.setContentText(ex.toString() + "\n" + stackTraceWriter.toString());//Faz com que todo erro seja impresso na caixa de Dialogo
                    alert.showAndWait();
                    
                    Logger.getLogger(FabricaEntityManager.class).error("falha ao criar EntityManager", ex);
                    throw ex;
		}
        }
        
}