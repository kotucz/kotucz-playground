/*
 * Created on Mar 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.odejava.xode;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

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
public class XODETransformVector extends XODETransform {
    
    private Vector3f position = null;
    private Vector3f euler = null;
    private Quat4f quaternion = null;
    private AxisAngle4f axisangle = null;
    private Float scale = null;
    
    public XODETransformVector(Node xodeTransformNode) {

        NodeList transformChildren = xodeTransformNode.getChildNodes();
				
		for (int j = 0; j < transformChildren.getLength(); j++) {
			
			Node currentNode = transformChildren.item(j);
			
			if (currentNode.getNodeName().equals("position")) {
			
				position = new Vector3f(
					Float.parseFloat(currentNode.getAttributes().getNamedItem("x").getNodeValue()),
					Float.parseFloat(currentNode.getAttributes().getNamedItem("y").getNodeValue()),
					Float.parseFloat(currentNode.getAttributes().getNamedItem("z").getNodeValue())
				);
				
			} else if (currentNode.getNodeName().equals("rotation")) {
				
				NodeList kChildren = currentNode.getChildNodes(); 
				
				for (int k = 0; k < kChildren.getLength(); k++) {
					
					Node currentNodeK = kChildren.item(k);

					if (currentNodeK.getNodeName().equals("euler")) {
						
						boolean degrees = false;
						Node aformat = currentNodeK.getAttributes().getNamedItem("aformat");
						if (aformat != null) {
							degrees = aformat.getNodeValue().equalsIgnoreCase("degrees");
						}
						
						euler = new Vector3f(
							Float.parseFloat(currentNodeK.getAttributes().getNamedItem("x").getNodeValue()),
							Float.parseFloat(currentNodeK.getAttributes().getNamedItem("y").getNodeValue()),
							Float.parseFloat(currentNodeK.getAttributes().getNamedItem("z").getNodeValue())
						);
						
						if (degrees) {
							euler.x = (float) Math.toRadians(euler.x);
							euler.y = (float) Math.toRadians(euler.y);
							euler.z = (float) Math.toRadians(euler.z);
						}
						XODEParserDOM.log.debug(euler);
						
					} else if (currentNodeK.getNodeName().equals("axisangle")) {
						boolean degrees = false;
						Node aformat = currentNodeK.getAttributes().getNamedItem("aformat");
						if (aformat != null) {
							degrees = aformat.getNodeValue().equalsIgnoreCase("degrees");
						}							
						axisangle = new AxisAngle4f(
								Float.parseFloat(currentNodeK.getAttributes().getNamedItem("x").getNodeValue()),
								Float.parseFloat(currentNodeK.getAttributes().getNamedItem("y").getNodeValue()),
								Float.parseFloat(currentNodeK.getAttributes().getNamedItem("z").getNodeValue()),
								Float.parseFloat(currentNodeK.getAttributes().getNamedItem("angle").getNodeValue())
						);					
						if (axisangle != null) {
							if (degrees) {
								axisangle.angle = (float) Math.toRadians(axisangle.angle);
							}

							
						}							
					} else if (currentNodeK.getNodeName().equals("quaternion")) {
						boolean degrees = false;
						Node aformat = currentNodeK.getAttributes().getNamedItem("aformat");
						if (aformat != null) {
							degrees = aformat.getNodeValue().equalsIgnoreCase("degrees");
						}		
						quaternion = new Quat4f(
								Float.parseFloat(currentNodeK.getAttributes().getNamedItem("x").getNodeValue()),
								Float.parseFloat(currentNodeK.getAttributes().getNamedItem("y").getNodeValue()),
								Float.parseFloat(currentNodeK.getAttributes().getNamedItem("z").getNodeValue()),
								Float.parseFloat(currentNodeK.getAttributes().getNamedItem("w").getNodeValue())
						);
					}
				}				
			} else if (currentNode.getNodeName().equals("scale")) {
				
				scale = new Float(currentNode.getAttributes().getNamedItem("scale").getNodeValue());
			}
		}
		
    }
	
    public Matrix4f getRelTransform() {
        Matrix4f relTransform = new Matrix4f();
        
		relTransform.setIdentity();
		
		if (euler != null) {
			
			Matrix4f tmp = new Matrix4f();
			if (euler.x != 0f) {
				tmp.setIdentity();
				tmp.rotX(euler.x);
				relTransform.mul(tmp);
			}
			if (euler.y != 0f) {
				tmp.setIdentity();
				tmp.rotY(euler.y);
				relTransform.mul(tmp);
			}
			if (euler.z != 0f) {
				tmp.setIdentity();
				tmp.rotZ(euler.z);
				relTransform.mul(tmp);
			}
		}

		if (quaternion != null) {
			XODEParserDOM.odeLog.debug("quaternion set");
			relTransform.setRotation(quaternion);
		}
		
		if (axisangle != null) {
			XODEParserDOM.odeLog.debug("axisangle set");
		    relTransform.setRotation(axisangle);
		}
					
		if (position != null) {
			XODEParserDOM.odeLog.debug("position set");
			relTransform.setTranslation(position);
		}
		
		if (scale != null) {
		    relTransform.setScale(scale.floatValue());	
		}
	
        return relTransform;
    }
    
    public Element buildElement(Document doc) {
	    Element element = doc.createElement("transform");
	    
	    if (euler != null) {
		    Element eulerElement = doc.createElement("euler");
		    eulerElement.setAttribute("x", euler.x +"");
		    eulerElement.setAttribute("y", euler.y +"");
		    eulerElement.setAttribute("z", euler.z +"");
	        
		    element.appendChild(eulerElement);
		}

		if (quaternion != null) {
		    Element quaternionElement = doc.createElement("quaternion");
		    quaternionElement.setAttribute("x", quaternion.x +"");
		    quaternionElement.setAttribute("y", quaternion.y +"");
		    quaternionElement.setAttribute("z", quaternion.z +"");
		    quaternionElement.setAttribute("w", quaternion.w +"");
		    
		    element.appendChild(quaternionElement);
		}
		
		if (axisangle != null) {
		    Element axisangleElement = doc.createElement("axisangle");
		    axisangleElement.setAttribute("x", axisangle.x +"");
		    axisangleElement.setAttribute("y", axisangle.y +"");
		    axisangleElement.setAttribute("z", axisangle.z +"");
		    axisangleElement.setAttribute("angle", axisangle.angle +"");
		    
		    element.appendChild(axisangleElement);
		}
					
		if (position != null) {
		    Element positionElement = doc.createElement("position");
		    positionElement.setAttribute("x", position.x +"");
		    positionElement.setAttribute("y", position.y +"");
		    positionElement.setAttribute("z", position.z +"");
		    
		    element.appendChild(positionElement);
		}
		
		if (scale != null) {
		    element.setAttribute("scale", scale.floatValue() +"");
		}
	    
	    return element;
    }

    /**
     * @return Returns the axisangle.
     */
    public AxisAngle4f getAxisangle() {
        return new AxisAngle4f(axisangle);
    }
    /**
     * @param axisangle The axisangle to set.
     */
    public void setAxisangle(AxisAngle4f axisangle) {
        this.axisangle = axisangle;
    }
    /**
     * @return Returns the euler.
     */
    public Vector3f getEuler() {
        return new Vector3f(euler);
    }
    /**
     * @param euler The euler to set.
     */
    public void setEuler(Vector3f euler) {
        this.euler = euler;
    }
    /**
     * @return Returns the position.
     */
    public Vector3f getPosition() {
        return new Vector3f(position);
    }
    /**
     * @param position The position to set.
     */
    public void setPosition(Vector3f position) {
        this.position = position;
    }
    /**
     * @return Returns the quaternion.
     */
    public Quat4f getQuaternion() {
        return new Quat4f(quaternion);
    }
    /**
     * @param quaternion The quaternion to set.
     */
    public void setQuaternion(Quat4f quaternion) {
        this.quaternion = quaternion;
    }
    /**
     * @return Returns the scale.
     */
    public Float getScale() {
        return scale;
    }
    /**
     * @param scale The scale to set.
     */
    public void setScale(Float scale) {
        this.scale = scale;
    }
}
