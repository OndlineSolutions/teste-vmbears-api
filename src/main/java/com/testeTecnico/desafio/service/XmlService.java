package com.testeTecnico.desafio.service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.testeTecnico.desafio.model.Agente;
import com.testeTecnico.desafio.model.Compra;
import com.testeTecnico.desafio.model.Geracao;
import com.testeTecnico.desafio.model.PrecoMedio;
import com.testeTecnico.desafio.model.Regiao;

@Service
public class XmlService {

	@Autowired
	private AgenteService agenteService;

	@Autowired
	private RegiaoService regiaoService;

	@Autowired
	private GeracaoService geracaoService;

	@Autowired
	private CompraService compraService;

	@Autowired
	private PrecoMedioService precoMedioService;

	public static final String GERACAO = "geracao";
	public static final String COMPRA = "compra";
	public static final String PRECO_MEDIO = "precoMedio";

	public List<Agente> validateXml(MultipartFile xml) throws ParserConfigurationException, SAXException {
		List<Agente> agentesPersist = new ArrayList<>();
		{
			try {

				InputStream is = xml.getInputStream();
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document document = db.parse(is);
				document.getDocumentElement().normalize();

				/* --- Agrupa por Agente --- */
				iniciaAgrupamentoAgentes(document, agentesPersist);
			}

			catch (Exception e) {
				System.out.println(e);
			}
		}
		return persistirDados(agentesPersist);
	}

	private List<Agente> persistirDados(List<Agente> agentes) {
		List<Agente> response = new ArrayList<>();
		agentes.forEach(ag -> {
			try {
				agenteService.salvarAgente(ag);

				Set<Regiao> regioesResponse = new HashSet<>();
				ag.getRegioes().forEach(rg -> {
					try {
						ExecutorService executorService = Executors.newFixedThreadPool(4);
						Regiao regiaoSave = regiaoService.salvarRegiao(rg);

						// Persiste Geração
						if (regiaoSave != null) {
							rg.getGeracao().forEach(g -> {
								g.getRegiao().setId(regiaoSave.getId());
								try {
									
									executorService.submit(() -> {
										try {
											geracaoService.salvarGeracao(g);
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									});
								} catch (Exception e) {
									e.printStackTrace();
								}
							});

							// Persiste Compra
							rg.getCompra().forEach(c -> {
								c.getRegiao().setId(regiaoSave.getId());
								try {
									executorService.submit(() -> {
										try {
											compraService.salvarCompra(c);
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									});
								} catch (Exception e) {
									e.printStackTrace();
								}
							});

							// Persiste PrecoMedio
							rg.getPrecoMedio().forEach(pm -> {
								pm.getRegiao().setId(regiaoSave.getId());
								try {
									executorService.submit(() -> {
										try {
											precoMedioService.salvarPrecoMedio(pm);
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									});
								} catch (Exception e) {
									e.printStackTrace();
								}
							});
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					regioesResponse.add(rg);
				});
				ag.setRegioes(regioesResponse);
				response.add(ag);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return response;
	}

	private void iniciaAgrupamentoAgentes(Document document, List<Agente> agentesPersist) throws Exception {
		NodeList agenteList = document.getElementsByTagName("agente");

		for (int idxAgente = 0; idxAgente < agenteList.getLength(); ++idxAgente) {
			Node node = agenteList.item(idxAgente);
			Element tElementAgente = (Element) node;

			Long codigoAgente = Long.valueOf(tElementAgente.getElementsByTagName("codigo").item(0).getTextContent());

			Agente responseAgente = agenteService.findByCodigo(codigoAgente);

			if (responseAgente == null) {
				String str = tElementAgente.getElementsByTagName("data").item(0).getTextContent();
				String[] split = str.split("T");
				String[] horaSplit = split[1].split("-");
				String strDate = split[0] + " " + horaSplit[0];
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

				LocalDateTime data = LocalDateTime.parse(strDate, formatter);

				Agente agente = new Agente(codigoAgente, data);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					System.out.println("\nCódigo: " + codigoAgente);
					System.out.println("Data: " + data);
					agente = agrupaRegiaoPorAgente(tElementAgente, agente);
				}
				agentesPersist.add(agente);
			}

		}
	}

	private Agente agrupaRegiaoPorAgente(Element tElementAgente, Agente agente) throws Exception {
		Set<Regiao> regioes = new HashSet<>();

		NodeList regiaoList = tElementAgente.getElementsByTagName("regiao");

		for (int idxRegiao = 0; idxRegiao < regiaoList.getLength(); ++idxRegiao) {
			Node nodeRegiao = regiaoList.item(idxRegiao);
			Element tElementRegiao = (Element) nodeRegiao;

			/* --- Identifica a sigla de cada região --- */
			String[] split = tElementRegiao.getAttributes().getNamedItem("sigla").toString().split("\"");
			String sigla = split[1];
			System.out.println("\nRegião: " + sigla);

			Regiao regiao = new Regiao(sigla, agente);

			/* --- Agrupa valores por geracao, compra e precoMedio --- */
			if (nodeRegiao.getNodeType() == Node.ELEMENT_NODE) {
				printValoresRegiao(tElementRegiao, GERACAO, regiao);
				printValoresRegiao(tElementRegiao, COMPRA, regiao);
				printValoresRegiao(tElementRegiao, PRECO_MEDIO, regiao);
			}
			regiao.getAgente().setCodigo(agente.getCodigo());
			regioes.add(regiao);
			agente.setRegioes(regioes);
		}
		return agente;
	}

	private void printValoresRegiao(Element tElementRegiao, String titulo, Regiao regiao) {
		NodeList geracaoList = tElementRegiao.getElementsByTagName(titulo);
		System.out.println("    " + titulo + ": ");

		String valores = "";
		for (int i = 0; i < geracaoList.getLength(); ++i) {
			Node nodeGeracao = geracaoList.item(i);
			Element tElementGeracao = (Element) nodeGeracao;

			if (nodeGeracao.getNodeType() == Node.ELEMENT_NODE) {

				NodeList valoresGeracaoList = tElementGeracao.getElementsByTagName("valor");
				for (int j = 0; j < valoresGeracaoList.getLength(); ++j) {

					String valor = tElementGeracao.getElementsByTagName("valor").item(0).getTextContent();
					valores += j == 0 ? valor : "," + valor;
					System.out.println("        Valor: " + valor);
				}
			}
		}

		switch (titulo) {
		case GERACAO:
			Set<Geracao> valoresGeracao = new HashSet<>();
			Geracao geracao = new Geracao(valores, regiao);
			valoresGeracao.add(geracao);
			regiao.setGeracao(valoresGeracao);
			break;
		case COMPRA:
			Set<Compra> valoresCompra = new HashSet<>();
			Compra compra = new Compra(valores, regiao);
			valoresCompra.add(compra);
			regiao.setCompra(valoresCompra);
			break;
		case PRECO_MEDIO:
			Set<PrecoMedio> valoresPrecoMedio = new HashSet<>();
			PrecoMedio precoMedio = new PrecoMedio(valores, regiao);
			valoresPrecoMedio.add(precoMedio);
			regiao.setPrecoMedio(valoresPrecoMedio);
			break;
		}
	}
}
