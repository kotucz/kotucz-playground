/*
 *      @(#)TexturedPlane.java 1.0 99/08/02 16:06:00
 *
 * Copyright (c) 1996-1999 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */
/*
   A class to create a simple textured plane.
 
 */
package cz.kotuc.chaos;

import com.sun.j3d.utils.image.TextureLoader;
//import NewTextureLoader;
import javax.media.j3d.*;
import javax.vecmath.*;

/**
 * TexturedPlane creates a single plane with texture mapping.
 */

  public class TexturedPlane extends Shape3D {
    TexturedPlane(String filename) {
        if (NewTextureLoader.getImageObserver() == null)
            System.out.println("call NewTextureLoader.setImageObserver()");
        this.setGeometry(createGeometry());
        if(filename != "")
                this.setAppearance(createAppearance(filename));
    }    

    Geometry createGeometry(){

        QuadArray plane = new QuadArray(4, GeometryArray.COORDINATES
                                           | GeometryArray.TEXTURE_COORDINATE_2
							);

        Point3f p = new Point3f(-1.0f,  1.0f,  0.0f);
        plane.setCoordinate(0, p);
        p.set(-1.0f, -1.0f,  0.0f);
        plane.setCoordinate(1, p);
        p.set(1.0f, -1.0f,  0.0f);
        plane.setCoordinate(2, p);
        p.set(1.0f,  1.0f,  0.0f);
        plane.setCoordinate(3, p);

        Point2f q = new Point2f( 0.0f,  1.0f);
        plane.setTextureCoordinate(0, q);
        q.set(0.0f, 0.0f);
        plane.setTextureCoordinate(1, q);
        q.set(1.0f, 0.0f);
        plane.setTextureCoordinate(2, q);
        q.set(1.0f, 1.0f);
        plane.setTextureCoordinate(3, q);

        return plane;
    }

    Appearance createAppearance(String filename) {

        Appearance appear = new Appearance();

        System.out.println("TexturedPlane attempt to load file: "+filename);
        TextureLoader loader = new NewTextureLoader(filename);
        ImageComponent2D image = loader.getImage();

        if(image == null) {
                System.out.println("load failed for texture: "+filename);
        }

        System.out.println("Image width  = " + image.getWidth());
        System.out.println("Image height = " + image.getHeight());
        
        // can't use parameterless constuctor
        Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                          image.getWidth(), image.getHeight());
        texture.setImage(0, image);
        texture.setEnable(true);
        texture.setMagFilter(Texture.NICEST);

        appear.setTexture(texture);

        appear.setTransparencyAttributes(
           new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.1f));

        return appear;
    }

  } // end of TexturedPlane class


