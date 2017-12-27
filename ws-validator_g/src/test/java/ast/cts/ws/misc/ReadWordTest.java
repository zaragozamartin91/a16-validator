//package ast.cts.ws.misc;
//
//import com.independentsoft.office.IContentElement;
//import com.independentsoft.office.word.WordDocument;
//import com.independentsoft.office.word.tables.IRowContent;
//import com.independentsoft.office.word.tables.Table;
//import org.junit.Test;
//
//import java.util.List;
//
//public class ReadWordTest {
//	@Test
//	public void readWord() throws Exception {
//		try {
//
//			WordDocument doc = new WordDocument("D:\\workspaces\\validators\\ws-validator_g\\out\\test\\resources\\A16_CTS_AST38457SPSS_Segundo_Filtro.doc");
//
//			List<Table> tables = doc.getTables();
//
//			for (Table table : tables) {
//				List<IRowContent> tableContent = table.getContent();
//				for (IRowContent rowContent : tableContent) {
//					List<IContentElement> cells = rowContent.getContentElements();
//					for (IContentElement cell : cells) {
//						System.out.println(cell);
//					}
//				}
//			}
//
//
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			e.printStackTrace();
//		}
//	}
//}
