package TestGame;


import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

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

	private float specularPower;

	public Renderer () {
		tranformation = new Transformation();
		specularPower = 10f;
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
        shaderProgram.createMaterialUniform("mat");
        shaderProgram.createUniform("specularPower");
        shaderProgram.createUniform("ambientLight");
        shaderProgram.createPointLightUniform("pointLight");
        shaderProgram.createDirectionalLightUniform("dirLight");

    }


    public void render(BasixWindow window, BItem[] gameItems, BCamera camera, Vector3f ambientLight, PointLight light, SpotLight spotLight, DirectionalLight dirLight) {
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

    		shaderProgram.setUniform("ambientLight", ambientLight);
    		shaderProgram.setUniform("specularPower", specularPower);

			// Get a copy of the light object and transform its position to view coordinates
			PointLight currPointLight = new PointLight(light);
			Vector3f lightPos = currPointLight.getPosition();
			Vector4f aux = new Vector4f(lightPos, 1);
			aux.mul(viewMatrix);
			lightPos.x = aux.x;
			lightPos.y = aux.y;
			lightPos.z = aux.z;
			shaderProgram.setUniform("pointLight", currPointLight);

			DirectionalLight currentDirLight = new DirectionalLight(dirLight);
			Vector4f dir = new Vector4f(currentDirLight.getDirection(), 0);
			dir.mul(viewMatrix);
			currentDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
			shaderProgram.setUniform("dirLight", currentDirLight);



			for(BItem item : gameItems){
				//might need to be outside of this loop
				shaderProgram.setUniform("texture_sampler", 0);
				BMesh mesh = item.getMesh();
    			Matrix4f modelViewMatrix = tranformation.getModelViewMatrix(item, viewMatrix);
    			shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
    			//shaderProgram.setUniform("colour", mesh.getColour());
    			//shaderProgram.setUniform("useColour", mesh.isTextured() ? 0 : 1);
				shaderProgram.setUniform("mat", mesh.getMaterial());
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
