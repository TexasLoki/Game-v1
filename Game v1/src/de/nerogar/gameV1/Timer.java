package de.nerogar.gameV1;

import java.util.ArrayList;

public class Timer {
	public float delta;
	private double lastFrame;
	private int fps;
	private double lastFps;
	public int mfFps;
	private ArrayList<Event> events = new ArrayList<Event>();
	private int framecount;
	//private ArrayList<String> eventNames = new ArrayList<String>();
	public static Timer instance = new Timer();

	public Timer() {

	}

	public void init() {
		lastFrame = getTime();
		lastFps = getTime();
	}

	private void printFPS() {
		if (getTime() - lastFps > 1000) {
			if (GameOptions.instance.getBoolOption("debug")) System.out.println("FPS: " + fps);
			mfFps = fps;
			fps = 0;
			lastFps += 1000;
		}
	}

	public void update() {
		fps++;
		framecount++;
		double time = getTime();
		delta = (float) (time - lastFrame);
		lastFrame = time;
		printFPS();
	}

	private double getTime() {
		//return (Sys.getTime() * 1000) / Sys.getTimerResolution();
		return System.nanoTime() / 1000000D;
	}

	//EVENTS
	public void registerEvent(String name, long seconds) {
		if (indexOfEvent(name) != -1) return;
		events.add(new Event(name, seconds * 10000000000L));
	}

	private int indexOfEvent(String name) {
		for (int i = 0; i < events.size(); i++) {
			if (name.equals(events.get(i).name)) return i;
		}
		return -1;
	}

	public boolean shellExecute(String name) {
		int index = indexOfEvent(name);
		return events.get(index).shellExecute();
	}

	public int getFramecount() {
		return framecount;
	}

	private class Event {
		public long nanoSeconds;
		public long lastExecute;
		public String name;

		public Event(String name, long nanoSeconds) {
			this.nanoSeconds = nanoSeconds;
			this.lastExecute = System.nanoTime();
			this.name = name;
		}

		public boolean shellExecute() {
			if (System.nanoTime() - lastExecute > nanoSeconds) {
				lastExecute += nanoSeconds;
				return true;
			}
			return false;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof Event) return (((Event) o).name.equals(name));
			return false;
		}
	}
}