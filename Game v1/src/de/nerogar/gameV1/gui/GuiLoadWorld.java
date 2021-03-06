package de.nerogar.gameV1.gui;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.RenderHelper;
import de.nerogar.gameV1.SaveProvider;

public class GuiLoadWorld extends Gui {
	private GElementButton loadButton;
	private GElementButton renameButton;
	private GElementButton deleteButton;
	private GElementButton createWorldButton;
	private GElementButton backButton;

	private GElementListBox saveList;
	private AlertGetMessage newWorldNameAlert;
	private AlertYesNo deleteWorldAlert;
	SaveProvider saveProvider;

	public GuiLoadWorld(Game game) {
		super(game);
	}

	@Override
	public boolean pauseGame() {
		return true;
	}

	@Override
	public String getName() {
		return "createWorld";
	}

	@Override
	public void init() {
		saveProvider = new SaveProvider();

		saveProvider.updateSaves();
		String[] saveNames = saveProvider.getSavesAsStrings();

		addGElement(new GElementTextLabel(genNewID(), 0.0f, 0.05f, 0.4f, 0.1f, "Singleplayer", FontRenderer.CENTERED));

		loadButton = new GElementButton(genNewID(), 0.05f, 0.3f, 0.3f, 0.1f, "load world", FontRenderer.CENTERED, "buttons/button.png", false, "");
		loadButton.enabled = false;

		renameButton = new GElementButton(genNewID(), 0.05f, 0.4f, 0.3f, 0.1f, "rename world", FontRenderer.CENTERED, "buttons/button.png", false, "");
		renameButton.enabled = false;

		deleteButton = new GElementButton(genNewID(), 0.05f, 0.5f, 0.3f, 0.1f, "delete world", FontRenderer.CENTERED, "buttons/button.png", false, "");
		deleteButton.enabled = false;

		createWorldButton = new GElementButton(genNewID(), 0.05f, 0.65f, 0.3f, 0.1f, "create new world", FontRenderer.CENTERED, "buttons/button.png", false, "");

		backButton = new GElementButton(genNewID(), 0.05f, 0.8f, 0.3f, 0.1f, "back", FontRenderer.CENTERED, "buttons/button.png", false, "");

		saveList = new GElementListBox(genNewID(), 0.4f, 0.0f, 0.6f, 1.0f, saveNames, "buttons/button.png", "buttons/scrollbar.png");
		saveList.showedItems = 10;

		addGElement(saveList);

		addGElement(loadButton);
		addGElement(renameButton);
		addGElement(deleteButton);
		addGElement(createWorldButton);
		addGElement(backButton);

		newWorldNameAlert = new AlertGetMessage(game, "Enter new worldname.", true);
		deleteWorldAlert = new AlertYesNo(game, "delete?");
	}

	@Override
	public void updateGui() {
		loadButton.enabled = saveList.clickedIndex != -1;
		renameButton.enabled = saveList.clickedIndex != -1;
		deleteButton.enabled = saveList.clickedIndex != -1;

		if (newWorldNameAlert.hasNewText) {
			String newWorldName = newWorldNameAlert.getText();
			if (newWorldName != null) {
				saveProvider.renameWorld(saveList.clickedIndex, newWorldName);
				saveList.text = saveProvider.getSavesAsStrings();
			}
		}

		if (deleteWorldAlert.getClicked()) {
			saveProvider.deleteWorld(saveList.clickedIndex);
			saveList.text = saveProvider.getSavesAsStrings();
			saveList.clickedIndex = -1;
		}

	}

	@Override
	public void render() {
		RenderHelper.renderDefaultGuiBackground();
		renderGui();
	}

	@Override
	public void clickButton(int id, int mouseButton) {
		if (id == loadButton.id && mouseButton == 0) {
			game.guiList.removeGui(getName());
			saveProvider.loadWorld(game, saveList.clickedIndex);
		} else if (id == renameButton.id && mouseButton == 0) {
			game.guiList.alert(newWorldNameAlert);
		} else if (id == deleteButton.id && mouseButton == 0) {
			game.guiList.alert(deleteWorldAlert);
		} else if (id == createWorldButton.id && mouseButton == 0) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiCreateWorld(game));
		} else if (id == backButton.id && mouseButton == 0) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiMain(game));
		}
	}
}