package com.segmed; 

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;

public abstract class MaskFieldUtil {
    /**
     * Monta a mascara para Moeda.
     *
     * @param textField TextField
     */
    public static void monetaryField(final TextField textField) {
        textField.setAlignment(Pos.CENTER_RIGHT);
        textField.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Platform.runLater(()->{
                    String value = textField.getText();
                    value = value.replaceAll("[^0-9]", "");
                    value = value.replaceAll("([0-9]{1})([0-9]{14})$", "$1.$2");
                    value = value.replaceAll("([0-9]{1})([0-9]{11})$", "$1.$2");
                    value = value.replaceAll("([0-9]{1})([0-9]{8})$", "$1.$2");
                    value = value.replaceAll("([0-9]{1})([0-9]{5})$", "$1.$2");
                    value = value.replaceAll("([0-9]{1})([0-9]{2})$", "$1,$2");
                    textField.setText(value);
                    positionCaret(textField);

                    textField.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                            if (newValue.length() > 17)
                                textField.setText(oldValue);
                        }
                    });
                });
            }
        });

        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean fieldChange) {
                if (!fieldChange) {
                    final int length = textField.getText().length();
                    if (length > 0 && length < 2) {
                        textField.setText(textField.getText() + "00");
                    }
                }
            }
        });
    }
    
    /**
     * Devido ao incremento dos caracteres das mascaras eh necessario que o cursor sempre se posicione no final da string.
     *
     * @param textField TextField
     */
    private static void positionCaret(final TextField textField) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Posiciona o cursor sempre a direita.
                textField.positionCaret(textField.getText().length());
            }
        });
    }
     /**
     * @param textField TextField.
     * @param length    Tamanho do campo.
     */
    private static void maxField(final TextField textField, final Integer length) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (newValue.length() > length)
                    textField.setText(oldValue);
            }
        });
    }
}