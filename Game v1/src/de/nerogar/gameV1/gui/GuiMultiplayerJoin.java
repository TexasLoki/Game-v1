package de.nerogar.gameV1.gui;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.GameOptions;
import de.nerogar.gameV1.RenderHelper;
import de.nerogar.gameV1.ServerList;

public class GuiMultiplayerJoin extends Gui {
	private GElementButton directConnectButton, backButton;
	private GElementButton listConnectButton, addServerButton, removeServerButton;

	private GElementTextField adressText, portText;
	private GElementListBox serverList;

	private AlertGetMessage newServerAdressAlert;

	public GuiMultiplayerJoin(Game game) {
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
		//addGElement(new GElementTextLabel(genNewID(), 0.0f, 0.05f, 0.4f, 0.1f, "Multiplayer", FontRenderer.CENTERED));
		setTitel("Join game");

		float leftX = 0.05f;
		float rightX = 0.6f;

		ServerList.instance.load();
		String[] servers = ServerList.instance.getAsStringArray();

		serverList = new GElementListBox(genNewID(), rightX, 0.2f, 1 - rightX, 0.5f, servers, "Buttons/button.png", "Buttons/scrollbar.png");
		listConnectButton = new GElementButton(genNewID(), rightX, 0.85f, 0.2f, 0.1f, "connect", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		addServerButton = new GElementButton(genNewID(), rightX, 0.7f, 0.2f, 0.1f, "add server", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		removeServerButton = new GElementButton(genNewID(), rightX + 0.2f, 0.7f, 0.2f, 0.1f, "remove server", FontRenderer.CENTERED, "Buttons/button.png", false, "");

		addGElement(new GElementTextLabel(genNewID(), leftX, 0.25f, 0.2f, 0.1f, "ip adress:", FontRenderer.RIGHT));
		adressText = new GElementTextField(leftX, 0.35f, 0.2f, 0.1f, "", "Buttons/textField.png");
		addGElement(new GElementTextLabel(genNewID(), leftX + 0.2f, 0.25f, 0.2f, 0.1f, "port:", FontRenderer.LEFT));
		portText = new GElementTextField(leftX + 0.2f, 0.35f, 0.15f, 0.1f, "4200", "Buttons/textField.png");

		directConnectButton = new GElementButton(genNewID(), 0.1f, 0.7f, 0.3f, 0.1f, "connect", FontRenderer.CENTERED, "Buttons/button.png", false, "");

		backButton = new GElementButton(genNewID(), 0.1f, 0.85f, 0.3f, 0.1f, "back", FontRenderer.CENTERED, "Buttons/button.png", false, "");

		addGElement(serverList);
		addGElement(listConnectButton);
		addGElement(addServerButton);
		addGElement(removeServerButton);

		addGElement(adressText);
		addGElement(portText);

		addGElement(directConnectButton);
		addGElement(backButton);

		newServerAdressAlert = new AlertGetMessage(game, "new server adress (ip:port)", true);
	}

	@Override
	public void updateGui() {
		directConnectButton.enabled = !adressText.getText().equals("");

		if (newServerAdressAlert.hasNewText) {
			String newServerAdress = newServerAdressAlert.getText();
			if (newServerAdress != null) {
				String[] newServerAdressParts = newServerAdress.split(":");
				String newServerIP = newServerAdressParts[0];
				int newServerPort = GameOptions.instance.standardPort;
				if (newServerAdressParts.length > 1) {
					try {
						newServerPort = Integer.parseInt(newServerAdressParts[1]);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}

				ServerList.instance.addServer(newServerIP, newServerPort);
				serverList.text = ServerList.instance.getAsStringArray();
			}
		}
	}

	@Override
	public void render() {
		RenderHelper.renderDefaultGuiBackground();
		renderGui();
	}

	@Override
	public void clickButton(int id, int mouseButton) {
		if (id == backButton.id) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiMultiplayer(game));
		} else if (id == addServerButton.id) {
			game.guiList.alert(newServerAdressAlert);
		} else if (id == removeServerButton.id) {
			if (serverList.clickedIndex >= 0) {
				ServerList.instance.removeServer(serverList.clickedIndex);
				serverList.text = ServerList.instance.getAsStringArray();
			}

		}
	}
}