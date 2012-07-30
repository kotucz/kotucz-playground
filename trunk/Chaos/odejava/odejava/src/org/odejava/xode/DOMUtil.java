/*
 * Copyright (c) 2004, William Denniss. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright 
 *
 * - Redistributions in binary form must reproduce the above 
 *   copyright notice, this list of conditions and the following 
 *   disclaimer in the documentation and/or other materials provided
 *   with the distribution.
 *
 * - Neither the name of William Denniss nor the names of
 *   contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) A
 * RISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE
 *
 */
package org.odejava.xode;

import org.w3c.dom.Node;
import java.io.*;

/**
 * Utilities
 * 
 * @author William Denniss
 */
public class DOMUtil {
	
	
	public static float attributeFloat(Node node, String attributeName) {
		
		return Float.parseFloat(attributeString(node, attributeName));
	}
	
	public static boolean attributeBoolean(Node node, String attributeName) {
		
			String attr = attributeString(node, attributeName);
			
			if (attr == null) {
				return false;
			} else {
				return attr.equalsIgnoreCase("true");
			}
		
	}
	
	public static String attributeString(Node node, String attributeName) {
		if (node.hasAttributes()) {
			Node attr = node.getAttributes().getNamedItem(attributeName);
			if (attr != null) {
				return attr.getNodeValue();
			}
		}
		
		return null;

	}
	

   /*
    *  Returns a deep copy of given object
    *
    *  @param oldObj object to be copied
    *  @return deep copy of the given object
    */
   static public Object deepCopy(Object oldObj) throws Exception
   {
      ObjectOutputStream oos = null;
      ObjectInputStream ois = null;
      try
      {
         ByteArrayOutputStream bos = 
               new ByteArrayOutputStream();
         oos = new ObjectOutputStream(bos);
         
         // serialize and pass the object
         oos.writeObject(oldObj);   
         oos.flush();               
         ByteArrayInputStream bin = 
               new ByteArrayInputStream(bos.toByteArray()); 
         ois = new ObjectInputStream(bin);                  
         
         // return the new object
         return ois.readObject(); 
      }
      catch(Exception e)
      {
         System.out.println("Exception in ObjectCloner = " + e);
         throw(e);
      }
      finally
      {
         oos.close();
         ois.close();
      }
   }
}
