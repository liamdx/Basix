package TestGame;


import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.*;

import java.nio.FloatBuffer;
import java.util.BitSet;

import org.lwjgl.system.MemoryUtil;

import EngineCore.*;


public class Renderer {
	
	private BShader shaderProgram;

	private static final float FOV = (float) Math.toRadians(70.0f);

	private static final float Z_NEAR = 0.01f;

	private static final float Z_FAR = 1000.0f;

	private float aspectRatio;

	private final Transformation tranformation;

	public Renderer () {
		tranformation = new Transformation();
	}
	
    public void init(BasixWindow window) throws Exception {

		aspectRatio = (float)window.getWidth() / window.getHeight();
    		// Create a shader to use for the created model
		shaderProgram = new BShader();
        shaderProgram.createVertexShader(BasixUtils.loadResource("/resources/vertex.vs"));
        shaderProgram.createFragmentShader(BasixUtils.loadResource("/resources/fragment.fs"));
        shaderProgram.link();



        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");
        shaderProgram.createUniform("colour");
        shaderProgram.createUniform("useColour");


    }


    public void render(BasixWindow window, BItem[] gameItems, BCamera camera) {
    		clear();
    		
    		if (window.isResized()) {
    			glViewport(0, 0, window.getWidth(), window.getHeight());
				aspectRatio = (float)window.getWidth() / window.getHeight();
    			window.setResized(false);
    		}

    		shaderProgram.bind();

    		Matrix4f projectionMatrix = tranformation.getProjectionMatrix(FOV,window.getWidth(),window.getHeight(),Z_NEAR,Z_FAR);
    		shaderProgram.setUniform("projectionMatrix", projectionMatrix);

    		Matrix4f viewMatrix = tranformation.getViewMatrix(camera);

			shaderProgram.setUniform("texture_sampler", 0);

			for(BItem item : gameItems){
				BMesh mesh = item.getMesh();
    			Matrix4f modelViewMatrix = tranformation.getModelViewMatrix(item, viewMatrix);
    			shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
    			//shaderProgram.setUniform("colour", mesh.getColour());
    			shaderProgram.setUniform("useColour", mesh.isTextured() ? 0 : 1);
    			item.getMesh().render();
			}
    		shaderProgram.unbind();
    		
    }
    
    public void cleanup() {
    		
    		if(shaderProgram != null) {
    			shaderProgram.cleanup();
    		}
    		
    
    }

	public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

}
