package com.segmed;
// Generated 24/05/2018 15:24:51 by Hibernate Tools 5.2.10.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * TipoExame generated by hbm2java
 */
@Entity
@Table(name = "TIPO_EXAME", schema = "SEGMED")
public class BDTipoExame implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int codigoTipo;
	private String tipo;

	public BDTipoExame() {
	}

	public BDTipoExame(int codigoTipo) {
		this.codigoTipo = codigoTipo;
	}

	public BDTipoExame(String tipo) {
		this.tipo = tipo;
	}

	@Id
	@GeneratedValue(generator="seq_tipo", strategy=GenerationType.AUTO)
	@SequenceGenerator(name="seq_tipo", sequenceName="seq_tipo", initialValue=1)
	@Column(name = "codigo_tipo", unique = true, nullable = false)
	public int getCodigoTipo() {
		return this.codigoTipo;
	}

	public void setCodigoTipo(int codigoTipo) {
		this.codigoTipo = codigoTipo;
	}

	@Column(name = "tipo", length = 30)
	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String toString() {
		return tipo;
	}

}
