package EngineCore;

import de.matthiasmann.twl.utils.PNGDecoder;
//import sun.awt.image.PNGImageDecoder;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;


public class BTexture {

    private final int id;


    public BTexture (String fileName) throws Exception {
        this(loadTexture(fileName));

    }

    public BTexture(int id){
        this.id = id;
    }

    public void bindTexture(){
        glBindTexture(GL_TEXTURE_2D,id);
    }

    public int getId(){
        return id;
    }

    public static int loadTexture(String filename) throws Exception{

        PNGDecoder decoder = new PNGDecoder(BTexture.class.getResourceAsStream(filename));
        ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        buf.flip();

        int textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(),decoder.getHeight(),0,GL_RGBA, GL_UNSIGNED_BYTE, buf);

        glGenerateMipmap(GL_TEXTURE_2D);

        return textureId;
    }

    public void cleanUp(){
        glDeleteTextures(id);
    }

}
