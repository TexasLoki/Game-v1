package de.nerogar.gameV1.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import de.nerogar.gameV1.InputHandler;
import de.nerogar.gameV1.graphics.*;

public class GElementTextField extends GElement {
	public boolean numOnly = false, aktiveMessage, active;
	private ArrayList<Character> text = new ArrayList<Character>();
	private String bgImage;
	private TextureBank textureBank = TextureBank.instance;

	public GElementTextField(float xPos, float yPos, float width, float height, String text, String bgImage) {
		this.xPos = xPos * xScale;
		this.yPos = yPos * yScale;
		this.width = width * xScale;
		this.height = height * yScale;

		for (int i = 0; i < text.length(); i++) {
			this.text.add(text.charAt(i));
		}

		this.bgImage = bgImage;
		textureBank.loadTexture(bgImage);
	}

	public void handleKeyboardInput() {
		char pressedKey = InputHandler.getPressedKey();

		if (pressedKey != 0) {
			if (numOnly) {
				if (pressedKey >= 48 && pressedKey <= 57) {
					text.add(pressedKey);
				} else if (pressedKey == 8) { //backspace
					if (text.size() > 0) text.remove(text.size() - 1);
				}
			} else {
				if (pressedKey >= 32) { //&& pressedKey <= 126) {
					text.add(pressedKey);
				} else if (pressedKey == 8) { //backspace
					if (text.size() > 0) text.remove(text.size() - 1);
				}
			}

			//System.out.println((int) pressedKey);
		}
	}

	public String getText() {
		String retText = "";
		for (int i = 0; i < text.size(); i++) {
			retText += text.get(i);
		}
		return retText;
	}

	public void setText(String newText) {
		text = new ArrayList<Character>();
		for (int i = 0; i < newText.length(); i++) {
			text.add(newText.charAt(i));
		}
	}

	public void render() {
		textureBank.bindTexture(bgImage);

		int state;

		if (!enabled) state = 0;
		else if (!active) state = 1;
		else if (active) state = 2;
		else if (aktiveMessage) state = 3;
		else state = 0;

		float yOffset = 1f / 4f * state;

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glColor3f(1.0f, 1.0f, 1.0f);

		GL11.glTexCoord2f(1, yOffset + 1f / 4f);
		GL11.glVertex3f(xPos + width, yPos + height, -1f);
		GL11.glTexCoord2f(1, yOffset);
		GL11.glVertex3f(xPos + width, yPos, -1f);
		GL11.glTexCoord2f(0, yOffset);
		GL11.glVertex3f(xPos, yPos, -1f);
		GL11.glTexCoord2f(0, yOffset + 1f / 4f);
		GL11.glVertex3f(xPos, yPos + height, -1f);

		GL11.glEnd();
		char[] textString = new char[text.size()];

		for (int i = 0; i < text.size(); i++) {
			textString[i] = text.get(i);
		}
		FontRenderer.renderFont(String.copyValueOf(textString), (int) xPos, (int) (yPos + height / 4f), width, height / 2f);
	}
}
