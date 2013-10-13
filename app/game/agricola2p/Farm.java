package game.agricola2p;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ArrayTable;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;

public class Farm {
	
	private Board board;
	
	public String color;
	
	public int wood = 0;
	public int stone = 0;
	public int reed = 0;
	public int fences = 9;
	
	protected int rowRangeMin = 0;
	protected int rowRangeMax = 6;
	
	protected int colRangeMin = 8;
	protected int colRangeMax = 12;
	
	protected ArrayTable<Integer, Integer, Lot> terrain = createTerrainTable();
	
	protected Set<Integer> getRowRange() {
		return ContiguousSet.create(Range.closed(rowRangeMin, rowRangeMax), 
				DiscreteDomain.integers());
	}
	protected Set<Integer> getColRange() {
		return ContiguousSet.create(Range.closed(colRangeMin, colRangeMax), 
				DiscreteDomain.integers());
	}
	
	protected ArrayTable<Integer, Integer, Lot> createTerrainTable() {
		return ArrayTable.create(getRowRange(),getColRange());
	}
	
	public List<Worker> workers = new ArrayList<Worker>();
	
	public Farm(Board board, String color) {
		this.board = board;
		this.color = color;
		workers.add(new Worker(color, false));
		workers.add(new Worker(color, false));
		workers.add(new Worker(color, false));
		for(Integer y : getRowRange()) {
			for(Integer x : getColRange()) {
				Lot tile = null;
				if(y % 2 == 0 && x % 2 == 0) {
					tile = new LotNull(x,y);
				}
				else if(y % 2 == 1 && x % 2 == 1) {
					tile = new LotPasture(x,y);
				}
				else {
					tile = new LotFence(x,y);
				}
				terrain.put(y, x, tile);
			}
		}
		((LotFence)terrain.get(4, 9)).built = true;
		((LotFence)terrain.get(5, 8)).built = true;
		((LotPasture)terrain.get(5, 9)).building = "cottage";
		((LotFence)terrain.get(5, 10)).built = true;
		((LotFence)terrain.get(6, 9)).built = true;
	}
	
	public Lot[][] getTerrain() {
		return terrain.toArray(Lot.class);
	}
	
}

