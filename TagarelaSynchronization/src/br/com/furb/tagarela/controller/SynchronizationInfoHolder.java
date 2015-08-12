package br.com.furb.tagarela.controller;

import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import br.com.furb.tagarela.listener.IOnImportComplete;
/**
 * Classe respons�vel por conter o DTO que ser� feito a importa��o e a refer�ncia
 * ao m�todo de retorno que retornar� o tipo T
 * 
 * 
 * @author Gilson Maciel
 *
 * @param <T>
 */
public class SynchronizationInfoHolder<T> {
	// DTO que cont�m as informa��es da importa��o
	public Class<?> dtoClass;
	// Listener para quando finalizar a importa��o dessa entidade
	public IOnImportComplete<T> onCompleteListener;
	// Par�metros para adicionar a request da url
	public List<BasicNameValuePair> urlParameters;
}
