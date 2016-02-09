package brood;

public class SCDemo {
	
	public static void main(String[] args) {
		
		
		player.showStats();
		player.units();
		player.warpBuilding("Pylon", 100, 0);
		player.warpBuilding("Pylon", 100, 0);
		player.warpBuilding("Gateway", 150, 0);
		player.warpBuilding("Pylon", 100, 0);
//		player.warpBuilding("Gateway", 150, 0);
		
		player.warpUnit("Probe");
		player.warpUnit("Probe");
		player.warpUnit("Probe");
		player.warpUnit("Probe");
		player.warpUnit("Probe");
		player.warpUnit("Probe");
		player.warpUnit("Probe");
		
		player.showStats();
		
		player.buildings();
		
		player.units();
		
		

//		new Probe("Minerals").start();
//		new Probe("Vespene gas").start();
//		new Probe("Minerals").start();
//		new Probe("Minerals").start();
	}
	
	
	static Player player = new Player();
	
	private static class Probe extends Thread{
		private String typeResource;
		
		public Probe(String typeResource) {
			super("Probe going for " + typeResource);
			this.typeResource = typeResource;
		}

		@Override
		public void run() {
			while(true)
				player.gatherResources(typeResource);
		}
		
	} 
}
