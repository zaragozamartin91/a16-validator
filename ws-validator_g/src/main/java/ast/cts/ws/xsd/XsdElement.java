package ast.cts.ws.xsd;

import javax.xml.xpath.XPathExpressionException;

public interface XsdElement {
	/**
	 * Obtiene el tipo crudo del elemento.
	 *
	 * @return Tipo crudo del elemento. String vacio si el elemento no tiene tipo.
	 * @throws XPathExpressionException Si ocurre un error al obtener el type usando xpath
	 */
	String getType() throws XPathExpressionException;

	/**
	 * Obtiene el tipo del elemento ignorando el prefijo de namespace.
	 *
	 * @param typePrefix Prefijo del type (ej: tns, xs)
	 * @return Tipo del elemento sin prefijo de namespace. String vacio si el elemento no tiene tipo.
	 * @throws XPathExpressionException Si ocurre un error al obtener el type usando xpath
	 */
	String getType(String typePrefix) throws XPathExpressionException;

	/**
	 * Retorna true si este elemento es {@link VoidXsdElement}
	 *
	 * @return true si este elemento es {@link VoidXsdElement}, false en caso contrario
	 */
	boolean isVoid();

	/**
	 * Retorna true si este elemento es complejo. Se entiende por elemento complejo a aquel que tiene jerarquia de hijos de tipo
	 * complexType -> sequence -> element.
	 *
	 * @return true si este elemento es complejo, false en caso contrario.
	 * @throws XPathExpressionException En caso que la expresion de xpath no aplique al elemento
	 */
	boolean isComplex() throws XPathExpressionException;

	/**
	 * Obtiene un {@link XsdElement} hijo del elemento this siguiendo la jerarquia complexType -> sequence -> element.
	 *
	 * @return Elemento hijo en caso de existir, {@link VoidXsdElement} en caso de no existir.
	 * @throws XPathExpressionException En caso que la expresion de xpath no aplique al elemento
	 */
	XsdElement getComplexChild() throws XPathExpressionException;

	/**
	 * Retorna true si algun {@link XsdElement} hijo de este elemento tiene el tipo indicado.
	 *
	 * @param type       Nombre del Tipo buscado.
	 * @param typePrefix Prefijo del tipo (ej: xns, tns, xs)
	 * @return True si algun {@link XsdElement} hijo de este elemento tiene el tipo indicado, false en caso contrario.
	 * @throws XPathExpressionException En caso que la expresion de xpath no aplique al elemento
	 */
	boolean hasChildrenWithType(String type, String typePrefix) throws XPathExpressionException;
}
