package br.com.furb.tagarela.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;
import br.com.furb.tagarela.dto.PlanoDTO;
import br.com.furb.tagarela.dto.PlanoPranchaDTO;
import br.com.furb.tagarela.dto.PranchaDTO;
import br.com.furb.tagarela.dto.SymbolDTO;
import br.com.furb.tagarela.dto.SymbolPranchaDTO;
import br.com.furb.tagarela.listener.IOnImportComplete;
import br.com.furb.tagarela.listener.IOnSynchronizationError;
import br.com.furb.tagarela.listener.IOnSynchronizationFinish;
import br.com.furb.tagarela.persistence.controller.AbstractController;
import br.com.furb.tagarela.persistence.model.Plano;
import br.com.furb.tagarela.persistence.model.Prancha;
import br.com.furb.tagarela.persistence.model.Symbol;
import br.com.furb.tagarela.util.ImageUtil;

public class TagarelaSynchronizationController extends AbstractSynchronizationController {
	private AbstractController controller;
	private IOnSynchronizationFinish finishListener;
	private Map<String, String> pranchaSymbolRelation;
	private String symbolParams;
	
	public TagarelaSynchronizationController(Context context,
			AbstractController controller,
			IOnSynchronizationFinish finishListener,
			IOnSynchronizationError errorListener) {
		super(context, errorListener);
		this.finishListener = finishListener;
		this.controller = controller;
	}
	
	@SuppressWarnings("rawtypes")
	public void importarNucleoTagarela() throws Exception {
		List<SynchronizationInfoHolder> imports = new ArrayList<>();
		imports.add(getPlanoSyncHolder());
		imports.add(getPranchaSyncHolder());
		
		this.importModel(imports, EnumTagarelaSynchTaskId.PLANO_PRANCHA.getId());
	}
	
	private void importarSymbolsId() {
		try {
			this.importSymbolIds();
		} catch (Exception e) {
			onErrorListener.onSynchronizationError(e);
		}
	}
	
	private void importarSymbols() {
		try {
			this.importSymbol();
		} catch (Exception e) {
			onErrorListener.onSynchronizationError(e);
		}
	}
	
	private SynchronizationInfoHolder<Plano> getPlanoSyncHolder() {
		SynchronizationInfoHolder<Plano> holder = new SynchronizationInfoHolder<Plano>();
		holder.dtoClass = PlanoDTO.class;
		holder.onCompleteListener = getOnPlanoCompleteListener();
		
		return holder;
	}
	
	private IOnImportComplete<Plano> getOnPlanoCompleteListener() {
		return new IOnImportComplete<Plano>() {

			@Override
			public void onImportComplete(List<Plano> data) {
				try {
					for (Plano plano : data) {
						controller.save(plano);
					}
				} catch (SQLException e) {
					Log.e(TagarelaSynchronizationController.this.getClass()
							.getSimpleName(), e.getMessage());
					onErrorListener.onSynchronizationError(e);
				}
			}
		};
	}
	
	private SynchronizationInfoHolder<Prancha> getPranchaSyncHolder() {
		SynchronizationInfoHolder<Prancha> holder = new SynchronizationInfoHolder<Prancha>();
		holder.dtoClass = PranchaDTO.class;
		holder.onCompleteListener = getOnPranchaCompleteListener();
		
		return holder;
	}
	
	private IOnImportComplete<Prancha> getOnPranchaCompleteListener() {
		return new IOnImportComplete<Prancha>() {

			@Override
			public void onImportComplete(List<Prancha> data) {
				try {
					for (Prancha prancha : data) {
						controller.save(prancha);
					}
					
					// TODO: Salvar o id para verificar no 
					// symbol_plans
					// depois do symbol_plans
					// trazer os private_symbols_fucking_stupid_plans
				} catch (SQLException e) {
					Log.e(TagarelaSynchronizationController.this.getClass()
							.getSimpleName(), e.getMessage());
					onErrorListener.onSynchronizationError(e);
				}
			}
		};
	}
	
	private void criarRelacaoPlanoPrancha() {
		try {
			this.importDTO(getPlanoPranchaSyncHolder(),
					EnumTagarelaSynchTaskId.CRIAR_RELACAO_PLANO_PRANCHA.getId());
		} catch (Exception e) {
			onErrorListener.onSynchronizationError(e);
		}
	}
	
	private SynchronizationInfoHolder<PlanoPranchaDTO> getPlanoPranchaSyncHolder() {
		SynchronizationInfoHolder<PlanoPranchaDTO> holder = new SynchronizationInfoHolder<PlanoPranchaDTO>();
		holder.dtoClass = PlanoPranchaDTO.class;
		holder.onCompleteListener = getOnPlanoPranchaCompleteListener();
		
		return holder;
	}
	
	private IOnImportComplete<PlanoPranchaDTO> getOnPlanoPranchaCompleteListener() {
		return new IOnImportComplete<PlanoPranchaDTO>() {
			@Override
			public void onImportComplete(List<PlanoPranchaDTO> data) {
				try {
					for (PlanoPranchaDTO planoPrancha : data) {
						// Base de dados apresenta algumas inconsistências
						// nencessário limpar pranchas que não possuem planos associados
						Plano plano = controller.getPlanoById(Integer
								.valueOf(planoPrancha.getIdPlano()));
						
						if (plano != null) {
							Prancha prancha = controller.getPranchaById(Integer
									.valueOf(planoPrancha.getIdPrancha()));
							prancha.setPlano(plano);
							controller.save(prancha);
						}
					}
				} catch (SQLException e) {
					onErrorListener.onSynchronizationError(e);
				}
			}
		};
	}
	
	@SuppressWarnings("rawtypes")
	private void importSymbol() throws Exception {
		List<SynchronizationInfoHolder> imports = new ArrayList<>();
		imports.add(getSymbolSyncHolder());
		
		this.importDTO(imports, EnumTagarelaSynchTaskId.SYMBOLS.getId());
	}
	
	private SynchronizationInfoHolder<SymbolDTO> getSymbolSyncHolder() {
		SynchronizationInfoHolder<SymbolDTO> holder = new SynchronizationInfoHolder<SymbolDTO>();
		holder.dtoClass = SymbolDTO.class;
		holder.onCompleteListener = getOnSymbolCompleteListener();
		
		List<BasicNameValuePair> urlParameters = new ArrayList<>();
		urlParameters.add(new BasicNameValuePair("symbols_id%5B%5D", symbolParams));
		
		holder.urlParameters = urlParameters;
		
		return holder;
	}
	
	private IOnImportComplete<SymbolDTO> getOnSymbolCompleteListener() {
		return new IOnImportComplete<SymbolDTO>() {

			@Override
			public void onImportComplete(List<SymbolDTO> data) {
				System.out.println("eeiii");
				
				try {
					for (SymbolDTO symbolDto : data) {
						Log.d("", "SYMBOL ID: " + symbolDto.getId());
						
						Symbol symbol = new Symbol();
						symbol.setId(symbolDto.getId());
						symbol.setName(symbolDto.getName());
						symbol.setCreatedAt(symbolDto.getCreatedAt());
						symbol.setUpdatedAt(symbolDto.getUpdatedAt());
						byte[] img = ImageUtil.getByteFromBase64Image(symbolDto.getImageStringRepresentation().replaceAll("@", "+"));
						symbol.setImage(img);
					
						controller.save(symbol);
					}
				} catch (SQLException e) {
					onErrorListener.onSynchronizationError(e);
				}
			}
		};
	}
	
	@SuppressWarnings("rawtypes")
	private void importSymbolIds() throws Exception {
		List<SynchronizationInfoHolder> imports = new ArrayList<>();
		imports.add(getSymbolIdSyncHolder());
		
		this.importDTO(imports, EnumTagarelaSynchTaskId.SYMBOLS_ID.getId());
	}
	
	private SynchronizationInfoHolder<SymbolPranchaDTO> getSymbolIdSyncHolder() {
		SynchronizationInfoHolder<SymbolPranchaDTO> holder = new SynchronizationInfoHolder<SymbolPranchaDTO>();
		holder.dtoClass = SymbolPranchaDTO.class;
		holder.onCompleteListener = getOnSymbolIdCompleteListener();
		
		return holder;
	}
	
	private IOnImportComplete<SymbolPranchaDTO> getOnSymbolIdCompleteListener() {
		return new IOnImportComplete<SymbolPranchaDTO>() {
			@Override
			public void onImportComplete(List<SymbolPranchaDTO> data) {
				if (!data.isEmpty()) {
					StringBuilder params = new StringBuilder();
					params.append(data.get(0).getId());
					pranchaSymbolRelation = new HashMap<>();
					
					for (int i = 1; i < data.size(); i++) {
						SymbolPranchaDTO dto = data.get(i);
//						Log.d("", "Symbol ID: " + dto.getIdSymbol());
//						Log.d("", "Prancha ID: " + dto.getIdPrancha());
						pranchaSymbolRelation.put(dto.getIdPrancha(),
								dto.getIdSymbol());
						params.append("&symbols_id%5B%5D=").append(dto.getIdSymbol());
					}
					symbolParams = params.toString();
				}
			}
		};
	}

	private void criarRelacaoSimboloPrancha() {
		try {
			for (Entry<String, String> entry: pranchaSymbolRelation.entrySet()) {
				Prancha prancha = controller.getPranchaById(Integer
						.valueOf(entry.getKey()));
				
				if (prancha != null) {
					Symbol symbol = controller.getSymbolById(Integer
							.valueOf(entry.getValue()));
					prancha.setSymbol(symbol);
					controller.save(prancha);
				}
			}			
			
			finishListener.onSynchronizationFinish();
		} catch (Exception e) {
			onErrorListener.onSynchronizationError(e);
		}
	}

	@Override
	protected void onAsynchTaskFinish(int synchId) {
		switch (synchId) {
		case 0:
			this.criarRelacaoPlanoPrancha();
			break;
		case 1:
			this.importarSymbolsId();
			break;
		case 2:
			this.importarSymbols();
			break;
		case 3:
			this.criarRelacaoSimboloPrancha();
			break;
		default:
			break;
		}
	}
	
	public enum EnumTagarelaSynchTaskId {
		PLANO_PRANCHA(0), 
		CRIAR_RELACAO_PLANO_PRANCHA(1),
		SYMBOLS_ID(2),
		SYMBOLS(3);
		
		private int id;
		
		private EnumTagarelaSynchTaskId(int id) {
			this.id = id;
		}
		
		public int getId() {
			return this.id;
		}
	}
}
