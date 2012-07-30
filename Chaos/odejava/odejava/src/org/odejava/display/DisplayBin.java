/*
 * Open Dynamics Engine for Java (odejava) Copyright (c) 2004, Odejava Project
 * Group, All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials
 * provided with the distribution. Neither the name of the odejava nor the
 * names of its contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.odejava.display;

import java.util.*;

/**
 * <p>Stores a List of BoundDisplayObjectS.  The DisplayBin is used by applications
 * to sync the transforms of their DisplayObjects with their ODE counterparts.</p>
 *
 * <p>For example, in Xith3D, one would link the TransformGroup which represents a box
 * object to an ODE box geometry object that is attached to a body.  By calling updateAll()
 * on this class, the Xith3D representation of the box will be updated to mirror that of the ODE
 * representation bringing the simulation to life.</p>
 *
 * @author William Denniss
 */
public class DisplayBin {
    /**
     * List containing all registered BoundDisplayObjectS
     */
    private List boundObjects;

    /**
     * Creates a DisplayBin with the default list type
     */
    public DisplayBin () {
        boundObjects = new ArrayList();
    }

    /**
     * Creates a DisplayBin with a new instance of the passed
     * List class.
     *
     * @param listType The List whose class will be used to contain the BoundDisplayObjectS
     */
    public DisplayBin (List listType) {
        try {
            boundObjects = (List) listType.getClass().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a BoundDisplayObject.
     *
     * @param toAdd the BoundDisplayObject to add.
     */
    public void add(BoundDisplayObject toAdd) {
        boundObjects.add(toAdd);
    }

    /**
     * Removes a BoundDisplayObject.
     *
     * @param toRemove the BoundDisplayObject to remove.
     */
    public void remove(BoundDisplayObject toRemove) {
        boundObjects.remove(toRemove);
    }

    /**
     * Adds all BoundDisplayObjects from the given DisplayBin into this one
     *
     * @param toAdd the DisplayBin whose objects will be added
     */
    public void add(DisplayBin toAdd) {
        for (Iterator i = toAdd.iterator(); i.hasNext(); ) {
            add((BoundDisplayObject) i.next());
        }

    }

    /**
     * Removes all BoundDisplayObjects from the given DisplayBin into this one
     *
     * @param toAdd the DisplayBin whose objects will be removed
     */
    public void remove(DisplayBin toAdd) {
        for (Iterator i = toAdd.iterator(); i.hasNext(); ) {
            remove((BoundDisplayObject) i.next());
        }

    }

    /**
     * Returns an Iterator of the list of BoundDisplayObjectS
     *
     * @return  an Iterator of the list of BoundDisplayObjectS
     */
    public Iterator iterator () {
        return listIterator();
    }

    /**
     * Returns a ListIterator of the list of BoundDisplayObjectS
     *
     * @return a ListIterator of the list of BoundDisplayObjectS
     */
    public ListIterator listIterator () {
        return boundObjects.listIterator();
    }


    /**
    * Calls update on all contained BoundDisplayObjectS
    */
    public void updateAll() {

        // If it's RandomAccess, then direct fetches are faster and more
        // efficient. Also, it doesn't generate garbage from having to fetch
        // an iterator.
        if(boundObjects instanceof RandomAccess) {
            ListIterator iterator = boundObjects.listIterator();

            int size = boundObjects.size();
            for (int i = 0; i < size; i++) {
                ((BoundDisplayObject) boundObjects.get(i)).update();
            }
        } else {
            ListIterator iterator = boundObjects.listIterator();

            while (iterator.hasNext()) {
                ((BoundDisplayObject) iterator.next()).update();
            }
        }
    }
}
