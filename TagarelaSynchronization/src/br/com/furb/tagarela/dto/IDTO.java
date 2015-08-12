package br.com.furb.tagarela.dto;

import java.io.Serializable;

/*
 * DataTransferObject padr�o de objeto de sincronismo para n�o
 * tratar diretamente com a entidade possibilitando outras valida��es
 * e envio para o servidor
 */
public interface IDTO extends Serializable {
	// url de consulta da entidade no heroku
	public String getEntityUrl();
	
	// utilizado no sincronismo como op��o para retornar a entidade do banco de dados
	public Class<?> getPojoReference();
}
