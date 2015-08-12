package br.com.furb.tagarela.controller;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import br.com.furb.tagarela.listener.IOnSynchronizationError;
import br.com.furb.tagarela.listener.IOnSynchronizationFinish;
import br.com.furb.tagarela.service.TagarelaSynchronizationService;


public abstract class AbstractSynchronizationController {
	protected TagarelaSynchronizationService syncService = new TagarelaSynchronizationService();
	protected Context context;
	protected Exception syncException;
	protected IOnSynchronizationError onErrorListener;
	protected IOnSynchronizationFinish onSynchFinishListener;
	
	public AbstractSynchronizationController(Context context) {
		this.context = context;
	}
	
	public AbstractSynchronizationController(Context context, IOnSynchronizationError onErrorListener) {
		this.context = context;
		this.onErrorListener = onErrorListener;
	}
	
	@SuppressWarnings("rawtypes")
	public <T> void importDTO(SynchronizationInfoHolder<T> syncObj, int requestId) throws Exception {
		List<SynchronizationInfoHolder> syncs = new ArrayList<>();
		syncs.add(syncObj);
		
		this.importDTO(syncs, requestId);
	}
	
	/**
	 * Retorna a lista de cada DTO que foi feito request de importação
	 * dispara o listener de conclusão após o retorno de todos os dtos
	 * para o controller concreto que decide quando replicar para a activity
	 * @param <T>
	 * 
	 * @param syncObjs
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void importDTO(final List<SynchronizationInfoHolder> syncObjs, int requestId) throws Exception {
		TagarelaAsyncTask<Object, Void, Void> task = getAsyncImportDTOTask();
		task.setRequestId(requestId);
		task.execute(syncObjs.toArray());
	}
	
	/**
	 * Retonra a lista de cada POJO que foi feito request de importação
	 * dispara o listener de conclusão após o retorno de todos os database models
	 * 
	 * @param syncObjs
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void importModel(final List<SynchronizationInfoHolder> syncObjs, int requestId) throws Exception {
		TagarelaAsyncTask<Object, Void, Void> task = getAsyncImportModelTask();
		task.setRequestId(requestId);
		task.execute(syncObjs.toArray());
	}
	
	public TagarelaAsyncTask<Object, Void, Void> getAsyncImportModelTask() {
		return new TagarelaAsyncTask<Object, Void, Void>() {
			
			@Override
			protected Void doInBackground(Object... params) {
				asynchImportModel(params);
				
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (syncException != null && onErrorListener != null) {
					Log.d("SYNC", "POST EXECUTE ERROR");
					onErrorListener.onSynchronizationError(syncException);
				} else {
					Log.d("SYNC", "POST EXECUTE COMPLETE");
					onAsynchTaskFinish(getRequestId());
				}
				
				syncException = null;
			}
		};
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void asynchImportModel(Object... params) {
		for (Object obj : params) {
			SynchronizationInfoHolder sync = (SynchronizationInfoHolder) obj;
			List<Object> data;
			try {
				data = syncService.importEntityModel(sync.dtoClass, sync.urlParameters);
				sync.onCompleteListener.onImportComplete(data);
			} catch (Exception e) {
				syncException = e;
				Log.e(this.getClass().getSimpleName(), e.getMessage());
			}
		}
	}
	
	public TagarelaAsyncTask<Object, Void, Void> getAsyncImportDTOTask() {
		return new TagarelaAsyncTask<Object, Void, Void>() {
			@Override
			protected Void doInBackground(Object... arg0) {
				asynchImportDTO(arg0);
				
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (syncException != null && onErrorListener != null) {
					onErrorListener.onSynchronizationError(syncException);
				} else {
					onAsynchTaskFinish(getRequestId());
				}
				
				syncException = null;
			}
		};
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void asynchImportDTO(Object... arg0) {
		for (Object obj : arg0) {
			SynchronizationInfoHolder sync = (SynchronizationInfoHolder) obj;
			List<Object> data;
			try {
				data = syncService.importEntityDTO(sync.dtoClass, sync.urlParameters);
				sync.onCompleteListener.onImportComplete(data);
			} catch (Exception e) {
				syncException = e;
				Log.e(this.getClass().getSimpleName(), e.getMessage());
			}
		}
	}
	
	private abstract class TagarelaAsyncTask<Param, Progress, Result> extends AsyncTask<Param, Progress, Result> {
		private int requestId;

		public int getRequestId() {
			return requestId;
		}

		public void setRequestId(int requestId) {
			this.requestId = requestId;
		}
	}
	
	/*
	 * Listener disparado quando todas as entidades foram importadas
	 */
	protected abstract void onAsynchTaskFinish(int synchId);

	public IOnSynchronizationError getOnErrorListener() {
		return onErrorListener;
	}

	public void setOnErrorListener(IOnSynchronizationError onErrorListener) {
		this.onErrorListener = onErrorListener;
	}

	public IOnSynchronizationFinish getOnSynchFinishListener() {
		return onSynchFinishListener;
	}

	public void setOnSynchFinishListener(
			IOnSynchronizationFinish onSynchFinishListener) {
		this.onSynchFinishListener = onSynchFinishListener;
	}
}
