package de.nerogar.gameV1.level;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.GameOptions;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.DNFileSystem.DNFile;
import de.nerogar.gameV1.object.Object3D;
import de.nerogar.gameV1.object.Object3DBank;
import de.nerogar.gameV1.object.ObjectSprite;
import de.nerogar.gameV1.physics.*;

public abstract class Entity {
	public Game game;
	public World world;
	public Bounding boundingBox = new BoundingAABB();
	public ObjectMatrix matrix = new ObjectMatrix(new Vector3d(0, 0, 0), // Position 0
			new Vector3d(0, 0, 0), // Rotation 0
			new Vector3d(1, 1, 1));  // Skalierung 1

	public Object3D object;
	public String texture;
	public boolean saveEntity = true;
	public boolean markToRemove = false;
	public float opacity = 1;
	public static final String NODEFOLDERSAVENAME = "entities";
	private static HashMap<String, Class<? extends Entity>> entityList = new HashMap<String, Class<? extends Entity>>();

	public Entity(Game game, World world, ObjectMatrix matrix) {
		this.game = game;
		this.world = world;
		this.matrix = matrix;
	}

	public void setObject(String objectName, String textureName) {
		if (objectName != "") {
			object = Object3DBank.instance.getObject(objectName);
		} else {
			object = null;
		}
		texture = textureName;
	}

	public void setSprite(float size, String textureName) {
		object = new ObjectSprite(size, world.player.camera);
		texture = textureName;
	}

	public World getWorld() {
		return world;
	}

	public void update(float time) {

	}

	public Bounding getBoundingBox() {

		if (boundingBox instanceof BoundingAABB) return matrix.getTransformedAABB((BoundingAABB) boundingBox);
		if (boundingBox instanceof BoundingCircle) return matrix.getTransformedCircle((BoundingCircle) boundingBox);

		// getTransformedOBB fehlt

		return new BoundingAABB();
	}

	public BoundingAABB getAABB() {

		return PhysicHelper.toAABB(getBoundingBox());

	}

	public void load(DNFile chunkFile, String folder) {
		matrix.position.setX(chunkFile.getDouble(folder + ".position.x"));
		matrix.position.setY(chunkFile.getDouble(folder + ".position.y"));
		matrix.position.setZ(chunkFile.getDouble(folder + ".position.z"));

		loadProperties(chunkFile, folder);
	}

	public void save(DNFile chunkFile, String folder) {
		chunkFile.addNode(folder + ".type", getNameTag());
		chunkFile.addNode(folder + ".position.x", matrix.position.getX());
		chunkFile.addNode(folder + ".position.y", matrix.position.getY());
		chunkFile.addNode(folder + ".position.z", matrix.position.getZ());

		saveProperties(chunkFile, folder);
	}

	public abstract void saveProperties(DNFile chunkFile, String folder);

	public abstract void loadProperties(DNFile chunkFile, String folder);

	public void render() {
		// BoundingRender.renderAABB((BoundingAABB)getBoundingBox(), 0x00FF00);
		if (GameOptions.instance.getBoolOption("debug")) {
			displayBoundingBox(getBoundingBox(), 0x00FF00);

		}
		if (object != null) {
			object.render(matrix, texture, opacity);
		}
	}

	public void displayBoundingBox(Bounding b, int color) {

		if (b instanceof BoundingAABB) BoundingRender.renderAABB((BoundingAABB) b, color);
		if (b instanceof BoundingCircle) BoundingRender.renderBall((BoundingCircle) b, color);

		// renderOBB fehlt

	}

	public abstract void interact(); // wenn andere entities oder objekte mit dieser entity interagieren

	public abstract void click(int key);// klick mit der maus, macht sinn, oder?

	public abstract String getNameTag();

	public abstract void init(World world);

	// register der Entities, wird zum laden/speichern gebraucht
	public static void registerEntity(Entity entity) {
		entityList.put(entity.getNameTag(), (Class<? extends Entity>) entity.getClass());
	}

	public static Entity getEntity(Game game, World world, String tagName) {
		if (tagName == null) return null;
		try {
			Constructor<? extends Entity> contructor = entityList.get(tagName).getConstructor(new Class[] { Game.class, World.class, ObjectMatrix.class });
			Entity entity = contructor.newInstance(game, world, new ObjectMatrix());
			return entity;
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void initEntityList(Game game) {
		ObjectMatrix objectMatrix = new ObjectMatrix();
		registerEntity(new EntityTree(game, null, objectMatrix));
		registerEntity(new EntityHouse(game, null, objectMatrix));
		registerEntity(new EntityHouseBlue(game, null, objectMatrix));
		registerEntity(new EntityHouseGreen(game, null, objectMatrix));
		registerEntity(new EntityHouseOrange(game, null, objectMatrix));
		registerEntity(new EntityHousePink(game, null, objectMatrix));
		registerEntity(new EntityHouseRed(game, null, objectMatrix));
		registerEntity(new EntityShrine(game, null, objectMatrix));
		registerEntity(new EntityTestparticle(game, null, objectMatrix));
	}

}
