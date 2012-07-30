/*
 * Open Dynamics Engine for Java (odejava) Copyright (c) 2004, Jani Laakso, All
 * rights reserved.
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
package org.odejava;

import org.odejava.ode.Ode;
import org.odejava.ode.SWIGTYPE_p_dxTriMeshData;
import org.odejava.ode.SWIGTYPE_p_float;
import org.odejava.ode.SWIGTYPE_p_int;

/**
 * A triangle mesh (TriMesh) represents an arbitrary collection of triangles.
 * The triangle mesh collision system has the following features:
 * <p>
 *
 * Any triangle soup can be represented i.e. the triangles are not required to
 * have any particular strip, fan or grid structure. Triangle meshes can
 * interact with spheres, boxes and rays. It works well for relatively large
 * triangles. It uses temporal coherence to speed up collision tests. When a
 * geom has its collision checked with a trimesh once, data is stored inside
 * the trimesh. This data can be cleared with the dGeomTriMeshClearTCCache
 * function. In the future it will be possible to disable this functionality.
 * <p>
 * Note: give index in such way that triangles are build clockwise (z is up).
 *
 * Created 16.12.2003 (dd.mm.yyyy)
 *
 * @author Jani Laakso E-mail: jani.laakso@itmill.com
 * @see http://odejava.dev.java.net
 *
 */
public class GeomTriMesh extends PlaceableGeom {

    /** Vertex values that make up this triangle mesh */
    private float[] vertices;

    /** Optional normal values that make up this triangle mesh */
    private float[] normals;

    /** Index values into the vertex/normal arrays for the mesh */
    private int[] indices;

    /**
     * Convenience constructor to create a new geometry triangle mesh that
     * is made from pure triangles without indices. Each set of 3 vertices
     * defines a triangle to be used. Internally this will create an index
     * array for you to make use of.
     *
     * @param vertices The collection of vertices to make the mesh from
     * @param numVertices The number of valid vertices to use (must be a
     *   multiple of 3)
     */
    public GeomTriMesh(float[] vertices, int numVertices) {
        super(null);

        int[] indices = new int[numVertices];
        for(int i = 0; i < numVertices; i++)
            indices[i] = i;

        this.vertices = vertices;
        this.indices = indices;

        init(vertices, null, indices, numVertices);
    }

    /**
     * Convenience constructor to create a new geometry triangle mesh that
     * is made from pure triangles without indices. Each set of 3 vertices
     * defines a triangle to be used. Internally this will create an index
     * array for you to make use of.
     *
     * @param vertices The collection of vertices to make the mesh from
     * @param normals The collection of face normals for each triangle
     * @param numVertices The number of valid vertices to use (must be a
     *   multiple of 3)
     */
    public GeomTriMesh(float[] vertices, float[] normals, int numVertices) {
        super(null);

        int[] indices = new int[numVertices];
        for(int i = 0; i < numVertices; i++)
            indices[i] = i;

        this.vertices = vertices;
        this.indices = indices;
        this.normals = normals;

        init(vertices, normals, indices, numVertices);
    }

    /**
     * Create a tri mesh from a set of vertices and indices, and no name set.
     *
     * @param vertices A list of vertices to create the geom with
     * @param indices A list of indices to define the triangles
     */
    public GeomTriMesh(float[] vertices, int[] indices) {
        super(null);

        this.vertices = vertices;
        this.indices = indices;

        init(vertices, null, indices, indices.length);
    }

    /**
     * Create a tri mesh from a set of vertices and indices, and no name set.
     *
     * @param vertices A list of vertices to create the geom with
     * @param normals The collection of face normals for each triangle
     * @param indices A list of indices to define the triangles
     */
    public GeomTriMesh(float[] vertices, float[] normals, int[] indices) {
        super(null);

        this.vertices = vertices;
        this.indices = indices;
        this.normals = normals;

        init(vertices, normals, indices, indices.length);
    }

    /**
     * Create trimesh geometry to specific space. Vertices are defined by x,y,z
     * groups in a single array.
     *
     * @param name An optional name to associate with this geometry
     * @param vertices A list of vertices to create the geom with
     * @param indices A list of indices to define the triangles
     */
    public GeomTriMesh(String name, float[] vertices, int[] indices) {
        super(name);

        this.vertices = vertices;
        this.indices = indices;

        init(vertices, null, indices, indices.length);
    }

    /**
     * Create trimesh geometry to specific space. Vertices are defined by x,y,z
     * groups in a single array.
     *
     * @param name An optional name to associate with this geometry
     * @param vertices A list of vertices to create the geom with
     * @param normals The collection of face normals for each triangle
     * @param indices A list of indices to define the triangles
     */
    public GeomTriMesh(String name,
                       float[] vertices,
                       float[] normals,
                       int[] indices) {
        super(name);

        this.vertices = vertices;
        this.indices = indices;
        this.normals = normals;

        init(vertices, normals, indices, indices.length);
    }

    /**
     * Get the list of indices that define these triangles.
     *
     * @return The index list
     */
    public int[] getIndices() {
        return indices;
    }

    /**
     * Get the list of vertices that contribute to the mesh.
     *
     * @return The vertex list.
     */
    public float[] getVertices() {
        return vertices;
    }

    /**
     * Get the list of normals that contribute to the mesh. If no normals were
     * defined, return null.
     *
     * @return The vertex list.
     */
    public float[] getNormals() {
        return normals;
    }

    /**
     * Common internal initialisation method.
     *
     * @param vertices A list of vertices to create the geom with
     * @param normals The collection of face normals for each triangle
     * @param indices A list of indices to define the triangles
     * @param numIndices The number of valid vertices to use (must be a
     *   multiple of 3)
     */
    private void init(float[] vertices,
                      float[] normals,
                      int[] indices,
                      int numIndices) {

        // Create ODE vertices
        SWIGTYPE_p_float tmpVertices = Ode.new_floatArray(vertices.length);
        for (int i = 0; i < vertices.length; i++) {
            Ode.floatArray_setitem(tmpVertices, i, vertices[i]);
        }

        // Create ODE indices
        SWIGTYPE_p_int tmpIndices = Odejava.createSwigArray(indices);

        // Create ODE TriMeshData
        SWIGTYPE_p_dxTriMeshData data = Ode.dGeomTriMeshDataCreate();

        // Build ODE TriMesh
        //Ode.dGeomTri

        if(normals != null) {
            SWIGTYPE_p_float tmpNormals = Odejava.createSwigArray(normals);

            Ode.dGeomTriMeshDataBuildSingle1(
                data,
                tmpVertices,
                12,
                vertices.length/3,
                tmpIndices,
                numIndices,
                12,
                tmpNormals);
        } else {
            Ode.dGeomTriMeshDataBuildSingle(
                data,
                tmpVertices,
                12,
                vertices.length/3,
                tmpIndices,
                indices.length,
                12);
        }

        spaceId = Ode.getPARENTSPACEID_ZERO();

        // Create ODE TriMesh
        geomId = Ode.dCreateTriMesh(spaceId, data, null, null, null);

        updateNativeAddr();
    }
}
