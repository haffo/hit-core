/**
 * This software was developed at the National Institute of Standards and
 * Technology by employees of the Federal Government in the course of their
 * official duties. Pursuant to title 17 Section 105 of the United States Code
 * this software is not subject to copyright protection and is in the public
 * domain. This is an experimental system. NIST assumes no responsibility
 * whatsoever for its use by other parties, and makes no guarantees, expressed
 * or implied, about its quality, reliability, or any other characteristic. We
 * would appreciate acknowledgement if the software is used. This software can
 * be redistributed and/or modified freely provided that any derivative works
 * bear some notice that they are derived from it, and any modified versions
 * bear some notice that they have been modified.
 */
package gov.nist.healthcare.tools.core.models.xml;

import gov.nist.healthcare.tools.core.models.message.MessageElementData;
import gov.nist.healthcare.tools.core.models.utils.XmlUtils;

import java.io.Serializable;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.located.Located;
import org.jdom2.xpath.XPathHelper;

/**
 * 
 * @author Harold Affo (NIST)
 * 
 */

public class XmlMessageElementData extends MessageElementData implements
		Serializable {

	private static final long serialVersionUID = 1L;

	private final XmlCoordinate start;

	private final XmlCoordinate end;

	private int column = -1;

	/**
	 * 
	 * @param element
	 */
	public XmlMessageElementData(Element element) {
		this(element, element.getName(), "none", 20000, 20000);
		if (element.getChildren().isEmpty()) {
			this.value = element.getValue();
		} else {
			this.value = null;
		}
	}

	private XmlCoordinate getStartCoordinate(Element element) {
		Located locatedElement = (Located) element;
		return new XmlCoordinate(locatedElement.getLine(), 0);
	}

	private XmlCoordinate getEndCoordinate(Element element) {
		return new XmlCoordinate(getEndLine(element), 1000);
		// Located locatedElement = (Located) element;
		// if (element.getChildren() == null || element.getChildren().isEmpty())
		// {
		// result = new Coordinate(getEndLine(element),
		// 1000);
		// } else {
		//
		// int line =
		//
		// List<Element> children = element.getChildren();
		// for (Element child : children) {
		// result.plus(getEndCoordinate(child));
		// }
		// }
		// return new Coordinate(locatedElement.getLine(),
		// locatedElement.getColumn() + element.getText().length());
	}

	private int getChildrenTotalLines(Element element) {
		if (element.getChildren() == null || element.getChildren().isEmpty()) {
			return 1;
		} else {
			int counter = 2;
			List<Element> children = element.getChildren();
			for (Element child : children) {
				counter = counter + getChildrenTotalLines(child);
			}
			return counter; // include end element
		}

	}

	// private int getEndLine(Element element) {
	// return ((Located) element).getLine() + getChildrenTotalLines(element);
	// }

	public XmlMessageElementData(Element element, String name, String usage,
			Integer minOccurs, Integer maxOccurs) {
		Located locatedElement = (Located) element;
		start = getStartCoordinate(element);
		end = getEndCoordinate(element);

		System.out.println("Element:" + XmlUtils.toString(element) + ", name:"
				+ name + ", start=" + start.getLine() + ", end="
				+ end.getLine());

		this.column = locatedElement.getColumn();
		this.path = XPathHelper.getAbsolutePath(element);
		this.name = name;
		this.usage = usage;
		this.minOccurs = minOccurs;
		this.maxOccurs = maxOccurs;
		this.value = null;
		this.description = toString();
	}

	/*
	 * public Element getElement() { return element; } public void
	 * setElement(Element element) { this.element = element; }
	 */
	@Override
	public String toString() {

		if (this.value != null) {
			return this.value;
		} else {
			StringBuffer buffer1 = new StringBuffer();
			buffer1.append(this.path).append(":".charAt(0)).append(this.name)
					.append(" ".charAt(0)).append(this.usage).append("")
					.append("[".charAt(0)).append(printMinOccurs())
					.append(",".charAt(0)).append(printMaxOccurs())
					.append("]".charAt(0));
			return buffer1.toString();
		}
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ (this.maxOccurs == null ? 0 : this.maxOccurs.hashCode());
		result = prime * result
				+ (this.minOccurs == null ? 0 : this.minOccurs.hashCode());
		result = prime * result
				+ (this.name == null ? 0 : this.name.hashCode());
		result = prime * result
				+ (this.path == null ? 0 : this.path.hashCode());
		result = prime * result
				+ (this.usage == null ? 0 : this.usage.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		XmlMessageElementData other = (XmlMessageElementData) obj;
		if (this.maxOccurs == null) {
			if (other.maxOccurs != null) {
				return false;
			}
		} else if (!this.maxOccurs.equals(other.maxOccurs)) {
			return false;
		}
		if (this.minOccurs == null) {
			if (other.minOccurs != null) {
				return false;
			}
		} else if (!this.minOccurs.equals(other.minOccurs)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.path == null) {
			if (other.path != null) {
				return false;
			}
		} else if (!this.path.equals(other.path)) {
			return false;
		}
		if (this.usage == null) {
			if (other.usage != null) {
				return false;
			}
		} else if (!this.usage.equals(other.usage)) {
			return false;
		}
		return true;
	}

	@Override
	public int getInstanceNumber() {

		return this.instanceNumber;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public XmlCoordinate getStart() {
		return start;
	}

	public XmlCoordinate getEnd() {
		return end;
	}

	private int getNumberOfLine(Element element) {
		String content = XmlUtils.toString(element);
		String[] lines = content.split(System.getProperty("line.separator"));
		return lines.length;
	}

	public int getEndLine(Element element) {
		return getStartLine(element) + getNumberOfLine(element) - 1;
	}

	public int getStartLine(Element element) {
		return ((Located) element).getLine();
	}

}
