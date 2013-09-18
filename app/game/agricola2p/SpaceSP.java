package game.agricola2p;

public class SpaceSP extends Space {

	private Board board;
	
	public int wood = 0;
	
	public SpaceSP(Board board) {
		super(board);
	}
	
	protected void onNewRound() {
		super.onNewRound();
		this.wood += 1;
	}
	
	protected void onTake() {
		super.onTake();
		board.activeFarm().wood += this.wood;
		this.wood = 0;
		
		board.startingPlayer = board.activeFarm().color;
		
	}
	
}
