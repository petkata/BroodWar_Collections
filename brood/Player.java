package brood;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Player {

	// resource type and quantity
	Map<String, Integer> resources;
	// current supply limit
	int supplyLimit;
	// total unit supply
	int unitSupply;
	
	//TODO Gas cost for units
	//    v-building name v-unit     v-supply v-cost     v-all units of this type
	Map<String, Map<Map<String, Map<Integer, Integer>>, Integer>> units;

	//       v-name     v-minerals   v-gas     v-number of this type of buildings
	Map<Map<String, Map<Integer, Integer>>, Integer> buildings;

	public Player() {
		this.buildings = new HashMap<>();
		Map<String, Map<Integer, Integer>> nexus = new HashMap<>();
		nexus.put("Nexus", new HashMap<>());
		nexus.get("Nexus").put(400, 0);
		this.buildings.put(nexus, 1);
		this.supplyLimit = 9;

		resources = new HashMap<>();
		resources.put("Minerals", 500);
		resources.put("Vespene gas", 0);

		this.units = new HashMap<>();
		this.units.put("Nexus", new HashMap<>());

		Map<String, Map<Integer, Integer>> probe = new HashMap<>();
		probe.put("Probe", new HashMap<>());
		probe.get("Probe").put(1, 50);
		this.units.get("Nexus").put(probe, 4);

		this.unitSupply = this.getCurrentSupply();
	}

	void gatherResources(String resource){
		if (selectProbe()) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (resource.equals("Minerals")) {
				System.out.println(Thread.currentThread().getName());
				this.resources.put("Minerals", this.resources.get("Minerals") + 5);
			}
			if (resource.equals("Vespene gas")) {
				System.out.println(Thread.currentThread().getName());
				this.resources.put("Vespene gas", this.resources.get("Vespene gas") + 4);
			}
			
		}
	}
	
	void collectMinerals() {
		if (selectProbe()) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("5 minerasl gathered");
			this.resources.put("Minerals", this.resources.get("Minerals") + 5);
		}
		showStats();
	}

	void collectGas() {
		if (selectProbe()) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(" gathered 4 gas");
			this.resources.put("Vespene gas", this.resources.get("Vespene gas") + 4);
		}
		showStats();
	}

	private boolean selectProbe() {

		for (Map<Map<String, Map<Integer, Integer>>, Integer> e : units.values()) {
			for (Map<String, Map<Integer, Integer>> unit : e.keySet()) {
				if (unit.containsKey("Probe")) {
					return true;
				}
			}
		}
		return false;
	}

	void warpBuilding(String buildingName, int mineralCost, int gasCost) {
		if (hasEnoughResources(mineralCost, gasCost)) {
			if (hasBuilding(buildingName)) {
				for (Entry<Map<String, Map<Integer, Integer>>, Integer> e : this.buildings.entrySet()) {
					if (e.getKey().containsKey(buildingName)) {
						for (Map<String, Map<Integer, Integer>> eKeys : this.buildings.keySet()) {
							if (eKeys.containsKey(buildingName)) {
								for (String keyName : eKeys.keySet()) {
									if (keyName.equals(buildingName)) {
										e.setValue(e.getValue() + 1);
									}
								}
							}
						}
					}
				}
			} 
			else {
				Map<String, Map<Integer, Integer>> structure = new HashMap<>();
				structure.put(buildingName, new HashMap<>());
				structure.get(buildingName).put(mineralCost, gasCost);
				this.buildings.put(structure, 1);
			}
			if (buildingName.equals("Pylon")) {
				this.supplyLimit+=8;
			}
			System.out.println(buildingName + " warped." + " -" +getBuildingMineralCost(buildingName) +" Minerals" );
			this.resources.put("Minerals", this.resources.get("Minerals") - getBuildingMineralCost(buildingName));
		} else {
			System.out.println("Not enough resources to construct " + buildingName);
		}
	}

	private boolean hasEnoughResources(int mineralCost, int gasCost) {
		if (this.resources.get("Minerals") >= mineralCost && this.resources.get("Vespene gas") >= gasCost) {
			return true;
		}
		return false;
	}

	private int getBuildingGasCost(String buildingName) {
		for (Map<String, Map<Integer, Integer>> building : this.buildings.keySet()) {
			if (building.containsKey(buildingName)) {
				for (int minerals : building.get(buildingName).values()) {
					return minerals;
				}
			}
		}
		return 0;
	}

	private int getBuildingMineralCost(String buildingName) {
		// v-name v-minerals v-gas v-number of this type of buildings
		// Map<Map<String, Map<Integer,Integer>>, Integer> buildings;
		for (Map<String, Map<Integer, Integer>> building : this.buildings.keySet()) {
			if (building.containsKey(buildingName)) {
				for (int minerals : building.get(buildingName).keySet()) {
					return minerals;
				}
			}
		}
		return 0;
	}

	private int getUnitGasCost(String unitName) {
		for (Map<Map<String, Map<Integer, Integer>>, Integer> value : this.units.values()) {
			for (Map<String, Map<Integer, Integer>> unitInfo : value.keySet()) {
				for (Entry<String, Map<Integer, Integer>> e : unitInfo.entrySet()) {
					if (e.getKey().equals(unitName)) {
						for (Integer gasCost : e.getValue().values()) {
							return gasCost;
						}
					}
				}
			}
		}
		return 0;
	}

	private int getUnitMineralCost(String unitName) {
		for (Map<Map<String, Map<Integer, Integer>>, Integer> value : this.units.values()) {
			for (Map<String, Map<Integer, Integer>> unitInfo : value.keySet()) {
				for (Entry<String, Map<Integer, Integer>> e : unitInfo.entrySet()) {
					if (e.getKey().equals(unitName)) {
						for (Integer mineralCost : e.getValue().values()) {
							return mineralCost;
						}
					}
				}
			}
		}
		return 0;
	}

	//TODO Gas cost for units
	void warpUnit(String unitName) {
		if (hasBuilding("Nexus")) {
			if (hasEnoughResources(getUnitMineralCost(unitName), 0)) {

				if (hasAdditionalPylons()) {
					System.out.println("Probe warped");

					this.unitSupply = getSupplyOfUnit("Probe") + this.getCurrentSupply();
					
					this.resources.put("Minerals", this.resources.get("Minerals") - getUnitMineralCost(unitName));
					
					setUnitCount("Probe");

				} else
					System.out.println("You Must Construct Additional Pylons!");
			}else 
				System.out.println("Not enough resources for warping " + unitName);

		} else
			System.out.println("Must have Nexus");

	}

	private int getUnitCount(String unitName) {
		for ( Map<Map<String, Map<Integer, Integer>>, Integer> allUnits : this.units.values()) {
			for (Entry<Map<String, Map<Integer, Integer>>, Integer> unit : allUnits.entrySet()) {
				for (String name : unit.getKey().keySet()) {
					if (name.equals(unitName)) {
						return unit.getValue();
					}
				}
			}
		}
		return 0;
	}

	private void setUnitCount(String unitName) {
		for (Entry<String, Map<Map<String, Map<Integer, Integer>>, Integer>> entry : this.units.entrySet()) {
			for (Entry<Map<String, Map<Integer, Integer>>, Integer> subEntry : entry.getValue().entrySet()) {
				for (String name : subEntry.getKey().keySet()) {
					if (name.equals(unitName)) {
						subEntry.setValue(subEntry.getValue() + 1);
					}
				}
			}
		}
	}

	private boolean hasAdditionalPylons() {
		int supplyFromPylons = 0;
		if (hasBuilding("Pylon")) {
			// v-name v-minerals v-gas v-number of this type of buildings
			// Map<Map<String, Map<Integer,Integer>>, Integer> buildings;
			for (Entry<Map<String, Map<Integer, Integer>>, Integer> e : this.buildings.entrySet()) {
				for (String structure : e.getKey().keySet()) {
					if (structure.equals("Pylon")) {
						supplyFromPylons = e.getValue() * 8;
					}
				}
			}
		}
		if (getCurrentSupply() + getSupplyOfUnit("Probe") <= supplyFromPylons + this.supplyLimit) {
			return true;
		}

		return false;

	}

	private int getCurrentSupply() {
		int currentSupply = 0;
		for (Entry<String, Map<Map<String, Map<Integer, Integer>>, Integer>> allUnits : this.units.entrySet()) {
			for (Entry<Map<String, Map<Integer, Integer>>, Integer> unit : allUnits.getValue().entrySet()) {
				for (String name : unit.getKey().keySet()) {
					currentSupply = getSupplyOfUnit(name) * unit.getValue();
				}
			}
		}
		return currentSupply;
	}

	int getSupplyOfUnit(String unit) {
		int unitSupply = 0;
		for (Map<Map<String, Map<Integer, Integer>>, Integer> value : this.units.values()) {
			for (Map<String, Map<Integer, Integer>> unitInfo : value.keySet()) {
				for (Entry<String, Map<Integer, Integer>> e : unitInfo.entrySet()) {
					if (e.getKey().equals(unit)) {
						for (Integer supply : e.getValue().keySet()) {
							unitSupply = supply;
						}
					}
				}
			}
		}
		return unitSupply;
	}

	boolean hasBuilding(String building) {
		for (Map<String, Map<Integer, Integer>> e : this.buildings.keySet()) {
			if (e.keySet().contains(building)) {
				return true;
			}
		}
		return false;
	}

	void showStats() {
		System.out.println("Supply: " + this.unitSupply + "/" + this.supplyLimit + " " + this.resources);
	}

	void units() {
		System.out.println(this.units);
	}

	void buildings() {
		System.out.println(this.buildings);
	}

}
