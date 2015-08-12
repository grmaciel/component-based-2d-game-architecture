package br.com.furb.tagarela.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import android.util.Log;
import br.com.furb.tagarela.dto.IDTO;
import br.com.furb.tagarela.persistence.model.IPojo;

import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TagarelaSynchronizationService {
	private static int URL_CHARS_PARAM_LIMIT = 500; 
	private static String LOG_TAG = "SYNCH_LOG";
	
	public <T extends IDTO> List<T> importEntityDTO(Class<T> dtoClass) throws Exception {
		return this.importEntityDTO(dtoClass, null);
	}
	
	public <T extends IDTO> List<T> importEntityDTO(Class<T> dtoClass, List<BasicNameValuePair> params) throws Exception {
		String url = getURLFromDTO(dtoClass);

        @SuppressWarnings("serial")
		TypeToken<List<T>> token = new TypeToken<List<T>>() {}
        .where(new TypeParameter<T>() {}, dtoClass);
        
        List<T> response = getJsonObject(url, token.getType(), params);
        
        return (List<T>) response;
	}
	
	public <T extends IPojo, U extends IDTO> List<T> importEntityModel(Class<U> dtoClass) throws Exception {
		return this.importEntityModel(dtoClass, null);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IPojo, U extends IDTO> List<T> importEntityModel(Class<U> dtoClass, List<BasicNameValuePair> params) throws Exception {
		String url = getURLFromDTO(dtoClass);

		@SuppressWarnings("serial")
		TypeToken<List<U>> token = new TypeToken<List<U>>() {}
        .where(new TypeParameter<U>() {}, dtoClass);
        
        
        List<IDTO> response = getJsonObject(url, token.getType(), params);

        if (response.isEmpty()) {
        	return new ArrayList<T>();
        }
        
        IDTO resp = response.get(0);
        Class<T> cls = (Class<T>) resp.getPojoReference();
        List<T> pojos = fetchDTOIntoPojo(cls, dtoClass, response);
        
        return pojos;
	}

	/**
	 * Utiliza reflexão para popular a entidade utilizando os campos do DTO.
	 * Se faz necessário a entidade ter exatamente os mesmos campos do DTO.
	 * 
	 * TODO: Tratar campos inexistentes para continuar e só não atribuir dados
	 * 
	 * @param pojoClass
	 * @param dtoClass
	 * @param response
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	protected <T extends IPojo, U extends IDTO> List<T> fetchDTOIntoPojo(
			Class<T> pojoClass, Class<U> dtoClass, List<IDTO> response)
			throws InstantiationException, IllegalAccessException,
			NoSuchFieldException {
		List<Field> dtoFields = new ArrayList<Field>();
		dtoFields = getAllFields(dtoFields, dtoClass);
		
        List<T> pojos = new ArrayList<>();
        
        for (IDTO dto : response) {
        	T pojo = pojoClass.newInstance();
        	
			Log.d(LOG_TAG, "Parsing DTO Instance: "
					+ dto.getClass().getSimpleName() + " into POJO: "
					+ pojo.getClass().getSimpleName());
        	
        	for (int i = 0; i < dtoFields.size(); i++) {
        		Field field = dtoFields.get(i);
        		
        		if (Modifier.isStatic(field.getModifiers())) {
        			continue;
        		}
        		
        		field.setAccessible(true);
        		Object value = field.get(dto);
        		Field destinyField = this.getField(pojoClass, field.getName());
        		destinyField.setAccessible(true);
        		destinyField.set(pojo, value);
        	}
        	pojos.add(pojo);
        }
        
		return pojos;
	}
	
	private Field getField(Class<?> clazz, String name) {
		Field destinyField = null;
		try {
			destinyField = clazz.getDeclaredField(name);
		} catch (NoSuchFieldException e) {
			destinyField = this.getField(clazz.getSuperclass(), name);
		}
		
		return destinyField;
	}
	
	private static List<Field> getAllFields(List<Field> fields, Class<?> type) {
	    fields.addAll(Arrays.asList(type.getDeclaredFields()));
	    
	    
		if (type.getSuperclass() != null
				&& !type.getSuperclass().getName()
						.equals(Object.class.getName())) {
	        fields = getAllFields(fields, type.getSuperclass());
	    }

	    return fields;
	}
	
	/*
	 * Método para converter o objeto Json no DTO específicado na 
	 * chamada do método.
	 * 
	 * Melhorias: Fazer uma camada de data converter e implementar o GsonConverter
	 * Possiblitando depois adicionar um XmlConverter e etc deixando mais dinâmico.
	 *  
	 */
	protected <T> List<T> getJsonObject(String dtoUrl, Type type, List<BasicNameValuePair> params) throws ClientProtocolException, IOException, URISyntaxException {
		Log.d(LOG_TAG, "Retrieving JSON for URL: " + dtoUrl);
		HttpClient client = new DefaultHttpClient();
		
		
		List<String> urls = new ArrayList<>();
		
		String paramsUrl = "";
		if (params != null) {
			dtoUrl += "?";
			for (BasicNameValuePair param : params) {
				paramsUrl+=param.getName() + "=" + param.getValue();
			}
		}
		
		
		// gambi para garantir que  venham todos os registros e não ocorrer
		// Request-URI Too Large
		// o ideal seria o server receber um requesição post ao invéz de get
		// ou usar algum filtro
		if (paramsUrl.length() > URL_CHARS_PARAM_LIMIT) { 
			// quebra individual de um parâmetro muito longo
			// como o caso dos symbols
			while (true) {
				if (paramsUrl.length() <= URL_CHARS_PARAM_LIMIT) {
					String newUrl = dtoUrl + paramsUrl;
					urls.add(newUrl);
					break;
				}
				
				int index = paramsUrl.lastIndexOf("&", URL_CHARS_PARAM_LIMIT - 50);
				String newUrl = dtoUrl + paramsUrl.substring(0, index);
				urls.add(newUrl);
				
				paramsUrl = paramsUrl.substring(index+1, paramsUrl.length());
			}
		} else {
			urls.add(dtoUrl + paramsUrl);
		}
		
		List<T> response = new ArrayList<T>();
		
		for (String url : urls) {
			HttpGet post = new HttpGet(url);
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-type", "application/json");
			HttpResponse response_ = client.execute(post);
			
			if (response_.getStatusLine().getStatusCode() == 200) {
				Log.d("", "Resp: " + response_.getStatusLine().getStatusCode());
				
				HttpEntity entity = response_.getEntity();
				InputStream content = entity.getContent();
				
				Reader reader = new InputStreamReader(content);
					
				GsonBuilder gsonBuilder = new GsonBuilder();
				Gson gson = gsonBuilder.create();
//				Object obj = gson.fromJson(reader, Object.class);
				List<T> resp = gson.fromJson(reader, type);
		        response.addAll(resp);
			}
			
		}
//	    post.getParams().setIntParameter("symbols_id", 25);
		
		//Perform the request and check the status code
		
	
		//NOVOOOO
//		HttpClient httpclient = new DefaultHttpClient();
//
//		List<NameValuePair> urlParam = new ArrayList<NameValuePair>();
//		urlParam.add(new BasicNameValuePair("symbols_id", "21,22" ));
//		URI uri = new URI(url + "?" + URLEncodedUtils.format(urlParam, "utf-8"));
        
        return response;
	}
	
	protected <T extends IDTO> String getURLFromDTO(Class<T> dtoClass) throws Exception {
		Log.d(LOG_TAG, "Getting URL from DTO: " + dtoClass.getSimpleName());
		
		String url = null;
		try {
			IDTO instance = dtoClass.newInstance();
			url = instance.getEntityUrl();
		} catch (InstantiationException e) {
			Log.e("SYNC ERROR", e.getMessage());
		} catch (IllegalAccessException e) {
			Log.e("SYNC ERROR", e.getMessage());
		}
		
		if (url == null) {
			throw new Exception("ENTIDADE SEM URL DE SINCRONISMO: "
					+ dtoClass.getSimpleName());
		}
		
		return url;
	}
	
	protected JSONArray getJsonArrayObject(String url) throws Exception {
        String str = "";
        HttpResponse response;
        HttpClient myClient = new DefaultHttpClient();
        HttpGet myConnection = new HttpGet(url);
        
        response = myClient.execute(myConnection);
        str = EntityUtils.toString(response.getEntity(), "UTF-8");
        JSONArray jArray = new JSONArray(str);
        
        return jArray;
	}
}
