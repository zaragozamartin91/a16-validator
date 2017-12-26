package ast.cts.ws;

import ast.cts.ws.a16.A16Doc;
import ast.cts.ws.a16.A16DocReader;
import ast.cts.ws.util.TypeComparator;
import ast.cts.ws.xsd.XsdReader;
import org.junit.Test;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class MainValidatorTest {
	@Test
	public void validate() throws Exception {
		int nameCol = 1;
		int typeCol = 2;
		String colDelim = "\t";
		String inputName = "SegundoFiltroFil";
		String outputName = "SegundoFiltroRet";
		A16DocReader a16DocReader = new A16DocReader(nameCol, typeCol, colDelim, inputName, outputName);

		InputStream a16stream = MainValidatorTest.class.getClassLoader().getResourceAsStream("SegundoFiltro-input.txt");
		A16Doc a16doc = a16DocReader.readA16Txt(a16stream);

		InputStream xsdStream = MainValidatorTest.class.getClassLoader().getResourceAsStream("SegundoFiltro_schema1.xsd");
		XsdReader xsdReader = XsdReader.fromStream(xsdStream);

		String xsdBasicTypePrefix = "xs";
		String xsdComplexTypePrefix = "tns";

		List<String> intAliases = Arrays.asList("numero", "numerico", "int", "integer");
		List<String> stringAliases = Arrays.asList("texto", "char", "varchar");
		List<String> decimalAliases = Arrays.asList("money", "moneda");
		TypeComparator typeComparator = new TypeComparator(intAliases, stringAliases, decimalAliases);

		MainValidator mainValidator = new MainValidator(a16doc, xsdReader, xsdBasicTypePrefix, xsdComplexTypePrefix, typeComparator);
		boolean lenientCheck = true;
		List<String> errMsgs = mainValidator.validate(lenientCheck);

		System.out.println();
		System.out.println("Errores detectados:");
		errMsgs.stream().forEach(System.out::println);
	}

}