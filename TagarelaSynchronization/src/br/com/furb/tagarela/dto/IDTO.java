package br.com.furb.tagarela.dto;

import java.io.Serializable;

/*
 * DataTransferObject padrão de objeto de sincronismo para não
 * tratar diretamente com a entidade possibilitando outras validações
 * e envio para o servidor
 */
public interface IDTO extends Serializable {
	// url de consulta da entidade no heroku
	public String getEntityUrl();
	
	// utilizado no sincronismo como opção para retornar a entidade do banco de dados
	public Class<?> getPojoReference();
}
