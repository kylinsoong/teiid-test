package org.teiid.test.sql.procedure;

import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stax.StAXSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.Service.Mode;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;

/**
 * http://www.oorsprong.org/websamples.countryinfo/CountryInfoService.wso?WSDL
 * 
 * @author kylin
 *
 */
public class CountryInfoServiceClient {
	
	static final String CapitalCity = "<CapitalCity xmlns=\"http://www.oorsprong.org/websamples.countryinfo\"><sCountryISOCode xmlns=\"\">CNA</sCountryISOCode></CapitalCity>";
	static final String CountriesUsingCurrency = "<CountriesUsingCurrency xmlns=\"http://www.oorsprong.org/websamples.countryinfo\"><sISOCurrencyCode xmlns=\"\">CNY</sISOCurrencyCode></CountriesUsingCurrency>";
	static final String CountryCurrency = "<CountryCurrency xmlns=\"http://www.oorsprong.org/websamples.countryinfo\"><sCountryISOCode xmlns=\"\">CNA</sCountryISOCode></CountryCurrency>";
	static final String CountryFlag = "<CountryFlag xmlns=\"http://www.oorsprong.org/websamples.countryinfo\"><sCountryISOCode xmlns=\"\">CNA</sCountryISOCode></CountryFlag>";
	static final String CountryIntPhoneCode = "<CountryIntPhoneCode xmlns=\"http://www.oorsprong.org/websamples.countryinfo\"><sCountryISOCode xmlns=\"\">CNA</sCountryISOCode></CountryIntPhoneCode>";
	
	static final String CountryISOCode = "<CountryISOCode xmlns=\"http://www.oorsprong.org/websamples.countryinfo\"><sCountryName xmlns=\"\">China</sCountryName></CountryISOCode>";
	static final String CountryName = "<CountryName xmlns=\"http://www.oorsprong.org/websamples.countryinfo\"><sCountryISOCode xmlns=\"\">CNA</sCountryISOCode></CountryName>";
	static final String CurrencyName = "<CurrencyName xmlns=\"http://www.oorsprong.org/websamples.countryinfo\"><sCurrencyISOCode xmlns=\"\">CNY</sCurrencyISOCode></CurrencyName>";
	static final String FullCountryInfo = "<FullCountryInfo xmlns=\"http://www.oorsprong.org/websamples.countryinfo\"><sCountryISOCode xmlns=\"\">CNA</sCountryISOCode></FullCountryInfo>";
	static final String FullCountryInfoAllCountries = "<FullCountryInfoAllCountries xmlns=\"http://www.oorsprong.org/websamples.countryinfo\"/>";

	static final String LanguageISOCode = "<LanguageISOCode xmlns=\"http://www.oorsprong.org/websamples.countryinfo\"><sLanguageName xmlns=\"\">Chinese</sLanguageName></LanguageISOCode>";
	static final String LanguageName = "<LanguageName xmlns=\"http://www.oorsprong.org/websamples.countryinfo\"><sISOCode xmlns=\"\">zh</sISOCode></LanguageName>";
	static final String ListOfContinentsByCode = "<ListOfContinentsByCode xmlns=\"http://www.oorsprong.org/websamples.countryinfo\"/>";
	static final String ListOfContinentsByName = "<ListOfContinentsByName xmlns=\"http://www.oorsprong.org/websamples.countryinfo\"/>";
	static final String ListOfCountryNamesByCode = "<ListOfCountryNamesByCode xmlns=\"http://www.oorsprong.org/websamples.countryinfo\"/>";
	
	static final String ListOfCountryNamesByName = "<ListOfCountryNamesByName xmlns=\"http://www.oorsprong.org/websamples.countryinfo\"/>";
	static final String ListOfCountryNamesGroupedByContinent = "<ListOfCountryNamesGroupedByContinent xmlns=\"http://www.oorsprong.org/websamples.countryinfo\"/>";
	static final String ListOfCurrenciesByCode = "<ListOfCurrenciesByCode xmlns=\"http://www.oorsprong.org/websamples.countryinfo\"/>";
	static final String ListOfCurrenciesByName = "<ListOfCurrenciesByName xmlns=\"http://www.oorsprong.org/websamples.countryinfo\"/>";
	static final String ListOfLanguagesByCode = "<ListOfLanguagesByCode xmlns=\"http://www.oorsprong.org/websamples.countryinfo\"/>";
	
	static final String ListOfLanguagesByName = "<ListOfLanguagesByName xmlns=\"http://www.oorsprong.org/websamples.countryinfo\"/>";
	
	public static void main(String[] args) throws XMLStreamException {
		
		final QName serviceName = new QName("http://www.oorsprong.org/websamples.countryinfo", "CountryInfoServiceSoap");
		final QName portName = new QName("http://www.oorsprong.org/websamples.countryinfo", "CountryInfoServiceSoap");
		
		Bus bus = BusFactory.getThreadDefaultBus();
		BusFactory.setThreadDefaultBus(null);
		Service service;
		try {
			service = Service.create(serviceName);
		} finally {
			BusFactory.setThreadDefaultBus(bus);
		}
		
		String bindingId = "http://schemas.xmlsoap.org/wsdl/soap/http";
		String endpointAddress = "http://www.oorsprong.org/websamples.countryinfo/CountryInfoService.wso?WSDL";
		service.addPort(portName, bindingId, endpointAddress);
		Dispatch<StAXSource> dispatch = service.createDispatch(portName, StAXSource.class, Mode.PAYLOAD);
		
		execute(dispatch, CapitalCity, "CapitalCityResult");
		execute(dispatch, CountriesUsingCurrency, "sName");
		execute(dispatch, CountryCurrency, "sISOCode", "sName");
		execute(dispatch, CountryFlag, "CountryFlagResult");
		execute(dispatch, CountryIntPhoneCode, "CountryIntPhoneCodeResult");
		
		System.out.println();
		
		execute(dispatch, CountryISOCode, "CountryISOCodeResult");
		execute(dispatch, CountryName, "CountryNameResult");
		execute(dispatch, CurrencyName, "CurrencyNameResult");
		execute(dispatch, FullCountryInfo, "sISOCode", "sName", "sCapitalCity", "sPhoneCode", "sContinentCode", "sCurrencyISOCode", "sCountryFlag");
		execute(dispatch, FullCountryInfoAllCountries, "sName", "sCapitalCity");
		
		System.out.println();
		
		execute(dispatch, LanguageISOCode, "LanguageISOCodeResult");
		execute(dispatch, LanguageName, "LanguageNameResult");
		execute(dispatch, ListOfContinentsByCode, "sCode", "sName");
		execute(dispatch, ListOfContinentsByName, "sCode", "sName");
		execute(dispatch, ListOfCountryNamesByCode, "sISOCode", "sName");
		
		System.out.println();
		
		execute(dispatch, ListOfCountryNamesByName, "sISOCode", "sName");
		execute(dispatch, ListOfCountryNamesGroupedByContinent, "sISOCode", "sName");
//		execute(dispatch, ListOfCurrenciesByCode, "sISOCode", "sName");
//		execute(dispatch, ListOfCurrenciesByName, "sISOCode", "sName");
//		execute(dispatch, ListOfLanguagesByCode, "sISOCode", "sName");
		
		System.out.println();
		
//		execute(dispatch, ListOfLanguagesByName, "sISOCode", "sName");
		
	}
	
	static void execute(Dispatch<StAXSource> dispatch, String operation, String... reponseElements) throws XMLStreamException {
		StAXSource returnValue = dispatch.invoke(formStAXSource(operation));
		System.out.println(parse(returnValue.getXMLStreamReader(), reponseElements));
	}
	
	static StAXSource formStAXSource(String xml) throws XMLStreamException {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		StAXSource source = new StAXSource(factory.createXMLStreamReader(new ByteArrayInputStream(xml.getBytes())));
		return source;
	}
	
	static List<String> parse(XMLStreamReader reader, String... elementNames) throws XMLStreamException {
		
		Set<String> set = new HashSet<>();
		for(String name : elementNames){
			set.add(name);
		}
		
		List<String> stateNames = new ArrayList<String>();
		
		while (true) {
			if (reader.getEventType() == END_DOCUMENT) {
				break;
			}
			
			if (reader.getEventType() == START_ELEMENT) {
				String cursor = reader.getLocalName();
				if (set.contains(cursor)) {
					reader.next();
					String value = reader.getText();
					stateNames.add(value);
				}
			}
			
			reader.next();
		}
		
		return stateNames;
	}

}
