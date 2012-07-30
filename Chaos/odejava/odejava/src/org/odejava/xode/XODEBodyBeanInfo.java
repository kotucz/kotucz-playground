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

import java.awt.Image;
import java.beans.*;

/**
 *
 * BeanInfo for the XODEBody class
 *
 * @author Levent Bayindir
 */
public class XODEBodyBeanInfo extends SimpleBeanInfo{
    
    public Image getIcon(int iconKind) {
        if (iconKind == BeanInfo.ICON_MONO_16x16 ||
                iconKind == BeanInfo.ICON_COLOR_16x16 ) {
            Image img = loadImage("data/icons/body16.gif");
            return img;
        }
        if (iconKind == BeanInfo.ICON_MONO_32x32 ||
                iconKind == BeanInfo.ICON_COLOR_32x32 ) {
            Image img = loadImage("data/icons/body32.gif");
            return img;
        }
        return null;
    }
    
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            /*
            PropertyDescriptor finiteRotationMode =
                    new PropertyDescriptor("finiteRotationMode", XODEBody.class);
            PropertyDescriptor finiteRotationAxisX =
                    new PropertyDescriptor("finiteRotationAxisX", XODEBody.class);
            PropertyDescriptor finiteRotationAxisY =
                    new PropertyDescriptor("finiteRotationAxisY", XODEBody.class);
            PropertyDescriptor finiteRotationAxisZ =
                    new PropertyDescriptor("finiteRotationAxisZ", XODEBody.class);
             */
            PropertyDescriptor totalMass =
                    new PropertyDescriptor("totalMass", XODEBody.class);
            
            //finiteRotationMode.setBound(true);
            //finiteRotationAxisX.setBound(true);
            //finiteRotationAxisY.setBound(true);
            //finiteRotationAxisZ.setBound(true);
            totalMass.setBound(true);
            
            PropertyDescriptor rv[] =
            {totalMass};
            
            return rv;
            
        } catch (IntrospectionException e) {
            throw new Error(e.toString());
        }
    }
    
}
