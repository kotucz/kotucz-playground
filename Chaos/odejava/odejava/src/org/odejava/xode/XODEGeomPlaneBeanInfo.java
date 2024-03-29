/*
 * Copyright (c) 2004, William Denniss. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright 
 *   notice, this list of conditions and the following disclaimer.
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

import java.beans.*;

/**
 *
 * BeanInfo for the XODEGeomPlane class
 *
 * @author Levent Bayindir
 */
public class XODEGeomPlaneBeanInfo extends SimpleBeanInfo {
    
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor a =
                    new PropertyDescriptor("a", XODEGeomPlane.class);
            PropertyDescriptor b =
                    new PropertyDescriptor("b", XODEGeomPlane.class);
            
            PropertyDescriptor c =
                    new PropertyDescriptor("c", XODEGeomPlane.class);
            
            PropertyDescriptor d =
                    new PropertyDescriptor("d", XODEGeomPlane.class);
                                    
            
            a.setBound(true);
            b.setBound(true);            
            c.setBound(true);
            d.setBound(true);            
                        
            PropertyDescriptor rv[] =
            {a, b, c, d};
            return rv;
            
        } catch (IntrospectionException e) {
            throw new Error(e.toString());
        }
    }  
}
