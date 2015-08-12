package br.com.furb.tagarela.listener;

import java.util.List;

public interface IOnImportComplete<T> {
	public void onImportComplete(List<T> data);
}
