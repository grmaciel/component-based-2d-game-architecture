package br.com.furb.tagarela.controller;

import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import br.com.furb.tagarela.listener.IOnImportComplete;
/**
 * Classe responsável por conter o DTO que será feito a importação e a referência
 * ao método de retorno que retornará o tipo T
 * 
 * 
 * @author Gilson Maciel
 *
 * @param <T>
 */
public class SynchronizationInfoHolder<T> {
	// DTO que contém as informações da importação
	public Class<?> dtoClass;
	// Listener para quando finalizar a importação dessa entidade
	public IOnImportComplete<T> onCompleteListener;
	// Parâmetros para adicionar a request da url
	public List<BasicNameValuePair> urlParameters;
}
