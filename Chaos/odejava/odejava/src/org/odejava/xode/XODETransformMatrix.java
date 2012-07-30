/*
 * Created on Mar 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.odejava.xode;

import javax.vecmath.Matrix4f;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author williamdenniss
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class XODETransformMatrix extends XODETransform {
    
    private Matrix4f relTransform = null;
    
    public XODETransformMatrix(Node xodeTransformNode) {

        NodeList transformChildren = xodeTransformNode.getChildNodes();
		
		for (int j = 0; j < transformChildren.getLength(); j++) {
			Node currentNode = transformChildren.item(j);
			
			if (currentNode.getNodeName().equals("matrix")) {
				relTransform.m00 = Float.parseFloat(currentNode.getAttributes().getNamedItem("m00").getNodeValue());
				relTransform.m01 = Float.parseFloat(currentNode.getAttributes().getNamedItem("m01").getNodeValue());
				relTransform.m02 = Float.parseFloat(currentNode.getAttributes().getNamedItem("m02").getNodeValue());
				relTransform.m03 = Float.parseFloat(currentNode.getAttributes().getNamedItem("m03").getNodeValue());

				relTransform.m10 = Float.parseFloat(currentNode.getAttributes().getNamedItem("m10").getNodeValue());
				relTransform.m11 = Float.parseFloat(currentNode.getAttributes().getNamedItem("m11").getNodeValue());
				relTransform.m12 = Float.parseFloat(currentNode.getAttributes().getNamedItem("m12").getNodeValue());
				relTransform.m13 = Float.parseFloat(currentNode.getAttributes().getNamedItem("m13").getNodeValue());

				relTransform.m20 = Float.parseFloat(currentNode.getAttributes().getNamedItem("m20").getNodeValue());
				relTransform.m21 = Float.parseFloat(currentNode.getAttributes().getNamedItem("m21").getNodeValue());
				relTransform.m22 = Float.parseFloat(currentNode.getAttributes().getNamedItem("m22").getNodeValue());
				relTransform.m23 = Float.parseFloat(currentNode.getAttributes().getNamedItem("m23").getNodeValue());

				relTransform.m30 = Float.parseFloat(currentNode.getAttributes().getNamedItem("m30").getNodeValue());
				relTransform.m31 = Float.parseFloat(currentNode.getAttributes().getNamedItem("m31").getNodeValue());
				relTransform.m32 = Float.parseFloat(currentNode.getAttributes().getNamedItem("m32").getNodeValue());
				relTransform.m33 = Float.parseFloat(currentNode.getAttributes().getNamedItem("m33").getNodeValue());
			}
		}
	
    }
    
    public Element buildElement(Document doc) {
	    Element element = doc.createElement("transform");
	    
	    Element matrix = doc.createElement("matrix");
	    matrix.setAttribute("m00", relTransform.m00+"");
	    matrix.setAttribute("m01", relTransform.m01+"");
	    matrix.setAttribute("m02", relTransform.m02+"");
	    matrix.setAttribute("m03", relTransform.m03+"");

	    matrix.setAttribute("m10", relTransform.m10+"");
	    matrix.setAttribute("m11", relTransform.m11+"");
	    matrix.setAttribute("m12", relTransform.m12+"");
	    matrix.setAttribute("m13", relTransform.m13+"");

	    matrix.setAttribute("m20", relTransform.m20+"");
	    matrix.setAttribute("m21", relTransform.m21+"");
	    matrix.setAttribute("m22", relTransform.m22+"");
	    matrix.setAttribute("m23", relTransform.m23+"");

	    matrix.setAttribute("m30", relTransform.m30+"");
	    matrix.setAttribute("m31", relTransform.m31+"");
	    matrix.setAttribute("m32", relTransform.m32+"");
	    matrix.setAttribute("m33", relTransform.m33+"");

	    element.appendChild(matrix);
	    
	    return element;
    }
    
    public Matrix4f getRelTransform() {
        return new Matrix4f(relTransform);
    }
    
    public void setTransformMatrix(Matrix4f matrix) {
        this.relTransform = matrix;
    }
}
