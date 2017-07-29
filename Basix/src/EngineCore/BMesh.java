package EngineCore;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;


import  static  org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL13;
import org.lwjgl.system.MemoryUtil;


public class BMesh {

    private final int vaoId;

    private final List<Integer> vboIdList;

    private final int vertexCount;

    private BMaterial mat;

    private Vector3f colour;

    public BMesh(float[] positions, float[] texCoords, float[] normals, int[] indices){
        FloatBuffer vertsBuffer = null;
        IntBuffer vertsIdxBuffer = null;
        FloatBuffer texCoordsBuffer = null;
        FloatBuffer vecNormalsBuffer = null;


        try{

            vertexCount = indices.length;
            System.out.println("Vertex Count = " + vertexCount);

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);
            vboIdList = new ArrayList<>();

            //vertex location buffer
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            vertsBuffer = MemoryUtil.memAllocFloat(positions.length);
            vertsBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, vertsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0,3,GL_FLOAT,false,0,0);



            //Texture coords
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            texCoordsBuffer = MemoryUtil.memAllocFloat(texCoords.length);
            texCoordsBuffer.put(texCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER,vboId);
            glBufferData(GL_ARRAY_BUFFER,texCoordsBuffer,GL_STATIC_DRAW);
            MemoryUtil.memFree(texCoordsBuffer);
            glVertexAttribPointer(1,2,GL_FLOAT,false,0,0);

            //Vertex Normals VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.length);
            vecNormalsBuffer.put(normals).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(2,3,GL_FLOAT,false,0,0);


            // Vertex Index Buffer
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            vertsIdxBuffer = MemoryUtil.memAllocInt(indices.length);
            vertsIdxBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER,vertsIdxBuffer,GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER,0);
            glBindVertexArray(0);

        } finally{

            if(vertsBuffer != null){
                MemoryUtil.memFree(vertsBuffer);
            }

            if(texCoordsBuffer != null){
                MemoryUtil.memFree(texCoordsBuffer);
            }
            if(vertsIdxBuffer != null){
                MemoryUtil.memFree(vertsIdxBuffer);
            }
        }
    }

    public int getVaoId(){
        return vaoId;
    }

    public int getVertexCount(){
        return vertexCount;
    }

    public boolean isTextured(){
        return this.mat.isTextured();
    }

    public BMaterial getMaterial(){
        return this.mat;
    }

    public void setMaterial(BMaterial mat){
        this.mat = mat;
    }

    public Vector3f getColour(){
        return this.colour;
    }

    public void setColour(Vector3f colour){
        this.colour = colour;
    }

    public void render(){

        BTexture tex = mat.getTex();

        if (tex != null) {
            //Not working for some reason, what GLXX package is this from?
            GL13.glActiveTexture(GL_TEXTURE0);

            glBindTexture(GL_TEXTURE_2D, tex.getId());
        }

        //bind to vao
        glBindVertexArray(getVaoId());
        //enable positions buffer
        glEnableVertexAttribArray(0);
        //enable colour buffer
        glEnableVertexAttribArray(1);
        // enable normals buffer
        glEnableVertexAttribArray(2);

        //draw verts
        glDrawElements(GL_TRIANGLES, getVertexCount(),GL_UNSIGNED_INT,0);

        //restore state (?)
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);

    }

    public void cleanUp(){
        glDisableVertexAttribArray(0);

        //fully delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER,0);

        for (int vboId : vboIdList){
            glDeleteBuffers(vboId);
        }

        BTexture tex = mat.getTex();
        if(tex != null){
            tex.cleanUp();
        }

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}
