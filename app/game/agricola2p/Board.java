package game.agricola2p;

import game.GameError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

public class Board extends game.Board {
	
	public static enum State {
		PLACE_WORKER,
		EXPANDING_FARM,
		WAITING_ON_COMMIT,
		BUILDING_STONE_FENCES,
		BUILDING_WOOD_FENCES
	}
	
	public boolean canCommit = false;
	
	public int round = 0;
	public int expansions = 4;
	public int troughs = 10;
	
	public int buildableFences = 0;
	public int buildableTroughs = 0;
	public int buildableStables = 0;
	public int buildableStalls = 0;
	
	public List<Task> tasks = new ArrayList<Task>();
	
	public Map<String, Action> actions = new HashMap<String, Action>();
	{
		actions.put("SP", new ActionSP(this));
		actions.put("Wx3", new Action3W(this));
		actions.put("Sx1", new Action1S(this));
		actions.put("Sx2", new Action2S(this));
		actions.put("WFen", new ActionWFen(this));
		actions.put("SFen", new ActionSFen(this));
		actions.put("RSW", new ActionRSW(this));
		actions.put("X", new ActionX(this));
	}

	public Farm redFarm = new Farm(this, "red");
	public Farm blueFarm = new Farm(this, "blue");

	public String startingPlayer = redFarm.color;
	public String currentPlayer;
	
	public State inputState = State.PLACE_WORKER;
	
	private String commandRoot = "";
	
	public Board() {
		onNewRound();
		
		for(Worker worker : activeFarm().workers) {
			worker.setUsable(true);
		}
	}

	public void runCommand(String command) throws GameError {
		switch(command) {
		case "commit" :
			buildableFences = 0;
			tasks.clear();
			
			currentPlayer = (currentPlayer.equals(redFarm.color))?blueFarm.color:redFarm.color;
			
			inputState = State.PLACE_WORKER;
			
			if(redFarm.workers.size() == 0 &&
			   blueFarm.workers.size() == 0) {
				onNewRound();
			}
			
			for(Worker worker : activeFarm().workers) {
				worker.setUsable(true);
			}
			
			canCommit = false;
			
			break;
		default :
			for(Worker worker : activeFarm().workers) {
				worker.setUsable(false);
			}
			
			String[] commandTokens = command.split(":");
			if(commandTokens[0].equals("") == false) {
				commandRoot = commandTokens[0];
			}
			
			Action action = actions.get(commandRoot);
			//if(action == null) then error, unknown command
			if(action != null) {
				if(commandTokens.length == 1)
					action.onTake();
				else
					action.onTake(commandTokens);
			}
		}
	}

	public Farm activeFarm() {
		if(redFarm.color.equals(currentPlayer)) {
			return redFarm;
		} else if(blueFarm.color.equals(currentPlayer)) {
			return blueFarm;
		} else {
			throw new RuntimeException("Invalid value for currentPlayer=\""+currentPlayer+"\"");
		}
	}
	
	public void onNewRound() {
		round++;
		currentPlayer = startingPlayer;
		for(Action action : actions.values()) {
			action.onNewRound();
		}
	}
	
}
