package br.com.engine.system.render;

import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import br.com.engine.component.IComponent;
import br.com.engine.game.Game;

public class OpenGLRenderSystem implements IRenderSystem, GLSurfaceView.Renderer {
	GLSurfaceView surfaceView;
	
	// mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

	private List<IComponent> components;

	public OpenGLRenderSystem(Context context) {
		this.surfaceView = new GLSurfaceView(context);
		configureSurfaceView();
	}

	public OpenGLRenderSystem(GLSurfaceView surfaceView) {
		this.surfaceView = surfaceView;
		configureSurfaceView();
	}
	

	private void configureSurfaceView() {
		this.surfaceView.setEGLContextClientVersion(2);
		this.surfaceView.setRenderer(this);
		this.surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}
	

	@Override
	public void render(List<IComponent> components) {
		this.components = components;
		surfaceView.requestRender();
	}

	@Override
	public void onDrawFrame(GL10 gl) {
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        
        for (IComponent comp : components) {
        	OpenGLHolder oGL = new OpenGLHolder();
        	oGL.mvpMatrix = mMVPMatrix;
        	RenderWrapper wrapper = new RenderWrapper(oGL);
        	comp.onRender(wrapper);
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		 // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(1.0f, 2.0f, 0.0f, 1.0f);
		
		Game.getInstance().startGame();
	}

	public GLSurfaceView getSurfaceView() {
		return surfaceView;
	}

	public void setSurfaceView(GLSurfaceView surfaceView) {
		this.surfaceView = surfaceView;
	}
	
	public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("GL", glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
	
	public static int loadShader(int type, String shaderCode){
	    // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
	    // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
	    int shader = GLES20.glCreateShader(type);

	    // add the source code to the shader and compile it
	    GLES20.glShaderSource(shader, shaderCode);
	    GLES20.glCompileShader(shader);

	    return shader;
	}
}
