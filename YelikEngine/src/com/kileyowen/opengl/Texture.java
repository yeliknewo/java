package com.kileyowen.opengl;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.kileyowen.utilities.Utilities;

import static org.lwjgl.opengl.GL11.*;

public class Texture {

	private int width, height, texture;

	public Texture(String path) {
		texture = load(path);
	}

	private int load(String path) {
		int[] pixels;
		try {
			BufferedImage image = ImageIO.read(new FileInputStream(path));
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}

		int[] data = new int[pixels.length];
		for (int i = 0; i < data.length; i++) {
			int alpha = (pixels[i] & 0xFF000000) >> 24;
			int red = (pixels[i] & 0xff0000) >> 16;
			int green = (pixels[i] & 0xff00) >> 8;
			int blue = (pixels[i] & 0xff);

			data[i] = alpha << 24 | blue << 16 | green << 8 | red;
		}

		int result = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, result);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE,
				Utilities.createIntBuffer(data));

		glBindTexture(GL_TEXTURE_2D, 0);

		return result;
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, texture);
	}

	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
