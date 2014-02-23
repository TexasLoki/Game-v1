package de.nerogar.gameV1.level;

import java.util.ArrayList;

import de.nerogar.DNFileSystem.DNNodePath;
import de.nerogar.gameV1.*;
import de.nerogar.gameV1.network.EntityPacket;
import de.nerogar.gameV1.network.EntityPacketClick;
import de.nerogar.gameV1.physics.BoundingAABB;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class EntityHouseGreen extends EntityBuilding {

	public EntityHouseGreen(Game game, World world, ObjectMatrix matrix) {
		super(game, world, matrix);
		size = new Position(2, 2);
		centerPosition = new Position(1, 1);
		resourceCost = new GameResources(200, 100, 100);
		boundingBox = new BoundingAABB(new Vector3d(-size.x / 2, 0, -size.z / 2), new Vector3d(size.x / 2, 1, size.z / 2));
	}

	@Override
	public void init(World world) {
		setObject("entities/houseGreen/mesh", "entities/houseGreen/texture.png");
	}

	@Override
	public void interact() {
		// TODO Auto-generated method stub
	}

	@Override
	public String getNameTag() {
		return "houseGreen";
	}

	@Override
	public void saveProperties(DNNodePath folder) {
		// TODO Auto-generated method stub
	}

	@Override
	public void loadProperties(DNNodePath folder) {
		// TODO Auto-generated method stub
	}

	@Override
	public void click(int key) {
		// TODO Auto-generated method stub
		if (key == 1) remove();
	}

	@Override
	public void updateServer(float time, ArrayList<EntityPacket> packets) {
		if (Math.random() < 0.05f) {
			if (faction.getUnitCount() < faction.getMaxUnitCount()) {
				EntityTestSoldier testSoldier = new EntityTestSoldier(game, world, new ObjectMatrix());
				double posX = matrix.position.getX() + Math.random() * 10 - 5;
				double posZ = matrix.position.getZ() + 3;
				Vector3d pos = new Vector3d(posX, world.land.getHeight(posX, posZ), posZ);
				testSoldier.matrix.setPosition(pos);
				testSoldier.faction = faction;
				world.spawnEntity(testSoldier);
			}

		}

		for (EntityPacket packet : packets) {
			if (packet instanceof EntityPacketClick) {
				int mouseButton = ((EntityPacketClick) packet).mouseButton;
				if (mouseButton == 1) {
					remove();
				}
			}
		}
	}

	@Override
	public void updateClient(float time, ArrayList<EntityPacket> packets) {

	}
}
